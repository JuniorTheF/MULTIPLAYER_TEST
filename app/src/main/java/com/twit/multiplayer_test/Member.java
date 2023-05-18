package com.twit.multiplayer_test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class Member {



    Member(){
        this.turn = 0;
        this.state = new State();
        this.stats = new Stats();
        this.treasures = new Treasures();
        this.name = "";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private String name;

    private Stats stats;

    public Integer getTurn() {
        return turn;
    }

    public void setTurn(Integer turn) {
        this.turn = turn;
    }

    private Integer turn;
    private State state;
    private Treasures treasures;

    public Member(Stats stats, State state, Treasures treasures, Integer turn, String name) {
        this.stats = stats;
        this.state = state;
        this.treasures = treasures;
        this.turn = turn;
        this.name = name;
    }

    public Stats getStats() {
        return stats;
    }

    public void setStats(Stats stats) {
        this.stats = stats;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public Treasures getTreasures() {
        return treasures;
    }

    public void setTreasures(Treasures treasures) {
        this.treasures = treasures;
    }

    @Override
    public String toString() {
        return "Member{" +
                "name='" + name + '\'' +
                ", stats=" + stats +
                ", turn=" + turn +
                ", state=" + state +
                ", treasures=" + treasures +
                '}';
    }
}

class Treasures{
    private ArrayList<Card> open;
    private ArrayList<Card> close;

    public Treasures(){
        this.open = new ArrayList<>();
        this.close = new ArrayList<>();
    };

    public Treasures(ArrayList<Card> open, ArrayList<Card> close) {
        this.open = open;
        this.close = close;
    }


    public ArrayList<Card> getOpen() {
        return open;
    }

    public void setOpen(ArrayList<Card> open) {
        this.open = open;
    }

    public ArrayList<Card> getClose() {
        return close;
    }

    public void setClose(ArrayList<Card> close) {
        this.close = close;
    }
}

class State{



    public State(){
        this.status = "alive";
        this.overboard = 0;
        this.seat = 0;
        this.injuries = 0;
        this.thirst = 0;
        this.brawled = 0;
        this.pulled = 0;
    }

    private String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getOverboard() {
        return overboard;
    }

    public void setOverboard(Integer overboard) {
        this.overboard = overboard;
    }

    private Integer overboard;
    private Integer injuries;
    private Integer thirst;
    private Integer brawled;
    private Integer pulled;

    public Integer getSeat() {
        return seat;
    }

    public void setSeat(Integer seat) {
        this.seat = seat;
    }

    private Integer seat;

    public State(Integer injuries, Integer thirst, Integer brawled, Integer pulled, Integer seat) {
        this.injuries = injuries;
        this.thirst = thirst;
        this.brawled = brawled;
        this.pulled = pulled;
        this.seat = seat;
    }


    public Integer getInjuries() {
        return injuries;
    }

    public void setInjuries(Integer injuries) {
        this.injuries = injuries;
    }

    public Integer getThirst() {
        return thirst;
    }

    public void setThirst(Integer thirst) {
        this.thirst = thirst;
    }

    public Integer getBrawled() {
        return brawled;
    }

    public void setBrawled(Integer brawled) {
        this.brawled = brawled;
    }

    public Integer getPulled() {
        return pulled;
    }

    public void setPulled(Integer pulled) {
        this.pulled = pulled;
    }
}

class Stats{

    @Override
    public String toString() {
        return "Stats{" +
                "role='" + role + '\'' +
                ", power=" + power +
                ", survival_bonus=" + survival_bonus +
                ", enemy='" + enemy + '\'' +
                ", friend='" + friend + '\'' +
                '}';
    }

    public Stats(){
        this.role = "";
        this.power = 0;
        this.survival_bonus = 0;
        this.enemy = "";
        this.friend = "";
    }
    private String role;
    private Integer power;
    private Integer survival_bonus;
    private String enemy;
    private String friend;

    public Stats(String role, Integer power, Integer survival_bonus, String enemy, String friend) {
        this.role = role;
        this.power = power;
        this.survival_bonus = survival_bonus;
        this.enemy = enemy;
        this.friend = friend;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Integer getPower() {
        return power;
    }

    public void setPower(Integer power) {
        this.power = power;
    }

    public Integer getSurvival_bonus() {
        return survival_bonus;
    }

    public void setSurvival_bonus(Integer survival_bonus) {
        this.survival_bonus = survival_bonus;
    }

    public String getEnemy() {
        return enemy;
    }

    public void setEnemy(String enemy) {
        this.enemy = enemy;
    }

    public String getFriend() {
        return friend;
    }

    public void setFriend(String friend) {
        this.friend = friend;
    }
}