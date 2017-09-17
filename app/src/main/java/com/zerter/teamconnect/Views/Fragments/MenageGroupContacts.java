package com.zerter.teamconnect.Views.Fragments;

import android.Manifest;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.zerter.teamconnect.Controlers.Cache;
import com.zerter.teamconnect.Controlers.Data;
import com.zerter.teamconnect.Controlers.MenageView.ListAdapterContacts;
import com.zerter.teamconnect.Controlers.MenageView.ListAdapterGroups;
import com.zerter.teamconnect.Controlers.MenageView.OnChangeListener;
import com.zerter.teamconnect.Controlers.MyTextView;
import com.zerter.teamconnect.Controlers.PermisionControler.OnResultListener;
import com.zerter.teamconnect.Controlers.PermisionControler.PermisionControler;
import com.zerter.teamconnect.Dialogs.ConfirmCreateGroup;
import com.zerter.teamconnect.MainActivity;
import com.zerter.teamconnect.Models.Contacts;
import com.zerter.teamconnect.Models.Group;
import com.zerter.teamconnect.Models.Person;
import com.zerter.teamconnect.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MenageGroupContacts extends Fragment {
    private String TAG = getClass().getName();
    public static ListView listView;
    public static Button buttonStworzGrupe;
    Data data;
    public static List<Person> selectedContacts = new ArrayList<>();
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.grupy_kontaktow, container, false);
        listView = (ListView) view.findViewById(R.id.ListViewGrupy);
        buttonStworzGrupe = (Button) view.findViewById(R.id.buttonEdytujGrupe);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        data = new Data(getActivity());
        setAdapter(1);
        buttonStworzGrupe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (buttonStworzGrupe.getText().equals("OK")) {
                    buttonStworzGrupe.setText(R.string.add_team);
                    FragmentManager manager = getFragmentManager();
                    ConfirmCreateGroup confirmCreateGroup = new ConfirmCreateGroup();
                    Bundle bundle = new Bundle();
                    bundle.putString("selected_contacts", new Gson().toJson(selectedContacts));
                    confirmCreateGroup.setArguments(bundle);
                    confirmCreateGroup.show(manager, "ConfirmCreateGroup");
//                    setAdapter();
                } else{
                    buttonStworzGrupe.setText(R.string.OK);
                   setAdapter(2);

                }
            }
        });

    }

    public void setAdapter(int typeList) {
        ListAdapterGroups adapterGroups = new ListAdapterGroups(getActivity(), getGroups());
        switch (typeList){
            case 1:
                if (getGroups()!=null) {
                    listView.setAdapter(adapterGroups);
                }
                break;
            case 2:
                ListAdapterContacts adapter = new ListAdapterContacts(getActivity(), data.getContacts());
                listView.setAdapter(adapter);
                break;
            default:
                listView.setAdapter(adapterGroups);
        }

    }

//    private class ListAdapterContacts extends ArrayAdapter<Person> {
//
//        String TAG = getClass().getName();
//        List<Person> zaznaczoneKontakty = new ArrayList<>();
//
//        private final Activity context;
//
//        ListAdapterContacts(@NonNull Activity context, List<Person> kontakty) {
//            super(context, R.layout.contact_group_item_list, kontakty);
//            this.context = context;
//        }
//
//        ListAdapterContacts(@NonNull Activity context, List<Person> kontakty, List<Person> zaznaczoneKontakty) {
//            super(context, R.layout.contact_group_item_list, kontakty);
//            Log.d(TAG, new Gson().toJson(zaznaczoneKontakty));
//            this.zaznaczoneKontakty = zaznaczoneKontakty;
//            this.context = context;
//        }
//
//        List<View> viewList = new ArrayList<>();
//
//        @NonNull
//        @Override
//        public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
//            final Person kontakt = getItem(position);
//            LayoutInflater inflater = context.getLayoutInflater();
//            convertView = inflater.inflate(R.layout.contact_group_item_list, null);
//
//            if (viewList.size() > position && viewList.get(position) != null){
//                return viewList.get(position);
//            }
//
//            final MyTextView name = (MyTextView) convertView.findViewById(R.id.contactItemListName);
//            final MyTextView number = (MyTextView) convertView.findViewById(R.id.contactItemListNumber);
//            if (kontakt != null) {
//                name.setText(kontakt.getName());
//                number.setText(kontakt.getNumber());
//
//            } else {
//                name.setText("n/a");
//            }
//            convertView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if (name.getCurrentTextColor() == getResources().getColor(R.color.textSelected)){
//                        name.setTextColor(getResources().getColor(R.color.textColor));
//                        selectedContacts.remove(getItem(position));
//                    }else {
//                        name.setTextColor(getResources().getColor(R.color.textSelected));
//                        selectedContacts.add(getItem(position));
//
//                    }
//                }
//            });
//
//            viewList.add(convertView);
//            return convertView;
//        }
//
//
//    }

//    public List<Person> getContacts() {
//        List<Person> personList = new ArrayList<>();
//        Cursor phones = getActivity().getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
//        if (phones != null) {
//            while (phones.moveToNext()) {
//                String name = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
//                String phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
//                Person person = new Person();
//                person.setName(name);
//                person.setNumber(phoneNumber);
//                for (Person p: personList){
//                    if (p.getNumber().equals(person.getNumber())) {
//                        personList.remove(personList.size()-1);
//                    }
//                }
//
//                personList.add(person);
//                //            contacts.getPersons().add(person);
//
//            }
//
//            Collections.sort(personList, new Comparator<Person>() {
//                @Override
//                public int compare(Person o1, Person o2) {
//                    return o1.getName().compareTo(o2.getName());
//                }
//            });
//        } else {
//            Toast.makeText(getActivity(), "Brak kontaktów w telefonie", Toast.LENGTH_LONG).show();
//        }
//        if (phones != null) {
//            phones.close();
//        }
//
//        return personList;
//    }

    private  List<Group> getGroups(){
        return data.getGroups();
    }

}
