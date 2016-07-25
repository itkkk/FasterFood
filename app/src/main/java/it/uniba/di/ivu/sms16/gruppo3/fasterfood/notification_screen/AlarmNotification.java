package it.uniba.di.ivu.sms16.gruppo3.fasterfood.notification_screen;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import java.util.GregorianCalendar;

public class AlarmNotification {

    public void setAlarm(Activity activity) {

        Long alertTime = new GregorianCalendar().getTimeInMillis()+10*1000;
        Intent alertIntent = new Intent(activity,NotificationAlertReceiver.class);
        AlarmManager alarmManager = (AlarmManager) activity.getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP,alertTime, PendingIntent.getBroadcast(activity,1,alertIntent,PendingIntent.FLAG_UPDATE_CURRENT));

    }
}
