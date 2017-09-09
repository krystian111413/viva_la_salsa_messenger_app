package com.kowalski.krystian.vivalasalsamessenger;

import android.Manifest;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class MainActivity extends Activity {
    public static final int MY_PERMISSIONS_REQUEST_SEND_SMS = 1;
    public static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 1;
    private final String TAG = getClass().getName();
    boolean readContacts = false;
    boolean sensSMS = false;
    private TextView mTextMessage;
    Message message = new Message();
    AddGroupContacts addGroupContacts = new AddGroupContacts();
    EdytujGrupyFragment edytujGrupyFragment = new EdytujGrupyFragment();
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            FragmentManager FM = getFragmentManager();
            FragmentTransaction FT = FM.beginTransaction();
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    mTextMessage.setText(R.string.title_home);
                    FT.replace(R.id.content, message);
                    FT.commit();
                    return true;
                case R.id.navigation_dashboard:
                    mTextMessage.setText(R.string.title_dashboard);

                    FT.replace(R.id.content, addGroupContacts);
                    FT.commit();
                    return true;
                case R.id.navigation_notifications:
                    mTextMessage.setText(R.string.title_notifications);
                    FT.replace(R.id.content, edytujGrupyFragment);
                    FT.commit();
                    return true;
            }
            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        permisionAccessReadConstacts();
        permisionAccessSendSMS();

        if (readContacts && sensSMS) {

            setLayout();
        }
    }

    private void setLayout() {
        getContacts();
        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        FragmentManager FM = getFragmentManager();
        FragmentTransaction FT = FM.beginTransaction();
        FT.addToBackStack("EdytujGrupyFragment");
        Message fragment = new Message();
        FT.add(R.id.content, fragment);
        FT.commit();
    }

    private void permisionAccessReadConstacts() {
        Log.d(TAG, "permision 1");
        if (ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {

            Log.d(TAG, "permision 2");
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                    Manifest.permission.READ_CONTACTS)) {


                Log.d(TAG, "permision 3");

            } else {
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.READ_CONTACTS},
                        MY_PERMISSIONS_REQUEST_READ_CONTACTS);

                Log.d(TAG, "permision 4");

            }
        } else {
            readContacts = true;
        }
    }

    private void permisionAccessSendSMS() {
        if (ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                    Manifest.permission.SEND_SMS)) {

            } else {
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.SEND_SMS},
                        MY_PERMISSIONS_REQUEST_SEND_SMS);

            }
        } else {
            sensSMS = true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MainActivity.MY_PERMISSIONS_REQUEST_SEND_SMS: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    setLayout();
                } else {
                    Toast.makeText(MainActivity.this, "Zezwól na uprawnienia!", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        }
    }

    public void getContacts() {
//        Contacts contacts = new Contacts();
        Cache.LISTA_KONTAKTOW = new ArrayList<>();

        Cursor phones = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
        if (phones != null) {
            while (phones.moveToNext()) {
                String name = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                String phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                Person person = new Person();
                person.setName(name);
                person.setNumber(phoneNumber);
                for (Person p: Cache.LISTA_KONTAKTOW){
                    if (p.getNumber().equals(person.getNumber())) {
                        Cache.LISTA_KONTAKTOW.remove(Cache.LISTA_KONTAKTOW.size()-1);
                    }
                }

                Cache.LISTA_KONTAKTOW.add(person);
                //            contacts.getPersons().add(person);

            }

            Collections.sort(Cache.LISTA_KONTAKTOW, new Comparator<Person>() {
                @Override
                public int compare(Person o1, Person o2) {
                    return o1.getName().compareTo(o2.getName());
                }
            });
        } else {
            Toast.makeText(MainActivity.this, "Brak kontaktów w telefonie", Toast.LENGTH_LONG).show();
        }
        if (phones != null) {
            phones.close();
        }
//        Toast.makeText(getApplicationContext(),"ilosc kontaktow: " + contacts.getPersons().size(), Toast.LENGTH_LONG).show();

    }

}
