package com.twit.multiplayer_test;

public class BrawlMember {

    private Member member;
    private boolean ready;

    public BrawlMember() {
    }

    public BrawlMember(Member member, boolean ready) {
        this.member = member;
        this.ready = ready;
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public boolean isReady() {
        return ready;
    }

    public void setReady(boolean ready) {
        this.ready = ready;
    }
}
