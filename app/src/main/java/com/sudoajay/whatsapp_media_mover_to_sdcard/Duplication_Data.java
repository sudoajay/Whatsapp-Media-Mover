package com.sudoajay.whatsapp_media_mover_to_sdcard;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;
import android.view.View;
import android.webkit.MimeTypeMap;

import com.sudoajay.whatsapp_media_mover_to_sdcard.Toast.CustomToast;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Duplication_Data {
    private List<File> getAllData = new LinkedList<>();
    private ArrayList<String> dataStore = new ArrayList<>();
    @SuppressLint("StaticFieldLeak")
    private static Context context;



    private static MessageDigest messageDigest;
    static {
        try {
            messageDigest = MessageDigest.getInstance("SHA-512");
        } catch (NoSuchAlgorithmException e) {
            CustomToast.ToastIt(context,"cannot initialize SHA-512 hash function");
        }
    }

    public void Duplication(Context context, File external_dir , File sd_Card_dir, int internal_Visible , int external_Visible ){

        Duplication_Data.context =context;

        Map<String, List<String>> lists = new HashMap<>();
        if(internal_Visible == View.VISIBLE) {
            if (FileExist(external_dir)) Get_All_Path(external_dir);
        }
        if(external_Visible == View.VISIBLE){
            if (FileExist(sd_Card_dir))Get_All_Path(sd_Card_dir);
        }


        // check for length in file
        DuplicatedFilesUsingLength();

        // check the file meme
        DuplicateFileType();

        // check for hash using "SHA-512"
        DuplicatedFilesUsingHashTable(lists);
        for (List<String> list : lists.values()) {
            if (list.size() > 1) {
                dataStore.addAll(list);
                dataStore.add("And");

            }
        }
    }
    private void DuplicatedFilesUsingLength(){
        ArrayList<Long> getAllDataLength =new ArrayList<>();
        for(File data: getAllData){
            getAllDataLength.add(data.length());
        }

        for(int i = getAllDataLength.size()-1 ;i >0;i--){
            for(int j = 0 ;j <getAllDataLength.size();j++){
                if(i !=j){
                    if(getAllDataLength.get(i).equals(getAllDataLength.get(j)))break;
                    if(j==getAllDataLength.size()-1){
                        getAllDataLength.remove(i);
                        getAllData.remove(i);
                    }
                }
            }
        }
    }

    private void DuplicateFileType(){
        ArrayList<String> getAllDataType =new ArrayList<>();
        for(File data: getAllData){
            getAllDataType.add(getMimeType(Uri.fromFile(data)));
        }

        for(int i = getAllDataType.size()-1 ;i >0;i--){
            for(int j = 0 ;j <getAllDataType.size();j++){
                if(i !=j){
                    if(getAllDataType.get(i) == null || getAllDataType.get(i).equals(getAllDataType.get(j)))break;
                    if(j==getAllDataType.size()-1){
                        getAllDataType.remove(i);
                        getAllData.remove(i);
                    }
                }
            }
        }
    }


    private String getMimeType(Uri uri) {
        String mimeType;
        if (Objects.equals(uri.getScheme(), ContentResolver.SCHEME_CONTENT)) {
            ContentResolver cr = context.getContentResolver();
            mimeType = cr.getType(uri);
        } else {
            String fileExtension = MimeTypeMap.getFileExtensionFromUrl(uri
                    .toString());
            mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(
                    fileExtension.toLowerCase());
        }
        return mimeType;
    }

    private void DuplicatedFilesUsingHashTable(Map<String, List<String>> lists) {
        for (File child : getAllData) {
            try {
                FileInputStream fileInput = new FileInputStream(child);
                byte[] fileData = new byte[(int) child.length()];
                fileInput.read(fileData);
                fileInput.close();
                String uniqueFileHash = new BigInteger(1, messageDigest.digest(fileData)).toString(16);
                List<String> list = lists.get(uniqueFileHash);

                if (list == null) {
                    list = new LinkedList<>();
                    lists.put(uniqueFileHash, list);
                }
                list.add(child.getAbsolutePath());
            } catch (IOException e) {
                throw new RuntimeException("cannot read file " + child.getAbsolutePath(), e);
            }
        }
    }

    private void Get_All_Path(File directory) {
        for (File child : Objects.requireNonNull(directory.listFiles())) {
            if (child.isDirectory()) {
                Get_All_Path(child);
            }else {
                if(!child.getName().equals(".nomedia"))
                    getAllData.add(child);
            }
        }
    }

    public ArrayList<String> getList() {
        return dataStore;
    }

    private boolean FileExist(File path){
        return   (path.listFiles() != null && path.exists());
    }
}
