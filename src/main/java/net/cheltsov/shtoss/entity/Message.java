package net.cheltsov.shtoss.entity;

import java.util.Date;

public class Message extends Entity {
    private int message_ID;
    private int conversationID;
    private User user;
    private String text;
    private Date date;

    public int getMessage_ID() {
        return message_ID;
    }

    public void setMessageID(int message_ID) {
        this.message_ID = message_ID;
    }

    public int getConversationID() {
        return conversationID;
    }

    public void setConversationID(int conversationID) {
        this.conversationID = conversationID;
    }

    public void setMessage_ID(int message_ID) {
        this.message_ID = message_ID;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
