package net.cheltsov.shtoss.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.Objects;

public class Game implements Serializable {

    private static final long serialVersionUID = 1L;
    private int gameID;
    private BigDecimal bid;
    private int userID;
    private Date date;

    public Game() {
    }

    public int getGameID() {
        return gameID;
    }

    public void setGameID(int gameID) {
        this.gameID = gameID;
    }

    public BigDecimal getBid() {
        return bid;
    }

    public void setBid(BigDecimal bid) {
        this.bid = bid;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
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
        return gameID == game.gameID &&
                userID == game.userID &&
                Objects.equals(bid, game.bid) &&
                Objects.equals(date, game.date);
    }

    @Override
    public int hashCode() {

        return Objects.hash(gameID, bid, userID, date);
    }

    @Override
    public String toString() {
        return "Game{" +
                "gameID=" + gameID +
                ", bid=" + bid +
                ", userID=" + userID +
                ", date=" + date +
                '}';
    }
}
