package com.zerter.teamconnect.Service;

import android.app.Service;
import android.content.Intent;
import android.icu.text.DateFormat;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.gson.Gson;
import com.zerter.teamconnect.AddPlan.Plan;
import com.zerter.teamconnect.Controlers.Data;
import com.zerter.teamconnect.SendMessage.SendMessage;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Android service to send planned messages
 */

public class MessageService extends Service {
    private final String TAG = getClass().getName();
    Integer daily = 1000 * 60 * 60 * 24; // 1sec * 60sec * 60min * 24h
    Integer weekly = 1000 * 60 * 60 * 24 * 7; // 1sec * 60sec * 60min * 24h * 7days
    Integer monthly = 1000 * 60 * 60 * 24 * 7 * 4; // 1sec * 60sec * 60min * 24h * 7days * 4 weeks
    Integer yearly = 1000 * 60 * 60 * 24 * 7 * 4 * 12; // 1sec * 60sec * 60min * 24h * 7days * 4 weeks * 12 month
    List<ServiceObject> timers;
    Data data;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        Timer timer = new Timer();

        data = new Data(this);
        timer.schedule(new MyTimeTask(data), 0, 10000); // 10 sec reload all settings
//        timers.add(timer);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        for (ServiceObject timer : timers) {
            timer.getTimer().cancel();
        }
    }

    void setPlanedMessage(Plan plan, Boolean edited) {
        DateFormat dateFormatter;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            dateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            MyTimeTask myTimeTask = new MyTimeTask(plan);
            Date date = null;
            try {
//                date = dateFormatter.parse("2018-01-02" + " " + "18:35" + ":00");
                date = dateFormatter.parse(plan.getDate() + " " + plan.getTime() + ":00");

            } catch (ParseException e) {
                e.printStackTrace();
            }
            if (!edited) {
                Timer timer = new Timer();



                switch (plan.getRepeatValue()) {
                    case 0:
                        timer.schedule(myTimeTask, date);
                        break;
                    case 1:
                        timer.schedule(myTimeTask, date, daily);
                        break;
                    case 2:
                        timer.schedule(myTimeTask, date, weekly);
                        break;
                    case 3:
                        timer.schedule(myTimeTask, date, monthly);
                        break;
                    case 4:
                        timer.schedule(myTimeTask, date, yearly);
                        break;

                    default:
                        timer.schedule(myTimeTask, date);
                        break;
                }
                timers.add(new ServiceObject(timer, plan));
            } else {

                for (ServiceObject object :
                        timers) {
                    if (object.getPlan().getId().equals(plan.getId())) {
                        switch (plan.getRepeatValue()) {
                            case 0:
                                object.getTimer().schedule(myTimeTask, date);
                                break;
                            case 1:
                                object.getTimer().schedule(myTimeTask, date, daily);
                                break;
                            case 2:
                                object.getTimer().schedule(myTimeTask, date, weekly);
                                break;
                            case 3:
                                object.getTimer().schedule(myTimeTask, date, monthly);
                                break;
                            case 4:
                                object.getTimer().schedule(myTimeTask, date, yearly);
                                break;

                            default:
                                object.getTimer().schedule(myTimeTask, date);
                                break;
                        }
                    }


                }
            }

        }
    }

    private class MyTimeTask extends TimerTask {

        Data data = null;
        Plan plan;

        public MyTimeTask(Plan plan) {
            this.plan = plan;
        }

        MyTimeTask(Data data) {
            this.data = data;
        }


        public void run() {
            if (data != null) {
                Log.d(TAG, "data != null");
                List<Plan> planList = new ArrayList<>();
                for (Plan plan :
                        data.getMessagesPlaned()) {

                    if (!plan.getPlaned()) {
                        setPlanedMessage(plan, false);
                        Log.d(TAG, "set");

                    } else if (plan.getEdited()) {
                        setPlanedMessage(plan, true);
                    }

                    plan.setEdited(false);
                    plan.setPlaned(true);
                    planList.add(plan);
                }
                data.setMessagesPlaned(new Gson().toJson(planList));
            } else {
                SendMessage sendMessage = new SendMessage();
//            for (Group group :
//                    groups) {
//                sendMessage.sendMessage(group, message);
//            }
//            sendMessage.sendMessage("538496880","test");
                Log.d(TAG, "DZIALA");
            }
        }
    }

    private class ServiceObject {
        Timer timer;
        Plan plan;

        public ServiceObject(Timer timer, Plan plan) {
            this.timer = timer;
            this.plan = plan;
        }

        public Timer getTimer() {
            return timer;
        }

        public void setTimer(Timer timer) {
            this.timer = timer;
        }

        public Plan getPlan() {
            return plan;
        }

        public void setPlan(Plan plan) {
            this.plan = plan;
        }
    }
}
