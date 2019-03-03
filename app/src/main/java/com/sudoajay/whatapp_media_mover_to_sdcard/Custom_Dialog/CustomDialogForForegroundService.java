package com.sudoajay.whatapp_media_mover_to_sdcard.Custom_Dialog;

import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.view.ContextThemeWrapper;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Switch;

import com.sudoajay.whatapp_media_mover_to_sdcard.ForegroundService.Foreground;
import com.sudoajay.whatapp_media_mover_to_sdcard.R;
import com.sudoajay.whatapp_media_mover_to_sdcard.sharedPreferences.TraceBackgroundService;

import java.util.Objects;

public class CustomDialogForForegroundService extends DialogFragment implements AdapterView.OnItemSelectedListener {

    // blank constructor
    public CustomDialogForForegroundService() {

    }

    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootview = inflater.inflate(R.layout.setting_layout_design_foreground, container, false);



        final TraceBackgroundService traceBackgroundService = new TraceBackgroundService(Objects.requireNonNull(getContext()));
        // setup and instalizition for getSharedPreferences
        // configuration or setup the sharedPreferences
        final Switch switchForeground = rootview.findViewById(R.id.switchForeground);

        // check if background Service is working or Not
        if(!traceBackgroundService.isForegroundServiceWorking())
            switchForeground.setChecked(false);



        // globally variable
        Button cancel_Button = rootview.findViewById(R.id.cancelButton);
        Button ok_Button = rootview.findViewById(R.id.ok_Button);
        ImageView back_Image_View_Change = rootview.findViewById(R.id.back_Image_View_Change);


        back_Image_View_Change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dissmiss();
            }
        });


        ok_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!traceBackgroundService.isBackgroundServiceWorking()) {
                    if (switchForeground.isChecked()) {
                        // shared Preference changes
                        traceBackgroundService.setForegroundServiceWorking(true);

                        if(!isServiceRunningInForeground(Objects.requireNonNull(getContext()),Foreground.class)) {
                            // push foreground service
                            Intent startIntent = new Intent(CustomDialogForForegroundService.this.getContext(), Foreground.class);
                            startIntent.putExtra("com.sudoajay.whatapp_media_mover_to_sdcard.ForegroundService"
                                    , "Start_Foreground");
                            Objects.requireNonNull(CustomDialogForForegroundService.this.getActivity()).startService(startIntent);
                        }
                        // dismiss or close the dialog
                        dismiss();
                    } else {
                        int theme;
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                            theme = R.style.AppTheme;
                        } else {
                            theme = android.R.style.Theme_Holo_Dialog;
                        }
                        new AlertDialog.Builder(new ContextThemeWrapper
                                (CustomDialogForForegroundService.this.getContext(), theme))
                                .setTitle(getResources().getString(R.string.custom_Dialog_Box_Heading))
                                .setMessage(getResources().getString(R.string.custom_Dialog_Box_Text))

                                // Specifying a listener allows you to take an action before dismissing the dialog.
                                // The dialog is automatically dismissed when a dialog button is clicked.
                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        // Continue with delete operation

                                        // shared Preference changes
                                        traceBackgroundService.setForegroundServiceWorking(false);
                                        if(!isServiceRunningInForeground(Objects.requireNonNull(getContext()),Foreground.class)) {
                                            // push foreground service
                                            Intent stopIntent = new Intent(CustomDialogForForegroundService.this.getContext(), Foreground.class);
                                            stopIntent.putExtra("com.sudoajay.whatapp_media_mover_to_sdcard.ForegroundService"
                                                    , "Stop_Foreground");
                                            Objects.requireNonNull(CustomDialogForForegroundService.this.getActivity()).startService(stopIntent);
                                        }
                                        dismiss();
                                    }
                                })

                                // A null listener allows the button to dismiss the dialog and take no further action.
                                .setNegativeButton(android.R.string.no, null)
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .show();
                    }
                }else{
                    dismiss();
                }
            }
        });
        cancel_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dissmiss();
            }
        });

        this.setCancelable(false);
        return rootview;
    }

    public void onStart() {
        // This MUST be called first! Otherwise the view tweaking will not be present in the displayed Dialog (most likely overriden)
        super.onStart();

        forceWrapContent(this.getView());
    }

    private void forceWrapContent(View v) {
        // Start with the provided view
        View current = v;
        DisplayMetrics dm = new DisplayMetrics();
        Objects.requireNonNull(getActivity()).getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        // Travel up the tree until fail, modifying the LayoutParams
        do {
            // Get the parent
            ViewParent parent = current.getParent();

            // Check if the parent exists
            if (parent != null) {
                // Get the view
                try {
                    current = (View) parent;
                } catch (ClassCastException e) {
                    // This will happen when at the top view, it cannot be cast to a View
                    break;
                }

                // Modify the layout
                current.getLayoutParams().width = width - ((10 * width) / 100);
                current.getLayoutParams().height = height - ((70 * height) / 100);
            }
        } while (current.getParent() != null);

        // Request a layout to be re-done
        current.requestLayout();
    }

    @Override
    public void onDismiss(DialogInterface dialog) {

        super.onDismiss(dialog);
    }

    public void Dissmiss() {

        this.dismiss();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        int position1 = position;
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
    public static boolean isServiceRunningInForeground(Context context, Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                if (service.foreground) {
                    return true;
                }
            }
        }
        return false;
    }

}
