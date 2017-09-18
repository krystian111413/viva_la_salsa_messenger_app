package com.zerter.teamconnect.Controlers.MenageView;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.google.gson.Gson;
import com.zerter.teamconnect.Controlers.MyTextView;
import com.zerter.teamconnect.Models.Person;
import com.zerter.teamconnect.R;
import com.zerter.teamconnect.Views.Fragments.MenageGroupContacts;

import java.util.ArrayList;
import java.util.List;

/**
 * ListAdapterContacts
 */

public class ListAdapterContacts extends ArrayAdapter<Person> {

    private String TAG = getClass().getName();
    private List<Person> zaznaczoneKontakty = new ArrayList<>();

    private final Activity context;

    public ListAdapterContacts(@NonNull Activity context, List<Person> kontakty) {
        super(context, R.layout.contact_group_item_list, kontakty);
        this.context = context;
    }

    ListAdapterContacts(@NonNull Activity context, List<Person> kontakty,@Nullable List<Person> zaznaczoneKontakty) {
        super(context, R.layout.contact_group_item_list, kontakty);
        Log.d(TAG, new Gson().toJson(zaznaczoneKontakty));
        this.zaznaczoneKontakty = zaznaczoneKontakty;
        this.context = context;
    }

    private List<View> viewList = new ArrayList<>();

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final Person kontakt = getItem(position);
        LayoutInflater inflater = context.getLayoutInflater();
        convertView = inflater.inflate(R.layout.contact_group_item_list, null);

        if (viewList.size() > position && viewList.get(position) != null){
            return viewList.get(position);
        }

        final MyTextView name = (MyTextView) convertView.findViewById(R.id.contactItemListName);
        final MyTextView number = (MyTextView) convertView.findViewById(R.id.contactItemListNumber);
        if (kontakt != null) {
            name.setText(kontakt.getName());
            number.setText(kontakt.getNumber());

        } else {
            name.setText("n/a");
        }
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (name.getCurrentTextColor() == getContext().getResources().getColor(R.color.textSelected)){
                    name.setTextColor(getContext().getResources().getColor(R.color.textColor));
                    MenageGroupContacts.selectedContacts.remove(getItem(position));

                    if (zaznaczoneKontakty != null){
                        zaznaczoneKontakty.remove(kontakt);
                    }

                }else {
                    name.setTextColor(getContext().getResources().getColor(R.color.textSelected));
                    MenageGroupContacts.selectedContacts.add(getItem(position));

                }
            }
        });

        if (zaznaczoneKontakty.contains(kontakt)){
            name.setTextColor(getContext().getResources().getColor(R.color.textSelected));
            MenageGroupContacts.selectedContacts.add(getItem(position));
        }

        viewList.add(convertView);
        return convertView;
    }
}
