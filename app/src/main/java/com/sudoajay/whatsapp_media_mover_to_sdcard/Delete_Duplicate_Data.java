package com.sudoajay.whatsapp_media_mover_to_sdcard;

import android.net.Uri;
import android.os.Environment;
import android.support.v4.provider.DocumentFile;

import com.sudoajay.whatsapp_media_mover_to_sdcard.Permission.AndroidSdCardPermission;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class Delete_Duplicate_Data {

    private List<String> list_Header;
    private HashMap<String, List<String>> list_Header_Child;
    private Show_Duplicate_File show_duplicate_file;
    private String step_Into[], externalPath;
    private DocumentFile sd_Card_documentFile;
    private String sdCardPath;
    private int steps_Into;

    public Delete_Duplicate_Data(List<String> list_Header, HashMap<String, List<String>> list_Header_Child, Show_Duplicate_File show_duplicate_file) {
        this.list_Header = list_Header;
        this.list_Header_Child = list_Header_Child;
        this.show_duplicate_file = show_duplicate_file;

        // garb and store the data from shared preference
        AndroidSdCardPermission android_SdCard_Permission = new AndroidSdCardPermission(show_duplicate_file.getApplicationContext());
        sdCardPath = android_SdCard_Permission.getSd_Card_Path_URL();
        String string_URI = android_SdCard_Permission.getString_URI();

        externalPath = Environment.getExternalStorageDirectory().getAbsolutePath();

        if (string_URI != null) {
            sd_Card_documentFile = DocumentFile.fromTreeUri(show_duplicate_file.getApplicationContext(), Uri.parse(string_URI));
        }
        Main_Method();
    }

    public void Main_Method() {
        for (int i = 0; i < list_Header.size(); i++) {
            Seprate_The_Data(list_Header_Child.get(list_Header.get(i)));
            show_duplicate_file.getMultiThreading_task().onProgressUpdate();
        }
    }

    public void Seprate_The_Data(List<String> list) {
        for (int i = 1; i < list.size(); i++) {
            if (list.get(i).contains(externalPath)) {
                Delete_The_Data_From_Internal_Storage(list.get(i));
            } else {
                DeleteTheDataFromExternalStorage(list.get(i));
            }
        }
    }

    public void Delete_The_Data_From_Internal_Storage(String path) {

        File file = new File(path);
        boolean wasSuccessful = file.delete();
        if (file.exists()) {
            try {
                wasSuccessful = file.getCanonicalFile().delete();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (file.exists()) {
                show_duplicate_file.getApplicationContext().deleteFile(file.getName());
            }
        }

    }
    public void DeleteTheDataFromExternalStorage(String path) {
        DocumentFile sdCardDocument = sd_Card_documentFile;

        if (sdCardDocument != null) {
            String[] spiltSdPath = path.split(sdCardPath + "/");
            String[] spilt = spiltSdPath[1].split("/");
            for (String part : spilt) {
                DocumentFile nextDocument = sdCardDocument.findFile(part);
                if (nextDocument != null) {
                    sdCardDocument = nextDocument;
                }

            }

            sdCardDocument.delete();
        }

    }



}
