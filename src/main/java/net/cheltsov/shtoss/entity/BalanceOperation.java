package net.cheltsov.shtoss.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;

public class BalanceOperation implements Serializable {
    private static final long serialVersionUID = 1L;
    BigDecimal amount;
    String type;
    Date date;
    BigDecimal balance;

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BalanceOperation operation = (BalanceOperation) o;
        return Objects.equals(amount, operation.amount) &&
                Objects.equals(type, operation.type) &&
                Objects.equals(date, operation.date) &&
                Objects.equals(balance, operation.balance);
    }

    @Override
    public int hashCode() {

        return Objects.hash(amount, type, date, balance);
    }

    @Override
    public String toString() {
        return "BalanceOperation{" +
                "amount=" + amount +
                ", type='" + type + '\'' +
                ", date=" + date +
                ", balance=" + balance +
                '}';
    }
}
