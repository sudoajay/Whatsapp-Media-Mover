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
    private int steps_Into;

    public Delete_Duplicate_Data(List<String> list_Header, HashMap<String, List<String>> list_Header_Child, Show_Duplicate_File show_duplicate_file) {
        this.list_Header = list_Header;
        this.list_Header_Child = list_Header_Child;
        this.show_duplicate_file = show_duplicate_file;

        // garb and store the data from shared preference
        AndroidSdCardPermission android_SdCard_Permission = new AndroidSdCardPermission(show_duplicate_file.getApplicationContext());
        String sd_Card_Path_URL = android_SdCard_Permission.getSd_Card_Path_URL();
        String string_URI = android_SdCard_Permission.getString_URI();

        externalPath = Environment.getExternalStorageDirectory().getAbsolutePath();

        if (string_URI != null) {
            String sd_Card_Uri = Split_The_URI(string_URI);
            Uri sd_Card_URL = Uri.parse(sd_Card_Uri);
            sd_Card_documentFile = DocumentFile.fromTreeUri(show_duplicate_file.getApplicationContext(), sd_Card_URL);
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
                Delete_The_Data_From_External_Storage(list.get(i));
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


    public void Delete_The_Data_From_External_Storage(String path) {
        try {
            String[] save = path.split("/WhatsApp/");
            DocumentFile documentFile = sd_Card_documentFile.findFile("WhatsApp");
            step_Into = save[1].split("/");
            steps_Into = 0;
            Document(documentFile);
        } catch (Exception ignored) {

        }
    }

    public void Document(DocumentFile documentFile) {
        if (steps_Into < step_Into.length) {
            DocumentFile documentFile1 = documentFile.findFile(step_Into[steps_Into]);
            steps_Into++;
            Document(documentFile1);
        } else {
            documentFile.delete();
        }
    }

    public String Split_The_URI(String url) {
        String save[] = url.split("%3A");
        return save[0] + "%3A";
    }

//    public String check_For_Duplicate(DocumentFile file, String name) {
//        DocumentFile[] Files = file.listFiles();
//        for (DocumentFile files : Files) {
//            if (files.getName().equalsIgnoreCase(name)) {
//                return files.getName();
//            }
//        }
//        return name;
//    }

}
