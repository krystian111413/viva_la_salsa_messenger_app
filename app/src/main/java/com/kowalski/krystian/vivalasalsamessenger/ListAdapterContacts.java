package com.kowalski.krystian.vivalasalsamessenger;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;

import java.util.ArrayList;
import java.util.List;

/**
 * Adapter listy
 */

class ListAdapterContacts extends ArrayAdapter<Contacts> {

    private Typeface typeFace;
    ListAdapterContacts(@NonNull Context context, List<Contacts> kontakty) {
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
        final CheckBox checkBox = (CheckBox) convertView.findViewById(R.id.checkBoxGrupa);
        if (grupa != null) {
            checkBox.setText(grupa.getName());
        }else {
            checkBox.setText("n/a");
        }
        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkBox.isChecked()){
                    Cache.ZAZNACZONE_GRUPY.add(grupa);
//                    Toast.makeText(getContext(),"id: " + position, Toast.LENGTH_LONG).show();
                }else {
                    Cache.ZAZNACZONE_GRUPY.remove(grupa);
                }
            }
        });
        checkBox.setTypeface(typeFace);
        return convertView;
    }
}
