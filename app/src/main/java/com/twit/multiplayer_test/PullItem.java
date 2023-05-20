package com.twit.multiplayer_test;

public class PullItem {

    NavCard navCard;
    boolean isChoosen;

    public PullItem() {
    }

    public PullItem(NavCard navCard, boolean isChoosen) {
        this.navCard = navCard;
        this.isChoosen = isChoosen;
    }

    public NavCard getNavCard() {
        return navCard;
    }

    public void setNavCard(NavCard navCard) {
        this.navCard = navCard;
    }

    public boolean isChoosen() {
        return isChoosen;
    }

    public void setChoosen(boolean choosen) {
        isChoosen = choosen;
    }
}
