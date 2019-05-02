package com.sudoajay.whatsapp_media_mover_to_sdcard.Receive_Boot_Completed;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.sudoajay.whatsapp_media_mover_to_sdcard.sharedPreferences.TraceBackgroundService;

public class MyRebootReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        TraceBackgroundService traceBackgroundService = new TraceBackgroundService(context);
        if (!traceBackgroundService.isBackgroundServiceWorking()) {
            Intent serviceIntent = new Intent(context, ForegroundServiceBoot.class);
            serviceIntent.setAction("RebootReceiver");
            context.startService(serviceIntent);
        }
    }
}