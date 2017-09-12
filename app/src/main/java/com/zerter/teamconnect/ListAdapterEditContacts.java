package com.zerter.teamconnect;

import android.app.Activity;
import android.app.FragmentManager;
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
import android.widget.TextView;

import com.zerter.teamconnect.Controlers.Cache;
import com.zerter.teamconnect.Models.Contacts;

import java.util.ArrayList;
import java.util.List;

/**
 * Adapter listy
 */

class ListAdapterEditContacts extends ArrayAdapter<Contacts> {
    private Typeface typeFace;
    private String TAG = getClass().getName();
    private Activity activity;
    ListAdapterEditContacts(@NonNull Activity context, List<Contacts> kontakty) {
        super(context, 0, kontakty);
        this.activity = context;
        Cache.ZAZNACZONE_GRUPY = new ArrayList<>();
        this.typeFace=Typeface.createFromAsset(context.getAssets(),"fonts/Roboto-Regular.ttf");
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final Contacts grupa = getItem(position);

        if (convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_listview,parent,false);
        }
        final TextView textView = (TextView) convertView.findViewById(R.id.textViewItemView);
        if (grupa != null) {
            textView.setText(grupa.getName());
        }else {
            textView.setText("n/a");
        }

        final Button zmieńNazwę = (Button) convertView.findViewById(R.id.button_zmien_nazwe);
        zmieńNazwę.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager manager = activity.getFragmentManager();
                NadpiszNazwęGrupy nadpiszNazwęGrupy = new NadpiszNazwęGrupy();
                if (grupa != null) {
                    Bundle bundle = new Bundle();
                    bundle.putString("nazwa_grupy", grupa.getName());
                    nadpiszNazwęGrupy.setArguments(bundle);
                    Log.d(TAG,grupa.getName());
                }else {
                    Log.d(TAG,"grupa == null");
                }
                nadpiszNazwęGrupy.show(manager, "NadpiszNazwęGrupy");
            }
        });
        textView.setTypeface(typeFace);

        return convertView;
    }
}
