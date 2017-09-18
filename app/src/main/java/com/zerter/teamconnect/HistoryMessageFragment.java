package com.zerter.teamconnect;

import android.app.Activity;
import android.app.Fragment;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import com.github.aakira.expandablelayout.ExpandableWeightLayout;
import com.zerter.teamconnect.Controlers.Data;
import com.zerter.teamconnect.Controlers.MyTextView;
import com.zerter.teamconnect.Models.Message;

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
        List<Message> messageList = new ArrayList<>();
        if (data.getMessages() != null){
            messageList = data.getMessages();
        }
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

        List<View> viewList = new ArrayList<>();
        @NonNull
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            final Message message = this.messageList.get(position);
            LayoutInflater inflater = context.getLayoutInflater();
            convertView = inflater.inflate(R.layout.message_item_history, null, true);
            if (viewList.size() > position && viewList.get(position) != null){
                return viewList.get(position);
            }

            final ExpandableWeightLayout expandableLayout
                    = (ExpandableWeightLayout) convertView.findViewById(R.id.expandableLayout);
            final MyTextView team = (MyTextView) convertView.findViewById(R.id.teamName);
            final MyTextView msg = (MyTextView) convertView.findViewById(R.id.messageHistory);
            final MyTextView date = (MyTextView) convertView.findViewById(R.id.historyDate);
            final ImageView arrow = (ImageView) convertView.findViewById(R.id.historyArrow);
            if (message != null) {
                team.setText(message.getGroup().getName());
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


                    if (expandableLayout.isExpanded())
                        arrow.setImageBitmap( BitmapFactory.decodeResource(getActivity().getResources(),
                                R.drawable.ic_arrow_down_drop_circle_outline_white_24dp));
                    else {
                        arrow.setImageBitmap( BitmapFactory.decodeResource(getActivity().getResources(),
                                R.drawable.ic_arrow_up_drop_circle_outline_white_24dp));
                    }
                }
            });
            return convertView;
        }
    }

}
