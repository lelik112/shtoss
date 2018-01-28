package net.cheltsov.shtoss.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

public class UserRating implements Serializable {
    private static final long serialVersionUID = 1L;
    private String userLogin;
    private int allGames;
    private int winGames;
    private BigDecimal totalBid;

    public String getUserLogin() {
        return userLogin;
    }

    public void setUserLogin(String userLogin) {
        this.userLogin = userLogin;
    }

    public int getAllGames() {
        return allGames;
    }

    public void setAllGames(int allGames) {
        this.allGames = allGames;
    }

    public int getWinGames() {
        return winGames;
    }

    public void setWinGames(int winGames) {
        this.winGames = winGames;
    }

    public BigDecimal getTotalBid() {
        return totalBid;
    }

    public void setTotalBid(BigDecimal totalBid) {
        this.totalBid = totalBid;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserRating rating = (UserRating) o;
        return allGames == rating.allGames &&
                winGames == rating.winGames &&
                Objects.equals(userLogin, rating.userLogin) &&
                Objects.equals(totalBid, rating.totalBid);
    }

    @Override
    public int hashCode() {

        return Objects.hash(userLogin, allGames, winGames, totalBid);
    }

    @Override
    public String toString() {
        return "UserRating{" +
                "userLogin='" + userLogin + '\'' +
                ", allGames=" + allGames +
                ", winGames=" + winGames +
                ", totalBid=" + totalBid +
                '}';
    }
}
