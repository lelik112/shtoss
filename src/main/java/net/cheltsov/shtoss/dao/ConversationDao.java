package net.cheltsov.shtoss.dao;

import net.cheltsov.shtoss.entity.Conversation;
import net.cheltsov.shtoss.entity.Message;
import net.cheltsov.shtoss.exception.DaoException;

import java.util.List;

/**
 * Provides methods to access data base for <tt>Conversation</tt> class
 */
public interface ConversationDao {
    int createConversation(Conversation conversation) throws DaoException;

    boolean addMessage(Message message) throws DaoException;

    int findLastMessageId(int conversationId) throws DaoException;

    List<Conversation> findAllUserConversations(int userId) throws DaoException;

    List<Conversation> findAllConversations() throws DaoException;

    Conversation findConversationById(int conversationId) throws DaoException;

    List<Message> findAllConversationMessages(int conversationId) throws DaoException;

}
