package net.cheltsov.shtoss.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.Objects;

public class Payment implements Serializable {
    private static final long serialVersionUID = 1L;
    private int paymentId;
    private BigDecimal amount;
    private int userId;
    private Date date;

    public int getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(int paymentId) {
        this.paymentId = paymentId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
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
        Payment payment = (Payment) o;
        return paymentId == payment.paymentId &&
                userId == payment.userId &&
                Objects.equals(amount, payment.amount) &&
                Objects.equals(date, payment.date);
    }

    @Override
    public int hashCode() {

        return Objects.hash(paymentId, amount, userId, date);
    }

    @Override
    public String toString() {
        return "Payment{" +
                "paymentId=" + paymentId +
                ", amount=" + amount +
                ", userId=" + userId +
                ", date=" + date +
                '}';
    }
}
