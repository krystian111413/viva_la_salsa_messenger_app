package com.zerter.teamconnect.Models;

/**
 * Model Message
 */

public class Message {
    private String message;
    private String date;
    private Team team = new Team();

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }
}
