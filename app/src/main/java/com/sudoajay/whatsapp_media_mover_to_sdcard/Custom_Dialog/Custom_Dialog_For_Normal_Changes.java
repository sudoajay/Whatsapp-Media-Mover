package com.sudoajay.whatsapp_media_mover_to_sdcard.Custom_Dialog;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import com.sudoajay.whatsapp_media_mover_to_sdcard.After_MainTransferFIle;
import com.sudoajay.whatsapp_media_mover_to_sdcard.R;
import org.angmarch.views.NiceSpinner;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class Custom_Dialog_For_Normal_Changes extends DialogFragment implements AdapterView.OnItemSelectedListener {

    // globally variable
    private After_MainTransferFIle after_mainTransferFIle ;
    private int normal_change=0;
    private NiceSpinner customSpinner;

    public Custom_Dialog_For_Normal_Changes(){

    }
    @SuppressLint("ValidFragment")
    public Custom_Dialog_For_Normal_Changes(After_MainTransferFIle after_mainTransferFIle,int normal_change ){
        this.after_mainTransferFIle =after_mainTransferFIle;
        this.normal_change=normal_change;
    }

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootview = inflater.inflate(R.layout.normal_changes_layout,container,false);

        // reference
        ImageView back_Image_View_Change = rootview.findViewById(R.id.back_Image_View_Change);
        Button cancel_Button = rootview.findViewById(R.id.cancelButton);
        Button ok_Button = rootview.findViewById(R.id.ok_Button);
        customSpinner = rootview.findViewById(R.id.customSpinner);

        // Spinner Drop down elements
        ArrayList<String> categories = new ArrayList<>();
        categories.add("0 Days Before");
        categories.add("1 Days Before");
        categories.add("5 Days Before");
        categories.add("10 Days Before");
        categories.add("20 Days Before");
        categories.add("1 Month Before");
        categories.add("2 Month Before");
        categories.add("6 month Before");
        List<String> dataset = new LinkedList<>(categories);
        customSpinner.attachDataSource(dataset);

        if(normal_change != 0) customSpinner.setSelectedIndex(normal_change-1);

        ok_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                normal_change = customSpinner.getSelectedIndex()+1;
                after_mainTransferFIle.setNormal_Changes(normal_change);
                Dissmiss();
            }
        });
        cancel_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Dissmiss();
            }
        });
        back_Image_View_Change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dissmiss();
            }
        });

        this.setCancelable(false);
        return rootview;
    }

    public void onItemSelected(AdapterView<?> parent, View view, int pos,long id) {
       normal_change =pos+1;
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

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
                current.getLayoutParams().height = height-((13*height)/100);

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
        after_mainTransferFIle.Call_Custom_Dailog_Option_Changes();
        this.dismiss();
    }
}
