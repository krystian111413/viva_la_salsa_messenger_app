package com.zerter.teamconnect.AddPlan;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zerter.teamconnect.Controlers.Data;
import com.zerter.teamconnect.Dialogs.DialogFragmentToAddContactToGroup;
import com.zerter.teamconnect.Models.Group;
import com.zerter.teamconnect.R;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;

/**
 * Dodawanie nowego planu
 */

public class AddPlanViewSet extends Fragment {


    Button add, selectGroup;
    EditText name, textMessage;
    DatePicker datePicker;
    TimePicker time;
    Switch day, week, month, year;
    Integer DIALOG_FRAGMENT = 1;
    Fragment fragment;
    ListView selectedGroups;
    List<Group> groupList = null;

    Plan plan = null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.add_plan_view, container, false);

        add = (Button) view.findViewById(R.id.AddPlanButton);
        selectGroup = (Button) view.findViewById(R.id.selectGroupAddPlan);
        name = (EditText) view.findViewById(R.id.nameOfPlan);
        textMessage = (EditText) view.findViewById(R.id.editText_tresc_smsa);
        datePicker = (DatePicker) view.findViewById(R.id.calendar);
        day = (Switch) view.findViewById(R.id.switchOfDay);
        week = (Switch) view.findViewById(R.id.switchOfWeek);
        month = (Switch) view.findViewById(R.id.switchOfMonth);
        year = (Switch) view.findViewById(R.id.switchOfYear);
        time = (TimePicker) view.findViewById(R.id.timePicker);
        selectedGroups = (ListView) view.findViewById(R.id.listViewSelectedGroups);

        plan = new Gson().fromJson(getArguments().getString("plan"),Plan.class);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        fragment = this;

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addNewPlan();
            }
        });

        if (plan != null){
            add.setText(R.string.save);
            name.setText(plan.getName());
            textMessage.setText(plan.getText());
            day.setChecked(plan.getDay());
            week.setChecked(plan.getWeek());
            month.setChecked(plan.getMonth());
            year.setChecked(plan.getYear());
            setAdapter();
        }


        selectGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getFragmentManager();
                DialogFragmentToAddContactToGroup dialog = new DialogFragmentToAddContactToGroup();
                dialog.setTargetFragment(fragment, DIALOG_FRAGMENT);
                dialog.show(fragmentManager, "DialogFragmentToAddContactToGroup");
            }
        });

        setSwichersLogic();

    }

    private void setSwichersLogic() {
        day.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (day.isChecked()) {
                    week.setChecked(true);
                    month.setChecked(true);
                    year.setChecked(true);
                }
            }
        });
        week.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (week.isChecked()) {

                    month.setChecked(true);
                    year.setChecked(true);
                }else {
                    day.setChecked(false);
                }
            }
        });
        month.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (month.isChecked()) {

                    month.setChecked(true);
                    year.setChecked(true);
                }else {
                    day.setChecked(false);
                    week.setChecked(false);
                }
            }
        });
        year.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!year.isChecked()){
                    day.setChecked(false);
                    week.setChecked(false);
                    month.setChecked(false);
                }
            }
        });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == DIALOG_FRAGMENT && resultCode == RESULT_OK) {
            setAdapter();
        }
    }

    private void setAdapter() {
        String groups = getActivity().getIntent().getExtras().getString("groups");
        Type type = new TypeToken<List<Group>>() {
        }.getType();
        List<Group> groupList = new Gson().fromJson(groups, type);
        ListAdapterGroups adapter = null;
        if (groupList != null) {
            adapter = new ListAdapterGroups(getActivity(), groupList);
        } else if (plan != null) {
            adapter = new ListAdapterGroups(getActivity(), plan.getGroups());
        }else {
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
            TextView name = (TextView) convertView.findViewById(R.id.myTextViewContacts);
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
            Plan plan = new Plan();
            plan.setName(name.getText().toString().trim());
            String date = "";
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                date = time.getHour() + ":" + time.getMinute() + " " + datePicker.getDayOfMonth() + "-" + datePicker.getMonth() + "-" + datePicker.getYear();

            } else {
                date = datePicker.getDayOfMonth() + "-" + datePicker.getMonth() + "-" + datePicker.getYear();
            }

            plan.setDate(date);
            plan.setText(textMessage.getText().toString());
            plan.setDay(day.isChecked());
            plan.setWeek(week.isChecked());
            plan.setMonth(month.isChecked());
            plan.setYear(year.isChecked());
            plan.setGroups(groupList);


            planList.add(plan);
            data.setMessagesPlaned(new Gson().toJson(planList));
            getActivity().onBackPressed();
        }
    }

    private boolean checkAllForms() {
        if (name == null || name.getText().toString().equals("")) {
            Toast.makeText(getActivity(), R.string.please_set_name_of_plan, Toast.LENGTH_SHORT).show();
//            name.requestFocus();
            return false;
        }
        if (textMessage == null || textMessage.getText().toString().equals("")) {
            Toast.makeText(getActivity(), R.string.please_set_text_of_message, Toast.LENGTH_SHORT).show();
//            textMessage.requestFocus();
            return false;
        }
        if (groupList==null){
            Toast.makeText(getActivity(), R.string.please_select_group, Toast.LENGTH_SHORT).show();
//            selectGroup.requestFocus();
            return false;
        }

        return true;
    }
}
