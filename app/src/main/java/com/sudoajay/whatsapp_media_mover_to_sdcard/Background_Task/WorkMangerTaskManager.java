package com.sudoajay.whatsapp_media_mover_to_sdcard.Background_Task;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.sudoajay.whatsapp_media_mover_to_sdcard.sharedPreferences.TraceBackgroundService;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class WorkMangerTaskManager extends Worker {

    private List<OneTimeWorkRequest> list = new ArrayList<>();

    public WorkMangerTaskManager(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {


        OneTimeWorkRequest everyDayWork =
                new OneTimeWorkRequest.Builder(WorkMangerTaskA.class).addTag("Regular Data Size").setInitialDelay(20
                        , TimeUnit.MINUTES).build();

        OneTimeWorkRequest onceAWeekWork =
                new OneTimeWorkRequest.Builder(WorkMangerTaskB.class).addTag("Weekly Duplicate Size").setInitialDelay(3
                        , TimeUnit.HOURS).build();

        OneTimeWorkRequest backgroundTask =
                new OneTimeWorkRequest.Builder(WorkMangerTaskC.class).addTag("Duplication Size").setInitialDelay(1
                        , TimeUnit.HOURS).build();


        TraceBackgroundService traceBackgroundService = new TraceBackgroundService(getApplicationContext());
        traceBackgroundService.setTaskA();

        Calendar calendars = Calendar.getInstance();
        Date todayDate = calendars.getTime();

        // specific date from database
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);


        // Check for Date A Task
        Date date = null;
        try {
            date = dateFormat.parse(traceBackgroundService.getTaskA());

            if (dateFormat.format(todayDate).equals(dateFormat.format(date)) || date.before(todayDate)) {
                list.add(everyDayWork);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        // Check for Date B Task
        try {
            date = dateFormat.parse(traceBackgroundService.getTaskB());

            if (dateFormat.format(todayDate).equals(dateFormat.format(date)) || date.before(todayDate)) {
                list.add(onceAWeekWork);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        // Check for Date C Task
        try {
            date = dateFormat.parse(traceBackgroundService.getTaskC());

            if (dateFormat.format(todayDate).equals(dateFormat.format(date)) || date.before(todayDate)) {
                list.add(backgroundTask);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        WorkManager.getInstance(getApplicationContext())
                .beginWith(list)
                .enqueue();


        return Result.success();
    }


}
