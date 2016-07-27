package it.uniba.di.ivu.sms16.gruppo3.fasterfood.notification_screen;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import it.uniba.di.ivu.sms16.gruppo3.fasterfood.R;

public class NotificationAlertReceiver extends BroadcastReceiver{
    private int NOTIFICATION_ID = 1;

    @Override
    public void onReceive(Context context, Intent intent) {
        showVerySimpleNotification(context, intent);
    }

    private void showVerySimpleNotification(Context context, Intent intent) {
        // MODIFICHE TAT FUNZIONANTE
        String localName = intent.getStringExtra("localName");
        float ratingReview = intent.getFloatExtra("ratingLocal",0.0f);
        int nmbReview = intent.getIntExtra("numberRating",0);

        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        final String contextText = context.getResources().getString(R.string.contex_text_notification_ff);
        final String contextTitle = context.getResources().getString(R.string.title_notification_ff) + " " + localName;

        System.out.println("NOTIFICA");
        Intent reviewIntent = new Intent(context,ReviewActivity.class);
        reviewIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        reviewIntent.putExtra("LocalName",localName);
        reviewIntent.putExtra("RatingReview",ratingReview);
        reviewIntent.putExtra("NumberReview",nmbReview);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(ReviewActivity.class);
        stackBuilder.addNextIntent(reviewIntent);
        PendingIntent actionPendingIntent = stackBuilder.getPendingIntent(0,PendingIntent.FLAG_CANCEL_CURRENT);
               // PendingIntent.getActivity(context,0,reviewIntent,PendingIntent.FLAG_CANCEL_CURRENT);

        Notification notification = new NotificationCompat.Builder(context)
                .setSmallIcon(R.mipmap.ic_launcher) // Inserimento logo
                .setSound(alarmSound) // Inserimento suoneria predefinita per la notifica
                .setLights(0xff00ff00,300,1000) // Impostazione led
                .setAutoCancel(true)
                .setContentIntent(actionPendingIntent) // Passaggio nell'Activity designata
                .setStyle(new NotificationCompat.BigTextStyle().bigText(contextText)) // Uso dei messaggi molti grandi
                .setContentText(contextText) // Inserimento testo
                .setContentTitle(contextTitle).build(); // Inserimento titolo e costruzione notifica
        notification.flags = Notification.FLAG_SHOW_LIGHTS; // Uso del led
        notification.flags = Notification.FLAG_AUTO_CANCEL; // Rimozione notifica se cliccata dall'utente
        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE); // Costruzione NotificationManager
        mNotificationManager.notify(NOTIFICATION_ID, notification); // Visualizzazione notifica
    }
}
