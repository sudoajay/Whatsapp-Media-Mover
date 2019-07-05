package com.sudoajay.whatsapp_media_mover_to_sdcard.Permission;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;
import androidx.core.app.NotificationManagerCompat;
import android.view.View;
import android.widget.TextView;

import com.sudoajay.whatsapp_media_mover_to_sdcard.After_MainTransferFIle;
import com.sudoajay.whatsapp_media_mover_to_sdcard.R;
import com.sudoajay.whatsapp_media_mover_to_sdcard.Show_Duplicate_File;

public class Notification_Permission_Check {


    private Activity activity;
    private After_MainTransferFIle after_mainTransferFIle;
    private Show_Duplicate_File show_duplicate_file;

    public Notification_Permission_Check( Activity activity,After_MainTransferFIle after_mainTransferFIle){
        this.activity =activity;
        this.after_mainTransferFIle =after_mainTransferFIle;
    }
    public Notification_Permission_Check( Activity activity,Show_Duplicate_File show_duplicate_file){
        this.activity =activity;
        this.show_duplicate_file =show_duplicate_file;
    }

    private void Open_Setting(){
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                Uri.parse("package:" + activity.getPackageName()));
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent);
    }

    public boolean check_Notification_Permission(){
        return NotificationManagerCompat.from(activity.getApplicationContext()).areNotificationsEnabled();
    }

    public void Custom_AertDialog(){
            final Dialog dialog = new Dialog(activity);
            dialog.setCancelable(false);
            dialog.setContentView(R.layout.activity_custom_notification_permission);
            TextView button_No = dialog.findViewById(R.id.no_button);
            TextView button_Yes = dialog.findViewById(R.id.yes_Button);
            // if button is clicked, close the custom dialog

            button_Yes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Open_Setting();
                    dialog.dismiss();
                    if(show_duplicate_file == null)after_mainTransferFIle.Send_Permission_To_Transfer();
                    else{
                        show_duplicate_file.Call_Custom_Dailog("   Are You Sure To Delete ?");
                    }
                }
            });
            button_No.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    if(show_duplicate_file == null)after_mainTransferFIle.Send_Permission_To_Transfer();
                    else{
                        show_duplicate_file.Call_Custom_Dailog("   Are You Sure To Delete ?");
                    }                }
            });

            dialog.show();

    }
}
