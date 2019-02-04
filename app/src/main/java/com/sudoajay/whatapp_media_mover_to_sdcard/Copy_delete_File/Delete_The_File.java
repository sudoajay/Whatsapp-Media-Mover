package com.sudoajay.whatapp_media_mover_to_sdcard.Copy_delete_File;

import android.content.Context;
import android.util.Log;
import android.view.View;

import com.sudoajay.whatapp_media_mover_to_sdcard.After_MainTransferFIle;
import com.sudoajay.whatapp_media_mover_to_sdcard.sharedPreferences.WhatsappPathSharedpreferences;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by sudoajay on 2/22/18.
 */

public class Delete_The_File {

    private String external_Path_URL;
    private After_MainTransferFIle after_main_transferFIle;
    private String whats_App_Media_Path;
    private long getSize ;
    private int get_Data_Count;
    private List<File> only_Selected_File ;
    private int normal_Changes;
    private String whatsapp_Path,process;

    public Delete_The_File(String external_Path_URL, String whats_App_Media_Path, After_MainTransferFIle after_main_transferFIle
            , List<File> only_Selected_File , int normal_Changes ,String process, Context context){
        this.external_Path_URL=external_Path_URL;
        this.whats_App_Media_Path = whats_App_Media_Path;
        this.after_main_transferFIle = after_main_transferFIle;
        this.only_Selected_File= only_Selected_File;
        this.normal_Changes =normal_Changes;
        this.process= process;

        // shared preferences use to grab the data
        WhatsappPathSharedpreferences whatsappPathSharedpreferences = new WhatsappPathSharedpreferences(context);
        whatsapp_Path = whatsappPathSharedpreferences.getWhatsapp_Path();
    }
    public void get_File_Path(int databases , int audio , int video ,int document ,int images , int gif ,int voice
            ,int profile , int sticker   ){
        if(databases == View.VISIBLE){
            Remove_DataBase_Other_Files(new File(external_Path_URL+ whatsapp_Path+"Databases"));
        }
        if(audio == View.VISIBLE){
            Delete_The_File(external_Path_URL+whats_App_Media_Path+ whatsapp_Path.substring(0,whatsapp_Path.length()-1)+" Audio/");
        }
        if(video == View.VISIBLE){
            Delete_The_File(external_Path_URL+whats_App_Media_Path+ whatsapp_Path.substring(0,whatsapp_Path.length()-1)+" Video/");
        }
        if(document == View.VISIBLE){
            Delete_The_File(external_Path_URL+whats_App_Media_Path+ whatsapp_Path.substring(0,whatsapp_Path.length()-1)+" Documents/");
        }
        if(images == View.VISIBLE){
            Delete_The_File(external_Path_URL+whats_App_Media_Path+ whatsapp_Path.substring(0,whatsapp_Path.length()-1)+" Images/");
        }
        if(gif == View.VISIBLE){
            Delete_The_File(external_Path_URL+whats_App_Media_Path+ whatsapp_Path.substring(0,whatsapp_Path.length()-1)+" Animated Gifs/");
        }
        if(voice == View.VISIBLE){
            Delete_The_File(external_Path_URL+whats_App_Media_Path+ whatsapp_Path.substring(0,whatsapp_Path.length()-1)+" Voice Notes/");
        }
        if(profile == View.VISIBLE){
            Delete_The_File(external_Path_URL+whats_App_Media_Path+  "/WallPaper/");
            Delete_The_File(external_Path_URL+whats_App_Media_Path+ whatsapp_Path.substring(0,whatsapp_Path.length()-1)+" Profile Photos/");
            Delete_The_File(external_Path_URL+whats_App_Media_Path+ "/.Statuses/");
        }
        if(sticker == View.VISIBLE){
            Delete_The_File(external_Path_URL+whats_App_Media_Path+ whatsapp_Path.substring(0,whatsapp_Path.length()-1)+" Stickers/");
        }


    }
    public void Delete_The_File(String path){
        File file = new File(path);
        deleteRecursive(file);

        }
    public void deleteRecursive(File fileOrDirectory) {
        if (fileOrDirectory.isDirectory())
            for (File child : fileOrDirectory.listFiles())
                deleteRecursive(child);
        getSize+= after_main_transferFIle.getStorage_Info().getFileSizeInBytes(fileOrDirectory.getAbsolutePath());
        if(Check_For_Extension(fileOrDirectory.getAbsolutePath()) && !Selected_The_File(fileOrDirectory) && Convert_The_LastMoified(fileOrDirectory.lastModified())) {
            get_Data_Count++;
            fileOrDirectory.delete();
        }
        if(!process.equals("Background"))
        after_main_transferFIle.getMultiThreading_task().onProgressUpdate();
    }
    public boolean Check_For_Extension(String path){
        int i = path.lastIndexOf('.');
        String extension="";
        if (i > 0) {
            extension = path.substring(i+1);
        }
        return extension.equals("jpg") || extension.equals("mp3") || extension.equals("mp4")
                || extension.equals("pptx") || extension.equals("pdf") || extension.equals("docx")
                || extension.equals("opus") || extension.equals("m4a")
                || extension.equals("amr") || extension.equals("aac");

    }
    public long getGetSize() {
        return getSize;
    }

    public int getGet_Data_Count() {
        return get_Data_Count;
    }
    public boolean Selected_The_File(File file){
        for (File data: only_Selected_File)
            if(file.equals(data))
                return true;

        return false;

    }

    public void Remove_DataBase_Other_Files(File database_File){
        List<File> files = new ArrayList<>();
        try{
            for(File database : database_File.listFiles()){
                files.add(database);
            }
            Convert_Into_Last_Modified(files);
            for (int i = files.size()-1 ; i >=1;i--){
                getSize+= after_main_transferFIle.getStorage_Info().getFileSizeInBytes(files.get(i).getAbsolutePath());
                get_Data_Count++;
                new File(files.get(i).getAbsolutePath()).delete();
            }
        }catch (Exception e){
            Log.i("E" , e.getMessage());
        }
    }
    public void Convert_Into_Last_Modified(List<File> files){
        File temp_File;
        for (int i = 0 ; i < files.size();i++){
            for (int j = i ; j < files.size()-1;j++){
                Date date1 = new Date(files.get(i).lastModified());
                Date date2 = new Date(files.get(j+1).lastModified());
                if(date1.compareTo(date2) < 0){
                    temp_File=files.get(i);
                    files.set(i,files.get(j+1));
                    files.set(j+1,temp_File);
                }
            }
        }
    }
    public boolean Convert_The_LastMoified(long last_Modified){

        int days =0;

        Calendar current_Time = Calendar.getInstance();
        Calendar calendar_Last_Modified =Calendar.getInstance();
        calendar_Last_Modified.setTime(new Date(last_Modified));

        int current_Date = current_Time.get(Calendar.DATE);
        int last_Modified_Current_Time = calendar_Last_Modified.get(Calendar.DATE);

        int current_Month = current_Time.get(Calendar.MONTH);
        int last_Modified_Current_Month = calendar_Last_Modified.get(Calendar.MONTH);

        int current_Year = current_Time.get(Calendar.YEAR);
        int last_Modified_Current_Year = calendar_Last_Modified.get(Calendar.YEAR);

        days += (360*(current_Year-last_Modified_Current_Year));
        days += (30*(current_Month-last_Modified_Current_Month));
        days += ((current_Date-last_Modified_Current_Time));

        return days >= normal_Changes;
    }
}
