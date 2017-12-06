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
    private Integer repeatValue; // 0 - 4: none -> daily -> weekly -> monthly -> yearly

    public Integer getRepeatValue() {
        return repeatValue;
    }

    public void setRepeatValue(Integer repeatValue) {
        this.repeatValue = repeatValue;
    }

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

    public List<Group> getGroups() {
        return groups;
    }

    public void setGroups(List<Group> groups) {
        this.groups = groups;
    }
}
