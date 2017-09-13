package com.zerter.teamconnect;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;

import com.google.gson.Gson;
import com.zerter.teamconnect.Controlers.Cache;
import com.zerter.teamconnect.Controlers.Data;
import com.zerter.teamconnect.Controlers.MyTextView;
import com.zerter.teamconnect.Models.Contacts;
import com.zerter.teamconnect.Models.Person;

import java.util.ArrayList;
import java.util.List;

public class EdytujGrupyFragment extends Fragment {


    private final String TAG = getClass().getName();
    public static ListView listView;
    Button buttonZapiszGrupe, buttonAnulujEdycje;
    Data data;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.edytuj_grupy, container, false);
        listView = (ListView) view.findViewById(R.id.ListViewGrupy);
        buttonZapiszGrupe = (Button) view.findViewById(R.id.button_Zapisz);
        buttonAnulujEdycje = (Button) view.findViewById(R.id.button_anuluj_edycje);

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

                Cache.ZAZNACZONE_GRUPY = new ArrayList<>();
                Cache.ZAZNACZONE_GRUPY.add(data.wczytajGrupyKontaktow().get(position));
                ListAdapterPerson adapter = new ListAdapterPerson(getActivity(), Cache.LISTA_KONTAKTOW, Cache.ZAZNACZONE_GRUPY.get(0).getPersons());
                listView.setAdapter(adapter);
                buttonZapiszGrupe.setVisibility(View.VISIBLE);
                buttonAnulujEdycje.setVisibility(View.VISIBLE);
                Log.d(TAG, Cache.ZAZNACZONE_GRUPY.get(0).getName());

            }
        });

        buttonZapiszGrupe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Contacts> contactsList = data.wczytajGrupyKontaktow();
//                Toast.makeText(getActivity(), Cache.ZAZNACZONE_GRUPY.get(0).getName(), Toast.LENGTH_SHORT).show();
                for (int i = 0; i < contactsList.size(); i++) {

                    if (contactsList.get(i).getName().equals(Cache.ZAZNACZONE_GRUPY.get(0).getName())) {
                        contactsList.get(i).setPersons(Cache.ZAZNACZONE_KONTAKTY);
                    }
                }
                data.zapiszGrupyKontaktow(new Gson().toJson(contactsList));
                setAdapter();
                buttonZapiszGrupe.setVisibility(View.GONE);
                buttonAnulujEdycje.setVisibility(View.GONE);
            }
        });

        buttonAnulujEdycje.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonZapiszGrupe.setVisibility(View.GONE);
                buttonAnulujEdycje.setVisibility(View.GONE);
                setAdapter();
            }
        });
    }




    public void setAdapter() {
//        Arrays.sort(Cache.LISTA_KONTAKTOW.get());
        if (data.wczytajGrupyKontaktow() != null) {
            ListAdapterEditContacts adapter = new ListAdapterEditContacts(getActivity(), data.wczytajGrupyKontaktow());
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

}
