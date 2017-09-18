package com.zerter.teamconnect.Models;

import java.util.ArrayList;
import java.util.List;

/**
 * Model listy kontaktow
 */

public class Team {
    private List<Person> persons = new ArrayList<>();
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Person> getPersons() {
        return persons;
    }

    public void setPersons(List<Person> persons) {
        this.persons = persons;
    }
}
