package net.cheltsov.shtoss.entity;

import java.math.BigDecimal;
import java.sql.Date;

public class Game extends Entity {

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
    public String toString() {
        return "Game{" +
                "gameID=" + gameID +
                ", bid=" + bid +
                ", userID=" + userID +
                ", date=" + date +
                '}';
    }
}
