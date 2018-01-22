package net.cheltsov.shtoss.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

public class News implements Serializable {
    private static final long serialVersionUID = 1L;
    private int newsId;
    private User user;
    private String caption;
    private String text;
    private Date date;

    public int getNewsId() {
        return newsId;
    }

    public void setNewsId(int newsId) {
        this.newsId = newsId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
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
        News news = (News) o;
        return newsId == news.newsId &&
                Objects.equals(user, news.user) &&
                Objects.equals(caption, news.caption) &&
                Objects.equals(text, news.text) &&
                Objects.equals(date, news.date);
    }

    @Override
    public int hashCode() {

        return Objects.hash(newsId, user, caption, text, date);
    }

    @Override
    public String toString() {
        return "News{" +
                "newsId=" + newsId +
                ", user=" + user +
                ", caption='" + caption + '\'' +
                ", text='" + text + '\'' +
                ", date=" + date +
                '}';
    }
}
