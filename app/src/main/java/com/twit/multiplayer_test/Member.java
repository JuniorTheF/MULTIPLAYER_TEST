package com.twit.multiplayer_test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class Member {

    Member(){
        this.state = new State();
        this.stats = new Stats();
        this.treasures = new Treasures();
    }


    private Stats stats;
    private State state;
    private Treasures treasures;

    public Member(Stats stats, State state, Treasures treasures) {
        this.stats = stats;
        this.state = state;
        this.treasures = treasures;
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
}

class Treasures{
    private ArrayList<String> open;
    private ArrayList<String> close;

    public Treasures(){
        this.open = new ArrayList<>();
        this.close = new ArrayList<>();
        this.close.add("");
        this.open.add("");
    };

    public Treasures(ArrayList<String> open, ArrayList<String> close) {
        this.open = open;
        this.close = close;
    }


    public ArrayList<String> getOpen() {
        return open;
    }

    public void setOpen(ArrayList<String> open) {
        this.open = open;
    }

    public ArrayList<String> getClose() {
        return close;
    }

    public void setClose(ArrayList<String> close) {
        this.close = close;
    }
}

class State{



    public State(){
        this.injuries = 0;
        this.fatigue = 0;
        this.brawled = 0;
        this.pulled = 0;
    }

    private Integer injuries;
    private Integer fatigue;
    private Integer brawled;
    private Integer pulled;

    public State(Integer injuries, Integer fatigue, Integer brawled, Integer pulled) {
        this.injuries = injuries;
        this.fatigue = fatigue;
        this.brawled = brawled;
        this.pulled = pulled;
    }


    public Integer getInjuries() {
        return injuries;
    }

    public void setInjuries(Integer injuries) {
        this.injuries = injuries;
    }

    public Integer getFatigue() {
        return fatigue;
    }

    public void setFatigue(Integer fatigue) {
        this.fatigue = fatigue;
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