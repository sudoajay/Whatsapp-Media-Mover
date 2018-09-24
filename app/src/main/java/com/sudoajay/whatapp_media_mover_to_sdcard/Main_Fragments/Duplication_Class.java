package com.sudoajay.whatapp_media_mover_to_sdcard.Main_Fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.sudoajay.whatapp_media_mover_to_sdcard.Android_External_Writeable_Permission;
import com.sudoajay.whatapp_media_mover_to_sdcard.Android_Permission_Required;
import com.sudoajay.whatapp_media_mover_to_sdcard.Duplication_Data;
import com.sudoajay.whatapp_media_mover_to_sdcard.Main_Navigation;
import com.sudoajay.whatapp_media_mover_to_sdcard.R;
import com.sudoajay.whatapp_media_mover_to_sdcard.Database_Classes.Sd_Card_DataBase;
import com.sudoajay.whatapp_media_mover_to_sdcard.Sd_Card_Path;
import com.sudoajay.whatapp_media_mover_to_sdcard.Show_Duplicate_File;
import com.sudoajay.whatapp_media_mover_to_sdcard.Storage_Info;
import java.io.File;
import java.io.Serializable;
import dmax.dialog.SpotsDialog;


public class Duplication_Class extends Fragment {
    private ImageView internal_Check,external_Check,internal_Image_View,external_Image_View;
    private TextView internal_Text_View , external_Text_View;
    private Android_Permission_Required android_permission_required;
    private Android_External_Writeable_Permission android_external_writeable_permission;
    private Button file_Size_text;
    private String sd_Card_Path_URL ,string_URI;
    private Uri sdCard_Uri ;
    private TextView toast_TextView;
    private long size;
    private Toast toast;
    private View layout,layouts;
    private Button scan_Button;
    private Duplication_Data duplication_data = new Duplication_Data();
    private AlertDialog alertDialog ;
    private MultiThreading_Task multiThreading_task = new MultiThreading_Task();
    private Main_Navigation main_navigation ;
    private Storage_Info storage_info;

    public Duplication_Class() {
        // Required empty public constructor
    }

    public Duplication_Class createInstance(Main_Navigation main_navigation){
        this.main_navigation = main_navigation;
        return this;
    }
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        layout =inflater.inflate(R.layout.fragment_duplicate_class, container, false);

        references();
        LayoutInflater inflaters = getLayoutInflater();
        layouts = inflaters.inflate(R.layout.activity_custom_toast,
                (ViewGroup) layout.findViewById(R.id.toastcustom));
        if (!android_permission_required.isExternalStorageWritable()){
            android_permission_required.call_Thread();
                internal_Check.setVisibility(View.INVISIBLE);
                external_Check.setVisibility(View.INVISIBLE);

        }else {
            if(!new File(android_permission_required.getExternal_Path()+storage_info.getWhatsapp_Path()+"/").exists()){

                internal_Check.setVisibility(View.INVISIBLE);
            }
        }

        if(android_external_writeable_permission.isGetting()) {
            external_Check.setVisibility(View.INVISIBLE);
        }else{
            if(!new File(android_external_writeable_permission.getSd_Card_Path_URL()+storage_info.getWhatsapp_Path()+"/").exists()) {
                external_Check.setVisibility(View.INVISIBLE);
            }

        }

        Show_Size();

