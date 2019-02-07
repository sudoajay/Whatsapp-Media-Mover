package com.sudoajay.whatapp_media_mover_to_sdcard.Background_Task;


import android.content.Context;
import android.support.annotation.NonNull;

import com.sudoajay.whatapp_media_mover_to_sdcard.Notification.NotifyNotification;

import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class WorkMangerTaskA extends Worker {


    private Context context;

    public WorkMangerTaskA(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        this.context = context;
    }

    @NonNull
    @Override
    public Result doWork() {

        NotifyNotification notify_notification = new NotifyNotification(context);
        notify_notification.notify( "Size Of Whatsapp Data");
        return Result.success();
    }
}

