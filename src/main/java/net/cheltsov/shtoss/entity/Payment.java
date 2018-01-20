package net.cheltsov.shtoss.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.Objects;

public class Payment implements Serializable {
    private static final long serialVersionUID = 1L;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Payment payment = (Payment) o;
        return ID == payment.ID &&
                userID == payment.userID &&
                Objects.equals(amount, payment.amount) &&
                Objects.equals(date, payment.date);
    }

    @Override
    public int hashCode() {

        return Objects.hash(ID, amount, userID, date);
    }

    @Override
    public String toString() {
        return "Payment{" +
                "ID=" + ID +
                ", amount=" + amount +
                ", userID=" + userID +
                ", date=" + date +
                '}';
    }
}
