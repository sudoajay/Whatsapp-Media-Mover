package com.sudoajay.whatapp_media_mover_to_sdcard.Permission;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;

import com.sudoajay.whatapp_media_mover_to_sdcard.R;

public class AndroidExternalStoragePermission {

    private Context context;
    private Activity activity;
    private String external_Path;
    public AndroidExternalStoragePermission(Context context , Activity activity){
       this.context = context;
       this.activity = activity;
       external_Path = Environment.getExternalStorageDirectory().getAbsolutePath();
    }


    public  void Storage_Permission_Granted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (ContextCompat.checkSelfPermission(context,
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                int my_Permission_Request = 1;
                if (ActivityCompat.shouldShowRequestPermissionRationale(activity,
                        android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                    ActivityCompat.requestPermissions(activity,
                            new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, my_Permission_Request);

                } else {

                    ActivityCompat.requestPermissions(activity,
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, my_Permission_Request);


                }
            }

        }

    }
    public void call_Thread(){
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Call_Custom_Permission_Dailog();

            }
        },1800);
    }
    public void Call_Custom_Permission_Dailog() {
        final Dialog dialog = new Dialog(context);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.activity_custom_dialog_permission);
        Button button_Learn_More = dialog.findViewById(R.id.see_More_button);
        Button button_Continue = dialog.findViewById(R.id.ok_Button);
        // if button is clicked, close the custom dialog

        button_Learn_More.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    final String url = "https://developer.android.com/training/permissions/requesting.html";
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(url));
                    activity.startActivity(i);
                }catch (Exception ignored){

                }
            }
        });
        button_Continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Storage_Permission_Granted();
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    public boolean isExternalStorageWritable() {
        String permission = Manifest.permission.WRITE_EXTERNAL_STORAGE;
        int res = activity.checkCallingOrSelfPermission(permission);
        return (res == PackageManager.PERMISSION_GRANTED);
    }

    public String getExternal_Path() {
        return external_Path;
    }
}
