package com.sudoajay.whatapp_media_mover_to_sdcard.Notification;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Build;
import android.os.Environment;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.sudoajay.whatapp_media_mover_to_sdcard.Duplication_Data;
import com.sudoajay.whatapp_media_mover_to_sdcard.Main_Navigation;
import com.sudoajay.whatapp_media_mover_to_sdcard.R;
import com.sudoajay.whatapp_media_mover_to_sdcard.Storage_Info;
import com.sudoajay.whatapp_media_mover_to_sdcard.sharedPreferences.SdCardPathSharedPreference;

import java.io.File;
import java.util.List;

/**
 * Helper class for showing and canceling alert
 * notifications.
 * <p>
 * This class makes heavy use of the {@link NotificationCompat.Builder} helper
 * class to create notifications in a backward-compatible way.
 */
public class NotifyNotification {
    /**
     * The unique identifier for this type of notification.
     */
    private static final String NOTIFICATION_TAG = "Alert_";
    private Context context;
    private NotificationManager notificationManager;
    private String total_Size= "0 MB";
    private Activity activity;


    /**
     * Shows the notification, or updates a previously shown notification of
     * this type, with the given parameters.
     * <p>
     * TODO: Customize this method's arguments to present relevant content in
     * the notification.
     * <p>
     * TODO: Customize the contents of this method to tweak the behavior and
     * presentation of alert  notifications. Make
     * sure to follow the
     * <a href="https://developer.android.com/design/patterns/notifications.html">
     * Notification design guidelines</a> when doing so.
     *
     * @see #cancel(Context)
     */

    // Constructor
    public NotifyNotification(final Context context ){
        this.context = context;
    }

    public void notify(final String notification_Hint) {

        // local variable
        String text="",channel_id;
        final Resources res = context.getResources();
        Intent intent;

        // Grab The Size From Database
        // Whatsapp Size
        GrabTheSize(notification_Hint);

        // setup intent and passing value
        intent = new Intent(context,Main_Navigation.class);

        if(notification_Hint.equalsIgnoreCase("Size Of Duplication Data"))
            intent.putExtra("passing","DuplicateData");


        // setup according Which Type
        // if There is no data match with query

        channel_id = context.getString(R.string.notify_Notification); // channel_id
        text = total_Size ;


        // now check for null notification manger
        if (notificationManager == null) {
            notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        }

        // this check for android Oero In which Channel Id Come as New Feature
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            assert notificationManager != null;
            NotificationChannel mChannel = notificationManager.getNotificationChannel(channel_id);
            if (mChannel == null) {
                mChannel = new NotificationChannel(channel_id, notification_Hint, importance);
                notificationManager.createNotificationChannel(mChannel);
            }
        }

        final NotificationCompat.Builder builder = new NotificationCompat.Builder(context,channel_id)

                // Set appropriate defaults for the notification light, sound,
                // and vibration.
                .setDefaults(Notification.DEFAULT_ALL)
                // Set required fields, including the small icon, the
                // notification title, and text.
                .setContentTitle(notification_Hint)
                .setContentText(text )

                // All fields below this line are optional.

                // Use a default priority (recognized on devices running Android
                // 4.1 or later)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
           //     .setSound(uri)
                // Provide a large icon, shown with the notification in the
                // notification drawer on devices running Android 3.0 or later.
                // Set ticker text (preview) information for this notification.
                .setTicker(notification_Hint)

                // Show a number. This is useful when stacking notifications of
                // a single type.
                .setNumber(1)

                // If this notification relates to a past or upcoming event, you
                // should set the relevant time information using the setWhen
                // method below. If this call is omitted, the notification's
                // timestamp will by set to the time at which it was shown.
                // TODO: Call setWhen if this notification relates to a past or
                // upcoming event. The sole argument to this method should be
                // the notification timestamp in milliseconds.
                //.setWhen(...)

                // Set the pending intent to be initiated when the user touches
                // the notification.
                .setContentIntent(
                        PendingIntent.getActivity(
                                context,
                                0,
                                intent,
                                PendingIntent.FLAG_UPDATE_CURRENT))

                // Show an expanded list of items on devices running Android 4.1
                // or later.


                // Example additional actions for this notification. These will
                // only show on devices running Android 4.1 or later, so you
                // should ensure that the activity in this notification's
                // content intent provides access to the same actions in
                // another way.


                // Automatically dismiss the notification when it is touched.
                .setAutoCancel(true);

        // check if there ia data with empty
        // more and view button classification
            builder.setSmallIcon(R.mipmap.ic_launcher);
            notify(context, builder.build());
    }
    @TargetApi(Build.VERSION_CODES.ECLAIR)
    private void notify(final Context context, final Notification notification ) {

        notificationManager.notify(NOTIFICATION_TAG, 0, notification);
    }


    /**
     * Cancels any notifications of this type previously shown using
     * {@link #notify( String)}.
     */
    @TargetApi(Build.VERSION_CODES.ECLAIR)
    private static void cancel(final Context context) {
        final NotificationManager nm = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);
        nm.cancel(NOTIFICATION_TAG, 0);
    }

    private void GrabTheSize(final String hint) {
        // local size
        String size, whatsappPath, externalPath, sdCardPath;
        List<String> path;
        long longSize = 0;
        int externalVisible = View.VISIBLE, sdCardVisible = View.VISIBLE;

        // fallback error
        try {

            if (hint.equalsIgnoreCase("Size Of Whatsapp Data")) {
                Storage_Info storage_info = new Storage_Info(context);
                total_Size = storage_info.getWhatsAppInternalMemorySize();
            } else if(hint.equalsIgnoreCase("Size Of Duplication Data")) {
                // get the sd card path
                SdCardPathSharedPreference sdCardPathSharedPreference = new SdCardPathSharedPreference(context);
                sdCardPath = sdCardPathSharedPreference.getSdCardPath();
                // get the external sd card path
                externalPath = Environment.getExternalStorageDirectory().getAbsolutePath();

                Storage_Info storage_info = new Storage_Info(context);

                // check for external Storage Is exist
                if (!new File(externalPath).exists()) externalVisible = View.INVISIBLE;

                // check for sd storage is exist
                if (!new File(sdCardPath).exists()) sdCardVisible = View.INVISIBLE;

                Duplication_Data duplication_data = new Duplication_Data();
                duplication_data.Duplication(context, new File(externalPath + storage_info.getWhatsapp_Path() + "/" + ""),
                        new File(sdCardPath + storage_info.getWhatsapp_Path() + "/" + ""), externalVisible, sdCardVisible);
                path = duplication_data.getList();

                for (String get : path) {
                    if (!get.equalsIgnoreCase("and")) {
                        longSize += new File(get).length();
                    }
                }

                total_Size = storage_info.Convert_It(longSize);
            }else if(hint.contains("Error on Data")){
                total_Size= "Please do this process manually (One Time)";
            }else{
                total_Size = "";
            }

        }catch (Exception e){
            Toast.makeText(context,context.getResources().getText(R.string.fallBackError),Toast.LENGTH_LONG).show();
        }
    }


}
