package com.kowalski.krystian.vivalasalsamessenger;

import java.util.ArrayList;
import java.util.List;

/**
 * Model listy kontaktow
 */

class Contacts {
    private List<Person> persons = new ArrayList<>();
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    List<Person> getPersons() {
        return persons;
    }

    void setPersons(List<Person> persons) {
        this.persons = persons;
    }
}
