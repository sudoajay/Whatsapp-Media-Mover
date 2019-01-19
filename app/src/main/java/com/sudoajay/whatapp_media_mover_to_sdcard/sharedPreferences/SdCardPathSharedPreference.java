package com.sudoajay.whatapp_media_mover_to_sdcard.sharedPreferences;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import com.sudoajay.whatapp_media_mover_to_sdcard.R;
import java.util.Objects;
import static android.content.Context.MODE_PRIVATE;

public class SdCardPathSharedPreference {

    // global varibale
    private SharedPreferences.Editor editor;
    private String sdCardPath,stringURI;
    private Context context;

    // constructor
    @SuppressLint("CommitPrefEdits")
    public SdCardPathSharedPreference(Context context){
        SharedPreferences pref =Objects.requireNonNull(context.getSharedPreferences(context.
                getString(R.string.MY_PREFS_NAME), MODE_PRIVATE));
        editor = pref.edit();
        this.context =context;

        // default value pass
        // grab the data from shared preference
        sdCardPath = pref.getString(context.getString(R.string.sdCardPath), "");
        stringURI = pref.getString(context.getString(R.string.stringUri), "");

    }

    public String getSdCardPath() {
        return sdCardPath;
    }

    public void setSdCardPath(String sdCardPath) {
        this.sdCardPath = sdCardPath;

        // send thd data to shared preferences
        editor.putString(context.getString(R.string.sdCardPath),sdCardPath);
        editor.apply();
    }

    public String getStringURI() {
        return stringURI;
    }

    public void setStringURI(String stringURI) {
        this.stringURI = stringURI;

        editor.putString(context.getString(R.string.stringUri),stringURI);
        editor.apply();
    }
}
