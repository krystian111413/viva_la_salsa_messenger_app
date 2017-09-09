package com.kowalski.krystian.vivalasalsamessenger;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class AddGroupContacts extends Fragment {


    public static ListView listView;
    Button buttonStworzGrupe, buttonUsunGrupe;
    Data data;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.grupy_kontaktow, container, false);
        listView = (ListView) view.findViewById(R.id.ListViewGrupy);
        buttonStworzGrupe = (Button) view.findViewById(R.id.buttonEdytujGrupe);
        buttonUsunGrupe = (Button) view.findViewById(R.id.buttonUsunGrupe);
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
                    buttonStworzGrupe.setText("DODAJ GRUPE");
                    FragmentManager manager = getFragmentManager();
                    ConfirmCreateGroup confirmCreateGroup = new ConfirmCreateGroup();
                    confirmCreateGroup.show(manager, "ConfirmCreateGroup");
//                    setAdapter();
                } else {
                    buttonStworzGrupe.setText("OK");
                    ListAdapterPerson adapter = new ListAdapterPerson(getActivity(), Cache.LISTA_KONTAKTOW);
                    listView.setAdapter(adapter);

                }
            }
        });
        buttonUsunGrupe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Contacts> contactsList = new ArrayList<>();
                if (data.wczytajGrupyKontaktow() != null)
                    contactsList = data.wczytajGrupyKontaktow();
                for (int i = 0; i < Cache.ZAZNACZONE_GRUPY.size(); i++) {
                    String nazwaZaznaczonejGrupy = Cache.ZAZNACZONE_GRUPY.get(i).getName();
                    for (int j = 0; j < contactsList.size(); j++) {
                        if (contactsList.get(j).getName().equals(nazwaZaznaczonejGrupy)) {
                            //usun z glownej listy
                            contactsList.remove(j);
                        }
                    }
                }


//                contactsList.remove(Cache.ZAZNACZONE_GRUPY);
                data.zapiszGrupyKontaktow(new Gson().toJson(contactsList));
                setAdapter();
            }
        });
    }

    public void setAdapter() {
//        Arrays.sort(Cache.LISTA_KONTAKTOW.get());
        if (data.wczytajGrupyKontaktow() != null) {
            ListAdapterContacts adapter = new ListAdapterContacts(getActivity(), data.wczytajGrupyKontaktow());
            listView.setAdapter(adapter);
        }
    }


}
