package com.twit.multiplayer_test;

public class TradeItem {
    Card card;

    public TradeItem(Card card, boolean isChoosen) {
        this.card = card;
        this.isChoosen = isChoosen;
    }

    public TradeItem() {
    }

    public Card getCard() {
        return card;
    }

    public void setCard(Card card) {
        this.card = card;
    }

    public boolean isChoosen() {
        return isChoosen;
    }

    public void setChoosen(boolean choosen) {
        isChoosen = choosen;
    }

    boolean isChoosen;
}
