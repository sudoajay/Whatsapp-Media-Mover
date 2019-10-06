package com.sudoajay.whatsapp_media_mover_to_sdcard.Background_Task;


import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.sudoajay.whatsapp_media_mover_to_sdcard.Notification.NotifyNotification;
import com.sudoajay.whatsapp_media_mover_to_sdcard.sharedPreferences.TraceBackgroundService;

public class WorkMangerTaskA extends Worker {



    public WorkMangerTaskA(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);

    }

    @NonNull
    @Override
    public Result doWork() {
        getWork(getApplicationContext());

        TraceBackgroundService traceBackgroundService = new TraceBackgroundService(getApplicationContext());
        traceBackgroundService.setBackgroundServiceWorking(true);

        return Result.success();
    }
    public static void getWork(final Context context){


        // set next date
        TraceBackgroundService traceBackgroundService = new TraceBackgroundService(context);
        traceBackgroundService.setTaskA();

        // notifiy it
        NotifyNotification notify_notification = new NotifyNotification(context);
        notify_notification.notify( "Size Of Whatsapp Data");


    }
}

