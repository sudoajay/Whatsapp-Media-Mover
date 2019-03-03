package com.sudoajay.whatapp_media_mover_to_sdcard.sharedPreferences;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

import com.sudoajay.whatapp_media_mover_to_sdcard.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Lincoln on 05/05/16.
 */
public class TraceBackgroundService {
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private Context _context;
    private String taskA ,taskB,taskC;

    // shared pref mode
    private final int PRIVATE_MODE = 0;


    @SuppressLint("CommitPrefEdits")
    public TraceBackgroundService(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(context.getString(R.string.MY_PREFS_NAME), PRIVATE_MODE);
        editor = pref.edit();



        taskA = pref.getString(context.getString(R.string.task_A_NextDate),NextDate(24));
        taskB = pref.getString(context.getString(R.string.task_B_NextDate),NextDate((7*24)));
        taskC = pref.getString(context.getString(R.string.task_C_NextDate),"");
        editor.apply();

    }

    public String getTaskA() {
        return taskA;
    }

    public void setTaskA(String taskA) {
        editor.putString(_context.getString(R.string.task_A_NextDate),taskA);
        editor.apply();
        this.taskA = taskA;

    }

    public String getTaskB() {
        return taskB;
    }

    public void setTaskB(String taskB) {
        editor.putString(_context.getString(R.string.task_B_NextDate),taskB);
        editor.apply();
        this.taskB = taskB;    }

    public String getTaskC() {
        return taskC;
    }

    public void setTaskC(String taskc) {
        editor.putString(_context.getString(R.string.task_C_NextDate),taskC);
        editor.apply();
        this.taskC = taskC;    }

    public void setBackgroundServiceWorking(boolean backgroundServiceWorking) {
        editor.putBoolean(_context.getString(R.string.background_Service_Working), backgroundServiceWorking);
        editor.apply();
    }

    public boolean isBackgroundServiceWorking() {
        return pref.getBoolean(_context.getString(R.string.background_Service_Working), true);
    }

    public void setForegroundServiceWorking(final boolean foregroundServiceWorking) {
        editor.putBoolean(_context.getString(R.string.foreground_Service_Working), foregroundServiceWorking);
        editor.apply();
    }

    public boolean isForegroundServiceWorking() {
        return pref.getBoolean(_context.getString(R.string.foreground_Service_Working), true);
    }
    public String NextDate(int hour){
        // get Today Date as default
        Calendar calendar = Calendar.getInstance();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        calendar.add(Calendar.HOUR,hour);
         dateFormat.setTimeZone(calendar.getTimeZone());
        return dateFormat.format(calendar.getTime());
    }

    public static boolean  CheckForBackground(String date){

        // today date
        Calendar calendar = Calendar.getInstance();
        Date todayDate= calendar.getTime();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        try {
            Date getDate =  dateFormat.parse(date);

        if(todayDate.after(getDate)){

            return false;
        }
        } catch (ParseException e) {
            return true;
        }

        return true;

    }
}
