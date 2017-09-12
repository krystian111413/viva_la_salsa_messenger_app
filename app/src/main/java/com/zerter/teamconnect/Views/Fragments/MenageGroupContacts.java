package com.zerter.teamconnect.Views.Fragments;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;

import com.google.gson.Gson;
import com.zerter.teamconnect.Controlers.Cache;
import com.zerter.teamconnect.Controlers.Data;
import com.zerter.teamconnect.Controlers.MyTextView;
import com.zerter.teamconnect.Dialogs.ConfirmCreateGroup;
import com.zerter.teamconnect.Models.Contacts;
import com.zerter.teamconnect.Models.Person;
import com.zerter.teamconnect.R;

import java.util.ArrayList;
import java.util.List;

public class MenageGroupContacts extends Fragment {


    public static ListView listView;
    Button buttonStworzGrupe, buttonUsunGrupe;
    Data data;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.grupy_kontaktow, container, false);
        listView = (ListView) view.findViewById(R.id.ListViewGrupy);
        buttonStworzGrupe = (Button) view.findViewById(R.id.buttonEdytujGrupe);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        data = new Data(getActivity());
        setAdapter();
        buttonStworzGrupe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (buttonStworzGrupe.getText().equals("OK")) {
                    buttonStworzGrupe.setText(R.string.add_team);
                    FragmentManager manager = getFragmentManager();
                    ConfirmCreateGroup confirmCreateGroup = new ConfirmCreateGroup();
                    confirmCreateGroup.show(manager, "ConfirmCreateGroup");
//                    setAdapter();
                } else {
                    buttonStworzGrupe.setText(R.string.OK);
                    ListAdapterPerson adapter = new ListAdapterPerson(getActivity(), Cache.LISTA_KONTAKTOW);
                    listView.setAdapter(adapter);

                }
            }
        });
//        buttonUsunGrupe.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                List<Contacts> contactsList = new ArrayList<>();
//                if (data.wczytajGrupyKontaktow() != null)
//                    contactsList = data.wczytajGrupyKontaktow();
//                for (int i = 0; i < Cache.ZAZNACZONE_GRUPY.size(); i++) {
//                    String nazwaZaznaczonejGrupy = Cache.ZAZNACZONE_GRUPY.get(i).getName();
//                    for (int j = 0; j < contactsList.size(); j++) {
//                        if (contactsList.get(j).getName().equals(nazwaZaznaczonejGrupy)) {
//                            //usun z glownej listy
//                            contactsList.remove(j);
//                        }
//                    }
//                }
//
//
////                contactsList.remove(Cache.ZAZNACZONE_GRUPY);
//                data.zapiszGrupyKontaktow(new Gson().toJson(contactsList));
//                setAdapter();
//            }
//        });
    }

    public void setAdapter() {
//        Arrays.sort(Cache.LISTA_KONTAKTOW.get());
        if (data.wczytajGrupyKontaktow() != null) {
            ListAdapterContacts adapter = new ListAdapterContacts(getActivity(), data.wczytajGrupyKontaktow());
            listView.setAdapter(adapter);
        }
    }

    class ListAdapterPerson extends ArrayAdapter<Person> {

        String TAG = getClass().getName();
        List<Person> zaznaczoneKontakty = new ArrayList<>();
        private final Activity context;

        ListAdapterPerson(@NonNull Activity context, List<Person> kontakty) {
            super(context, R.layout.grupa_kontaktow, kontakty);
            this.context = context;
            Cache.ZAZNACZONE_KONTAKTY = new ArrayList<>();
        }

        ListAdapterPerson(@NonNull Activity context, List<Person> kontakty, List<Person> zaznaczoneKontakty) {
            super(context, R.layout.grupa_kontaktow, kontakty);
            Log.d(TAG, new Gson().toJson(zaznaczoneKontakty));
            this.zaznaczoneKontakty = zaznaczoneKontakty;
            this.context = context;
            Cache.ZAZNACZONE_KONTAKTY = new ArrayList<>();
        }

        @NonNull
        @Override
        public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            final Person kontakt = getItem(position);

            LayoutInflater inflater = context.getLayoutInflater();
            convertView = inflater.inflate(R.layout.grupa_kontaktow, null, false);

            final MyTextView myTextView = (MyTextView) convertView.findViewById(R.id.myTextViewContacts);
            if (kontakt != null) {
                myTextView.setText(kontakt.getName() + "\n" + kontakt.getNumber());
            } else {
                myTextView.setText("n/a");
            }


            return convertView;
        }
    }

    private class ListAdapterContacts extends ArrayAdapter<Contacts> {


        public ListAdapterContacts(@NonNull Context context, List<Contacts> kontakty) {
            super(context, 0, kontakty);
            Cache.ZAZNACZONE_GRUPY = new ArrayList<>();
        }

        @NonNull
        @Override
        public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            final Contacts grupa = getItem(position);

            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.grupa_kontaktow, parent, false);
            }
            final MyTextView myTextView = (MyTextView) convertView.findViewById(R.id.myTextViewContacts);
            if (grupa != null) {
                myTextView.setText(grupa.getName());
            } else {
                myTextView.setText("n/a");
            }

            return convertView;
        }
    }

}
