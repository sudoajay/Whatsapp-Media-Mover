package com.sudoajay.whatsapp_media_mover_to_sdcard.Receive_Boot_Completed;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class MyRebootReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent serviceIntent = new Intent(context, ForegroundServiceBoot.class);
        serviceIntent.putExtra("caller", "RebootReceiver");
        context.startService(serviceIntent);
    }
}