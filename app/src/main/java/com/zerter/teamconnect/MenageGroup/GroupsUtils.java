package com.zerter.teamconnect.MenageGroup;

import android.app.Activity;

import com.zerter.teamconnect.Controlers.Data;
import com.zerter.teamconnect.MenageGroup.googlegroups.ImportGroups;
import com.zerter.teamconnect.MenageGroup.googlegroups.Item;
import com.zerter.teamconnect.Models.Group;
import com.zerter.teamconnect.Models.Person;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by krystiankowalski on 21.01.2018.
 */

public class GroupsUtils {

    public static List<Group> getGroups(Activity activity){
        List<Group> groupList = GroupsUtils.parseGoogleGroupsToApplicationGroup(activity);
        List<Group> applicationGroups = new Data(activity).getGroups();
        if (!applicationGroups.isEmpty()) {
            groupList.addAll(applicationGroups);
        }
        return groupList;
    }

    private static List<Group> parseGoogleGroupsToApplicationGroup(Activity activity){
        LinkedHashMap<Item,ArrayList<Item>> googleGroups =  new ImportGroups(activity).initContactList();

        List<Group> groupList = new ArrayList<>();


        for (Item key : googleGroups.keySet()) {
            Group group = new Group();
            group.setName(key.name);
            List<Person> personList = new ArrayList<>();

            for (Item item :
                    googleGroups.get(key)) {
                Person person = new Person();
                person.setName(item.getName());
                person.setNumber(item.getPhNo());
                personList.add(person);
            }
            group.setPersons(personList);
            group.setGoogleGroup(true);
            groupList.add(group);
        }

        return groupList;
    }
}
