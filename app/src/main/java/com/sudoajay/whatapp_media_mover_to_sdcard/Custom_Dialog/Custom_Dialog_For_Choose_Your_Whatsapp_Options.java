package com.sudoajay.whatapp_media_mover_to_sdcard.Custom_Dialog;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
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
import android.widget.ImageView;
import android.widget.Spinner;

import com.sudoajay.whatapp_media_mover_to_sdcard.Custom_Spin_adapter_For_Setting;
import com.sudoajay.whatapp_media_mover_to_sdcard.Database_Classes.Whatsapp_Mode_DataBase;
import com.sudoajay.whatapp_media_mover_to_sdcard.Main_Navigation;
import com.sudoajay.whatapp_media_mover_to_sdcard.R;

import java.util.ArrayList;
import java.util.Objects;

public class Custom_Dialog_For_Choose_Your_Whatsapp_Options extends DialogFragment implements AdapterView.OnItemSelectedListener {

    // globally variable
    private Button cancel_Button , ok_Button;
    private Main_Navigation main_navigation ;
    private Spinner type_Spinner;
    private String define;
    private ArrayList<String> whats_App_Path = new ArrayList<>();
    private ArrayList<String> types;
    private int position;
    private String whatsapp_Path;
    private Whatsapp_Mode_DataBase whatsapp_mode_dataBase;
    private ImageView back_Image_View_Change;
    public Custom_Dialog_For_Choose_Your_Whatsapp_Options(){

    }
    @SuppressLint("ValidFragment")
    public Custom_Dialog_For_Choose_Your_Whatsapp_Options(Main_Navigation main_navigation){
        this.main_navigation =main_navigation;

        whatsapp_mode_dataBase = new Whatsapp_Mode_DataBase(main_navigation);
        if(!whatsapp_mode_dataBase.check_For_Empty()){
            Cursor cursor= whatsapp_mode_dataBase.Get_All_Data();
            cursor.moveToNext();
            whatsapp_Path = cursor.getString(1); // /Gbwhatsapp/

        }else{
            whatsapp_Path="/WhatsApp/";
        }
    }

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootview = inflater.inflate(R.layout.setting_layout_design,container,false);

        cancel_Button = rootview.findViewById(R.id.cancel_Button);
        ok_Button  =rootview.findViewById(R.id.ok_Button);
        type_Spinner = rootview.findViewById(R.id.type_Spinner);
        back_Image_View_Change = rootview.findViewById(R.id.back_Image_View_Change);

        types = new ArrayList<>();
        types.add("Original Whatsapp");
        types.add("GbWhatsapp (Modded)");
        types.add("OgWhatsapp(Modded)");
        types.add("Whatsapp Plus(Modded");

        whats_App_Path.add("/WhatsApp/");
        whats_App_Path.add("/GBWhatsApp/");
        whats_App_Path.add("/OGWhatsApp/");

        define = whats_App_Path.get(0);

        // Spinner Drop down elements
        Custom_Spin_adapter_For_Setting customSpinnerAdapter=new Custom_Spin_adapter_For_Setting(main_navigation,types);
        type_Spinner.setAdapter(customSpinnerAdapter);

        type_Spinner.setSelection(setType_Selection());
        type_Spinner.setOnItemSelectedListener(this);

        back_Image_View_Change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dissmiss();
            }
        });

        ok_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(position ==0 || position == 3){
                    define = whats_App_Path.get(0);
                }else if(position ==1){
                    define =whats_App_Path.get(1);
                }else{
                    define = whats_App_Path.get(2);
                }

                if(whatsapp_mode_dataBase.check_For_Empty()){
                    whatsapp_mode_dataBase.Fill_It(define,"Yes");

                }else{
                    whatsapp_mode_dataBase.Update_The_Table("1",define ,"Yes");

                }
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
                current.getLayoutParams().height = height-((62*height)/100);
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
      this.position= position;
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
    public int setType_Selection(){
        if(whatsapp_Path.equalsIgnoreCase(whats_App_Path.get(0))) return 0;
        else if(whatsapp_Path.equalsIgnoreCase(whats_App_Path.get(1))) return 1;
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
