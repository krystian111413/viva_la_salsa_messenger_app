package com.zerter.teamconnect.Dialogs;

import android.app.DialogFragment;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.zerter.teamconnect.Controlers.Cache;
import com.zerter.teamconnect.Controlers.Data;
import com.zerter.teamconnect.Controlers.MyTextView;
import com.zerter.teamconnect.Views.Fragments.MenageGroupContacts;
import com.zerter.teamconnect.Models.Contacts;
import com.zerter.teamconnect.R;

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
            MenageGroupContacts.listView.setAdapter(adapter);
        }
    }
    class ListAdapterContacts extends ArrayAdapter<Contacts> {

        private Typeface typeFace;
        public ListAdapterContacts(@NonNull Context context, List<Contacts> kontakty) {
            super(context, 0, kontakty);
            Cache.ZAZNACZONE_GRUPY = new ArrayList<>();
            this.typeFace=Typeface.createFromAsset(context.getAssets(),"fonts/Roboto-Light.ttf");
        }

        @NonNull
        @Override
        public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            final Contacts grupa = getItem(position);

            if (convertView == null){
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.grupa_kontaktow,parent,false);
            }
            final MyTextView myTextView = (MyTextView) convertView.findViewById(R.id.myTextViewContacts);
            if (grupa != null) {
                myTextView.setText(grupa.getName());
            }else {
                myTextView.setText("n/a");
            }

            return convertView;
        }
    }
}
