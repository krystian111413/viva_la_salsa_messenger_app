package com.zerter.teamconnect.Views.Fragments;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.zerter.teamconnect.Controlers.Cache;
import com.zerter.teamconnect.Controlers.Data;
import com.zerter.teamconnect.Controlers.MyTextView;
import com.zerter.teamconnect.Models.Contacts;
import com.zerter.teamconnect.R;

import java.util.ArrayList;
import java.util.List;

public class Message extends Fragment {


    private final String TAG = getClass().getName();
    Button wyslij;
    EditText editTextTrescWiadomosci;
    ListView listView;
    Data data;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_message,container,false);

        wyslij = (Button) view.findViewById(R.id.button_wyslij);
        editTextTrescWiadomosci = (EditText) view.findViewById(R.id.editText_tresc_smsa);
        listView = (ListView) view.findViewById(R.id.ListViewListaGrupKontaktow);

        return view;
    }

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        data = new Data(getActivity());
        setAdapter();
        wyslij.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Contacts> zaznaczoneGrupy = Cache.ZAZNACZONE_GRUPY;
                for (int i = 0; i < zaznaczoneGrupy.size(); i++){
                    for (int j = 0; j < zaznaczoneGrupy.get(i).getPersons().size(); j++){
                        sendSMS(zaznaczoneGrupy.get(i).getPersons().get(j).getNumber(),editTextTrescWiadomosci.getText().toString().trim());
//                        Log.d(TAG,zaznaczoneGrupy.get(i).getPersons().get(j).getNumber());
                    }
                }
                new AlertDialog.Builder(getActivity())
                        .setMessage("Wiadomość została wysłana")
                        .show();
                editTextTrescWiadomosci.getText().clear();
            }
        });
    }



    private void sendSMS(String phoneNumber, String message) {
        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(phoneNumber, null, message, null, null);
    }
    private void setAdapter(){
        if (data.wczytajGrupyKontaktow() != null){
            ListAdapterContacts adapter = new ListAdapterContacts(getActivity(),data.wczytajGrupyKontaktow());
            listView.setAdapter(adapter);
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
