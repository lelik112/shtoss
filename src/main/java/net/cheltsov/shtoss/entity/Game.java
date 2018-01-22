package net.cheltsov.shtoss.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.Objects;

public class Game implements Serializable {

    private static final long serialVersionUID = 1L;
    private int gameId;
    private BigDecimal bid;
    private int userId;
    private Date date;

    public Game() {
    }

    public int getGameId() {
        return gameId;
    }

    public void setGameId(int gameId) {
        this.gameId = gameId;
    }

    public BigDecimal getBid() {
        return bid;
    }

    public void setBid(BigDecimal bid) {
        this.bid = bid;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
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
        Game game = (Game) o;
        return gameId == game.gameId &&
                userId == game.userId &&
                Objects.equals(bid, game.bid) &&
                Objects.equals(date, game.date);
    }

    @Override
    public int hashCode() {

        return Objects.hash(gameId, bid, userId, date);
    }

    @Override
    public String toString() {
        return "Game{" +
                "gameId=" + gameId +
                ", bid=" + bid +
                ", userId=" + userId +
                ", date=" + date +
                '}';
    }
}
