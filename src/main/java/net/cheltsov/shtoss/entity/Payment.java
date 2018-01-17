package net.cheltsov.shtoss.entity;

import java.math.BigDecimal;
import java.sql.Date;

public class Payment extends Entity {
    private int ID; // TODO: 14.12.2017 Можно тут int?
    private BigDecimal amount;
    private int userID;
    private Date date;

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
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
}
