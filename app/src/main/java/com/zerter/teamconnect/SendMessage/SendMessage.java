package com.zerter.teamconnect.SendMessage;

import android.telephony.SmsManager;
import android.util.Log;

import com.zerter.teamconnect.Models.Group;
import com.zerter.teamconnect.Models.Person;

/**
 * Created by krystiankowalski on 24.12.2017.
 */

public class SendMessage {

    public void sendMessage(Group group, String message){
        for (Person p :
                group.getPersons()) {
            sendMessage(p.getNumber(), message);
        }
    }

    public void sendMessage(String phoneNumber, String message) {
        Log.d("sendSMS","numer: " + phoneNumber);
        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(phoneNumber, null, message, null, null);
    }
}
