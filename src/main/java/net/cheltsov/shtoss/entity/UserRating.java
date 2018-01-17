package net.cheltsov.shtoss.entity;

import java.math.BigDecimal;

public class UserRating extends Entity {
    String userLogin;
    int allGames;
    int winGames;
    BigDecimal totalBid;

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
}
