package net.cheltsov.shtoss.entity;

import java.util.Date;
import java.util.List;

public class Conversation extends Entity {
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

}
