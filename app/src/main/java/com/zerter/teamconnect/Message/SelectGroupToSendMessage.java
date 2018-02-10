package com.zerter.teamconnect.Message;

import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.zerter.teamconnect.Controlers.Data;
import com.zerter.teamconnect.Controlers.MyTextView;
import com.zerter.teamconnect.GeneralActivity;
import com.zerter.teamconnect.MenageGroup.GroupsUtils;
import com.zerter.teamconnect.Models.Group;
import com.zerter.teamconnect.Models.Message;
import com.zerter.teamconnect.Models.Person;
import com.zerter.teamconnect.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Okno dialogowe przy potwierdzeniu stworzenia nowej grupy
 */

public class SelectGroupToSendMessage extends DialogFragment {

    Button sendButton;
    ListView listView;
    List<Group> groups = new ArrayList<>();
    Data data;

    private String TAG = getClass().getName();
    String message;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.select_group_to_send_message, null);
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        data = new Data(getActivity());
        sendButton = view.findViewById(R.id.buttonWyślij);
        listView = (ListView) view.findViewById(R.id.listViewGroupToSend);
        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (GroupsUtils.getGroups(getActivity()) == null || GroupsUtils.getGroups(getActivity()).isEmpty()) {
            Toast.makeText(getActivity(), R.string.No_groups_error, Toast.LENGTH_LONG).show();
            dismiss();
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            message = bundle.getString("message");
        } else {
            Toast.makeText(getActivity(), "No messsage", Toast.LENGTH_SHORT).show();
            dismiss();
        }
        setAdapter();

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (Group group :
                        groups) {
                    for (Person person :
                            group.getPersons(getActivity())) {

                        GeneralActivity.sendSMS(person.getNumber().replace(" ", ""), message);
                    }

                    List<com.zerter.teamconnect.Models.Message> messageList = new ArrayList<com.zerter.teamconnect.Models.Message>();
                    if (data.getMessages() != null) {
                        messageList = data.getMessages();
                    }
                    Message msg = new Message();
                    msg.setMessage(message);
                    Calendar calander = Calendar.getInstance();
                    Integer cDay = calander.get(Calendar.DAY_OF_MONTH);
                    Integer cMonth = calander.get(Calendar.MONTH) + 1;
                    Integer cYear = calander.get(Calendar.YEAR);
                    Integer cHour = new Date().getHours();
                    Integer cMinute = calander.get(Calendar.MINUTE);
                    String date = cHour + ":" + cMinute + " " + cDay + "-" + cMonth + "-" + cYear;
                    msg.setDate(String.valueOf(date));
                    msg.setGroup(group);
                    messageList.add(msg);
                    data.setMessages(new Gson().toJson(messageList));
                }
                Toast.makeText(getActivity(), R.string.Message_was_sent, Toast.LENGTH_LONG).show();
                dismiss();

            }
        });
    }

    void setAdapter() {
        if (GroupsUtils.getGroups(getActivity()) != null && !GroupsUtils.getGroups(getActivity()).isEmpty()) {
            ListAdapterGroups adapter = new ListAdapterGroups(getActivity(), GroupsUtils.getGroups(getActivity()));
            listView.setAdapter(adapter);
        }
    }

    static class ViewHolder{
        MyTextView myTextView;
        View mView;
    }

    private class ListAdapterGroups extends ArrayAdapter<Group> {

        Data data;
        Context context;

        public ListAdapterGroups(@NonNull Context context, List<Group> groupList) {
            super(context, 0, groupList);
            this.data = new Data(context);
            this.context = context;
        }

//        List<View> viewList = new ArrayList<>();

        @NonNull
        @Override
        public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {

            final ViewHolder viewHolder;

            if (convertView == null) {
                LayoutInflater inflater = getActivity().getLayoutInflater();
                convertView = inflater.inflate(R.layout.grupa_kontaktow_to_select, null);
                viewHolder = new ViewHolder();
                viewHolder.myTextView = convertView.findViewById(R.id.myTextViewContacts);
                viewHolder.mView = convertView;
                convertView.setTag(viewHolder);
            }else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            final Group grupa = getItem(position);

//            if (viewList.size() > position && viewList.get(position) != null) {
//                return viewList.get(position);
//            }

            if (grupa != null) {
                viewHolder.myTextView.setText(grupa.getName());
            } else {
                viewHolder.myTextView.setText("n/a");
            }
            viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (viewHolder.myTextView.getCurrentTextColor() == getContext().getResources().getColor(R.color.textSelected)) {
                        viewHolder.myTextView.setTextColor(getContext().getResources().getColor(R.color.colorPrimary));

                        List<Group> newListGroup = new ArrayList<Group>();
                        for (Group group :
                                groups) {
                            if (!group.getName().equals(grupa.getName())) {
                                newListGroup.add(group);
                            }
                        }
                        groups = newListGroup;
                    } else {
                        viewHolder.myTextView.setTextColor(getContext().getResources().getColor(R.color.textSelected));
                        groups.add(grupa);
                    }
                }
            });
//            viewList.add(convertView);

            return convertView;
        }
    }
}
