package net.cheltsov.shtoss.entity;

public enum Suit {
    SPADES(0x2664), HEARTS(0x2661), DIAMONDS(0x2662), CLUBS(0x2667);

    private final int code;

    Suit(int code) {
        this.code = code;
    }

    public String getHtmlCode() {
        return "&#" + code + ';';
    }
}
