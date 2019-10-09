package com.sudoajay.whatsapp_media_mover_to_sdcard.sharedPreferences;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import com.sudoajay.whatsapp_media_mover_to_sdcard.R;
import java.util.Objects;
import static android.content.Context.MODE_PRIVATE;

public class WhatsappPathSharedpreferences {

    // global varibale
    private SharedPreferences.Editor editor;
    private String whatsapp_Path,whats_App_Media_Path;
    private Context context;

    // constructor
    @SuppressLint("CommitPrefEdits")
    public WhatsappPathSharedpreferences(final Context context){

        this.context =context;
        SharedPreferences pref =Objects.requireNonNull(context.getSharedPreferences(context.
                getString(R.string.MY_PREFS_NAME), MODE_PRIVATE));
        editor = pref.edit();

        whatsapp_Path = pref.getString(context.getString(R.string.keyValue), null);
       if (whatsapp_Path == null) {
            whatsapp_Path="/WhatsApp/";
        }
        whats_App_Media_Path = whatsapp_Path + "Media/";

    }

    public String getWhatsapp_Path() {
        return whatsapp_Path ;
    }

    public void setWhatsapp_Path(String whatsapp_Path) {
        editor.putString(context.getString(R.string.keyValue),whatsapp_Path);
        editor.apply();
        this.whatsapp_Path = whatsapp_Path;
        whats_App_Media_Path = whatsapp_Path + "Media";
    }

    public String getWhats_App_Media_Path() {
        return whats_App_Media_Path;
    }

}
