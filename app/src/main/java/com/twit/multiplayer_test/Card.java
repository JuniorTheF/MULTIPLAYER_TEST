package com.twit.multiplayer_test;

import java.util.ArrayList;
import java.util.List;

public class Card{
    public String getResource() {
        return resource;
    }

    public void setResource(String resource) {
        this.resource = resource;
    }

    String resource;
    String type;
    String name;

    Card(){
    };

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    Card(String type, String name, String resource) {
        this.name = name;
        this.type = type;
        this.resource = resource;
    }
}

class NavCard extends Card {
    private List<String> bort;
    private int gull;
    private List<String> thirst;
    private int fighters;
    private int workers;

    NavCard(List<String> bort, List<String> thirst, int gull, int fighters, int workers, String resource) {
        this.bort = bort;
        this.thirst = thirst;
        this.gull = gull;
        this.fighters = fighters;
        this.workers = workers;
        this.resource = resource;
    };

    NavCard(){

    };



    public List<String> getBort() {
        return bort;
    }

    public void setBort(List<String> bort) {
        this.bort = bort;
    }

    public int getGull() {
        return gull;
    }

    public void setGull(int gull) {
        this.gull = gull;
    }

    public List<String> getThirst() {
        return thirst;
    }

    public void setThirst(List<String> thirst) {
        this.thirst = thirst;
    }

    public int getFighters() {
        return fighters;
    }

    public void setFighters(int fighters) {
        this.fighters = fighters;
    }

    public int getWorkers() {
        return workers;
    }

    public void setWorkers(int workers) {
        this.workers = workers;
    }


}
