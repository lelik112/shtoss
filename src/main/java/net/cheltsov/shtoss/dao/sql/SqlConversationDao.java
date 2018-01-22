package net.cheltsov.shtoss.dao.sql;

import net.cheltsov.shtoss.dao.ConversationDao;
import net.cheltsov.shtoss.entity.Conversation;
import net.cheltsov.shtoss.entity.Message;
import net.cheltsov.shtoss.entity.User;
import net.cheltsov.shtoss.exception.DaoException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SqlConversationDao extends SqlAbstractDao implements ConversationDao {
    private static final String DAO_C_CONVERSATION_ID = "c.conversation_id";
    private static final String DAO_M_CONVERSATION_ID = "m.conversation_id";
    private static final String DAO_C_TOPIC = "c.topic";
    private static final String DAO_M_MESSAGE_ID = "m.message_id";
    private static final String DAO_LAST_MESSAGE_TIME = "time";
    private static final String DAO_LAST_MESSAGE_USER_ID = "last_mess_user_id";
    private static final String DAO_LAST_MESSAGE_LOGIN = "last_mess_login";
    private static final String DAO_LAST_MESSAGE_TEXT = "last_mess_text";
    private static final String DAO_C_USER_ID = "c.user_id";
    private static final String DAO_M_USER_ID = "m.user_id";
    private static final String DAO_U_USER_LOGIN = "u.login";
    private static final String DAO_M_TEXT = "m.text";
    private static final String DAO_M_MESSAGE_TIME = "m.message_time";

    private static final String SQL_CREATE_CONVERSATION = "INSERT INTO conversation (user_id, topic) VALUES (?, ?)";
    private static final String SQL_ADD_MESSAGE = "INSERT INTO message (message_id, conversation_id, user_id, text) VALUES (?, ?, ?, ?)";
    private static final String SQL_FIND_LAST_MESSAGE_ID = "SELECT max(message_id) FROM message WHERE conversation_id = ?";
    private static final String SQL_FIND_CONVERSATION_BY_ID = "SELECT c.user_id, c.topic, u.login FROM conversation c " +
            "JOIN user u ON c.user_id = u.user_id WHERE conversation_id = ?";
    private static final String SQL_FIND_ALL_USER_CONVERSATIONS = "SELECT c.conversation_id, c.user_id, u.login, c.topic, max(m.message_time) time, " +
            "(SELECT user_id FROM message WHERE conversation_id = c.conversation_id ORDER BY message_time DESC LIMIT 1) AS last_mess_user_id, " +
            "(SELECT login FROM user WHERE user_id = last_mess_user_id) AS last_mess_login, " +
            "(SELECT text FROM message WHERE conversation_id = c.conversation_id ORDER BY message_time DESC LIMIT 1)  AS last_mess_text " +
            "FROM conversation c JOIN message m ON c.conversation_id = m.conversation_id JOIN user u ON c.user_id = u.user_id " +
            "GROUP BY c.conversation_id HAVING c.user_id = ? ORDER BY time DESC";
    private static final String SQL_FIND_ALL_CONVERSATIONS = "SELECT c.conversation_id, c.user_id, u.login, c.topic, max(m.message_time) time, " +
            "(SELECT user_id FROM message WHERE conversation_id = c.conversation_id ORDER BY message_time DESC LIMIT 1) AS last_mess_user_id, " +
            "(SELECT login FROM user WHERE user_id = last_mess_user_id) AS last_mess_login, " +
            "(SELECT text FROM message WHERE conversation_id = c.conversation_id ORDER BY message_time DESC LIMIT 1)  AS last_mess_text " +
            "FROM conversation c JOIN message m ON c.conversation_id = m.conversation_id JOIN user u ON c.user_id = u.user_id " +
            "GROUP BY c.conversation_id  ORDER BY time DESC";
    private static final String SQL_FIND_ALL_CONVERSATION_MESSAGES = "SELECT m.message_id, m.conversation_id, m.user_id, m.text, m.message_time, u.login " +
                                                                    "FROM message m JOIN user u ON m.user_id = u.user_id WHERE m.conversation_id = ?";

    private static SqlConversationDao methodLevelConversationDao = new SqlConversationDao();

    private SqlConversationDao(){}

    SqlConversationDao(SqlInitializer initializer) {
        super(initializer.getConnection());
        initializer.addDao(this);
    }

    public static SqlConversationDao getMethodLevelConversationDao() {
        return methodLevelConversationDao;
    }

    @Override
    public int createConversation(Conversation conversation) throws DaoException {
        Connection cn = getConnection();
        try (PreparedStatement ps = cn.prepareStatement(SQL_CREATE_CONVERSATION, Statement.RETURN_GENERATED_KEYS)){
            ps.setInt(1, conversation.getUser().getUserId());
            ps.setString(2, conversation.getTopic());
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1);
            } else {
                return -1;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DaoException("Problem with preparing statement", e);
        } finally {
            releaseConnectionIfLocal(cn);
        }
    }

    @Override
    public boolean addMessage(Message message) throws DaoException {
        Connection cn = getConnection();
        try (PreparedStatement ps = cn.prepareStatement(SQL_ADD_MESSAGE)){
            ps.setInt(1, message.getMessageId());
            ps.setInt(2, message.getConversationId());
            ps.setInt(3, message.getUser().getUserId());
            ps.setString(4, message.getText());
            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            throw new DaoException("Problem with preparing statement", e);
        } finally {
            releaseConnectionIfLocal(cn);
        }
    }

    @Override
    public int findLastMessageID(int conversationID) throws DaoException {
        return findIntByInt(SQL_FIND_LAST_MESSAGE_ID, conversationID);
    }

    @Override
    public List<Conversation> findAllUserConversations(int userID) throws DaoException {
        Connection cn = getConnection();
        try (PreparedStatement ps = cn.prepareStatement(SQL_FIND_ALL_USER_CONVERSATIONS)) {
            ps.setInt(1, userID);
            ResultSet  rs = ps.executeQuery();
            return buildConversations(rs);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DaoException("Problem with preparing statement", e);
        } finally {
            releaseConnectionIfLocal(cn);
        }
    }

    @Override
    public List<Conversation> findAllConversations() throws DaoException {
        Connection cn = getConnection();
        try (Statement s = cn.createStatement()) {
            ResultSet  rs = s.executeQuery(SQL_FIND_ALL_CONVERSATIONS);
            return buildConversations(rs);
        } catch (SQLException e) {
            throw new DaoException("Problem with preparing statement", e);
        } finally {
            releaseConnectionIfLocal(cn);
        }
    }

    @Override
    public Conversation findConversationByID(int conversationID) throws DaoException {
        Connection cn = getConnection();
        try (PreparedStatement ps = cn.prepareStatement(SQL_FIND_CONVERSATION_BY_ID)) {
            ps.setInt(1, conversationID);
            ResultSet  rs = ps.executeQuery();
            Conversation conversation = new Conversation();
            User user = new User();
            if (rs.next()) {
                conversation.setConversationId(conversationID);
                conversation.setTopic(rs.getString(DAO_C_TOPIC));
                user.setUserId(rs.getInt(DAO_C_USER_ID));
                user.setLogin(rs.getString(DAO_U_USER_LOGIN));
                conversation.setUser(user);
            }
            return conversation;
        } catch (SQLException e) {
            throw new DaoException("Problem with preparing statement", e);
        } finally {
            releaseConnectionIfLocal(cn);
        }
    }

    private List<Conversation> buildConversations(ResultSet rs) throws SQLException {
        List<Conversation> conversations = new ArrayList<>();
        while (rs.next()) {
            Conversation conversation = new Conversation();
            User conversationUser = new User();
            Message lastMessage = new Message();
            conversation.setConversationId(rs.getInt(DAO_C_CONVERSATION_ID));
            conversationUser.setUserId(rs.getInt(DAO_C_USER_ID));
            conversationUser.setLogin(rs.getString(DAO_U_USER_LOGIN));
            conversation.setUser(conversationUser);
            conversation.setTopic(rs.getString(DAO_C_TOPIC));
            User messageUser = new User();
            messageUser.setLogin(rs.getString(DAO_LAST_MESSAGE_LOGIN));
            messageUser.setUserId(rs.getInt(DAO_LAST_MESSAGE_USER_ID));
            lastMessage.setUser(messageUser);
            lastMessage.setDate(rs.getTimestamp(DAO_LAST_MESSAGE_TIME));
            lastMessage.setText(rs.getString(DAO_LAST_MESSAGE_TEXT));
            conversation.setLastMessage(lastMessage);
            conversations.add(conversation);
        }
        return conversations;
    }

    @Override
    public List<Message> findAllConversationMessages(int conversationID) throws DaoException {
        Connection cn = getConnection();
        try (PreparedStatement ps = cn.prepareStatement(SQL_FIND_ALL_CONVERSATION_MESSAGES)) {
            ps.setInt(1, conversationID);
            ResultSet  rs = ps.executeQuery();
            return buildMessages(rs);
        } catch (SQLException e) {
            throw new DaoException("Problem with preparing statement", e);
        } finally {
            releaseConnectionIfLocal(cn);
        }
    }

    private List<Message> buildMessages(ResultSet rs) throws SQLException {
        List<Message> messages = new ArrayList<>();
        while (rs.next()) {
            Message message = new Message();
            User user = new User();
            message.setMessageID(rs.getInt(DAO_M_MESSAGE_ID));
            message.setConversationId(rs.getInt(DAO_M_CONVERSATION_ID));
            user.setUserId(rs.getInt(DAO_M_USER_ID));
            user.setLogin(rs.getString(DAO_U_USER_LOGIN));
            message.setUser(user);
            message.setText(rs.getString(DAO_M_TEXT));
            message.setDate(rs.getTimestamp(DAO_M_MESSAGE_TIME));
            messages.add(message);
        }
        return messages;
    }
}
