package com.sudoajay.whatapp_media_mover_to_sdcard;

import android.content.Context;
import android.view.View;

import com.sudoajay.whatapp_media_mover_to_sdcard.Toast.CustomToast;

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

public class Duplication_Data {
    private List<File> getAllData = new LinkedList<>();
    private ArrayList<String> dataStore = new ArrayList<>();
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
        if(internal_Visible == View.VISIBLE) Get_All_Path(external_dir);
        if(external_Visible == View.VISIBLE)Get_All_Path(sd_Card_dir);

        // check for length in file
        DuplicatedFilesUsingLength();
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
    public void DuplicatedFilesUsingHashTable(Map<String, List<String>> lists) {
        for (File child : getAllData) {
            try {
                FileInputStream fileInput = new FileInputStream(child);
                byte fileData[] = new byte[(int) child.length()];
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
    public void Get_All_Path(File directory  ){
        for (File child : directory.listFiles()) {
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
}
