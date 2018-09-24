package com.sudoajay.whatapp_media_mover_to_sdcard.Main_Fragments;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.sudoajay.whatapp_media_mover_to_sdcard.After_MainTransferFIle;
import com.sudoajay.whatapp_media_mover_to_sdcard.Android_External_Writeable_Permission;
import com.sudoajay.whatapp_media_mover_to_sdcard.Android_Permission_Required;
import com.sudoajay.whatapp_media_mover_to_sdcard.Main_Navigation;
import com.sudoajay.whatapp_media_mover_to_sdcard.R;
import com.sudoajay.whatapp_media_mover_to_sdcard.Database_Classes.Sd_Card_DataBase;
import com.sudoajay.whatapp_media_mover_to_sdcard.Sd_Card_Path;
import com.sudoajay.whatapp_media_mover_to_sdcard.Storage_Info;

import java.io.File;

public class MainTransferFIle extends Fragment {
    private TextView toast_TextView;
    private Button move_Button , copy_Button , remove_Button,file_Size_Text,restore_Button;
    private String sd_Card_Path_URL="",string_URI ="";
    private long get_Size;
    private Uri sdCard_Uri ;
    private View layout,layouts;
    private Toast toast;
    private Handler handler;
    private boolean whats_App_File_Exist_Internal,whats_App_File_Exist_External ;
    private Sd_Card_DataBase sd_card_dataBase;
    private Main_Navigation main_navigation;
    private Android_Permission_Required android_permission_required;
    private Android_External_Writeable_Permission android_external_writeable_permission;
    private Storage_Info storage_info;


    public MainTransferFIle() {
        // Required empty public constructor
    }

    public MainTransferFIle createInstance(Main_Navigation main_navigation){
        this.main_navigation = main_navigation;
        return this;
    }
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        sd_card_dataBase = new Sd_Card_DataBase(main_navigation);

        layout =inflater.inflate(R.layout.fragment_main_transfer_file, container, false);

        Reference();
        if (!android_permission_required.isExternalStorageWritable())
            android_permission_required.call_Thread();

        LayoutInflater inflaters = getLayoutInflater();
        layouts = inflaters.inflate(R.layout.activity_custom_toast,
                (ViewGroup) layout.findViewById(R.id.toastcustom));
        call_Thread();
        return layout;

    }
    public void Reference(){

        move_Button =  layout.findViewById(R.id.move_Button);
        copy_Button = layout.findViewById(R.id.copy_Button);
        remove_Button =  layout.findViewById(R.id.remove_Button);
        file_Size_Text =  layout.findViewById(R.id.file_Size_text);
        restore_Button =  layout.findViewById(R.id.restore_Button);

        // on click process
        OnClick_Class onClick_class = new OnClick_Class();
        move_Button.setOnClickListener(onClick_class);
        copy_Button.setOnClickListener(onClick_class);
        remove_Button.setOnClickListener(onClick_class);
        restore_Button.setOnClickListener(onClick_class);

        // create object classes

        android_permission_required = new Android_Permission_Required(main_navigation ,main_navigation);
        android_external_writeable_permission = new Android_External_Writeable_Permission(main_navigation,main_navigation,this);
        storage_info = new Storage_Info(android_external_writeable_permission.getSd_Card_Path_URL(),main_navigation);

    }


    public void checked_For_Not_Get_Error(){
        File file = new File(android_permission_required.getExternal_Path()+storage_info.getWhatsapp_Path());
        if(!file.exists() || !android_permission_required.isExternalStorageWritable() ){
            whats_App_File_Exist_Internal = false;
            move_Button.animate().alpha(0.5f);
            copy_Button.animate().alpha(0.5f);
            remove_Button.animate().alpha(0.5f);
        }else {
            whats_App_File_Exist_Internal =true;
            remove_Button.animate().alpha(1f);
            move_Button.animate().alpha(1f);
            copy_Button.animate().alpha(1f);
        }

        if(!(new File(android_external_writeable_permission.getSd_Card_Path_URL()+storage_info.getWhatsapp_Path())).exists()) {
            restore_Button.animate().alpha(0.5f);
            whats_App_File_Exist_External = false;

        }
        else{
            restore_Button.animate().alpha(1f);
            whats_App_File_Exist_External = true;
        }


        if( android_external_writeable_permission.getSd_Card_Path_URL().equals("") || isSamePath() || android_external_writeable_permission.isGetting() ){
            move_Button.animate().alpha(0.5f);
            copy_Button.animate().alpha(0.5f);
            restore_Button.animate().alpha(0.5f);

        }
    }
