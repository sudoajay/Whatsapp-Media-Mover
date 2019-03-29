package com.sudoajay.whatsapp_media_mover_to_sdcard.Receive_Boot_Completed;

import android.app.IntentService;
import android.content.Intent;

import com.sudoajay.whatsapp_media_mover_to_sdcard.ForegroundService.Foreground;

import java.util.Objects;

public class ForegroundServiceBoot extends IntentService {

    public ForegroundServiceBoot(){
        super(null);
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        String intentType = Objects.requireNonNull(intent.getExtras()).getString("caller");
        assert intentType != null;
        if (intentType.equalsIgnoreCase("RebootReceiver")) {
            Intent startIntent = new Intent(getApplicationContext(), Foreground.class);
            startIntent.putExtra("com.sudoajay.whatapp_media_mover_to_sdcard.ForegroundService"
                    , "Start_Foreground");
            startService(startIntent);
        }
        //Do reboot stuff
        //handle other types of callers, like a notification.
    }
}

