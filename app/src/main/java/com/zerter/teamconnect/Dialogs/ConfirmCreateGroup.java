package com.zerter.teamconnect.Dialogs;

import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zerter.teamconnect.Controlers.Data;
import com.zerter.teamconnect.Controlers.MenageView.ListAdapterGroups;
import com.zerter.teamconnect.Models.Group;
import com.zerter.teamconnect.Models.Person;
import com.zerter.teamconnect.R;
import com.zerter.teamconnect.Views.Fragments.MenageGroupContacts;

import java.util.ArrayList;
import java.util.List;

/**
 * Okno dialogowe przy potwierdzeniu stworzenia nowej grupy
 */

public class ConfirmCreateGroup extends DialogFragment {

    Button buttonZapisz, buttonAnuluj;
    EditText nazwaGrupy;
    Data data;

    List<Person> selectedContacts = new ArrayList<>();
    private String TAG = getClass().getName();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.confirm_decision, null);
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        data = new Data(getActivity());
        buttonAnuluj = (Button) view.findViewById(R.id.buttonAnuluj);
        buttonZapisz = (Button) view.findViewById(R.id.buttonZapisz);
        nazwaGrupy = (EditText) view.findViewById(R.id.editTextNazwaGrupy);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            java.lang.reflect.Type type = new TypeToken<List<Person>>(){}.getType();
            String list = bundle.getString("selected_contacts");
            selectedContacts = new Gson().fromJson(list,type);
            if (selectedContacts != null) {
                for (Person p :
                        selectedContacts) {
                    Log.d(TAG, "contact name: " + p.getName());
                }
            }else {
                Log.d(TAG,"NO CONTACTS");
            }
        }

        buttonZapisz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (nazwaGrupy.getText().toString().isEmpty()) {
                    Toast.makeText(getActivity(), "Wprowadź nazwę grupy", Toast.LENGTH_LONG).show();
                } else if (data.getGroupsNames().contains(nazwaGrupy.getText().toString())){
                    Toast.makeText(getActivity(), R.string.group_alredy_exist,Toast.LENGTH_SHORT).show();
                }
                else {
                    Group group = new Group();
                    group.setPersons(selectedContacts);
                    group.setName(nazwaGrupy.getText().toString());
                    stworzGrupeKontaktow(group);
                    setAdapter();
                    dismiss();
                }
            }
        });
        buttonAnuluj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setAdapter();
                dismiss();
            }
        });

        nazwaGrupy.requestFocus();
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,0);
    }

    private void stworzGrupeKontaktow(Group newContactsGroup) {
        List<Group> groupList = new ArrayList<>();
        if (data.getGroups() != null)
            groupList = data.getGroups();
        groupList.add(newContactsGroup);
        data.setGroups(new Gson().toJson(groupList));
    }
    public void setAdapter() {
//        Arrays.sort(Cache.LISTA_KONTAKTOW.get());
        if (data.getGroups() != null) {
            ListAdapterGroups adapter = new ListAdapterGroups(getActivity(), data.getGroups());
            MenageGroupContacts.listView.setAdapter(adapter);
        }
    }
}
