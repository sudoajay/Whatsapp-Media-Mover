package com.sudoajay.whatsapp_media_mover_to_sdcard.sharedPreferences;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import com.sudoajay.whatsapp_media_mover_to_sdcard.R;

import java.util.Objects;
import static android.content.Context.MODE_PRIVATE;

public class TickOnButtonSharedPreference {

    // globally variable
    private SharedPreferences.Editor editor;
    private Context context;
    private boolean[] tickArray = new boolean[9];

    // constructor
    @SuppressLint("CommitPrefEdits")
    public TickOnButtonSharedPreference(Context context) {
        this.context = context;
        SharedPreferences pref = Objects.requireNonNull(context.getSharedPreferences(context.
                getString(R.string.MY_PREFS_NAME), MODE_PRIVATE));
        editor = pref.edit();

        for (int i = 0; i < 9; i++){
            tickArray[i]=pref.getBoolean(context.getResources().getStringArray(R.array.arrayTick)[i], true);
        }
    }

    public Boolean getTickArray(int i) {
        return tickArray[i];
    }

    public void setTickArray(boolean[] tickArray) {
        this.tickArray = tickArray;
        for (int i = 0; i < 9; i++){
                editor.putBoolean(context.getResources().getStringArray(R.array.arrayTick)[i],tickArray[i]);
        }
        editor.apply();
    }
}
