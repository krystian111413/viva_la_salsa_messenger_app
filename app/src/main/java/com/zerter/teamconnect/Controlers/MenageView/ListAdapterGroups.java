package com.zerter.teamconnect.Controlers.MenageView;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;

import com.google.gson.Gson;
import com.zerter.teamconnect.Controlers.Data;
import com.zerter.teamconnect.Controlers.MyTextView;
import com.zerter.teamconnect.MenageGroup.ConfirmDecisionDialog;
import com.zerter.teamconnect.Models.Group;
import com.zerter.teamconnect.R;
import com.zerter.teamconnect.MenageGroup.MenageGroupContacts;

import java.util.ArrayList;
import java.util.List;

/**
 * Adapter do listy grup
 */

public class ListAdapterGroups extends ArrayAdapter<Group> {
    private List<Group> groupList = new ArrayList<>();
    Data data;
//    Context context;
    Activity activity;
    public ListAdapterGroups(@NonNull Activity activity, List<Group> groupList) {
        super(activity, 0, groupList);
        this.groupList = groupList;
        this.data = new Data(activity);
        this.activity = activity;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final Group grupa = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.grupa_kontaktow, parent, false);
        }
        final MyTextView myTextView = (MyTextView) convertView.findViewById(R.id.myTextViewContacts);
        final ImageButton delete = (ImageButton) convertView.findViewById(R.id.imageButtonDelete);
        final ImageButton edit = (ImageButton) convertView.findViewById(R.id.imageButtonEdit);
        if (grupa != null) {
            myTextView.setText(grupa.getName());
        } else {
            myTextView.setText("n/a");
        }

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ConfirmDecisionDialog dialog = new ConfirmDecisionDialog();
                Bundle bundle = new Bundle();
                bundle.putString("group",new Gson().toJson(grupa));
                dialog.setArguments(bundle);
                dialog.show(activity.getFragmentManager(),ConfirmDecisionDialog.class.getName());


//                if (groupList.contains(getItem(position))) {
//                    groupList.remove(getItem(position));
//                    data.setGroups(new Gson().toJson(groupList));
//                    ListAdapterGroups listAdapterGroups = new ListAdapterGroups(getContext(),groupList);
//                    if (MenageGroupContacts.listView != null){
//                        MenageGroupContacts.listView.setAdapter(listAdapterGroups);
//                    }
//                }
            }
        });

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ListAdapterContacts listAdapterContacts = new ListAdapterContacts((Activity) activity,data.getContacts(),grupa);
                if (MenageGroupContacts.listView != null){
                    MenageGroupContacts.listView.setAdapter(listAdapterContacts);
                    MenageGroupContacts.groupView = false;
                }
            }
        });

        return convertView;
    }






}
