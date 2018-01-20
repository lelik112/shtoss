package net.cheltsov.shtoss.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

public class Message implements Serializable {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Message message = (Message) o;
        return message_ID == message.message_ID &&
                conversationID == message.conversationID &&
                Objects.equals(user, message.user) &&
                Objects.equals(text, message.text) &&
                Objects.equals(date, message.date);
    }

    @Override
    public int hashCode() {

        return Objects.hash(message_ID, conversationID, user, text, date);
    }

    @Override
    public String toString() {
        return "Message{" +
                "message_ID=" + message_ID +
                ", conversationID=" + conversationID +
                ", user=" + user +
                ", text='" + text + '\'' +
                ", date=" + date +
                '}';
    }
}
