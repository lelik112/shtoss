package net.cheltsov.shtoss.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

public class Message implements Serializable {
    private static final long serialVersionUID = 1L;
    private int messageId;
    private int conversationId;
    private User user;
    private String text;
    private Date date;

    public int getMessageId() {
        return messageId;
    }

    public void setMessageID(int message_ID) {
        this.messageId = message_ID;
    }

    public int getConversationId() {
        return conversationId;
    }

    public void setConversationId(int conversationId) {
        this.conversationId = conversationId;
    }

    public void setMessageId(int messageId) {
        this.messageId = messageId;
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
        return messageId == message.messageId &&
                conversationId == message.conversationId &&
                Objects.equals(user, message.user) &&
                Objects.equals(text, message.text) &&
                Objects.equals(date, message.date);
    }

    @Override
    public int hashCode() {

        return Objects.hash(messageId, conversationId, user, text, date);
    }

    @Override
    public String toString() {
        return "Message{" +
                "messageId=" + messageId +
                ", conversationId=" + conversationId +
                ", user=" + user +
                ", text='" + text + '\'' +
                ", date=" + date +
                '}';
    }
}
