package com.twit.multiplayer_test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;


class Lobby {

    Lobby(){};

    public Lobby(String hostName, Map<String, Member>members, String name, String hostId, String maxCount, String number, ArrayList<Card> treasuresDeck, ArrayList<NavCard> navCardsDeck, String gameState, Integer turn){
        this.hostName = hostName;
        this.members = members;
        this.name = name;
        this.hostId = hostId;
        this.maxCount = maxCount;
        this.number = number;
        this.navCardsDeck = navCardsDeck;
        this.treasuresDeck = treasuresDeck;
        this.gameState = gameState;
        this.turn = turn;
    }

    public String getGameState() {
        return gameState;
    }

    public void setGameState(String gameState) {
        this.gameState = gameState;
    }

    private String gameState;

    public Integer getTurn() {
        return turn;
    }

    public void setTurn(Integer turn) {
        this.turn = turn;
    }

    private Integer turn;

    private ArrayList<Card> treasuresDeck;
    private ArrayList<NavCard> navCardsDeck;
    private String hostId;
    public String getNumber() {
        return number;
    }
    public void setNumber(String number) {
        this.number = number;
    }
    private String number;
    private String hostName;
    private String maxCount;
    private String name;
    private Map<String, Member> members;
    public String getHostId() {
        return hostId;
    }
    public void setHostId(String hostId) {
        this.hostId = hostId;
    }
    public String getHostName() {
        return hostName;
    }
    public void setHostName(String hostName) {
        this.hostName = hostName;
    }
    public String getMaxCount() {
        return maxCount;
    }
    public void setMaxCount(String maxCount) {
        this.maxCount = maxCount;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public Map<String, Member> getMembers() {
        return members;
    }
    public void setMembers(Map<String, Member> members) {
        this.members = members;
    }

    public ArrayList<Card> getTreasuresDeck() {
        return treasuresDeck;
    }

    public void setTreasuresDeck(ArrayList<Card> treasuresDeck) {
        this.treasuresDeck = treasuresDeck;
    }

    public ArrayList<NavCard> getNavCardsDeck() {
        return navCardsDeck;
    }

    public void setNavCardsDeck(ArrayList<NavCard> navCardsDeck) {
        this.navCardsDeck = navCardsDeck;
    }
}