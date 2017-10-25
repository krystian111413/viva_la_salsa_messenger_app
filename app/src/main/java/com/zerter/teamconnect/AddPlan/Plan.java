package com.zerter.teamconnect.AddPlan;

import com.zerter.teamconnect.Models.Group;

import java.util.ArrayList;
import java.util.List;

/**
 * Model planu
 */

public class Plan {
    private String date;
    private String text;
    private String name;
    private Boolean week;
    private Boolean month;
    private Boolean year;
    private List<Group> groups = new ArrayList<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Boolean getWeek() {
        return week;
    }

    public void setWeek(Boolean week) {
        this.week = week;
    }

    public Boolean getMonth() {
        return month;
    }

    public void setMonth(Boolean month) {
        this.month = month;
    }

    public Boolean getYear() {
        return year;
    }

    public void setYear(Boolean year) {
        this.year = year;
    }

    public List<Group> getGroups() {
        return groups;
    }

    public void setGroups(List<Group> groups) {
        this.groups = groups;
    }
}
