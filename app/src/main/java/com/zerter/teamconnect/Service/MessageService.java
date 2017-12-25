package com.zerter.teamconnect.Service;

import android.app.Service;
import android.content.Intent;
import android.icu.text.DateFormat;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.zerter.teamconnect.AddPlan.Plan;
import com.zerter.teamconnect.Controlers.Data;
import com.zerter.teamconnect.Models.Group;
import com.zerter.teamconnect.SendMessage.SendMessage;

import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Android service to send planned messages
 */

public class MessageService extends Service {
    Integer dayly = 1000 * 60 * 60 * 24; // 1sec * 60sec * 60min * 24h
    Integer weekly = 1000 * 60 * 60 * 24 * 7; // 1sec * 60sec * 60min * 24h * 7days
    Integer monthly = 1000 * 60 * 60 * 24 * 7 * 4; // 1sec * 60sec * 60min * 24h * 7days * 4 weeks
    Integer yearly = 1000 * 60 * 60 * 24 * 7 * 4 * 12; // 1sec * 60sec * 60min * 24h * 7days * 4 weeks * 12 month

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        Data data = new Data(this);

        for (Plan plan :
                data.getMessagesPlaned()) {
            setPlanedMessage(plan);
        }

    }

    void setPlanedMessage(Plan plan) {
        DateFormat dateFormatter;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            dateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            Date date = null;
            try {
                date = dateFormatter.parse(plan.getDate() + " " + plan.getTime() + ":00");

            } catch (ParseException e) {
                e.printStackTrace();
            }

            Timer timer = new Timer();

            switch (plan.getRepeatValue()) {
                case 0:
                    timer.schedule(new MyTimeTask(plan.getGroups(), plan.getText()), date);
                    break;
                case 1:
                    timer.schedule(new MyTimeTask(plan.getGroups(), plan.getText()), date, dayly);
                    break;
                case 2:
                    timer.schedule(new MyTimeTask(plan.getGroups(), plan.getText()), date, weekly);
                    break;
                case 3:
                    timer.schedule(new MyTimeTask(plan.getGroups(), plan.getText()), date, monthly);
                    break;
                case 4:
                    timer.schedule(new MyTimeTask(plan.getGroups(), plan.getText()), date, yearly);
                    break;

                default:
                    timer.schedule(new MyTimeTask(plan.getGroups(), plan.getText()), date);
                    break;
            }

        }
    }

    private static class MyTimeTask extends TimerTask {

        List<Group> groups;
        String message;

        MyTimeTask(List<Group> groups, String message) {
            this.groups = groups;
            this.message = message;
        }

        public void run() {
            SendMessage sendMessage = new SendMessage();
            for (Group group :
                    groups) {
                sendMessage.sendMessage(group, message);
            }
        }
    }
}
