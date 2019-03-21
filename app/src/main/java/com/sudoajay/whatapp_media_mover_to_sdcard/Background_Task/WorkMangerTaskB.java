package com.sudoajay.whatapp_media_mover_to_sdcard.Background_Task;


import android.content.Context;
import android.support.annotation.NonNull;

import com.sudoajay.whatapp_media_mover_to_sdcard.Notification.NotifyNotification;
import com.sudoajay.whatapp_media_mover_to_sdcard.sharedPreferences.BackgroundProcess;
import com.sudoajay.whatapp_media_mover_to_sdcard.sharedPreferences.TraceBackgroundService;

import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class WorkMangerTaskB extends Worker {




    public WorkMangerTaskB(@NonNull Context context, @NonNull WorkerParameters workerParams) {
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
        traceBackgroundService.setTaskB(traceBackgroundService.NextDate(7*24));

        NotifyNotification notify_notification = new NotifyNotification(context);
        notify_notification.notify( "Size Of Duplication Data");

        // set the Task is done
        BackgroundProcess backgroundProcess = new BackgroundProcess(context);
        backgroundProcess.setTaskBDone(true);
    }
}

