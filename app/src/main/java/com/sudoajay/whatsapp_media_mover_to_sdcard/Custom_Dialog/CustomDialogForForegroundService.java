package com.sudoajay.whatsapp_media_mover_to_sdcard.Custom_Dialog;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Switch;

import androidx.annotation.Nullable;
import androidx.appcompat.view.ContextThemeWrapper;
import androidx.fragment.app.DialogFragment;

import com.sudoajay.whatsapp_media_mover_to_sdcard.ForegroundService.Foreground;
import com.sudoajay.whatsapp_media_mover_to_sdcard.R;
import com.sudoajay.whatsapp_media_mover_to_sdcard.sharedPreferences.TraceBackgroundService;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class CustomDialogForForegroundService extends DialogFragment implements AdapterView.OnItemSelectedListener {

    private TraceBackgroundService traceBackgroundService;
    private Activity activity;
    // blank constructor
    public CustomDialogForForegroundService() {

    }

    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootview = inflater.inflate(R.layout.setting_layout_design_foreground, container, false);

        activity = getActivity();

       traceBackgroundService = new TraceBackgroundService(Objects.requireNonNull(getContext()));
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
                if (!traceBackgroundService.isBackgroundServiceWorking()) {
                    if (switchForeground.isChecked()) {
                        // shared Preference changes

                        if (!isServiceRunningInForeground(activity)) {
                            // push foreground service
                            Intent startIntent = new Intent(CustomDialogForForegroundService.this.getContext(), Foreground.class);
                            startIntent.putExtra("com.sudoajay.whatapp_media_mover_to_sdcard.ForegroundDialog"
                                    , "Start_Foreground");
                            Objects.requireNonNull(CustomDialogForForegroundService.this.getActivity()).startService(startIntent);
                        }
                        traceBackgroundService.setForegroundServiceWorking(true);

                        // dismiss or close the dialog
                        dismiss();
                    } else {
                        if (isServiceRunningInForeground(activity)) {

                            int theme;
                            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP)
                                theme = R.style.AppTheme;
                            else {
                                theme = android.R.style.Theme_Holo_Dialog;
                            }
                            new AlertDialog.Builder(new ContextThemeWrapper
                                    (activity, theme))
                                    .setTitle(getResources().getString(R.string.custom_Dialog_Box_Heading))
                                    .setMessage(getResources().getString(R.string.custom_Dialog_Box_Text))

                                    // Specifying a listener allows you to take an action before dismissing the dialog.
                                    // The dialog is automatically dismissed when a dialog button is clicked.
                                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            // Continue with delete operation

                                            // shared Preference changes
                                            // push foreground service
                                            Intent stopIntent = new Intent(activity, Foreground.class);
                                            stopIntent.putExtra("com.sudoajay.whatapp_media_mover_to_sdcard.ForegroundDialog"
                                                    , "Stop_Foreground");
                                            Objects.requireNonNull(activity).startService(stopIntent);

                                            traceBackgroundService.setForegroundServiceWorking(false);

                                            dismiss();
                                        }
                                    })

                                    // A null listener allows the button to dismiss the dialog and take no further action.
                                    .setNegativeButton(android.R.string.no, null)
                                    .setIcon(android.R.drawable.ic_dialog_alert)
                                    .show();
                        }else{
                            dismiss();
                        }

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
    public void onDismiss(@NotNull DialogInterface dialog) {

        super.onDismiss(dialog);
    }

    private void Dissmiss() {

        this.dismiss();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private boolean isServiceRunningInForeground(Context context) {
        try {
            if(traceBackgroundService.isForegroundServiceWorking()){
            ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
                for (ActivityManager.RunningServiceInfo service : Objects.requireNonNull(manager).getRunningServices(Integer.MAX_VALUE)) {
                    if (Foreground.class.getName().equals(service.service.getClassName())) {
                    if (service.foreground) {
                        return true;
                    }
                }
            }
        }
            return false;
        }catch (Exception e){
            return !ServicesWorking();
        }
    }

    private boolean ServicesWorking() {
        traceBackgroundService.isBackgroundWorking();
        return !traceBackgroundService.isBackgroundServiceWorking();
    }
}
