package com.sudoajay.whatapp_media_mover_to_sdcard;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.widget.Toast;

import com.sudoajay.whatapp_media_mover_to_sdcard.Database_Classes.Sd_Card_DataBase;
import com.sudoajay.whatapp_media_mover_to_sdcard.Main_Fragments.Duplication_Class;
import com.sudoajay.whatapp_media_mover_to_sdcard.Main_Fragments.Home;
import com.sudoajay.whatapp_media_mover_to_sdcard.Main_Fragments.MainTransferFIle;

import java.io.File;

@SuppressLint("Registered")
public class Android_External_Writeable_Permission {
    private Activity activity;
    private Context context;
    private String sd_Card_Path_URL = "",string_URI;
    private final int REQUEST_CODE_OPEN_DOCUMENT_TREE =42;
    private Sd_Card_DataBase sd_card_dataBase;
    private Duplication_Class duplication_class;
    private Handler handler;
    private MainTransferFIle mainTransferFIle;
    private Home home;

    public Android_External_Writeable_Permission(Activity activity , Context context, Duplication_Class duplication_class){
        this.activity= activity;
        this.context =context;
        this.duplication_class=duplication_class;
        Grab();
    }
    public Android_External_Writeable_Permission(Activity activity , Context context, MainTransferFIle mainTransferFIle){
        this.activity= activity;
        this.context =context;
        this.mainTransferFIle=mainTransferFIle;
        Grab();
    }
    public Android_External_Writeable_Permission(Activity activity , Context context, Home home){
        this.activity= activity;
        this.context =context;
        this.home=home;
        Grab();
    }

    public void call_Thread(){
        handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                    Call_Custom_Dailog_Changes();


            }
        },1800);
    }
    public void Storage_Access_FrameWork(){
        final Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE);
        if(mainTransferFIle != null) mainTransferFIle.startActivityForResult(intent, REQUEST_CODE_OPEN_DOCUMENT_TREE);
        else if(duplication_class != null) {
            duplication_class.startActivityForResult(intent, REQUEST_CODE_OPEN_DOCUMENT_TREE);
        }else{
            home.startActivityForResult(intent, REQUEST_CODE_OPEN_DOCUMENT_TREE);
        }

    }

    public void Call_Custom_Dailog_Changes() {

        FragmentTransaction ft = ((FragmentActivity)activity).getSupportFragmentManager().beginTransaction();
        Sd_Card_dialog sd_card_dialog=new Sd_Card_dialog(this);
        sd_card_dialog.show(ft, "dialog");

    }
    public boolean isGetting(){
        return (sd_Card_Path_URL.equals(Environment.getExternalStorageDirectory().getAbsolutePath())) || (!new File(sd_Card_Path_URL).exists());
    }
    public void Grab(){
        sd_card_dataBase = new Sd_Card_DataBase(context);
        if(!sd_card_dataBase.check_For_Empty()){
            Cursor cursor= sd_card_dataBase.Get_All_Data();
            cursor.moveToNext();
            sd_Card_Path_URL = cursor.getString(1);
            string_URI =cursor.getString(2);


        }
    }

    public String getString_URI() {
        return string_URI;
    }

    public String getSd_Card_Path_URL() {
        return sd_Card_Path_URL;
    }


    public void setSd_Card_Path_URL(String sd_Card_Path_URL) {
        this.sd_Card_Path_URL = sd_Card_Path_URL;
    }

    public void setString_URI(String string_URI) {
        this.string_URI = string_URI;
    }
}
