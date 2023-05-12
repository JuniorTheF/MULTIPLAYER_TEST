package com.twit.multiplayer_test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;


class Lobby {

    Lobby(){};

    public Lobby(String hostName, Map<String, Member>members, String name, String hostId, String maxCount, String number, ArrayList<Card> treasuresDeck, ArrayList<NavCard> navCardsDeck, String gameState, Integer turn, Brawl brawl, HeroDialog dialog){
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
        this.brawl = brawl;
        this.dialog = dialog;
    }

    public HeroDialog getDialog() {
        return dialog;
    }

    public void setDialog(HeroDialog dialog) {
        this.dialog = dialog;
    }

    HeroDialog dialog;

    public Brawl getBrawl() {
        return brawl;
    }

    public void setBrawl(Brawl brawl) {
        this.brawl = brawl;
    }

    private Brawl brawl;
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

class Brawl{
    String state;

    public String getGoal() {
        return goal;
    }

    public void setGoal(String goal) {
        this.goal = goal;
    }

    String goal;
    ArrayList<Member> attacker;
    ArrayList<Member> defender;

    Brawl(){}

    public Brawl(String state, ArrayList<Member> attacker, ArrayList<Member> defender, String goal) {
        this.state = state;
        this.attacker = attacker;
        this.defender = defender;
        this.goal = goal;
    }


    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public ArrayList<Member> getAttacker() {
        return attacker;
    }

    public void setAttacker(ArrayList<Member> attacker) {
        this.attacker = attacker;
    }

    public ArrayList<Member> getDefender() {
        return defender;
    }

    public void setDefender(ArrayList<Member> defender) {
        this.defender = defender;
    }
}

class HeroDialog{
    Member sender;
    Integer sender_accept;

    HeroDialog(){}

    public HeroDialog(Member sender, Integer sender_accept, Integer reciever_accept, Member reciever, ArrayList<Card> sender_open, ArrayList<Card> sender_close, ArrayList<Card> receiver_open, ArrayList<Card> receiver_close) {
        this.sender = sender;
        this.sender_accept = sender_accept;
        this.reciever_accept = reciever_accept;
        this.reciever = reciever;
        this.sender_open = sender_open;
        this.sender_close = sender_close;
        this.receiver_open = receiver_open;
        this.receiver_close = receiver_close;
    }

    public Member getSender() {
        return sender;
    }

    public void setSender(Member sender) {
        this.sender = sender;
    }

    public Integer getSender_accept() {
        return sender_accept;
    }

    public void setSender_accept(Integer sender_accept) {
        this.sender_accept = sender_accept;
    }

    public Integer getReciever_accept() {
        return reciever_accept;
    }

    public void setReciever_accept(Integer reciever_accept) {
        this.reciever_accept = reciever_accept;
    }

    public Member getReciever() {
        return reciever;
    }

    public void setReciever(Member reciever) {
        this.reciever = reciever;
    }

    public ArrayList<Card> getSender_open() {
        return sender_open;
    }

    public void setSender_open(ArrayList<Card> sender_open) {
        this.sender_open = sender_open;
    }

    public ArrayList<Card> getSender_close() {
        return sender_close;
    }

    public void setSender_close(ArrayList<Card> sender_close) {
        this.sender_close = sender_close;
    }

    public ArrayList<Card> getReceiver_open() {
        return receiver_open;
    }

    public void setReceiver_open(ArrayList<Card> receiver_open) {
        this.receiver_open = receiver_open;
    }

    public ArrayList<Card> getReceiver_close() {
        return receiver_close;
    }

    public void setReceiver_close(ArrayList<Card> receiver_close) {
        this.receiver_close = receiver_close;
    }

    Integer reciever_accept;
    Member reciever;
    ArrayList<Card> sender_open;
    ArrayList<Card> sender_close;
    ArrayList<Card> receiver_open;
    ArrayList<Card> receiver_close;

}