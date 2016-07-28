package it.uniba.di.ivu.sms16.gruppo3.fasterfood.notification_screen;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;

import java.util.ArrayList;

import it.uniba.di.ivu.sms16.gruppo3.fasterfood.R;
import it.uniba.di.ivu.sms16.gruppo3.fasterfood.SplashActivity;

public class NotificationAlertReceiver extends IntentService /*extends BroadcastReceiver*/ {
    private static final String STACKING_NOTIFICATIONS = "stacking_notifications";
    private static final String SERVICE_NAME = "NotificationAlertReceiver";
    private NotificationManager mNotificationManager;

    public NotificationAlertReceiver() {
        super(SERVICE_NAME);
    }

    /*@Override
    public void onReceive(Context context, Intent intent) {
        showVerySimpleNotification(context, intent);
    }*/

    private void showVerySimpleNotification(Context context, Intent intent) {
        // MODIFICHE TAT FUNZIONANTE
        String localName = intent.getStringExtra("localName");
        float ratingReview = intent.getFloatExtra("ratingLocal",0.0f);
        int nmbReview = intent.getIntExtra("numberRating",0);

        int notification_ID = loadNotification(context);

        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        final String contextText = context.getResources().getString(R.string.contex_text_notification_ff);
        final String contextTitle = context.getResources().getString(R.string.title_notification_ff) + " " + localName;

        // PER INVIARE GLI ELEMENTI DAL BROADCAST RECEIVER ALL ACTIVITY
        Intent reviewIntent = new Intent(context,ReviewActivity.class);
        reviewIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        reviewIntent.putExtra("LocalName",localName);
        reviewIntent.putExtra("RatingReview",ratingReview);
        reviewIntent.putExtra("NumberReview",nmbReview);

        // GESTIONE DI NAVIGAZIONE DELLE NOTIFICHE SECONDO IL MATERIAL DESIGN
        /*TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        //stackBuilder.addParentStack(SplashActivity.class);
        stackBuilder.addParentStack(ReviewActivity.class);
        stackBuilder.addNextIntent(reviewIntent);
        PendingIntent actionPendingIntent = stackBuilder.getPendingIntent(0,PendingIntent.FLAG_UPDATE_CURRENT);*/
        PendingIntent actionPendingIntent = PendingIntent.getActivity(context,0,reviewIntent,PendingIntent.FLAG_UPDATE_CURRENT);

        // CREAZIONE DELLA NOTIFICA
        Notification notification = new NotificationCompat.Builder(context)
                .setSmallIcon(R.mipmap.ic_launcher) // Inserimento logo
                .setSound(alarmSound) // Inserimento suoneria predefinita per la notifica
                .setLights(0xff00ff00,300,1000) // Impostazione led
                .setGroup(STACKING_NOTIFICATIONS) // Gestione stack di notificazione
                .setContentIntent(actionPendingIntent) // Passaggio nell'Activity designata
                .setStyle(new NotificationCompat.BigTextStyle().bigText(contextText)) // Uso dei messaggi molti grandi
                .setContentText(contextText) // Inserimento testo
                .setContentTitle(contextTitle).build(); // Inserimento titolo e costruzione notifica
        notification.flags = Notification.FLAG_SHOW_LIGHTS; // Uso del led
        notification.flags = Notification.FLAG_AUTO_CANCEL; // Rimozione notifica se cliccata dall'utente
        mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE); // Costruzione NotificationManager
        mNotificationManager.notify(notification_ID, notification); // Visualizzazione notifica

        saveNotification(context,++notification_ID);
    }

    // PER GESTIRE LA MULTI NOTIFICA UTILIZZO SHARED PREFERENCES PER OTTENERE ID DIFFERENTI
    private int loadNotification(Context context) {
        SharedPreferences prefs = context.getSharedPreferences("PREFS_NAME",Context.MODE_PRIVATE);
        return prefs.getInt("NOTIFICATION_ID",1);
    }

    private void saveNotification(Context context, int notification_ID) {
        SharedPreferences prefs = context.getSharedPreferences("PREFS_NAME",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("NOTIFICATION_ID",notification_ID);
        editor.apply();
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        showVerySimpleNotification(getApplicationContext(),intent);
    }

    /*@Override
    public void onDestroy() {
        super.onDestroy();
        System.out.println("SONO QUI");
        mNotificationManager.cancelAll();
    }*/
}
