package net.cheltsov.shtoss.service;

import net.cheltsov.shtoss.dao.AbstractDaoFactory;
import net.cheltsov.shtoss.dao.ConversationDao;
import net.cheltsov.shtoss.dao.DaoManager;
import net.cheltsov.shtoss.dao.Initializer;
import net.cheltsov.shtoss.entity.Conversation;
import net.cheltsov.shtoss.entity.Message;
import net.cheltsov.shtoss.entity.User;
import net.cheltsov.shtoss.exception.DaoException;
import net.cheltsov.shtoss.exception.ServiceException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * A service layer class implementing all the logic concerning conversations
 */
public class ConversationService {
    private static final Logger LOGGER = LogManager.getLogger();
    private static AbstractDaoFactory factory = DaoManager.getDaoFactory();

    /**
     * Creates a conversation's topic by user and saves it in data base
     *
     * @param topic topic of conversation
     * @param text  text of first message
     * @param user  the user which it is created by
     * @return conversation, wrapped in Optional if the conversation was created, <tt>Optional.empty()</tt> otherwise
     */
    public static Optional<Conversation> createTopicByUser(String topic, String text, User user) {
        return createTopic(topic, text, user, user);
    }

    /**
     * Creates a conversation's topic by user with ADMIN status and saves it in data base
     *
     * @param topic     topic of conversation
     * @param text      text of first message
     * @param creator   the user with ADMIN status which it is created by
     * @param userLogin login of user which is recipient of this conversation
     * @return conversation, wrapped in Optional if the conversation was created, <tt>Optional.empty()</tt> otherwise
     */
    public static Optional<Conversation> createTopicByAdmin(String topic, String text, User creator, String userLogin) {
        try {
            return createTopic(topic, text, creator, factory.getUserDao().findUserByLogin(userLogin));
        } catch (DaoException e) {
            LOGGER.catching(e);
            return Optional.empty();
        }
    }

    private static Optional<Conversation> createTopic(String topic, String text, User creator, User user) {
        if (topic == null && text == null && creator == null && user == null) {
            return Optional.empty();
        }
        Conversation conversation = new Conversation();
        conversation.setTopic(topic);
        Message message = new Message();
        message.setText(text);
        message.setUser(creator);
        conversation.setUser(user);
        Initializer initializer = null;
        try {
            initializer = factory.getInitializer();
            initializer.setAutoCommit(false);
            ConversationDao conversationDao = factory.getConversationDao(initializer);
            message.setConversationId(conversationDao.createConversation(conversation));
            conversation.setConversationId(message.getConversationId());
            message.setMessageID(conversationDao.findLastMessageId(message.getConversationId()) + 1);
            message.setDate(new Date());
            conversationDao.addMessage(message);
            message.setDate(new Date(System.currentTimeMillis()));
            conversation.setLastMessage(message);
            initializer.commit();
            return Optional.of(conversation);
        } catch (DaoException e) {
            LOGGER.catching(e);
            initializer.rollback();
            return Optional.empty();
        } finally {
            initializer.close();
        }
    }

    /**
     * Finds all conversations
     * @return the list of conversations
     * @throws ServiceException if any exceptions occurred on the DAO layer
     */
    public static List<Conversation> findConversations() throws ServiceException {
        try {
            return factory.getConversationDao().findAllConversations();
        } catch (DaoException e) {
            throw new ServiceException("Exception while finding conversations", e);
        }
    }

    /**
     * Finds all user's conversations
     * @param user the user whose messages are needed to be found
     * @return the list of conversations of the user
     * @throws ServiceException if any exceptions occurred on the DAO layer
     */
    public static List<Conversation> findConversations(User user) throws ServiceException {
        try {
            return factory.getConversationDao().findAllUserConversations(user.getUserId());
        } catch (DaoException e) {
            throw new ServiceException("Exception while finding conversations", e);
        }
    }

    /**
     * Finds the conversation by the specific id
     *
     * @param conversationId specific id of conversation
     * @param user           the user whose conversation is needed to be found
     * @return found conversation
     * @throws ServiceException if any exceptions occurred on the DAO layer
     */
    public static Conversation findConversationById(int conversationId, User user) throws ServiceException {
        try {
            Conversation conversation = factory.getConversationDao().findConversationById(conversationId);
            if (user.getRole() != User.Role.ADMIN && conversation.getUser().getUserId() != user.getUserId()) {
                throw new ServiceException("Unauthorized access");
            }
            return conversation;
        } catch (DaoException e) {
            throw new ServiceException("Exception while finding conversation", e);
        }
    }

    /**
     * Finds messages of the given id of conversation
     *
     * @param conversationId id of conversation of messages to be found
     * @return the list of found messages
     * @throws ServiceException if any exceptions occurred on the DAO layer
     */
    public static List<Message> findMessages(int conversationId) throws ServiceException {
        try {
            return factory.getConversationDao().findAllConversationMessages(conversationId);
        } catch (DaoException e) {
            throw new ServiceException("Exception while finding messages", e);
        }
    }

    /**
     * Adds a message in the given conversation
     * @param user the author of the message
     * @param conversation the conversation where the message has to be added
     * @param text the text of the message
     * @return the message, wrapped in Optional if the conversation was created, <tt>Optional.empty()</tt> otherwise
     */
    public static Optional<Message> addMessage(User user, Conversation conversation, String text) {
        Message message = new Message();
        message.setConversationId(conversation.getConversationId());
        message.setUser(user);
        message.setText(text);
        Initializer initializer = null;
        try {
            initializer = factory.getInitializer();
            initializer.setAutoCommit(false);
            ConversationDao conversationDao = factory.getConversationDao(initializer);
            message.setMessageID(conversationDao.findLastMessageId(conversation.getConversationId()) + 1);
            conversationDao.addMessage(message);
            message.setDate(new Date(System.currentTimeMillis()));
            initializer.close();
            return Optional.of(message);
        } catch (DaoException e) {
            LOGGER.catching(e);
            initializer.rollback();
            return Optional.empty();
        } finally {
            initializer.close();
        }
    }
}
