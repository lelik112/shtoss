package net.cheltsov.shtoss.dao;

import net.cheltsov.shtoss.entity.Conversation;
import net.cheltsov.shtoss.entity.Message;
import net.cheltsov.shtoss.exception.DaoException;

import java.util.List;
import java.util.Optional;

public interface ConversationDao {
    int createConversation(Conversation conversation) throws DaoException;
    boolean addMessage(Message message) throws DaoException;
    int findLastMessageID(int conversationID) throws DaoException;
    List<Conversation> findAllUserConversations(int userID) throws DaoException;
    List<Conversation> findAllConversations() throws DaoException;
    Conversation findConversationByID(int conversationID) throws DaoException;
    List<Message> findAllConversationMessages(int conversationID) throws DaoException;


}
