package com.sudoajay.whatapp_media_mover_to_sdcard;


import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

public class MyFireBaseInstanceMessageService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.i("getInfo" , remoteMessage.getFrom());

        Map<String, String> data = remoteMessage.getData();
            String url = data.get("Url");
        //Check if the message contains data
        if(remoteMessage.getData().size() > 0) {
            Log.d( "Message data:",  remoteMessage.getData()+"");
        }

        //Check if the message contains notification

        if(remoteMessage.getNotification() != null) {
            Log.d( "Mesage body:", remoteMessage.getNotification().getBody());
            sendNotification(remoteMessage.getNotification().getBody(),url);
        }
    }

    /**
     * Dispay the notification
     * @param body
     */
    private void sendNotification(String body,String url) {

        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0/*Request code*/, i, PendingIntent.FLAG_ONE_SHOT);
        //Set sound of notification
        Uri notificationSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder notifiBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Media Mover")
                .setContentText(body)
                .setAutoCancel(true)
                .setSound(notificationSound)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0 /*ID of notification*/, notifiBuilder.build());
    }
}