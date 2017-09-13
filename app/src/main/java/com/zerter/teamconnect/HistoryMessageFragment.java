package com.zerter.teamconnect;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.github.aakira.expandablelayout.ExpandableWeightLayout;
import com.zerter.teamconnect.Controlers.Data;
import com.zerter.teamconnect.Controlers.MyTextView;
import com.zerter.teamconnect.Models.Message;
import com.zerter.teamconnect.Models.Team;

import java.util.ArrayList;
import java.util.List;

public class HistoryMessageFragment extends Fragment {


    private final String TAG = getClass().getName();
    public static ListView listView;

    Data data;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.edytuj_grupy, container, false);
        listView = (ListView) view.findViewById(R.id.ListViewGrupy);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        data = new Data(getActivity());
        setAdapter();


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });


    }




    public void setAdapter() {
        Message message = new Message();
        Team team = new Team();
        team.setName("DUPA");
        message.setTeam(team);
        message.setMessage("Treść wiadomości\n  messageList.add(message);\n private class ListAdapterMessage extends ArrayAdapter<Message> {");
        List<Message> messageList = new ArrayList<>();
        for(int i = 0; i< 15;i++){
            messageList.add(message);
        }

        ListAdapterMessage listAdapterMessage = new ListAdapterMessage(getActivity(),messageList);
        listView.setAdapter(listAdapterMessage);
    }

    private class ListAdapterMessage extends ArrayAdapter<Message> {

        String TAG = getClass().getName();
        List<Message> messageList = new ArrayList<>();
        private final Activity context;

        ListAdapterMessage(@NonNull Activity context, List<Message> messageList) {
            super(context, R.layout.grupa_kontaktow, messageList);
            this.context = context;
        }

        @NonNull
        @Override
        public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            final Message message = getItem(position);

            LayoutInflater inflater = context.getLayoutInflater();
            convertView = inflater.inflate(R.layout.message_item_history, null, false);
            final ExpandableWeightLayout expandableLayout
                    = (ExpandableWeightLayout) convertView.findViewById(R.id.expandableLayout);
            final MyTextView team = (MyTextView) convertView.findViewById(R.id.teamName);
            final MyTextView msg = (MyTextView) convertView.findViewById(R.id.messageHistory);
            if (message != null) {
                team.setText(message.getTeam().getName());
                msg.setText(message.getMessage());
            } else {
                team.setText("n/a");
                msg.setText("n/a");
            }
            team.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    msg.measure(0, 0);
                    expandableLayout.setMinimumHeight(msg.getMeasuredHeight() + 70);
                    expandableLayout.toggle();
                    expandableLayout.expand();
                    expandableLayout.collapse();
                }
            });


            return convertView;
        }
    }

}
