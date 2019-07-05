package com.sudoajay.whatsapp_media_mover_to_sdcard.Custom_Dialog;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
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

import com.sudoajay.whatsapp_media_mover_to_sdcard.Main_Navigation;
import com.sudoajay.whatsapp_media_mover_to_sdcard.R;
import com.sudoajay.whatsapp_media_mover_to_sdcard.sharedPreferences.WhatsappPathSharedpreferences;

import org.angmarch.views.NiceSpinner;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class Custom_Dialog_For_Choose_Your_Whatsapp_Options extends DialogFragment implements AdapterView.OnItemSelectedListener {

    private Main_Navigation main_navigation ;
    private String define;
    private ArrayList<String> whats_App_Path = new ArrayList<>();
    private String whatsapp_Path;
    private NiceSpinner customSpinner;
    private WhatsappPathSharedpreferences whatsappPathSharedpreferences;


    // blank constructor
    public Custom_Dialog_For_Choose_Your_Whatsapp_Options(){

    }
    @SuppressLint({"ValidFragment", "CommitPrefEdits"})
    public Custom_Dialog_For_Choose_Your_Whatsapp_Options(Main_Navigation main_navigation){
        this.main_navigation =main_navigation;
    }

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootview = inflater.inflate(R.layout.setting_layout_design_whatsapp,container,false);


        // setup and instalizition for getSharedPreferences
        // configuration or setup the sharedPreferences
        whatsappPathSharedpreferences = new WhatsappPathSharedpreferences(Objects.requireNonNull(getContext()));
        whatsapp_Path = whatsappPathSharedpreferences.getWhatsapp_Path();

        // globally variable
        Button cancel_Button = rootview.findViewById(R.id.cancelButton);
        Button ok_Button = rootview.findViewById(R.id.ok_Button);
        ImageView back_Image_View_Change = rootview.findViewById(R.id.back_Image_View_Change);
        customSpinner = rootview.findViewById(R.id.customSpinner);

        // array for shows in spinner
        ArrayList<String> types = new ArrayList<>();
        types.add("Original Whatsapp");
        types.add("GbWhatsapp (Modded)");
        types.add("OgWhatsapp(Modded)");
        types.add("Whatsapp Plus(Modded");

        // array for whatsapp path
        whats_App_Path.add("/WhatsApp/");
        whats_App_Path.add("/GBWhatsApp/");
        whats_App_Path.add("/OGWhatsApp/");

        // default value
        define = whats_App_Path.get(0);

        // spinner send items to linkedlist
        List<String> dataSpinner = new LinkedList<>(types);
        customSpinner.attachDataSource(dataSpinner);

        customSpinner.setSelectedIndex(setType_Selection());

        back_Image_View_Change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dissmiss();
            }
        });

        ok_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(customSpinner.getSelectedIndex() ==0 || customSpinner.getSelectedIndex() == 3){
                    define = whats_App_Path.get(0);
                }else if(customSpinner.getSelectedIndex() ==1){
                    define =whats_App_Path.get(1);
                }else{
                    define = whats_App_Path.get(2);
                }

                // transfer to the string key
                // after than save the  shared preference
                whatsappPathSharedpreferences.setWhatsapp_Path(define);

                Restart_The_Application();

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
                current.getLayoutParams().width = width-((10*width)/100);
                current.getLayoutParams().height = height-((45 *height)/100);
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
        int position1 = position;
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
    public int setType_Selection(){
        if(whatsapp_Path.equals(whats_App_Path.get(0))) return 0;
        else if(whatsapp_Path.equals(whats_App_Path.get(1))) return 1;
        else{
            return 2;
        }
    }
    public void Restart_The_Application(){
        Intent i = main_navigation.getBaseContext().getPackageManager()
                .getLaunchIntentForPackage(main_navigation.getBaseContext().getPackageName() );
        assert i != null;
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        main_navigation.finish();
        startActivity(i);
    }
}
