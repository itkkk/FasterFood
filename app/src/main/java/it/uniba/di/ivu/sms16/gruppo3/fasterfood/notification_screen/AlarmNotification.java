package it.uniba.di.ivu.sms16.gruppo3.fasterfood.notification_screen;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import java.util.GregorianCalendar;

public class AlarmNotification {

    public void setAlarm(Activity activity, Bundle reviewBundle) {

        Long alertTime = new GregorianCalendar().getTimeInMillis()+10*1000;

        Intent alertIntent = new Intent(activity,NotificationAlertReceiver.class);

        // MODIFICHE DEL TAT - FUNZIONANTE
        alertIntent.putExtra("localName",reviewBundle.getString("NameLocal"));
        alertIntent.putExtra("ratingLocal",reviewBundle.getFloat("RatingLocal"));
        alertIntent.putExtra("numberRating",reviewBundle.getInt("NumberRating"));
        //alertIntent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);

        AlarmManager alarmManager = (AlarmManager) activity.getSystemService(Context.ALARM_SERVICE);

        alarmManager.set(AlarmManager.RTC_WAKEUP,alertTime, PendingIntent.getBroadcast(activity,1,alertIntent,PendingIntent.FLAG_UPDATE_CURRENT));
        //alarmManager.set(AlarmManager.RTC_WAKEUP,alertTime, PendingIntent.getService(activity,1,alertIntent,PendingIntent.FLAG_UPDATE_CURRENT));
        System.out.println("Invio la notifica");
    }
}
