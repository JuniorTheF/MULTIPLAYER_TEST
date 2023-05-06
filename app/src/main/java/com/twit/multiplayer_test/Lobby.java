package com.twit.multiplayer_test;

import java.util.List;
import java.util.Map;
import java.util.TreeSet;

class Lobby{
    String lobby_name;
    Integer max_members;
    TreeSet<String> members;
    Lobby(Lobby l){
        this.lobby_name = l.lobby_name;
        this.max_members = l.max_members;
        this.members = l.members;
    }
    Lobby(Integer max, TreeSet<String> members, String lobby_name){
        this.lobby_name = lobby_name;
        this.members = members;
        this.max_members = max;
    }
}

class Lobby2 {


    private String hostId;

    private String hostName;

    private String maxCount;

    private String name;

    private List<String> members;

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

    public List<String> getMembers() {
        return members;
    }

    public void setMembers(List<String> members) {
        this.members = members;
    }

}