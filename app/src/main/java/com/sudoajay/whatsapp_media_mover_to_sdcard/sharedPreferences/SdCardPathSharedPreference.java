package com.sudoajay.whatsapp_media_mover_to_sdcard.sharedPreferences;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

import com.sudoajay.whatsapp_media_mover_to_sdcard.R;

import static android.content.Context.MODE_PRIVATE;

public class SdCardPathSharedPreference {

    // global varibale
    private SharedPreferences.Editor editor;
    private String sdCardPath, stringURI;
    private Context context;
    private SharedPreferences pref;


    // constructor
    @SuppressLint("CommitPrefEdits")
    public SdCardPathSharedPreference(Context context) {
        this.context = context;
        pref = context.getSharedPreferences(context.getString(R.string.MY_PREFS_NAME), MODE_PRIVATE);
        editor = pref.edit();


        // default value pass
        // grab the data from shared preference
        editor.putString(context.getString(R.string.sdCardPath), "");
        editor.putString(context.getString(R.string.stringUri), "");

    }

    public String getSdCardPath() {
        return pref.getString(context.getString(R.string.sdCardPath), "");
    }

    public void setSdCardPath(String sdCardPath) {
        this.sdCardPath = sdCardPath;

        // send thd data to shared preferences
        editor.putString(context.getString(R.string.sdCardPath), sdCardPath);
        editor.apply();
    }

    public String getStringURI() {
        return pref.getString(context.getString(R.string.stringUri), "");
    }

    public void setStringURI(String stringURI) {
        this.stringURI = stringURI;

        editor.putString(context.getString(R.string.stringUri), stringURI);
        editor.apply();
    }
}