//
 public void Enter_Whats_App_Folder(){
        if(android_permission_required.isExternalStorageWritable())
            file_Size_Text.setText("Data Size - "+storage_info.getWhatsAppInternalMemorySize());
 }
    public boolean onCreateOptionsMenu(Menu menu) {
// Inflate the menu; this adds items to the action bar if it is present.
        main_navigation.getMenuInflater().inflate(R.menu.menu_bar, menu);
        return true;
    }
    // Activity's overrided method used to perform click events on menu items
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK)
            return;
            sdCard_Uri = data.getData();
            main_navigation.grantUriPermission(main_navigation.getPackageName(), sdCard_Uri, Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            main_navigation.getContentResolver().takePersistableUriPermission(sdCard_Uri, Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                    sd_Card_Path_URL = Sd_Card_Path.getFullPathFromTreeUri(sdCard_Uri, main_navigation);
                    if(sd_card_dataBase.check_For_Empty()) {
                        sd_card_dataBase.Fill_It(sd_Card_Path_URL,sdCard_Uri.toString());
                    }else {
                        sd_card_dataBase.Update_The_Table("1",sd_Card_Path_URL , sdCard_Uri.toString());
                    }
            if(new File(sd_Card_Path_URL).exists()) string_URI  = Split_The_URI(sdCard_Uri.toString());
            android_external_writeable_permission.setSd_Card_Path_URL(sd_Card_Path_URL);
            android_external_writeable_permission.setString_URI(string_URI);

    }

    public String Split_The_URI(String url){
        String save[] = url.split("%3A");
        return save[0]+"%3A";
    }
    public void Toast_It(String Message) {
        toast_TextView = layouts.findViewById(R.id.text);
        if (toast == null || toast.getView().getWindowVisibility() != View.VISIBLE) {
            toast = new Toast(main_navigation.getApplicationContext());
            toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
            toast.setDuration(Toast.LENGTH_LONG);
            toast.setView(layouts);
            toast_TextView.setText(Message);
            toast.show();
        } else {
            toast.cancel();
        }

    }


    public boolean isSamePath(){ return android_permission_required.getExternal_Path().equals(android_external_writeable_permission.getSd_Card_Path_URL()); }

        public void call_Thread(){
        handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                    try{
                        Enter_Whats_App_Folder();
                        checked_For_Not_Get_Error();
                    }catch (Exception e){

                }

                call_Thread();

            }
        },1000);
    }
    public class  OnClick_Class implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            try {
                Enter_Whats_App_Folder();
            }catch (Exception e)
            {

            }
            checked_For_Not_Get_Error();
            switch (v.getId()) {
                case R.id.move_Button:
                    if (!android_permission_required.isExternalStorageWritable()) {
                        Toast_It("No Permission Granted");
                        android_permission_required.call_Thread();
                        call_Thread();
                    } else if (!whats_App_File_Exist_Internal)
                        Toast_It("No WhatsApp Data Present");
                    else if(move_Button.getAlpha() == 0.5f && android_external_writeable_permission.isGetting()){
                        Toast_It("Select The Sd Card  ");
                        android_external_writeable_permission.call_Thread();
                        call_Thread();
                    }
                    else {
                        Intent move_Intent = new Intent(main_navigation.getApplicationContext(), After_MainTransferFIle.class);
                        move_Intent.putExtra("move_copy_remove_restore","move");
                        move_Intent.putExtra("sd_card_path",android_external_writeable_permission.getSd_Card_Path_URL());
                        move_Intent.putExtra("Sd_Card_URI" ,  android_external_writeable_permission.getString_URI());
                        startActivity(move_Intent);
                    }
                    break;
                case R.id.copy_Button :
                    if(!android_permission_required.isExternalStorageWritable()) {
                        Toast_It("No Permission Granted");
                        android_permission_required.call_Thread();
                        call_Thread();
                    } else if(!whats_App_File_Exist_Internal)
                        Toast_It("No WhatsApp Data Present");
                    else if(copy_Button.getAlpha() == 0.5f && android_external_writeable_permission.isGetting()){
                        Toast_It("Select The Sd Card  ");
                        android_external_writeable_permission.call_Thread();
                        call_Thread();
                    }
                    else {
                        Intent copy_Intent = new Intent(main_navigation.getApplicationContext(), After_MainTransferFIle.class);
                        copy_Intent.putExtra("move_copy_remove_restore","copy");
                        copy_Intent.putExtra("sd_card_path",android_external_writeable_permission.getSd_Card_Path_URL());
                        copy_Intent.putExtra("Sd_Card_URI" ,  android_external_writeable_permission.getString_URI());
                        startActivity(copy_Intent);
                    }
                    break;
                case R.id.remove_Button :
                    if(!android_permission_required.isExternalStorageWritable()) {
                        Toast_It("No Permission Granted");
                        android_permission_required.call_Thread();
                        call_Thread();
                    } else if(!whats_App_File_Exist_Internal)
                        Toast_It("No WhatsApp Data Present");
                    else {
                        Intent remove_Intent = new Intent(main_navigation.getApplicationContext(), After_MainTransferFIle.class);
                        remove_Intent.putExtra("move_copy_remove_restore","remove");
                        remove_Intent.putExtra("sd_card_path",android_external_writeable_permission.getSd_Card_Path_URL());
                        startActivity(remove_Intent);
                    }
                    break;
                case R.id.restore_Button:
                    if(!android_permission_required.isExternalStorageWritable()) {
                        Toast_It("No Permission Granted");
                        android_permission_required.call_Thread();
                        call_Thread();
                    }
                    else if(restore_Button.getAlpha() == 0.5f && android_external_writeable_permission.isGetting()){
                        Toast_It("Select The Sd Card  ");
                        android_external_writeable_permission.call_Thread();
                        call_Thread();
                    }
                    else if(!whats_App_File_Exist_External)
                        Toast_It("No WhatsApp Data Present");
                    else {
                        Intent restore_Intent = new Intent(main_navigation.getApplicationContext(), After_MainTransferFIle.class);
                        restore_Intent.putExtra("move_copy_remove_restore","restore");
                        restore_Intent.putExtra("sd_card_path",android_external_writeable_permission.getSd_Card_Path_URL());
                        restore_Intent.putExtra("Sd_Card_URI" ,  android_external_writeable_permission.getString_URI());
                        startActivity(restore_Intent);
                    }
                    break;
            }


        }
    }

}
