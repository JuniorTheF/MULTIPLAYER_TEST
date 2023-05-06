package com.twit.multiplayer_test;

import java.util.List;
import java.util.Map;
import java.util.TreeSet;


class Lobby {

    Lobby(){};

    public Lobby(String hostName, List<String>members, String name, String hostId, String maxCount, String number){
        this.hostName = hostName;
        this.members = members;
        this.name = name;
        this.hostId = hostId;
        this.maxCount = maxCount;
        this.number = number;
    }


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