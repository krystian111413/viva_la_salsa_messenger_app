package com.kowalski.krystian.vivalasalsamessenger;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.List;

/**
 * Okno dialogowe przy potwierdzeniu stworzenia nowej grupy
 */

public class NadpiszNazwęGrupy extends DialogFragment {

    Button buttonZapisz, buttonAnuluj;
    EditText nazwaGrupy;
    Data data;
    Bundle bundle;
    String TAG = getClass().getName();
    String nazwa = null;
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
        bundle = getArguments();
        if (bundle != null) {
            nazwa = bundle.getString("nazwa_grupy");
            Log.d(TAG,nazwa);
        }else {
            Log.d(TAG,"nazwa is null");
        }
        nazwaGrupy.setText(nazwa);
        final String finalNazwa = nazwa;
        buttonZapisz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (nazwaGrupy.getText().toString().isEmpty()) {
                    Toast.makeText(getActivity(), "Wprowadź nazwę grupy", Toast.LENGTH_LONG).show();
                } else {
                    List<Contacts> contacts = data.wczytajGrupyKontaktow();
                    for (int i = 0; i < contacts.size(); i++){
                        if (contacts.get(i).getName().equals(finalNazwa)){
                            contacts.get(i).setName(nazwaGrupy.getText().toString());
                        }
                    }
                    data.zapiszGrupyKontaktow(new Gson().toJson(contacts));
                    setAdapter();
                    dismiss();
                }
            }
        });
        buttonAnuluj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                setAdapter();
                dismiss();
            }
        });
    }

    public void setAdapter() {
        if (data.wczytajGrupyKontaktow() != null) {
            ListAdapterEditContacts adapter = new ListAdapterEditContacts(getActivity(), data.wczytajGrupyKontaktow());

            EdytujGrupyFragment.listView.setAdapter(adapter);
        }
    }

}
