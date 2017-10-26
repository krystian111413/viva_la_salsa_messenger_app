package com.zerter.teamconnect.AddPlan;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.zerter.teamconnect.Controlers.Data;
import com.zerter.teamconnect.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Widok listy zaplanowanych smsów
 */

public class AddPlanFragment extends Fragment {
    private Button addButton;
    private ListView plans;
    private Data data;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.plany_view,container,false);

        addButton = (Button) view.findViewById(R.id.AddPlan);
        plans = (ListView) view.findViewById(R.id.ListViewPlan);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        data = new Data(getActivity());

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToAddPlanView();
            }
        });


        setAdapter();
    }

    @Override
    public void onResume() {
        super.onResume();
        setAdapter();
    }

    @Override
    public void onStart() {
        super.onStart();
        setAdapter();
    }

    void goToAddPlanView(String... object){
        Bundle bundle = new Bundle();
        if (object.length > 0){
            bundle.putString("plan",object[0]);
        }
        AddPlanViewSet viewFragment = new AddPlanViewSet();
        viewFragment.setArguments(bundle);
        FragmentManager FM = getFragmentManager();
        FragmentTransaction FT = FM.beginTransaction();
        FT.addToBackStack("AddPlanViewSet");
        FT.add(R.id.GeneralContener, viewFragment);
        FT.commit();
    }

    private void setAdapter() {
        if (getPlans() != null){
            Adapter adapter = new Adapter(getActivity(),getPlans());
            plans.setAdapter(adapter);
        }

    }


    private List<Plan> getPlans(){
        if (data.getMessagesPlaned() != null) {
            return data.getMessagesPlaned();
        }
        return null;
    }

    private class Adapter extends ArrayAdapter<Plan>{
        List<Plan>  planList = new ArrayList<>();

        public Adapter(@NonNull Context context, @NonNull List<Plan> objects) {
            super(context, R.layout.plan_item_list, objects);
            this.planList = objects;
        }

        @NonNull
        @Override
        public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.plan_item_list, parent, false);
            }
            assert convertView != null;
            TextView textView = (TextView) convertView.findViewById(R.id.planName);
            textView.setText(getItem(position).getName());
            ImageButton edit = (ImageButton) convertView.findViewById(R.id.imageButtonEdit);
            ImageButton delete = (ImageButton) convertView.findViewById(R.id.imageButtonDelete);

            edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String jsonObjectOfPlan = new Gson().toJson(getItem(position));
                    goToAddPlanView(jsonObjectOfPlan);
                }
            });

            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    List<Plan> plansTmp = new ArrayList<>();
                    for (Plan plan:
                         planList) {
                        if (getItem(position).getName() != plan.getName()){
                            plansTmp.add(plan);
                        }
                    }
                    data.setMessagesPlaned(new Gson().toJson(plansTmp));
                    setAdapter();
                }
            });
            return convertView;
        }
    }
}
