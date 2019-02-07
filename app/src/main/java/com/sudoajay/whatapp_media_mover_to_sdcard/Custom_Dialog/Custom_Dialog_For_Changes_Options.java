package com.sudoajay.whatapp_media_mover_to_sdcard.Custom_Dialog;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.Button;
import android.widget.ImageView;

import com.sudoajay.whatapp_media_mover_to_sdcard.After_MainTransferFIle;
import com.sudoajay.whatapp_media_mover_to_sdcard.R;

public class Custom_Dialog_For_Changes_Options extends DialogFragment {

    private After_MainTransferFIle after_mainTransferFIle ;

    public Custom_Dialog_For_Changes_Options(){

    }
    @SuppressLint("ValidFragment")
    public Custom_Dialog_For_Changes_Options(After_MainTransferFIle after_mainTransferFIle){
        this.after_mainTransferFIle =after_mainTransferFIle;
    }

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootview = inflater.inflate(R.layout.changes_options_layout,container,false);

        // globally variable
        Button normal_Changes_Button = rootview.findViewById(R.id.normal_Changes_Button);
        Button deep_Changes_Button = rootview.findViewById(R.id.deep_Changes_Button);
        ImageView back_Image_View_Change = rootview.findViewById(R.id.back_Image_View_Change);

        normal_Changes_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            after_mainTransferFIle.Call_Custom_Dailog_Normal_Changes();
            Dissmiss();
            }
        });
        back_Image_View_Change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dissmiss();
            }
        });

        deep_Changes_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                after_mainTransferFIle.Call_Custom_Dailog_Deep_Changes();
                Dissmiss();
            }
        });

        this.setCancelable(true);
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
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
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
                current.getLayoutParams().height = height-((68*height)/100);
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
}
