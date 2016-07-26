package it.uniba.di.ivu.sms16.gruppo3.fasterfood.notification_screen;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v7.app.NotificationCompat;
import it.uniba.di.ivu.sms16.gruppo3.fasterfood.R;

public class NotificationAlertReceiver extends BroadcastReceiver{
    private static final int NOTIFICATION_ID = 1;

    @Override
    public void onReceive(Context context, Intent intent) {
        showVerySimpleNotification(context);
    }

    private void showVerySimpleNotification(Context context) {

        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        final String contextText = context.getResources().getString(R.string.contex_text_notification_ff);
        final String contextTitle = context.getResources().getString(R.string.title_notification_ff);

        Intent reviewIntent = new Intent(context,ReviewActivity.class);
        PendingIntent actionPendingIntent = PendingIntent.getActivity(context,0,reviewIntent,PendingIntent.FLAG_CANCEL_CURRENT);

        Notification notification = new NotificationCompat.Builder(context)
                .setSmallIcon(R.mipmap.ic_launcher) // Inserimento logo
                .setSound(alarmSound) // Inserimento suoneria predefinita per la notifica
                //.setAutoCancel(true) // Notifica cancellabile
                .setLights(0xff00ff00,300,1000) // Impostazione led
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