        return  layout;

    }
    public void references(){

        internal_Check = layout.findViewById(R.id.internal_Check);
        external_Check = layout.findViewById(R.id.external_Check);
        internal_Image_View =layout.findViewById(R.id.internal_Image_View);
        external_Image_View = layout.findViewById(R.id.external_Image_View);
        scan_Button = layout.findViewById(R.id.scan_Button);
        internal_Text_View = layout.findViewById(R.id.internal_Text_View);
        external_Text_View = layout.findViewById(R.id.external_Text_View);
        file_Size_text = layout.findViewById(R.id.file_Size_text);

        // onclick
        OnClick_Class onClick_class = new OnClick_Class();
        internal_Check.setOnClickListener(onClick_class);
        external_Check.setOnClickListener(onClick_class);
        scan_Button.setOnClickListener(onClick_class);
        external_Image_View.setOnClickListener(onClick_class);
        internal_Image_View.setOnClickListener(onClick_class);
        internal_Text_View.setOnClickListener(onClick_class);
        external_Text_View.setOnClickListener(onClick_class);

        // create new clas object
        android_external_writeable_permission = new Android_External_Writeable_Permission(main_navigation,main_navigation ,this);
        android_permission_required = new Android_Permission_Required(main_navigation, main_navigation);
        storage_info= new Storage_Info(android_external_writeable_permission.getSd_Card_Path_URL(),main_navigation);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Sd_Card_DataBase sd_card_dataBase = new Sd_Card_DataBase(main_navigation);
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


    @SuppressLint("SetTextI18n")
    public void Show_Size(){
        size=0;
        if(internal_Check.getVisibility() == View.VISIBLE && external_Check.getVisibility() == View.VISIBLE) {
            size +=storage_info.getFileSizeInBytes(android_permission_required.getExternal_Path() + storage_info.getWhatsapp_Path()+"/");
            size+=storage_info.getFileSizeInBytes(android_external_writeable_permission.getSd_Card_Path_URL()+
                    storage_info.getWhatsapp_Path()+"/");
            file_Size_text.setText("Data Size - " + storage_info.Convert_It(size));
        }
        else if(external_Check.getVisibility() == View.VISIBLE) {
                        size+=storage_info.getFileSizeInBytes(android_external_writeable_permission.getSd_Card_Path_URL()+
                    storage_info.getWhatsapp_Path()+"/");
            file_Size_text.setText("Data Size - " + storage_info.Convert_It(size));

        }
        else  if(internal_Check.getVisibility() == View.VISIBLE) {
            size +=storage_info.getFileSizeInBytes(android_permission_required.getExternal_Path() + storage_info.getWhatsapp_Path()+"/");
            file_Size_text.setText("Data Size - " + storage_info.Convert_It(size));

        }
    }

    @SuppressLint("StaticFieldLeak")
    public class MultiThreading_Task extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            alertDialog = new SpotsDialog.Builder()
                    .setContext(main_navigation)
                    .setMessage("Scanning...")
                    .setCancelable(false)
                    .setTheme(R.style.Custom)
                    .build();

            alertDialog.show();
            super.onPreExecute();
        }
        @Override
        protected String doInBackground(String... strings) {
            duplication_data.Duplication(new File(android_permission_required.getExternal_Path()+storage_info.getWhatsapp_Path()+"/") ,
                    new File(android_external_writeable_permission.getSd_Card_Path_URL()+storage_info.getWhatsapp_Path()+"/"),
                    internal_Check.getVisibility(),external_Check.getVisibility());

    return null;
        }
        @Override
        protected void onProgressUpdate(String... values) {

            super.onProgressUpdate(values);
        }
        @Override
        protected void onPostExecute(String s) {
            alertDialog.dismiss();

            Bundle extra = new Bundle();
            extra.putSerializable("Duplication_Class_Data", (Serializable) duplication_data.getList());

            Intent intent = new Intent(main_navigation, Show_Duplicate_File.class);
            intent.putExtra("Duplication_Class_Data" , extra);
            startActivity(intent);
            super.onPostExecute(s);

        }

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
    public class  OnClick_Class implements View.OnClickListener {

        @SuppressLint("SetTextI18n")
        @Override
        public void onClick(View v) {
            switch (v.getId()) {

                case R.id.internal_Check :
                case R.id.internal_Image_View :
                case R.id.internal_Text_View :
                    if(internal_Check.getVisibility() == View.VISIBLE) {
                        internal_Check.setVisibility(View.INVISIBLE);
                        size -= storage_info.getFileSizeInBytes(android_permission_required.getExternal_Path() + storage_info.getWhatsapp_Path()+"/");
                    }
                    else{
                        if(!android_permission_required.isExternalStorageWritable()) {
                            Toast_It("No Permission Granted");
                            android_permission_required.call_Thread();
                        }
                        else if(!new File(android_permission_required.getExternal_Path()+storage_info.getWhatsapp_Path()).exists()){
                            Toast_It("No WhatsApp Data Present");
                        }
                        else {
                            internal_Check.setVisibility(View.VISIBLE);
                            size +=storage_info.getFileSizeInBytes(android_permission_required.getExternal_Path() + storage_info.getWhatsapp_Path()+"/");

                        }

                    }
                    break;

                case R.id.external_Check :
                case R.id.external_Image_View :
                case R.id.external_Text_View :
                    if(external_Check.getVisibility() == View.VISIBLE) {
                        external_Check.setVisibility(View.INVISIBLE);
                        size-=storage_info.getFileSizeInBytes(android_external_writeable_permission.getSd_Card_Path_URL()+storage_info.getWhatsapp_Path()+"/");

                    }
                    else{
                        android_external_writeable_permission.Grab();

                        if(android_external_writeable_permission.isGetting()) {
                            Toast_It("Select The Sd Card  ");
                            android_external_writeable_permission.call_Thread();
                        }  else if(!new File(android_external_writeable_permission.getSd_Card_Path_URL()+storage_info.getWhatsapp_Path()+"/").exists()){
                            Toast_It("No WhatsApp Data Present");
                        }

                        else {
                            external_Check.setVisibility(View.VISIBLE);
                            size+=storage_info.getFileSizeInBytes(android_external_writeable_permission.getSd_Card_Path_URL()+
                                    storage_info.getWhatsapp_Path()+"/");

                        }


                    }
                    break;
                case R.id.scan_Button:
                    if(internal_Check.getVisibility() == View.VISIBLE || external_Check.getVisibility() == View.VISIBLE)
                        multiThreading_task.execute();
                    break;

            }
            file_Size_text.setText("Data Size - " + storage_info.Convert_It(size));

        }

    }
}

