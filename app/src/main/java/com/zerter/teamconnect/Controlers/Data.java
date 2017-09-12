package com.zerter.teamconnect.Controlers;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zerter.teamconnect.Models.Contacts;

import java.util.List;

/**
 * Klasa zarządzająca danymi lokalnymi aplikacji
 */

public class Data {
    private Context context;

    public Data(Context context) {
        this.context = context;
    }

    public void zapiszGrupyKontaktow(String value){
        SharedPreferences sharedPreferences = context.getSharedPreferences("MyData", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("contacts", value);
        editor.apply();
    }
    public List<Contacts> wczytajGrupyKontaktow(){
        SharedPreferences sharedPreferences = context.getSharedPreferences("MyData", Context.MODE_PRIVATE);
        String s = sharedPreferences.getString("contacts", "");
        java.lang.reflect.Type type = new TypeToken<List<Contacts>>(){}.getType();
        return new Gson().fromJson(s,type);
    }
}
