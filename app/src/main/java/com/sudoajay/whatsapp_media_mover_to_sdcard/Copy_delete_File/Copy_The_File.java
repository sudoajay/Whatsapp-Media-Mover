package com.sudoajay.whatsapp_media_mover_to_sdcard.Copy_delete_File;

import android.content.Context;
import android.provider.DocumentsContract;
import android.support.v4.provider.DocumentFile;
import android.view.View;
import android.webkit.MimeTypeMap;

import com.sudoajay.whatsapp_media_mover_to_sdcard.After_MainTransferFIle;
import com.sudoajay.whatsapp_media_mover_to_sdcard.sharedPreferences.WhatsappPathSharedpreferences;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * Created by sudoajay on 2/22/18.
 */

public class Copy_The_File {
    private String external_Path_Url,whats_App_Media_Path;
    private DocumentFile sd_Card_documentFile;
    private After_MainTransferFIle after_main_transferFIle;
    private int get_Data_Count,normal_Changes;
    private boolean copy_Done;
    private boolean stop;
    private List<File> only_Selected_File ;
    private String whatsapp_Path,process;
    private Context context;
    private static final int BUFFER = 2048;

    public Copy_The_File(String external_Path_Url, String whats_App_Media_Path, DocumentFile sd_Card_documentFile,
                         After_MainTransferFIle after_main_transferFIle, List<File> only_Selected_File , int normal_Changes,String process, Context context){
        this.external_Path_Url = external_Path_Url;
        this.whats_App_Media_Path = whats_App_Media_Path;
        this.sd_Card_documentFile=sd_Card_documentFile;
        this.after_main_transferFIle = after_main_transferFIle;
        this.only_Selected_File= only_Selected_File;
        this.normal_Changes = normal_Changes;
        this.process=process;
        this.context=context;


        // shared preferences use to grab the data
        WhatsappPathSharedpreferences whatsappPathSharedpreferences = new WhatsappPathSharedpreferences(context);
        whatsapp_Path = whatsappPathSharedpreferences.getWhatsapp_Path();

    }
    public void Copy_Folder_As_Per_Tick(int database, int audio , int video ,int document ,int images , int gif ,int voice
            ,int profile , int sticker  ){

        if(database == View.VISIBLE){
            Copy_Folder(11);
        }
        if(audio == View.VISIBLE){
            Copy_Folder(1);
        }
        if(video == View.VISIBLE){
            Copy_Folder(2);
        }
        if(document == View.VISIBLE){
            Copy_Folder(3);
        }
        if(images == View.VISIBLE){
            Copy_Folder(4);
        }
        if(gif == View.VISIBLE){
            Copy_Folder(5);
        }
        if(voice == View.VISIBLE){
            Copy_Folder(10);
        }
        if(profile == View.VISIBLE){
            Copy_Folder(7);
            Copy_Folder(8);
            Copy_Folder(6);
        }
        if(sticker == View.VISIBLE){
            Copy_Folder(9);
        }

        copy_Done = true;
    }
    public void Copy_Folder(int folder_No){
        try{
            File folder_File;
            DocumentFile exact_Path;
            if(folder_No!=11) {
                 exact_Path = Return_Absolute_Path(Return_Path(folder_No));

                 folder_File = new File(external_Path_Url + whats_App_Media_Path + "/" + Return_Path(folder_No));
                 Get_List(exact_Path , folder_File);
            } else
            {
                folder_File = new File(external_Path_Url +whatsapp_Path + Return_Path(folder_No));
                 exact_Path = Return_Database_Path(Return_Path(folder_No));
                Remove_DataBase_Other_Files(folder_File);
                Delete_Database_File(exact_Path);
                File[] file = folder_File.listFiles();
                copyDocument(context, file[0], exact_Path);
            }

        }catch (Exception ignored){
        }
    }
    public DocumentFile Return_Absolute_Path(String folder_Name){
        DocumentFile whatsApp_dir =sd_Card_documentFile.findFile(check_For_Duplicate(sd_Card_documentFile ,
                whatsapp_Path.substring(1,whatsapp_Path.length()-1)));
        DocumentFile media_dir = Objects.requireNonNull(whatsApp_dir).findFile(check_For_Duplicate(whatsApp_dir ,"Media"));
        return  Objects.requireNonNull(media_dir).findFile(check_For_Duplicate(media_dir ,folder_Name));
    }
    public DocumentFile Return_Database_Path(String folder_Name){
        DocumentFile whatsApp_dir =sd_Card_documentFile.findFile(check_For_Duplicate(sd_Card_documentFile ,
                whatsapp_Path.substring(1,whatsapp_Path.length()-1)));
        assert whatsApp_dir != null;
        return  whatsApp_dir.findFile(check_For_Duplicate(whatsApp_dir ,folder_Name));
    }
    public void Delete_Database_File(DocumentFile documentFile){
        DocumentFile[] documentFile1 = documentFile.listFiles();
        for(DocumentFile f : documentFile1) {
            Objects.requireNonNull(documentFile.findFile(Objects.requireNonNull(f.getName()))).delete();
        }
    }
    public void Get_List(DocumentFile exact_Path,File folder_File){
        try{
            if(folder_File.exists() ) {
                File[] files = folder_File.listFiles();
                for (File file : files) {
                    if (!file.isDirectory()) {
                        if (!Selected_The_File(file) &&
                                Convert_The_LastMoified(file.lastModified()))
                            copyDocument(context, file, exact_Path);
                    } else {
                        Get_List(exact_Path.findFile("Sent"), file);
                        Get_List(exact_Path.findFile("Private"), file);
                    }

                }
            }
        }catch (Exception e){
        }
    }

