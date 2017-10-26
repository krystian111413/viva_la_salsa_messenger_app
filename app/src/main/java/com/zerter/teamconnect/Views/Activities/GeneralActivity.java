package com.zerter.teamconnect.Views.Activities;

import android.Manifest;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.SmsManager;
import android.text.Spannable;
import android.text.SpannableString;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.zerter.teamconnect.AddContact.AddContactFragment;
import com.zerter.teamconnect.AddPlan.AddPlanFragment;
import com.zerter.teamconnect.Controlers.CustomTypefaceSpan;
import com.zerter.teamconnect.Controlers.PermisionControler.OnResultListener;
import com.zerter.teamconnect.HistoryMessageFragment;
import com.zerter.teamconnect.R;
import com.zerter.teamconnect.Views.Fragments.MenageGroupContacts;
import com.zerter.teamconnect.Views.Fragments.MenageTemplates;
import com.zerter.teamconnect.Views.Fragments.Message;

public class GeneralActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    FloatingActionButton fab;
    public static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 0;
    public static final int MY_PERMISSIONS_REQUEST_SEND_SMS = 1;
    private static android.support.v7.app.ActionBar actionBar;
    private String TAG = getClass().getName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_general);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        actionBar = getSupportActionBar();
        setSupportActionBar(toolbar);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                Message message = new Message();
                setContener(message);
                fab.hide();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //own
        actionBarSetup();

        Message message = new Message();
        setContener(message);
        fab.hide();
        setFontsOnMenu(navigationView);
        hideSoftKeyboard();
        permisionAccessSendSMS();
    }

    @Override
    public void onBackPressed() {

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if(!MenageGroupContacts.groupView){
            permisionAccessReadConstacts(new OnResultListener() {
                @Override
                public void onResultAccepted() {
                    MenageGroupContacts menageGroupContacts = new MenageGroupContacts();
                    setContener(menageGroupContacts);
                }

                @Override
                public void onResultDenyed() {

                }
            });
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.general, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_send_msg) {
            Message message = new Message();
            setContener(message);

        } else if (id == R.id.nav_menage_teams) {
            permisionAccessReadConstacts(new OnResultListener() {
                @Override
                public void onResultAccepted() {
                    MenageGroupContacts menageGroupContacts = new MenageGroupContacts();
                    setContener(menageGroupContacts);
                }

                @Override
                public void onResultDenyed() {

                }
            });

        } else if (id == R.id.nav_history) {
            HistoryMessageFragment historyMessageFragment = new HistoryMessageFragment();
            setContener(historyMessageFragment);

        }else if (id == R.id.nav_template) {
            MenageTemplates menageTemplates = new MenageTemplates();
            setContener(menageTemplates);

        }else if (id == R.id.nav_plan) {
            AddPlanFragment addPlanFragment = new AddPlanFragment();
            setContener(addPlanFragment);

        }else if (id == R.id.nav_add_contact) {
            AddContactFragment addContact = new AddContactFragment();
            setContener(addContact);
        }


        if (id == R.id.nav_send_msg || id == R.id.nav_plan) {
            fab.hide();
        }else {
            fab.show();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void setContener(android.app.Fragment fragment){
        FragmentManager FM = getFragmentManager();
        FragmentTransaction FT = FM.beginTransaction();
        FT.replace(R.id.GeneralContener, fragment);
        FT.commit();
    }

    private void applyFontToMenuItem(MenuItem mi) {
        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/Oswald-Light.ttf");
        SpannableString mNewTitle = new SpannableString(mi.getTitle());
        mNewTitle.setSpan(new CustomTypefaceSpan("", font), 0, mNewTitle.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        mi.setTitle(mNewTitle);
    }

    private void setFontsOnMenu(NavigationView navView) {
        Menu m = navView.getMenu();
        for (int i = 0; i < m.size(); i++) {
            MenuItem mi = m.getItem(i);

            //for aapplying a font to subMenu ...
            SubMenu subMenu = mi.getSubMenu();
            if (subMenu != null && subMenu.size() > 0) {
                for (int j = 0; j < subMenu.size(); j++) {
                    MenuItem subMenuItem = subMenu.getItem(j);
                    applyFontToMenuItem(subMenuItem);
                }
            }

            //the method we have create in activity
            applyFontToMenuItem(mi);
        }
    }
    /**
     * Sets the Action Bar for new Android versions.
     */
    public static void actionBarSetup(String... titles) {

        if (actionBar != null) {
            if (titles.length > 0) {
                if (titles[0] != null) {
                    actionBar.setTitle(titles[0]);
                    actionBar.setSubtitle(null);
                } else {
                    actionBar.setTitle(R.string.app_name);
                    actionBar.setSubtitle(null);
                }
            } else {
                actionBar.setTitle(R.string.app_name);
                actionBar.setSubtitle(null);
            }
            if (titles.length > 1) {
                if (titles[1] != null) {
                    actionBar.setSubtitle(titles[1]);
                }
            }

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_CONTACTS : {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    MenageGroupContacts menageGroupContacts = new MenageGroupContacts();
                    setContener(menageGroupContacts);
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {
                    Toast.makeText(this,"denied",Toast.LENGTH_SHORT).show();

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
            }
            case MY_PERMISSIONS_REQUEST_SEND_SMS : {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    Toast.makeText(this,"accepted",Toast.LENGTH_SHORT).show();


                } else {
                    Toast.makeText(this,"denied",Toast.LENGTH_SHORT).show();

                    finish();
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }
    public void permisionAccessReadConstacts(OnResultListener listener) {
        Log.d(TAG, "permision 1");
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {

            Log.d(TAG, "permision 2");
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_CONTACTS)) {


                Log.d(TAG, "permision 3");

            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_CONTACTS},
                        MY_PERMISSIONS_REQUEST_READ_CONTACTS);

                Log.d(TAG, "permision 4");

            }
        } else {
            listener.onResultAccepted();
        }
    }
    public void hideSoftKeyboard() {
        if(getCurrentFocus()!=null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }

    public static void sendSMS(String phoneNumber, String message) {
        Log.d("sendSMS","numer: " + phoneNumber);
        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(phoneNumber, null, message, null, null);
    }

    public void permisionAccessSendSMS() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.SEND_SMS)) {

            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.SEND_SMS},
                        MY_PERMISSIONS_REQUEST_SEND_SMS);

            }
        }
    }
}
