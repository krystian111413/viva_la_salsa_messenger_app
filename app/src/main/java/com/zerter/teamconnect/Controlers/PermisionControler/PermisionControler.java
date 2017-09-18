package com.zerter.teamconnect.Controlers.PermisionControler;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

/**
 * permision controler
 */

public class PermisionControler extends Activity {

    private Activity activity;
    public static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 1;
    public static final int MY_PERMISSIONS_REQUEST_SEND_SMS = 0;
    private String TAG = getClass().getName();
    public PermisionControler(Activity activity) {
        this.activity = activity;
    }

    public void permisionAccessReadConstacts(OnResultListener listener) {
        Log.d(TAG, "permision 1");
        if (ContextCompat.checkSelfPermission(activity,
                Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {

            Log.d(TAG, "permision 2");
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity,
                    Manifest.permission.READ_CONTACTS)) {


                Log.d(TAG, "permision 3");

            } else {
                ActivityCompat.requestPermissions(activity,
                        new String[]{Manifest.permission.READ_CONTACTS},
                        MY_PERMISSIONS_REQUEST_READ_CONTACTS);

                Log.d(TAG, "permision 4");

            }
        } else {
            listener.onResultAccepted();
        }
    }

    public void permisionAccessSendSMS(OnResultListener listener) {
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
        } else {
            listener.onResultAccepted();
        }
    }
}

