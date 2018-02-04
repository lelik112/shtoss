package net.cheltsov.shtoss.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

public class User implements Serializable {
    private static final long serialVersionUID = 1L;
    private String login;
    transient private String password;
    private String email;
    private String firstName;
    private String lastName;
    private Role role = Role.GUEST;
    private int userId;
    private BigDecimal balance = new BigDecimal(0);

    public User() {
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        User user = (User) o;
        return userId == user.userId &&
                Objects.equals(login, user.login) &&
                Objects.equals(password, user.password) &&
                Objects.equals(email, user.email) &&
                Objects.equals(firstName, user.firstName) &&
                Objects.equals(lastName, user.lastName) &&
                role == user.role &&
                Objects.equals(balance, user.balance);
    }

    @Override
    public int hashCode() {
        return Objects.hash(login, password, email, firstName, lastName, role, userId, balance);
    }

    @Override
    public String toString() {
        return login;
    }

    public enum Role {
        GUEST, USER, ADMIN;
        public int getRoleID() {
            return ordinal();
        }
    }
}
