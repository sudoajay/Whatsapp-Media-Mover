package com.sudoajay.whatapp_media_mover_to_sdcard.sharedPreferences;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

import com.sudoajay.whatapp_media_mover_to_sdcard.R;

/**
 * Created by Lincoln on 05/05/16.
 */
public class BackgroundProcess {
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private Context _context;
    // shared pref mode
    private final int PRIVATE_MODE = 0;


    @SuppressLint("CommitPrefEdits")
    public BackgroundProcess(final Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(context.getString(R.string.MY_PREFS_NAME), PRIVATE_MODE);
        editor = pref.edit();
    }

    public void setTaskADone(final boolean task) {
        editor.putBoolean(_context.getString(R.string.task_A_Done), task);
        editor.apply();
    }

    public boolean isTaskADone() {
        return pref.getBoolean(_context.getString(R.string.task_A_Done), true);
    }
    public void setTaskBDone(final boolean task) {
        editor.putBoolean(_context.getString(R.string.task_B_Done), task);
        editor.apply();
    }

    public boolean isTaskBDone() {
        return pref.getBoolean(_context.getString(R.string.task_B_Done), true);
    }
    public void setTaskCDone(final boolean task) {
        editor.putBoolean(_context.getString(R.string.task_C_Done), task);
        editor.apply();
    }

    public boolean isTaskCDone() {
        return pref.getBoolean(_context.getString(R.string.task_C_Done), true);
    }

}
