package com.sudoajay.whatsapp_media_mover_to_sdcard.ForegroundService;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.view.View;
import android.widget.Button;

import com.sudoajay.whatsapp_media_mover_to_sdcard.R;
import com.sudoajay.whatsapp_media_mover_to_sdcard.sharedPreferences.TraceBackgroundService;

import java.util.Objects;

public class ForegroundDialog {

    private Context mContext;
    private Activity activity;
    private TraceBackgroundService traceBackgroundService;

    // constructor
    public ForegroundDialog(final Context mContext, final Activity activity) {
        this.mContext = mContext;
        this.activity = activity;
    }

    public void call_Thread() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Call_Custom_Permission_Dailog();

            }
        }, 500);
    }

    private void Call_Custom_Permission_Dailog() {
        final Dialog dialog = new Dialog(mContext);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.activity_custom_foreground_permission);
        Button button_Learn_More = dialog.findViewById(R.id.see_More_button);
        Button button_Continue = dialog.findViewById(R.id.ok_Button);
        // if button is clicked, close the custom dialog

        button_Learn_More.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    final String url = "https://dontkillmyapp.com/problem?1";
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(url));
                    activity.startActivity(i);
                } catch (Exception ignored) {

                }
                dialog.dismiss();
            }
        });
        button_Continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    final String url = GetUrl();
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(url));
                    activity.startActivity(i);

                    traceBackgroundService = new TraceBackgroundService(mContext);
                    traceBackgroundService.setForegroundServiceWorking(true);

                    if (!isServiceRunningInForeground(mContext)) {
                        // call Foreground Thread();
                        Intent startIntent = new Intent(mContext, Foreground.class);
                        startIntent.putExtra("com.sudoajay.whatapp_media_mover_to_sdcard.ForegroundDialog"
                                , "Start_Foreground");

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            activity.startForegroundService(startIntent);
                        } else {
                            activity.startService(startIntent);
                        }

                    }
                } catch (Exception ignored) {

                }
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private String GetUrl() {
        String deviceMan = android.os.Build.MANUFACTURER;
        switch (deviceMan) {
            case "Xiaomi":
                return "https://dontkillmyapp.com/xiaomi";
            case "Nokia":
                return "https://dontkillmyapp.com/nokia";
            case "OnePlus":
                return "https://dontkillmyapp.com/oneplus";
            case "Huawei":
                return "https://dontkillmyapp.com/huawei";
            case "Meizu":
                return "https://dontkillmyapp.com/meizu";
            case "Samsung":
                return "https://dontkillmyapp.com/samsung";
            case "Sony":
                return "https://dontkillmyapp.com/sony";
            case "HTC":
                return "https://dontkillmyapp.com/htc";
            case "Google":
                return "https://dontkillmyapp.com/stock_android";
            case "Lenovo":
                return "https://dontkillmyapp.com/lenovo";
            default:
                return "https://dontkillmyapp.com/";

        }
    }

    private boolean isServiceRunningInForeground(Context context) {
        try {
            ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            for (ActivityManager.RunningServiceInfo service : Objects.requireNonNull(manager).getRunningServices(Integer.MAX_VALUE)) {
                if (Foreground.class.getName().equals(service.service.getClassName())) {
                    if (service.foreground) {
                        return true;
                    }
                }
            }
            return false;
        } catch (Exception e) {
            return !ServicesWorking();
        }
    }

    private boolean ServicesWorking() {
        traceBackgroundService.isBackgroundWorking();
        return !traceBackgroundService.isBackgroundServiceWorking();
    }
}
