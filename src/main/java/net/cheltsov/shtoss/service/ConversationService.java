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
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public class ConversationService {
    private static final Logger LOGGER = LogManager.getLogger();
    private static AbstractDaoFactory factory = DaoManager.getDaoFactory();

    public static Optional<Conversation> createTopicByUser(String topic, String text, User user) {
        return createTopic(topic, text, user, user);
    }

    public static Optional<Conversation> createTopicByAdmin(String topic, String text, User creator, String userLogin) {
        try {
            return createTopic(topic, text, creator, factory.getUserDao().findUserByLogin(userLogin));
        } catch (DaoException e) {
            LOGGER.catching(e);
            return Optional.empty();
        }
    }

    private static Optional<Conversation> createTopic(String topic, String text, User creator, User user) {
        if (topic == null && text == null && creator == null && user == null) return Optional.empty();
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
            message.setConversationID(conversationDao.createConversation(conversation));
            conversation.setConversationID(message.getConversationID());
            message.setMessageID(conversationDao.findLastMessageID(message.getConversationID()) + 1);
            message.setDate(new Date());
            conversationDao.addMessage(message);
            initializer.commit();
            return Optional.of(conversation);
        } catch (DaoException e) {
            LOGGER.log(Level.ERROR, "Exception while creating conversation", e);
            initializer.rollback();
            return Optional.empty();
        } finally {
            initializer.close();
        }
    }

    public static List<Conversation> findConversations() throws ServiceException {
        try {
            return factory.getConversationDao().findAllConversations();
        } catch (DaoException e) {
            throw new ServiceException("Exception while finding conversations", e);
        }
    }

    public static List<Conversation> findConversations(User user) throws ServiceException {
        try {
            return factory.getConversationDao().findAllUserConversations(user.getID());
        } catch (DaoException e) {
            throw new ServiceException("Exception while finding conversations", e);
        }
    }

    public static Conversation findConversationByID(int conversationID, User user) throws ServiceException {
        try {
            Conversation conversation = factory.getConversationDao().findConversationByID(conversationID);
            if (user.getRole() != User.Role.ADMIN && conversation.getUser().getID() != user.getID()) {
                throw new ServiceException("Unauthorized access");
            }
            return conversation;
        } catch (DaoException e) {
            throw new ServiceException("Exception while finding conversation", e);
        }
    }

    public static List<Message> findMessages(int conversationID) throws ServiceException {
        try {
            return factory.getConversationDao().findAllConversationMessages(conversationID);
        } catch (DaoException e) {
            throw new ServiceException("Exception while finding messages", e);
        }
    }

    public static Optional<Message> addMessage(User user, Conversation conversation, String text) {
        Message message = new Message();
        message.setConversationID(conversation.getConversationID());
        message.setUser(user);
        message.setText(text);
        Initializer initializer = null;
        try {
            initializer = factory.getInitializer();
            initializer.setAutoCommit(false);
            ConversationDao conversationDao = factory.getConversationDao(initializer);
            message.setMessageID(conversationDao.findLastMessageID(conversation.getConversationID()) + 1);
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
