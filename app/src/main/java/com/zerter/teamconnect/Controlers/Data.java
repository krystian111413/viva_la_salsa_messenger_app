package com.zerter.teamconnect.Controlers;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zerter.teamconnect.Models.Contacts;
import com.zerter.teamconnect.Models.Group;
import com.zerter.teamconnect.Models.Message;
import com.zerter.teamconnect.Models.Person;
import com.zerter.teamconnect.Models.Template;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Klasa zarządzająca danymi lokalnymi aplikacji
 */

public class Data {
    private Context context;

    public Data(Context context) {
        this.context = context;
    }

    public void clearData(){
        SharedPreferences sharedPreferences = context.getSharedPreferences("MyData", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }
    public void setMessagesPlaned(String value){
        SharedPreferences sharedPreferences = context.getSharedPreferences("MyData", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("messagesPlaned", value);
        editor.apply();
    }
    public List<Message> getMessagesPlaned(){
        SharedPreferences sharedPreferences = context.getSharedPreferences("MyData", Context.MODE_PRIVATE);
        String s = sharedPreferences.getString("messagesPlaned", "");
        java.lang.reflect.Type type = new TypeToken<List<Message>>(){}.getType();
        return new Gson().fromJson(s,type);
    }
    public void setMessages(String value){
        SharedPreferences sharedPreferences = context.getSharedPreferences("MyData", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("messages", value);
        editor.apply();
    }
    public List<Message> getMessages(){
        SharedPreferences sharedPreferences = context.getSharedPreferences("MyData", Context.MODE_PRIVATE);
        String s = sharedPreferences.getString("messages", "");
        java.lang.reflect.Type type = new TypeToken<List<Message>>(){}.getType();
        return new Gson().fromJson(s,type);
    }
    public void setTemplates(String value){
        SharedPreferences sharedPreferences = context.getSharedPreferences("MyData", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("templates", value);
        editor.apply();
    }
    public List<Template> getTemplates(){
        SharedPreferences sharedPreferences = context.getSharedPreferences("MyData", Context.MODE_PRIVATE);
        String s = sharedPreferences.getString("templates", "");
        java.lang.reflect.Type type = new TypeToken<List<Template>>(){}.getType();
        return new Gson().fromJson(s,type);
    }
    public void zapiszGrupyKontaktow(String value){
        SharedPreferences sharedPreferences = context.getSharedPreferences("MyData", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("contacts", value);
        editor.apply();
    }
    public List<Contacts> wczytajGrupyKontaktow(){
        SharedPreferences sharedPreferences = context.getSharedPreferences("MyData", Context.MODE_PRIVATE);
        String s = sharedPreferences.getString("contacts", "");
        java.lang.reflect.Type type = new TypeToken<List<Contacts>>(){}.getType();
        return new Gson().fromJson(s,type);
    }
    public List<Group> getGroups(){
        SharedPreferences sharedPreferences = context.getSharedPreferences("MyData", Context.MODE_PRIVATE);
        String s = sharedPreferences.getString("contacts", "");
        java.lang.reflect.Type type = new TypeToken<List<Group>>(){}.getType();
        return new Gson().fromJson(s,type);
    }

    public void setGroups(String value){
        SharedPreferences sharedPreferences = context.getSharedPreferences("MyData", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("contacts", value);
        editor.apply();
    }

    public void updateGroup(Group original, Group newGroup){
        List<Group> groupList = getGroups();
        List<Group> newGroupList = new ArrayList<>();
        for (Group group :
                groupList) {
            if (group.getName().equals(original.getName())){
                newGroupList.add(newGroup);
            }else {
                newGroupList.add(group);
            }
        }
        setGroups(new  Gson().toJson(newGroupList));
    }

    public List<String> getGroupsNames(){
        List<String> names = new ArrayList<>();
        if (getGroups()==null){
            return names;
        }
        for (Group group :
                getGroups()) {
            names.add(group.getName());
        }
        return names;
    }
    public List<Person> getContacts() {
        List<Person> personList = new ArrayList<>();
        Cursor phones = context.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
        if (phones != null) {
            while (phones.moveToNext()) {
                String name = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                String phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                Person person = new Person();
                person.setName(name);
                person.setNumber(phoneNumber);
                for (Person p: personList){
                    if (p.getNumber().equals(person.getNumber())) {
                        personList.remove(personList.size()-1);
                    }
                }

                personList.add(person);
                //            contacts.getPersons().add(person);

            }

            Collections.sort(personList, new Comparator<Person>() {
                @Override
                public int compare(Person o1, Person o2) {
                    return o1.getName().compareTo(o2.getName());
                }
            });
        } else {
            Toast.makeText(context, "Brak kontaktów w telefonie", Toast.LENGTH_LONG).show();
        }
        if (phones != null) {
            phones.close();
        }

        return personList;
    }
}
