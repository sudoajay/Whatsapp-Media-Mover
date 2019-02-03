package com.sudoajay.whatapp_media_mover_to_sdcard.Background_Task;


import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;

import com.sudoajay.whatapp_media_mover_to_sdcard.Notification.NotifyNotification;

import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class WorkMangerTaskB extends Worker {



    private Context context;
    public WorkMangerTaskB(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        this.context = context;
    }

    @NonNull
    @Override
    public Result doWork() {


        NotifyNotification notify_notification = new NotifyNotification(context);
        notify_notification.notify( "Scan And Delete The Duplication Data");
        return Result.success();
    }
}

