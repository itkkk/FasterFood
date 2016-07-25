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


        PendingIntent actionPendingIntent = PendingIntent.getActivity(context,0,new Intent(context,GetReviewActivity.class),0);

        Notification notification = new NotificationCompat.Builder(context)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setSound(alarmSound)
                .setAutoCancel(true)
                .setLights(0xff00ff00,300,1000) // USO DEL LED
                .setContentIntent(actionPendingIntent)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(contextText))
                .setContentText(contextText)
                .setContentTitle(contextTitle).build();
        notification.flags = Notification.FLAG_SHOW_LIGHTS; // USO DEL LED
        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(NOTIFICATION_ID, notification);
    }
}
