package com.sudoajay.whatapp_media_mover_to_sdcard;

import android.util.Log;
import android.view.View;

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
    private List<File> get_All_Data = new LinkedList<>();
    private List<String> Data_Store = new ArrayList<>();



    private static MessageDigest messageDigest;
    static {
        try {
            messageDigest = MessageDigest.getInstance("SHA-512");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("cannot initialize SHA-512 hash function", e);
        }
    }

    public void Duplication(File external_dir , File sd_Card_dir,int internal_Visible , int external_Visible ){

        Map<String, List<String>> lists = new HashMap<>();
        if(internal_Visible == View.VISIBLE) Get_All_Path(external_dir);
        if(external_Visible == View.VISIBLE)Get_All_Path(sd_Card_dir);
        findDuplicatedFiles(lists);
        for (List<String> list : lists.values()) {
            if (list.size() > 1) {
                Data_Store.addAll(list);
                Data_Store.add("And");

            }
        }
    }
    public void findDuplicatedFiles(Map<String, List<String>> lists) {
        for (File child : get_All_Data) {
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
                get_All_Data.add(child);
            }
        }
    }

    public List<String> getList() {
        return Data_Store;
    }
}
