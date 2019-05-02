package com.sudoajay.whatsapp_media_mover_to_sdcard.Receive_Boot_Completed;

import android.app.IntentService;
import android.content.Intent;

import com.sudoajay.whatsapp_media_mover_to_sdcard.ForegroundService.Foreground;

public class ForegroundServiceBoot extends IntentService {

    public ForegroundServiceBoot(){
        super(null);
    }


    @Override
    protected void onHandleIntent(Intent intent) {

        if (intent.getAction() != null && intent.getAction().equalsIgnoreCase("RebootReceiver")) {
            Intent startIntent = new Intent(getApplicationContext(), Foreground.class);
            startIntent.putExtra("com.sudoajay.whatapp_media_mover_to_sdcard.ForegroundDialog"
                    , "Start_Foreground");
            startService(startIntent);
        }
        //Do reboot stuff
        //handle other types of callers, like a notification.
    }
}

