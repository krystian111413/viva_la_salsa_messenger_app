package com.zerter.teamconnect.Models;

/**
 * Model Message
 */

public class Message {
    private String message;
    private String date;
    private Group group = new Group();

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


    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }
}
