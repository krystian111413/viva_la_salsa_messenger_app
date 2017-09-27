package com.zerter.teamconnect.Service

import android.app.Service
import android.content.Intent
import android.os.Handler
import android.os.IBinder
import android.telephony.SmsManager
import android.util.Log
import android.widget.Toast
import com.zerter.teamconnect.Controlers.Data
import com.zerter.teamconnect.Models.Message
import java.text.SimpleDateFormat
import java.util.*


/**
 * Usługa do wysyłania sms'ów
 */
class MessageService: Service() {
    private val mHandler = Handler()
    private var mTimer: Timer? = null
    val NOTIFY_INTERVAL = (10 * 1000).toLong()

    override fun onBind(p0: Intent?): IBinder {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onCreate() {
        // cancel if already existed
        if (mTimer != null) {
            mTimer!!.cancel()
        } else {
            // recreate new
            mTimer = Timer()
        }
        // schedule task
        mTimer!!.scheduleAtFixedRate(TimeDisplayTimerTask(), 0, NOTIFY_INTERVAL)
    }

    fun sendSMS(phoneNumber: String, message: String) {
        Log.d("sendSMS", "numer: " + phoneNumber)
        val sms = SmsManager.getDefault()
        sms.sendTextMessage(phoneNumber, null, message, null, null)
    }

    fun getMessagesDates(): MutableList<Message>? {
        val data = Data(applicationContext)
        return data.messagesPlaned
    }

    internal inner class TimeDisplayTimerTask : TimerTask() {

        override fun run() {
            // run on another thread
            mHandler.post(Runnable {
                // display toast
                Toast.makeText(applicationContext, dateTime,
                        Toast.LENGTH_SHORT).show()
            })
        }

        private // get date time in custom format
        val dateTime: String
            get() {
                val sdf = SimpleDateFormat("[yyyy/MM/dd - HH:mm:ss]")
                return sdf.format(Date())
            }

    }
}