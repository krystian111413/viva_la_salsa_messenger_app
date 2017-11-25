package com.zerter.teamconnect.Dialogs;

import android.app.DialogFragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import com.google.gson.Gson;
import com.zerter.teamconnect.Controlers.Data;
import com.zerter.teamconnect.Models.Group;
import com.zerter.teamconnect.R;
import com.zerter.teamconnect.Views.Fragments.MenageGroupContacts;

import java.util.ArrayList;
import java.util.List;

/**
 * Dialog to be sure to delete group
 */

public class ConfirmDecisionDialog extends DialogFragment{


    Button confirm, cancel;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.confirm_dialog,container,false);
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        getDialog().setCancelable(false);
        confirm = (Button) view.findViewById(R.id.buttonConfirm);
        cancel = view.findViewById(R.id.buttonDismiss);
        return view;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final List<Group> groupList = new Data(getActivity()).getGroups();
        Bundle bundle = getArguments();
        String json = bundle.getString("group");
        final Group group = new Gson().fromJson(json,Group.class);
        Log.d(getClass().getName(),json);


        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (veryfiGroupExist(group)){
                    Boolean allWorks = deleteGroupFromGroupList(group);
                    if (!allWorks){
                        Toast.makeText(getActivity(),"Error with delete group",Toast.LENGTH_SHORT).show();
                    }
                    setContener(new MenageGroupContacts());
                    dismiss();
                }else {
                    Toast.makeText(getActivity(), "Can not find group to delete", Toast.LENGTH_SHORT).show();
                }
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    private Boolean veryfiGroupExist(Group group){
        Boolean exist = false;

        for (Group g :
                new Data(getActivity()).getGroups()) {
            if (g.getName().equals(group.getName())) {
                exist = true;
            }
        }

        return exist;
    }

    private Boolean deleteGroupFromGroupList(Group group){
        Boolean didDeleted = false;
        List<Group> groupList = new ArrayList<>();
        for (Group g: new Data(getActivity()).getGroups()){
            if (g.getName().equals(group.getName())){
                didDeleted = true;
            }else {
                groupList.add(g);
            }
        }

        new Data(getActivity()).setGroups(new Gson().toJson(groupList));
        return didDeleted;
    }

    private void setContener(android.app.Fragment fragment){
        FragmentManager FM = getActivity().getFragmentManager();
        FragmentTransaction FT = FM.beginTransaction();
        FT.replace(R.id.GeneralContener, fragment);
        FT.commit();
    }
}
