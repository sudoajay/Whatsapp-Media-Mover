package com.sudoajay.whatapp_media_mover_to_sdcard.Custom_Dialog;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.AdapterView;
import android.widget.Button;

import com.sudoajay.whatapp_media_mover_to_sdcard.R;
import java.util.Objects;

public class CustomDialogForBackgroundTimer extends DialogFragment implements AdapterView.OnItemSelectedListener{

    // global variable
    private Button cancel_Button,ok_Button;

    public CustomDialogForBackgroundTimer(){

    }

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        View rootview = inflater.inflate(R.layout.activity_background_timer,container,false);

        Reference(rootview);

        // ok set on click
        ok_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        // cancel set on click
        cancel_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dissmiss();
            }
        });

        this.setCancelable(true);
        return rootview;
    }

    // reference the object
    private void Reference(View view){

        cancel_Button = view.findViewById(R.id.cancel_Button);
        ok_Button = view.findViewById(R.id.ok_Button);


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
                current.getLayoutParams().width = width-((10*width)/100);
                current.getLayoutParams().height = height-((50 *height)/100);
            }
        } while (current.getParent() != null);

        // Request a layout to be re-done
        current.requestLayout();
    }

    @Override
    public void onDismiss(DialogInterface dialog) {

        super.onDismiss(dialog);
    }

    public void Dissmiss(){

        this.dismiss();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
