package com.sudoajay.whatsapp_media_mover_to_sdcard.Copy_delete_File;

import android.view.View;

import com.sudoajay.whatsapp_media_mover_to_sdcard.After_MainTransferFIle;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Objects;

/**
 * Created by sudoajay on 3/11/18.
 */

public class Restore_The_Data {
    private String external_Path_Url , whats_App_Path,sd_Card_Path_Url,process;
    private After_MainTransferFIle after_main_transferFIle;
    private int get_Data_Count;
    private boolean stop;


    public Restore_The_Data( String external_Path_Url,String sd_Card_Path_Url, String whats_App_Path, After_MainTransferFIle after_main_transferFIle
        ,String process ){
        this.external_Path_Url = external_Path_Url;
        this.whats_App_Path = whats_App_Path;
        this.sd_Card_Path_Url = sd_Card_Path_Url;
        this.after_main_transferFIle = after_main_transferFIle;
        this.process =process;
    }

    public void WhatsFolder_Checked(){

        try{
            if(!(new File(external_Path_Url+"/WhatsApp").exists())){
                new File(external_Path_Url + "/WhatsApp").mkdir();
            }
            if(!(new File(external_Path_Url+whats_App_Path).exists())){
                new File(external_Path_Url + whats_App_Path).mkdir();
            }
            if(!(new File(external_Path_Url+"/WhatsApp/" + Return_Path(11)).exists())){
                new File(external_Path_Url + "/WhatsApp/" + Return_Path(11)).mkdir();
            }


            for (int i = 1 ; i<=10 ;i++){
                get_Data_Count++;
                if(!(new File(external_Path_Url+whats_App_Path+ "/"+Return_Path(i)).exists())){
                    new File(external_Path_Url + whats_App_Path + "/" + Return_Path(i)).mkdir();
                }
                if(!(new File(external_Path_Url+whats_App_Path+ "/"+Return_Path(i)+"/Sent").exists())){
                    new File(external_Path_Url + whats_App_Path + "/" + Return_Path(i) + "/Sent").mkdir();
                }
                if(!(new File(external_Path_Url+whats_App_Path+ "/"+Return_Path(i)+"/Private").exists())){
                    new File(external_Path_Url + whats_App_Path + "/" + Return_Path(i) + "/Private").mkdir();
                }
                if(!process.equals("Background"))
                after_main_transferFIle.getMultiThreading_task().onProgressUpdate();

            }

        } catch (Exception ignored) {

        }
    }

    private String Return_Path(int no) {
        switch (no){
            case 1: return "WhatsApp Audio";
            case 2: return "WhatsApp Video";
            case 3: return "WhatsApp Documents";
            case 4: return "WhatsApp Images";
            case 5: return "WhatsApp Animated Gifs";
            case 6: return "WallPaper";
            case 7: return "WhatsApp Profile Photos";
            case 8: return ".Statuses";
            case 9: return "WhatsApp Stickers";
            case 10:return "WhatsApp Voice Notes";
            default:return "Databases";
        }
    }
    public void Restore_Folder_As_Per_Tick(int database,int audio , int video ,int document ,int images , int gif ,int voice
            ,int profile , int sticker  ){
        get_Data_Count=0;
        if(database == View.VISIBLE){
            Restore_Folder_(11);
        }
        if(audio == View.VISIBLE){
            Restore_Folder_(1);
        }
        if(video == View.VISIBLE){
            Restore_Folder_(2);
        }
        if(document == View.VISIBLE){
            Restore_Folder_(3);
        }
        if(images == View.VISIBLE){
            Restore_Folder_(4);
        }
        if(gif == View.VISIBLE){
            Restore_Folder_(5);
        }
        if(voice == View.VISIBLE){
            Restore_Folder_(10);
        }
        if(profile == View.VISIBLE){
            Restore_Folder_(7);
            Restore_Folder_(8);
            Restore_Folder_(6);
        }
        if(sticker == View.VISIBLE){
            Restore_Folder_(9);
        }


    }

    private void Restore_Folder_(int no) {

        try{
            File file;
            if (no == 11) file = new File(sd_Card_Path_Url + "/WhatsApp/" + Return_Path(no));
            else {
                file = new File(sd_Card_Path_Url + whats_App_Path + "/" + Return_Path(no));
            }
            Search_File(file,no);
        } catch (Exception ignored) {

        }
    }

    private void Search_File(File file, int no) {
        File[] data = file.listFiles();
        assert data != null;
        for(File files:data){
            if(!files.isDirectory()){
                Restore(files.getAbsolutePath(),files.getName(),no);
            }else {
                Search_File(files , no);
            }
        }
    }

    private void Restore(String path, String getName, int no) {
        InputStream is;
        OutputStream os ;

        try{
            if(!stop) {
                get_Data_Count++;
                after_main_transferFIle.getStorage_Info().getFileSizeInBytes(path);
                if (no == 11) {
                    if (!new File(external_Path_Url + "/WhatsApp/" + Return_Path(no) + "/" + getName).exists()) {
                        new File(external_Path_Url + "/WhatsApp/" + Return_Path(no) + "/" + getName).createNewFile();
                        is = new FileInputStream(new File(path));
                        os = new FileOutputStream(new File(external_Path_Url + "/WhatsApp/" + Return_Path(no) + "/" + getName));

                        byte[] buffer = new byte[1024];
                        int read;
                        while ((read = is.read(buffer)) != -1) {
                            os.write(buffer, 0, read);
                        }

                    }
                } else {
                    if (!new File(external_Path_Url + whats_App_Path + "/" + Return_Path(no) + "/" + getName).exists()) {
                        new File(external_Path_Url + whats_App_Path + "/" + Return_Path(no) + "/" + getName).createNewFile();
                        is = new FileInputStream(new File(path));
                        os = new FileOutputStream(new File(external_Path_Url + whats_App_Path + "/" + Return_Path(no) + "/" + getName));

                        byte[] buffer = new byte[1024];
                        int read;
                        while ((read = is.read(buffer)) != -1) {
                            os.write(buffer, 0, read);
                        }

                    }
                }
            }
            if(!process.equals("Background"))
                after_main_transferFIle.getMultiThreading_task().onProgressUpdate();
        } catch (Exception e) {
            if (Objects.equals(e.getMessage(), "write failed: ENOSPC (No space left on device)"))
                stop=true;


        }
    }

    public int getGet_Data_Count() {
        return get_Data_Count;
    }

    public boolean isStop() {
        return stop;
    }

}
