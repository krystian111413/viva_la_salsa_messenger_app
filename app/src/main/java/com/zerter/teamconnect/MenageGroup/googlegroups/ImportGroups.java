package com.zerter.teamconnect.MenageGroup.googlegroups;

import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.GroupMembership;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.Groups;

import com.zerter.teamconnect.Models.Group;
import com.zerter.teamconnect.Models.Person;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by krystiankowalski on 12.01.2018.
 */

public class ImportGroups {
    private Context context;
    static List<Person> contacts = new ArrayList<>();

    public ImportGroups(Context context) {
        this.context = context;
        if (contacts.isEmpty()){
            contacts = new com.zerter.teamconnect.Controlers.Data(context).getContacts();
        }
    }

    public ArrayList<Item> fetchGroups() {
        ArrayList<Item> groupList = new ArrayList<Item>();
        String[] projection = new String[]{Groups._ID, Groups.TITLE};
        Cursor cursor = context.getContentResolver().query(Groups.CONTENT_URI,
                projection, null, null, null);
        ArrayList<String> groupTitle = new ArrayList<String>();
        while (cursor.moveToNext()) {
            Item item = new Item();
            item.id = cursor.getString(cursor.getColumnIndex(Groups._ID));
            String groupName = cursor.getString(cursor.getColumnIndex(Groups.TITLE));

            if (groupName.contains("Group:"))
                groupName = groupName.substring(groupName.indexOf("Group:") + "Group:".length()).trim();

            if (groupName.contains("Favorite_"))
                groupName = "Favorite";

            if (groupName.contains("Starred in Android") || groupName.contains("My Contacts"))
                continue;

            if (groupTitle.contains(groupName)) {
                for (Item group : groupList) {
                    if (group.name.equals(groupName)) {
                        group.id += "," + item.id;
                        break;
                    }
                }
            } else {
                groupTitle.add(groupName);
                item.name = groupName;
                groupList.add(item);
            }

        }

        cursor.close();
        Collections.sort(groupList, new Comparator<Item>() {
            public int compare(Item item1, Item item2) {
                return item2.name.compareTo(item1.name) < 0
                        ? 0 : -1;
            }
        });
        return groupList;
    }

    public LinkedHashMap<Item, ArrayList<Item>> initContactList() {
        LinkedHashMap<Item, ArrayList<Item>> groupList = new LinkedHashMap<Item, ArrayList<Item>>();
        ArrayList<Item> groupsList = fetchGroups();
        for (Item item : groupsList) {
            String[] ids = item.id.split(",");
            ArrayList<Item> groupMembers = new ArrayList<Item>();
            for (int i = 0; i < ids.length; i++) {
                String groupId = ids[i];
                groupMembers.addAll(fetchGroupMembers(groupId));
            }
//            item.name = item.name +" ("+groupMembers.size()+")";
            groupList.put(item, groupMembers);
        }
        return groupList;
    }

    public List<Person> getGroupMembersById(String id) {

        String[] ids = id.split(",");
        ArrayList<Item> groupMembers = new ArrayList<Item>();
        for (int i = 0; i < ids.length; i++) {
            String groupId = ids[i];
            groupMembers.addAll(fetchGroupMembers(groupId));
        }

        //parse to list persons
        List<Person> personList = new ArrayList<>();
        for (Item item :
                groupMembers) {
            Person person = new Person();
            person.setName(item.getName());
            person.setNumber(item.getPhNo());
            personList.add(person);
        }

        return personList;
    }

    public List<Group> getEmptyGroups() {
        List<Group> groupList = new ArrayList<>();
        ArrayList<Item> groupsList = fetchGroups();
        for (Item item :
                groupsList) {
            Group group = new Group();
            group.setId(item.id);
            group.setGoogleGroup(true);
            group.setName(item.name);
            groupList.add(group);
        }
        return groupList;
    }

    private ArrayList<Item> fetchGroupMembers(String groupId) {
        ArrayList<Item> groupMembers = new ArrayList<Item>();
        String where = GroupMembership.GROUP_ROW_ID + "=" + groupId
                + " AND "
                + GroupMembership.MIMETYPE + "='"
                + GroupMembership.CONTENT_ITEM_TYPE + "'";
        String[] projection = new String[]{GroupMembership.RAW_CONTACT_ID, ContactsContract.Data.DISPLAY_NAME};
        Cursor cursor = context.getContentResolver().query(ContactsContract.Data.CONTENT_URI, projection, where, null,
                ContactsContract.Data.DISPLAY_NAME + " COLLATE LOCALIZED ASC");
        while (cursor.moveToNext()) {
            Item item = new Item();
            item.name = cursor.getString(cursor.getColumnIndex(ContactsContract.Data.DISPLAY_NAME));
            item.id = cursor.getString(cursor.getColumnIndex(GroupMembership.RAW_CONTACT_ID));
            Cursor phoneFetchCursor = context.getContentResolver().query(Phone.CONTENT_URI,
                    new String[]{Phone.NUMBER, Phone.DISPLAY_NAME, Phone.TYPE},
                    Phone.CONTACT_ID + "=" + item.id, null, null);
            while (phoneFetchCursor.moveToNext()) {
                item.phNo = phoneFetchCursor.getString(phoneFetchCursor.getColumnIndex(Phone.NUMBER));
                item.phDisplayName = phoneFetchCursor.getString(phoneFetchCursor.getColumnIndex(Phone.DISPLAY_NAME));
                item.phType = phoneFetchCursor.getString(phoneFetchCursor.getColumnIndex(Phone.TYPE));
                if (!item.name.equals(item.phDisplayName)) {
                    for (Person p :
                            contacts) {
                        if (p.getName().equals(item.name)) {
                            item.phNo = p.getNumber();
                            item.phDisplayName = p.getName();
                            break;
                        }
                    }
                }
            }
            phoneFetchCursor.close();


            groupMembers.add(item);
        }
        cursor.close();
        return groupMembers;
    }


}
