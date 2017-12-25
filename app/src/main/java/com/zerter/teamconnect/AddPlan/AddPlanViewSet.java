package com.zerter.teamconnect.AddPlan;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zerter.teamconnect.AddContact.DialogFragmentToAddContactToGroup;
import com.zerter.teamconnect.Controlers.Data;
import com.zerter.teamconnect.Controlers.MyTextView;
import com.zerter.teamconnect.Models.Group;
import com.zerter.teamconnect.R;

import java.lang.reflect.Type;
import java.util.*;

import static android.app.Activity.RESULT_OK;

/**
 * Dodawanie nowego planu
 */

@SuppressLint("ValidFragment")
public class AddPlanViewSet extends Fragment {

    EventListener eventListener;

    public AddPlanViewSet(EventListener eventListener) {
        this.eventListener = eventListener;
    }

    Boolean EDIT_MODE = false;
    Button add, selectGroup, setTime, setDate;
    EditText name, textMessage;
    Integer DIALOG_FRAGMENT = 1, indexPlan;
    Fragment fragment;
    ListView selectedGroups;
    List<Group> groupList = null;
    MyTextView repeatInfo;
    SeekBar seekBarRepeatMessage;
    Plan plan = null;
    String defaultValueFromTextView, dateName, timeName, date, time;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.add_plan_view, container, false);

        add = view.findViewById(R.id.AddPlanButton);
        name = view.findViewById(R.id.nameOfPlan);
        textMessage = view.findViewById(R.id.editText_tresc_smsa);
        selectedGroups = view.findViewById(R.id.listViewSelectedGroups);
        selectGroup = view.findViewById(R.id.selectGroupAddPlan);
        repeatInfo = view.findViewById(R.id.infoRepeatTextView);
        seekBarRepeatMessage = view.findViewById(R.id.seekBarRepeatMessage);
        plan = new Gson().fromJson(getArguments().getString("plan"), Plan.class);
        setTime = view.findViewById(R.id.buttonTimeSet);
        setDate = view.findViewById(R.id.buttonDateSet);
        return view;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        defaultValueFromTextView = repeatInfo.getText().toString();
        fragment = this;
        dateName = setDate.getText().toString();
        timeName = setTime.getText().toString();

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addNewPlan();
            }
        });

        if (plan != null) {
            add.setText(R.string.save);
            name.setText(plan.getName());
            textMessage.setText(plan.getText());
            seekBarRepeatMessage.setProgress(plan.getRepeatValue());
            setValueRepeatMessageTextView(plan.getRepeatValue());
            setTime.setText(timeName + ": " + plan.getTime());
            setDate.setText(dateName + ": " + plan.getDate());
            groupList = plan.getGroups();
            setAdapter();
            EDIT_MODE = true;
            date = plan.getDate();
            time = plan.getTime();
            indexPlan = getPlanIndex(plan);

        } else {
            setValueRepeatMessageTextView(0);
        }


        selectGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (new Data(getActivity()).getGroups() != null) {
                    FragmentManager fragmentManager = getFragmentManager();
                    DialogFragmentToAddContactToGroup dialog = new DialogFragmentToAddContactToGroup();
                    dialog.setTargetFragment(fragment, DIALOG_FRAGMENT);
                    dialog.show(fragmentManager, "DialogFragmentToAddContactToGroup");
                } else {
                    Toast.makeText(getActivity(), R.string.you_have_no_group, Toast.LENGTH_SHORT).show();
                }
            }
        });


        logicOfSeeBar();

        calendarButton();
        timeButton();
    }

    private void calendarButton() {
        setDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DialogCalendar calendar = new DialogCalendar(new OnResult() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onResult(String value) {
                        setDate.setText(dateName + ": " + value);
                        date = value;
                    }
                });
                calendar.show(getActivity().getFragmentManager(), calendar.getClass().getName());
            }
        });
    }

    private void timeButton() {
        setTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                DialogTime dialogTime = new DialogTime(new OnResult() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onResult(String value) {
                        setTime.setText(timeName + ": " + value);
                        time = value;
                    }
                });
                dialogTime.show(getActivity().getFragmentManager(), dialogTime.getClass().getName());
            }
        });
    }

    private void logicOfSeeBar() {

        seekBarRepeatMessage.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                setValueRepeatMessageTextView(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    private void setValueRepeatMessageTextView(Integer seeBarProgress) {

        String value;
        switch (seeBarProgress) {
            case 0:
                value = defaultValueFromTextView + getString(R.string.none);
                repeatInfo.setText(value);
                break;
            case 1:
                value = defaultValueFromTextView + getString(R.string.daily);
                repeatInfo.setText(value);
                break;
            case 2:
                value = defaultValueFromTextView + getString(R.string.weekly);
                repeatInfo.setText(value);
                break;
            case 3:
                value = defaultValueFromTextView + getString(R.string.monthly);
                repeatInfo.setText(value);
                break;
            case 4:
                value = defaultValueFromTextView + getString(R.string.yearly);
                repeatInfo.setText(value);
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == DIALOG_FRAGMENT && resultCode == RESULT_OK) {
            setAdapter();
        }
    }

    private void setAdapter() {
        String groups = null;
        if (getActivity().getIntent().getExtras() != null) {
            groups = getActivity().getIntent().getExtras().getString("groups");
        }
        Type type = new TypeToken<List<Group>>() {
        }.getType();
        List<Group> groupList = new Gson().fromJson(groups, type);
        ListAdapterGroups adapter = null;
        if (groupList != null) {
            adapter = new ListAdapterGroups(getActivity(), groupList);
        } else if (plan != null) {
            adapter = new ListAdapterGroups(getActivity(), plan.getGroups());
        } else {
            Toast.makeText(getActivity(), "No selected groups", Toast.LENGTH_SHORT).show();
        }
        selectedGroups.setAdapter(adapter);
        getTotalHeightofListView(selectedGroups);

        this.groupList = groupList;
    }

    public static void getTotalHeightofListView(ListView listView) {

        ListAdapter mAdapter = listView.getAdapter();

        int totalHeight = 0;

        for (int i = 0; i < mAdapter.getCount(); i++) {
            View mView = mAdapter.getView(i, null, listView);

            mView.measure(
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),

                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));

            totalHeight += mView.getMeasuredHeight();
            Log.w("HEIGHT" + i, String.valueOf(totalHeight));

        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight
                + (listView.getDividerHeight() * (mAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();

    }

    private class ListAdapterGroups extends ArrayAdapter<com.zerter.teamconnect.Models.Group> {

        List<com.zerter.teamconnect.Models.Group> groups = new ArrayList<>();

        private ListAdapterGroups(@NonNull Context context, @NonNull List<com.zerter.teamconnect.Models.Group> objects) {
            super(context, R.layout.grupa_kontaktow_selected, objects);
            this.groups = objects;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            final com.zerter.teamconnect.Models.Group grupa = getItem(position);

            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.grupa_kontaktow_selected, parent, false);
            }
            TextView name = convertView.findViewById(R.id.myTextViewContacts);
            name.setText(grupa != null ? grupa.getName() : "error");
            return convertView;
        }

    }

    void addNewPlan() {
        if (checkAllForms()) {
            Data data = new Data(getActivity());
            List<Plan> planList = new ArrayList<>();
            if (data.getMessagesPlaned() != null) {
                planList = data.getMessagesPlaned();
            }
            Plan plan = new Plan(
                    date,
                    time,
                    textMessage.getText().toString(),
                    name.getText().toString(),
                    seekBarRepeatMessage.getProgress(),
                    groupList
            );
            if (EDIT_MODE){
                planList = updatePlanList(plan);
            }else {
                planList.add(plan);
            }
            data.setMessagesPlaned(new Gson().toJson(planList));
            getActivity().onBackPressed();
            eventListener.onEvent();
        }
    }

    private List<Plan> updatePlanList(Plan plan) {
        List<Plan> planList = new Data(getActivity()).getMessagesPlaned();
        List<Plan> plans = new ArrayList<>();
        for (int i = 0; i < planList.size();i++){
            if (i == indexPlan){
                plans.add(plan);
            }else {
                plans.add(planList.get(i));
            }
        }
        return plans;
    }

    private Integer getPlanIndex(Plan plan){
        List<Plan> plans = new Data(getActivity()).getMessagesPlaned();
        for (int i = 0; i< plans.size();i++){
            if (plan.getName().equals(plans.get(i).getName())){
                return i;
            }
        }
        return null;
    }

    private boolean checkAllForms() {
        //check name plan
        if (name == null || name.getText().toString().equals("")) {
            Toast.makeText(getActivity(), R.string.please_set_name_of_plan, Toast.LENGTH_SHORT).show();
            return false;
        }
        //check text name
        if (textMessage == null || textMessage.getText().toString().equals("")) {
            Toast.makeText(getActivity(), R.string.please_set_text_of_message, Toast.LENGTH_SHORT).show();
            return false;
        }
        //check group selected
        if (groupList == null) {
            Toast.makeText(getActivity(), R.string.please_select_group, Toast.LENGTH_SHORT).show();
            return false;
        }
        //check date
        if (dateName.equals(setDate.getText().toString())){
            Toast.makeText(getActivity(), R.string.please_set_date, Toast.LENGTH_SHORT).show();
            return false;
        }
        //check time
        if (timeName.equals(setTime.getText().toString())){
            Toast.makeText(getActivity(), R.string.please_set_time, Toast.LENGTH_SHORT).show();
            return false;
        }


        return true;
    }
}
