package com.kowalski.krystian.vivalasalsamessenger;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

/**
 * Adapter listy
 */

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
        Log.d(TAG,new Gson().toJson(zaznaczoneKontakty));
        this.zaznaczoneKontakty = zaznaczoneKontakty;
        this.context = context;
        Cache.ZAZNACZONE_KONTAKTY = new ArrayList<>();
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final Person kontakt = getItem(position);

        LayoutInflater inflater = context.getLayoutInflater();
            convertView = inflater.inflate(R.layout.grupa_kontaktow,null,false);

        final CheckBox checkBox = (CheckBox) convertView.findViewById(R.id.checkBoxGrupa);
        if (kontakt != null) {
            checkBox.setText(kontakt.getName() + "\n" + kontakt.getNumber());
        }else {
            checkBox.setText("n/a");
        }

        if (!zaznaczoneKontakty.isEmpty()){
            for (Person p :
                    zaznaczoneKontakty) {
                if (p.getName().equals(kontakt.getName()) && p.getNumber().equals(kontakt.getNumber())){
                    checkBox.setChecked(true);
                    if (!Cache.ZAZNACZONE_KONTAKTY.contains(kontakt)){
                        Cache.ZAZNACZONE_KONTAKTY.add(kontakt);
                    }
                    Log.d(getClass().getName(),kontakt.getName());
                }
            }
        }
        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkBox.isChecked()) {
                    Cache.ZAZNACZONE_KONTAKTY.add(kontakt);
                    Log.d(TAG, "dodano numer");
//                    Toast.makeText(getContext(), "id: " + position, Toast.LENGTH_LONG).show();
                }else {
                    Cache.ZAZNACZONE_KONTAKTY.remove(kontakt);
                    Log.d(TAG, "usunieto numer");
                }
            }
        });
        return convertView;
    }
}
