package com.sudoajay.whatsapp_media_mover_to_sdcard.sharedPreferences;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

import com.sudoajay.whatsapp_media_mover_to_sdcard.R;

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
    private static Context _context;

    // shared pref mode
    private final int PRIVATE_MODE = 0;


    @SuppressLint("CommitPrefEdits")
    public TraceBackgroundService(final Context _context) {
        this._context = _context;
        pref = _context.getSharedPreferences(_context.getString(R.string.MY_PREFS_NAME), PRIVATE_MODE);
        editor = pref.edit();


        // set default value
        if (!pref.contains(_context.getString(R.string.task_A_NextDate)))
            editor.putString(_context.getString(R.string.task_A_NextDate), NextDate(24));

        if (!pref.contains(_context.getString(R.string.task_B_NextDate)))
            editor.putString(_context.getString(R.string.task_B_NextDate), NextDate(5 * 24));
        editor.apply();
    }

    public String getTaskA() {
        return pref.getString(_context.getString(R.string.task_A_NextDate), NextDate(24));
    }

    public void setTaskA() {
        editor.putString(_context.getString(R.string.task_A_NextDate), NextDate(24));
        editor.apply();

    }

    public String getTaskB() {
        return pref.getString(_context.getString(R.string.task_B_NextDate), NextDate(5 * 24));
    }

    public void setTaskB() {
        editor.putString(_context.getString(R.string.task_B_NextDate), NextDate(5 * 24));
        editor.apply();
    }

    public String getTaskC() {
        return pref.getString(_context.getString(R.string.task_C_NextDate), "");
    }

    public void setTaskC(String taskC) {
        editor.putString(_context.getString(R.string.task_C_NextDate), taskC);
        editor.apply();
    }

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

    public static String NextDate(final int hour) {

        // get Today Date as default
        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.HOUR, hour);
        dateFormat.setTimeZone(calendar.getTimeZone());
        return dateFormat.format(calendar.getTime());
    }

    public void isBackgroundWorking() {

        // today date
        Calendar calendar = Calendar.getInstance();

        // juts add this for Yesterday
        calendar.add(Calendar.DATE, -1);

        Date yesterday = calendar.getTime();

        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

        try {

            if (!getTaskB().equals("") || !getTaskB().equals("Empty") || getTaskB() != null) {
                Date getDate = dateFormat.parse(getTaskC());
                if (yesterday.after(getDate))
                    setBackgroundServiceWorking(false);
                else {
                    setBackgroundServiceWorking(true);
                }
            }else{
                setBackgroundServiceWorking(true);
            }
        } catch (ParseException e) {
            setBackgroundServiceWorking(false);
        }

    }
}
