package com.zerter.teamconnect.Controlers;

import com.zerter.teamconnect.Models.Contacts;
import com.zerter.teamconnect.Models.Person;

import java.util.ArrayList;
import java.util.List;

/**
 * Obiekty wczytywane przy starcie apki
 */

public class Cache {
    public static List<Person> LISTA_KONTAKTOW = new ArrayList<>();
    public static List<Person> ZAZNACZONE_KONTAKTY = new ArrayList<>();
    public static List<Contacts> ZAZNACZONE_GRUPY = new ArrayList<>();
}
