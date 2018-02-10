package com.zerter.teamconnect.Models;

import android.content.Context;

import com.zerter.teamconnect.MenageGroup.googlegroups.ImportGroups;

import java.util.ArrayList;
import java.util.List;

/**
 * Model grupy
 */

public class Group {
    private String id;
    private List<Person> persons = new ArrayList<>();
    private String name;
    private Boolean isGoogleGroup = false;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Boolean getGoogleGroup() {
        return isGoogleGroup;
    }

    public void setGoogleGroup(Boolean googleGroup) {
        isGoogleGroup = googleGroup;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Person> getPersons() {
        return persons;

    }

    public List<Person> getPersons(Context context) {
//        return persons;
        if (isGoogleGroup) {
            return new ImportGroups(context).getGroupMembersById(id);
        } else {
            return persons;
        }
    }

    public void setPersons(List<Person> persons) {
        this.persons = persons;
    }
}
