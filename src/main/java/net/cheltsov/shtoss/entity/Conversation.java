package net.cheltsov.shtoss.entity;

import java.io.Serializable;
import java.util.Objects;

public class Conversation implements Serializable {
    private static final long serialVersionUID = 1L;
    private String topic;
    private int conversationID;
    private User user;
    private Message lastMessage;

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public int getConversationID() {
        return conversationID;
    }

    public void setConversationID(int conversationID) {
        this.conversationID = conversationID;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Message getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(Message lastMessage) {
        this.lastMessage = lastMessage;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Conversation that = (Conversation) o;
        return conversationID == that.conversationID &&
                Objects.equals(topic, that.topic) &&
                Objects.equals(user, that.user) &&
                Objects.equals(lastMessage, that.lastMessage);
    }

    @Override
    public int hashCode() {

        return Objects.hash(topic, conversationID, user, lastMessage);
    }

    @Override
    public String toString() {
        return "Conversation{" +
                "topic='" + topic + '\'' +
                ", conversationID=" + conversationID +
                ", user=" + user +
                ", lastMessage=" + lastMessage +
                '}';
    }
}
