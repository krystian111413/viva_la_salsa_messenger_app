package com.zerter.teamconnect.Dialogs;

import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.zerter.teamconnect.AddContact.AddContact;
import com.zerter.teamconnect.Controlers.Data;
import com.zerter.teamconnect.Controlers.MyTextView;
import com.zerter.teamconnect.Models.Group;
import com.zerter.teamconnect.R;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * DialogFragment do wyswietlenia listy grup i możliwosć dodania do nich kontaktu
 */

public class AddContactToGroup extends DialogFragment {

    List<Group> groups = new ArrayList<>();
    Button add;
    ListView listViewGroups;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.select_group_to_add_to_contact,container,false);
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        add = (Button) view.findViewById(R.id.buttonAddContactToGroups);
        listViewGroups = (ListView) view.findViewById(R.id.listViewGroupToAdd);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Data data = new Data(getActivity());
        setAdapter(data.getGroups());
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                AddContact addContact
                dismiss();
            }
        });
    }

    void setAdapter(List<Group> groups){
        ListAdapterGroups adapter = new ListAdapterGroups(getActivity(),groups);
        listViewGroups.setAdapter(adapter);
    }

    private class ListAdapterGroups extends ArrayAdapter<Group> {

        Data data;
        Context context;

        public ListAdapterGroups(@NonNull Context context, List<Group> groupList) {
            super(context, 0, groupList);
            this.data = new Data(context);
            this.context = context;
        }

        List<View> viewList = new ArrayList<>();

        @NonNull
        @Override
        public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            final Group grupa = getItem(position);
            LayoutInflater inflater = getActivity().getLayoutInflater();
            convertView = inflater.inflate(R.layout.grupa_kontaktow_to_select,null);

            if (viewList.size() > position && viewList.get(position) != null) {
                return viewList.get(position);
            }
            final MyTextView name = (MyTextView) convertView.findViewById(R.id.myTextViewContacts);
            if (grupa != null) {
                name.setText(grupa.getName());
            } else {
                name.setText("n/a");
            }
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (name.getCurrentTextColor() == getContext().getResources().getColor(R.color.textSelected)) {
                        name.setTextColor(getContext().getResources().getColor(R.color.colorPrimary));

                        List<Group> newListGroup = new ArrayList<Group>();
                        for (Group group :
                                groups) {
                            if (!group.getName().equals(grupa.getName())) {
                                newListGroup.add(group);
                            }
                        }
                        groups = newListGroup;
                    } else {
                        name.setTextColor(getContext().getResources().getColor(R.color.textSelected));
                        groups.add(grupa);
                    }
                }
            });
            viewList.add(convertView);
            return convertView;
        }
    }
}
