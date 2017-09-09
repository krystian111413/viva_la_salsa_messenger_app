package com.kowalski.krystian.vivalasalsamessenger;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

/**
 * Okno dialogowe przy potwierdzeniu stworzenia nowej grupy
 */

public class ConfirmCreateGroup extends DialogFragment {

    Button buttonZapisz, buttonAnuluj;
    EditText nazwaGrupy;
    Data data;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.confirm_decision, null);
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        data = new Data(getActivity());
        buttonAnuluj = (Button) view.findViewById(R.id.buttonAnuluj);
        buttonZapisz = (Button) view.findViewById(R.id.buttonZapisz);
        nazwaGrupy = (EditText) view.findViewById(R.id.editTextNazwaGrupy);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        buttonZapisz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (nazwaGrupy.getText().toString().isEmpty()) {
                    Toast.makeText(getActivity(), "Wprowadź nazwę grupy", Toast.LENGTH_LONG).show();
                } else {
                    Contacts contacts = new Contacts();
                    contacts.setPersons(Cache.ZAZNACZONE_KONTAKTY);
                    contacts.setName(nazwaGrupy.getText().toString());
                    stworzGrupeKontaktow(contacts);
                    setAdapter();
                    dismiss();
                }
            }
        });
        buttonAnuluj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setAdapter();
                dismiss();
            }
        });
    }

    private void stworzGrupeKontaktow(Contacts newContactsGroup) {
        List<Contacts> contactsList = new ArrayList<>();
        if (data.wczytajGrupyKontaktow() != null)
            contactsList = data.wczytajGrupyKontaktow();
        contactsList.add(newContactsGroup);
        data.zapiszGrupyKontaktow(new Gson().toJson(contactsList));
    }
    public void setAdapter() {
//        Arrays.sort(Cache.LISTA_KONTAKTOW.get());
        if (data.wczytajGrupyKontaktow() != null) {
            ListAdapterContacts adapter = new ListAdapterContacts(getActivity(), data.wczytajGrupyKontaktow());
            AddGroupContacts.listView.setAdapter(adapter);
        }
    }

}
