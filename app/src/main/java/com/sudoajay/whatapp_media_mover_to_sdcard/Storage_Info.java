package com.sudoajay.whatapp_media_mover_to_sdcard;

import android.content.Context;
import android.database.Cursor;
import android.os.Environment;
import android.os.StatFs;

import com.sudoajay.whatapp_media_mover_to_sdcard.Database_Classes.Whatsapp_Mode_DataBase;

import java.io.File;
import java.util.ArrayList;

import static java.util.jar.Pack200.Packer.ERROR;

public class Storage_Info {

    private String sd_Card_Path_URL;

    private long internal_Available_Size , internal_Total_Size , internal_WhatsApp_Size , external_Available_Size , external_Total_Size,external_WhatsApp_Size
                    ,internal_Other_Size, external_Other_Size;
    private Whatsapp_Mode_DataBase whatsapp_mode_dataBase;
    private String whatsapp_Path;

    public  boolean externalMemoryAvailable() {
        return new File(sd_Card_Path_URL).exists();
    }
    public Storage_Info(String sd_Card_Path_URL , Context context){
        this.sd_Card_Path_URL = sd_Card_Path_URL;
        whatsapp_mode_dataBase = new Whatsapp_Mode_DataBase(context);
        if(!whatsapp_mode_dataBase.check_For_Empty()){
            Cursor cursor= whatsapp_mode_dataBase.Get_All_Data();
            cursor.moveToNext();
            whatsapp_Path = cursor.getString(1);
        }

    }
    public  String getAvailableInternalMemorySize() {
        File path = Environment.getExternalStorageDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSizeLong();
        long availableBlocks = stat.getAvailableBlocksLong();
        internal_Available_Size = availableBlocks* blockSize;
        return Convert_It(internal_Available_Size);
    }

    public  String getTotalInternalMemorySize() {
        File path = Environment.getExternalStorageDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSizeLong();
        long totalBlocks = stat.getBlockCountLong();
        internal_Total_Size = totalBlocks* blockSize;
        return Convert_It(internal_Total_Size);
    }

    public  String getOtherInternalMemorySize() {
         
        return Convert_It(((internal_Other_Size=((internal_Total_Size-internal_Available_Size)-internal_WhatsApp_Size))));
    }

    public  String getWhatsAppInternalMemorySize() {
        File path = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+whatsapp_Path);
        if(path.exists())
            return Convert_It((internal_WhatsApp_Size = getFileSizeInBytes(path.getAbsolutePath())));
        else {
            return "0.00 MB";
        }
    }

    public  String getAvailableExternalMemorySize() {
        if (externalMemoryAvailable()) {
            StatFs stat = new StatFs(sd_Card_Path_URL);
            long blockSize = stat.getBlockSizeLong();
            long availableBlocks = stat.getAvailableBlocksLong();
            external_Available_Size = availableBlocks * blockSize;
            return Convert_It(external_Available_Size);
        } else {
            return "0.0 GB";
        }
    }

    public  String getTotalExternalMemorySize() {
        if (externalMemoryAvailable()) {
            StatFs stat = new StatFs(sd_Card_Path_URL);
            long blockSize = stat.getBlockSizeLong();
            long totalBlocks = stat.getBlockCountLong();
            external_Total_Size  =  totalBlocks*blockSize ;
            return Convert_It(external_Total_Size);
        } else {
            return "0.0 GB";
        }
    }
    public  String getOtherExternalMemorySize() {

        return Convert_It(((external_Other_Size=((external_Total_Size-external_Available_Size)-external_WhatsApp_Size))));
    }
    public  String getWhatsAppExternalMemorySize() {
        File path = new File(sd_Card_Path_URL+whatsapp_Path);
        if(path.exists())
        return Convert_It((external_WhatsApp_Size=getFileSizeInBytes(path.getAbsolutePath())));
        else {
            return "0.00 MB";
        }
    }

    public  String Convert_It(long size) {
        if (size > (1024 * 1024 * 1024)) {
            // GB
            return Convert_To_Decimal((float) size / (1024 * 1024 * 1024)) + " GB";
        } else if (size > (1024 * 1024)) {
            // MB
            return Convert_To_Decimal((float) size / (1024 * 1024)) + " MB";

        } else {
            // KB
            return Convert_To_Decimal((float) size / (1024)) + " KB";
        }

    }

    public  String Convert_To_Decimal(float value) {
        String size = value + "";
        if (value >= 1000) {
            return size.substring(0, 4);
        } else if (value >= 100) {
            return size.substring(0, 3);
        } else {
            if (size.length() == 2 || size.length() == 3) {
                return size.substring(0, 1);
            }
            return size.substring(0, 4);

        }

    }

    public long getFileSizeInBytes(String fileName) {
        long ret = 0;
        File f = new File(fileName);
        try {
            if (f.exists()) {
                if (f.isFile()) {
                    return f.length();
                } else if (f.isDirectory()) {
                    File[] contents = f.listFiles();
                    for (int i = 0; i < contents.length; i++) {
                        if (contents[i].exists()) {
                            if (contents[i].isFile()) {
                                ret += contents[i].length();
                            } else if (contents[i].isDirectory())
                                ret += getFileSizeInBytes(contents[i].getPath());
                        }
                    }
                }
            } else {
                ret = 0;
            }
        }catch (Exception e){

        }
        return ret;
    }

    public String getWhatsapp_Path() {
        return whatsapp_Path;
    }

    public long getInternal_Other_Size() {
        return internal_Other_Size;
    }

    public long getExternal_Other_Size() {
        return external_Other_Size;
    }

    public long getInternal_Total_Size() {
        return internal_Total_Size;
    }

    public long getInternal_WhatsApp_Size() {
        return internal_WhatsApp_Size;
    }


    public long getExternal_Total_Size() {
        return external_Total_Size;
    }

    public long getExternal_WhatsApp_Size() {
        return external_WhatsApp_Size;
    }

}
