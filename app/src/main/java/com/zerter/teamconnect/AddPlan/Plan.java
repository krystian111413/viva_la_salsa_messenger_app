package com.zerter.teamconnect.AddPlan;

import com.zerter.teamconnect.Models.Group;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Model planu
 */

public class Plan {
    private String id;
    private String date;
    private String time;
    private String text;
    private String name;
    private Integer repeatValue; // 0 - 4: none -> daily -> weekly -> monthly -> yearly
    private List<Group> groups = new ArrayList<>();
    private Boolean isPlaned = false;
    private Boolean isEdited = false;

    public Plan(String date, String time, String text, String name, Integer repeatValue, List<Group> groups) {
        this.id = UUID.randomUUID().toString();
        this.date = date;
        this.time = time;
        this.text = text;
        this.name = name;
        this.repeatValue = repeatValue;
        this.groups = groups;
        this.isPlaned = false;
        this.isEdited = false;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Integer getRepeatValue() {
        return repeatValue;
    }

    public void setRepeatValue(Integer repeatValue) {
        this.repeatValue = repeatValue;
    }

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

    public Boolean getPlaned() {
        return isPlaned;
    }

    public void setPlaned(Boolean planed) {
        isPlaned = planed;
    }

    public Boolean getEdited() {
        return isEdited;
    }

    public void setEdited(Boolean edited) {
        isEdited = edited;
    }
}
