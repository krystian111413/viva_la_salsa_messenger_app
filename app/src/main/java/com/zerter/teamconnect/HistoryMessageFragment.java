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
        team.setName("Nazwa Grupy");
        message.setTeam(team);
        message.setMessage("Treść wiadomości\n aaaa\n bcdf");
        message.setDate("15:49 11.09.1992");
        List<Message> messageList = new ArrayList<>();

        messageList.add(message);
        message = new Message();
        message.setTeam(team);
        message.setMessage("asdas asd asd asd as\nasd as das d\nasd asd fsd\nsda fsdf d gdf sd \nsd gdsfg dsf gsd\nfg dsdf gsdf \nsdf sdf gsdfg sd gsd asd asd as\nasd as das dasd asd fsdsda fsdf d gdf sd sd gdsfg dsf gsd\nfg ds");
        message.setDate("12:15 11.09.2017");
        messageList.add(message);
        message = new Message();
        message.setTeam(team);
        message.setMessage("start asd asd asd asdas asd asd asd asdas asd asd asd asdas asd asd asd asdas asd asd asd asdas asd asd asd asdas asd asd asd asdas asd asd asd asdas asd asd asd asdas asd asd asd asdas asd asd asd asdas asd asd asd asdas asd asd asd asdas asd asd asd asdas asd asd end ");
        message.setDate("16:00 16.05.2003");
        messageList.add(message);
        message = new Message();
        message.setTeam(team);
        message.setMessage("Przeanalizować pozostałe listy, zwracając szczególną uwagę na dokumenty\n" +
                "zawierające fragmenty o długości przekraczającej limit Współczynnika podobieństwa\n" +
                "2 (są one oznaczone pogrubioną czcionką). W przypadku takich dokumentów,\n" +
                "zwłaszcza znajdujących sie na czele listy, należy użyć linku „zaznacz fragmenty”\n" +
                "i sprawdzić, czy są one przede wszystkim krótkimi frazami rozrzuconymi po całym\n" +
                "dokumencie (w takiej sytuacji można je uznać za przypadkowe zapożyczenia), czy ");
        message.setDate("17:55 06.01.1969");
        messageList.add(message);

        ListAdapterMessage listAdapterMessage = new ListAdapterMessage(getActivity(), messageList);
        listView.setAdapter(listAdapterMessage);
    }

    private class ListAdapterMessage extends ArrayAdapter<Message> {

        String TAG = getClass().getName();
        List<Message> messageList = new ArrayList<>();
        private final Activity context;

        ListAdapterMessage(@NonNull Activity context, List<Message> messageList) {
            super(context, R.layout.grupa_kontaktow, messageList);
            this.context = context;
            this.messageList = messageList;
        }


        @NonNull
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
//            final Message message = getItem(position);
            final Message message = this.messageList.get(position);
            LayoutInflater inflater = context.getLayoutInflater();
            convertView = inflater.inflate(R.layout.message_item_history, null, true);
            final ExpandableWeightLayout expandableLayout
                    = (ExpandableWeightLayout) convertView.findViewById(R.id.expandableLayout);
            final MyTextView team = (MyTextView) convertView.findViewById(R.id.teamName);
            final MyTextView msg = (MyTextView) convertView.findViewById(R.id.messageHistory);
            final MyTextView date = (MyTextView) convertView.findViewById(R.id.historyDate);
            if (message != null) {
                team.setText(message.getTeam().getName());
                msg.setText(message.getMessage());
                date.setText(message.getDate());
            } else {
                team.setText("n/a");

            }
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (msg.getVisibility() == View.GONE) {
                        msg.setVisibility(View.VISIBLE);
                    }
                    expandableLayout.toggle();
                    expandableLayout.expand();
                    expandableLayout.collapse();
                }
            });
            return convertView;
        }
    }

}