    public String check_For_Duplicate(DocumentFile file , String name){
        DocumentFile[] Files = file.listFiles();
        for(DocumentFile files :Files){
            if(Objects.requireNonNull(files.getName()).equalsIgnoreCase(name)){
                return files.getName();
            }
        }
        return name;
    }

    public boolean isDuplicate(DocumentFile file, String name) {
        DocumentFile[] Files = file.listFiles();
        for (DocumentFile files : Files) {
            if (Objects.requireNonNull(files.getName()).equalsIgnoreCase(name)) {
                return true;
            }
        }
        return false;
    }
    public String Return_Path(int no){
        switch (no){
            case 1: return whatsapp_Path.substring(1,whatsapp_Path.length()-1)+" Audio";
            case 2: return whatsapp_Path.substring(1,whatsapp_Path.length()-1)+" Video";
            case 3: return whatsapp_Path.substring(1,whatsapp_Path.length()-1)+" Documents";
            case 4: return whatsapp_Path.substring(1,whatsapp_Path.length()-1)+" Images";
            case 5: return whatsapp_Path.substring(1,whatsapp_Path.length()-1)+" Animated Gifs";
            case 6: return "WallPaper";
            case 7: return whatsapp_Path.substring(1,whatsapp_Path.length()-1)+" Profile Photos";
            case 8: return ".Statuses";
            case 9: return whatsapp_Path.substring(1,whatsapp_Path.length()-1)+" Stickers";
            case 10:return whatsapp_Path.substring(1,whatsapp_Path.length()-1)+" Voice Notes";
            default:return"Databases";
        }
    }

    public boolean copyDocument(Context context, File file, DocumentFile dest) throws IOException {

        if (file.exists() && !file.isDirectory() && !isDuplicate(dest, file.getName())) {

            BufferedOutputStream bos = null;
            BufferedInputStream bis = null;
            byte[] data = new byte[BUFFER];
            int read ;
            try {

                String mimeType = getTypeForFile(file);
                String displayName = file.getName();
                DocumentFile destFile = dest.createFile(mimeType, displayName);

                int n = 0;
                while (destFile == null && n++ < 32) {
                    String destName = displayName + " (" + n + ")";
                    destFile = dest.createFile(mimeType, destName);
                }

                if (destFile == null) {
                    return false;
                }

                bos = new BufferedOutputStream(getOutputStream(context, destFile));
                bis = new BufferedInputStream(new FileInputStream(file));
                while ((read = bis.read(data, 0, BUFFER)) != -1) {
                    bos.write(data, 0, read);
                }

            } catch (Exception e) {
            } finally {
                //flush and close
                assert bos != null;
                bos.close();
                assert bis != null;
                bis.close();
            }
        }
        get_Data_Count++;
        if (!process.equals("Background"))
            after_main_transferFIle.getMultiThreading_task().onProgressUpdate();
        return true;
    }

    public static OutputStream getOutputStream(Context context, DocumentFile documentFile) throws FileNotFoundException {
        return context.getContentResolver().openOutputStream(documentFile.getUri());
    }

    public static String getTypeForFile(File file) {
        if (file.isDirectory()) {
            return DocumentsContract.Document.MIME_TYPE_DIR;
        } else {
            return getTypeForName(file.getName());
        }
    }

    public static String getTypeForName(String name) {
        final int lastDot = name.lastIndexOf('.');
        if (lastDot >= 0) {
            final String extension = name.substring(lastDot + 1).toLowerCase();
            final String mime = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
            if (mime != null) {
                return mime;
            }
        }

        return "image/";
    }



    public int getGet_Data_Count() {
        return get_Data_Count;
    }

    public boolean isCopy_Done() {
        return copy_Done;
    }

    public boolean Selected_The_File(File file){
        for (File data: only_Selected_File) {
            if (file.equals(data))
                return true;
        }

        return false;

    }
    public void Remove_DataBase_Other_Files(File database_File){
        try{
            List<File> files = new ArrayList<>(Arrays.asList(database_File.listFiles()));
            Convert_Into_Last_Modified(files);
            for (int i = files.size()-1 ; i >=1;i--){
                 new File(files.get(i).getAbsolutePath()).delete();
            }
        }catch (Exception e){
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


    public boolean isStop() {
        return stop;
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
