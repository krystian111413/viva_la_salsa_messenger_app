package com.zerter.teamconnect.Views.Fragments;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import com.google.gson.Gson;
import com.zerter.teamconnect.Controlers.Data;
import com.zerter.teamconnect.Controlers.MenageView.ListAdapterContacts;
import com.zerter.teamconnect.Controlers.MenageView.ListAdapterGroups;
import com.zerter.teamconnect.Dialogs.ConfirmCreateGroup;
import com.zerter.teamconnect.Models.Group;
import com.zerter.teamconnect.Models.Person;
import com.zerter.teamconnect.R;

import java.util.ArrayList;
import java.util.List;

public class MenageGroupContacts extends Fragment {
    private String TAG = getClass().getName();
    public static ListView listView;
    public static Button buttonStworzGrupe;
    public static Boolean EDIT_MODE = false;

    public static Boolean groupView = true;

    public static class UpdateGroupPackage{
        public static Group original = new Group();
        public static Group newGroup = new Group();
    }


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
                if (EDIT_MODE){
                    data.updateGroup(UpdateGroupPackage.original,UpdateGroupPackage.newGroup);
                    buttonStworzGrupe.setText(R.string.add_team);
                    setAdapter(1);
                    EDIT_MODE = false;
                }else {
                    if (buttonStworzGrupe.getText().equals("OK")) {
                        buttonStworzGrupe.setText(R.string.add_team);
                        FragmentManager manager = getFragmentManager();
                        ConfirmCreateGroup confirmCreateGroup = new ConfirmCreateGroup();
                        Bundle bundle = new Bundle();
                        bundle.putString("selected_contacts", new Gson().toJson(selectedContacts));
                        confirmCreateGroup.setArguments(bundle);
                        confirmCreateGroup.show(manager, "ConfirmCreateGroup");
//                    setAdapter();
                    } else {
                        buttonStworzGrupe.setText(R.string.OK);
                        setAdapter(2);

                    }
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
                groupView = true;
                break;
            case 2:
                ListAdapterContacts adapter = new ListAdapterContacts(getActivity(), getContacts());
                listView.setAdapter(adapter);
                groupView = false;
                break;
            default:
                if (getGroups()!=null) {
                    listView.setAdapter(adapterGroups);
                }
                groupView = true;
        }

    }

    private  List<Group> getGroups(){
        return data.getGroups();
    }

    private List<Person> getContacts(){
        return data.getContacts();
    }
}
