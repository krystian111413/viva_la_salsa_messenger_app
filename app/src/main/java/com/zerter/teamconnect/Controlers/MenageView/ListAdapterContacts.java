package com.zerter.teamconnect.Controlers.MenageView;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.zerter.teamconnect.Controlers.MyTextView;
import com.zerter.teamconnect.Models.Group;
import com.zerter.teamconnect.Models.Person;
import com.zerter.teamconnect.R;
import com.zerter.teamconnect.Views.Fragments.MenageGroupContacts;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * ListAdapterContacts
 */

public class ListAdapterContacts extends ArrayAdapter<Person> {

    private String TAG = getClass().getName();
    private List<Person> zaznaczoneKontakty = null;

    private final Activity context;

    public ListAdapterContacts(@NonNull Activity context, List<Person> kontakty) {
        super(context, R.layout.contact_group_item_list, kontakty);
        this.context = context;
    }

    ListAdapterContacts(@NonNull Activity context, List<Person> kontakty, @Nullable Group group) {
        super(context, R.layout.contact_group_item_list, kontakty);
        this.zaznaczoneKontakty = group.getPersons();
        this.context = context;
        if (MenageGroupContacts.buttonStworzGrupe != null){
            MenageGroupContacts.buttonStworzGrupe.setText(R.string.Update);
            MenageGroupContacts.EDIT_MODE = true;
            MenageGroupContacts.UpdateGroupPackage.original = group;
            MenageGroupContacts.UpdateGroupPackage.newGroup = group;
        }
        for (Person p :
                this.zaznaczoneKontakty) {
            Log.d(TAG,p.getName());
        }
    }

    private List<View> viewList = new ArrayList<>();

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull final ViewGroup parent) {
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

                    List<Person> personList = new ArrayList<Person>();
                    for (Person person :
                            MenageGroupContacts.selectedContacts ) {
                        if (!person.getName().equals(kontakt.getName()) && !person.getNumber().equals(kontakt.getNumber())){
                            personList.add(person);
                        }
                    }
                    MenageGroupContacts.selectedContacts = personList;

                    if (MenageGroupContacts.UpdateGroupPackage.newGroup.getPersons() != null){
                        List<Person> newSelectedContacts = new ArrayList<Person>();
                        for (Person person :
                                MenageGroupContacts.UpdateGroupPackage.newGroup.getPersons()) {
                            if (!(person.getName().equals(kontakt.getName()))
                                    && !(person.getNumber().equals(kontakt.getNumber()))){
                                newSelectedContacts.add(person);
                            }
                        }
                        zaznaczoneKontakty = newSelectedContacts;
                        MenageGroupContacts.UpdateGroupPackage.newGroup.setPersons(newSelectedContacts);
                    }


                }else {
                    name.setTextColor(getContext().getResources().getColor(R.color.textSelected));
                    MenageGroupContacts.selectedContacts.add(getItem(position));
                    MenageGroupContacts.UpdateGroupPackage.newGroup.getPersons().add(kontakt);
                }
            }
        });

        if (zaznaczoneKontakty != null){
            for (Person person: zaznaczoneKontakty) {
                if (kontakt != null) {
                    if (Objects.equals(person.getNumber(), kontakt.getNumber())
                            && person.getName().equals(kontakt.getName())){
                        name.setTextColor(getContext().getResources().getColor(R.color.textSelected));
                        MenageGroupContacts.selectedContacts.add(person);
                    }
                }
            }
        }


        viewList.add(convertView);
        return convertView;
    }
}
