package com.sudoajay.whatapp_media_mover_to_sdcard;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by sudoajay on 3/21/18.
 */

public class Make_Changes  {
    private ArrayList<File> save_Data = new ArrayList<>();
    private ArrayList<Long> save = new ArrayList<>(),get_Common_Count=new ArrayList<>();
    private ArrayList<String> arrayList = new ArrayList<>();
    public Make_Changes(){

    }

    public Make_Changes(String path){
        Get_All_File(path);
    }
    public Make_Changes(String path1,String path2,String path3){
        Get_FIle_Recursive(new File(path1));
        Get_FIle_Recursive(new File(path2));
        Get_All_File(path3);
    }

    public void Get_All_File(String path){
        Get_FIle_Recursive(new File(path));

        Find_Last_Modified();

        Check_For_Commom_Date();
        for(Long a : save) {
            arrayList.add(a+"");
        }

    }
    public void Get_FIle_Recursive(File fileOrDirectory) {
        if (fileOrDirectory.isDirectory())
            for (File child : fileOrDirectory.listFiles())
                Get_FIle_Recursive(child);
        if(Check_For_Extension(fileOrDirectory.getAbsolutePath()))
            save_Data.add(fileOrDirectory);

    }


    public void Find_Last_Modified(){
                    long replace;
                    File rep;

                    for (File f : save_Data)
                        save.add(f.lastModified());

                    for(int j = 0 ; j< save.size(); j++ ){
                        for(int k = j ; k< save.size()-1; k++ ){
                            Date date1 = new Date(save.get(j));
                            Date date2 = new Date(save.get(k+1));
                            if(date1.compareTo(date2) < 0){
                                replace = save.get(j);
                                save.set(j,save.get(k+1));
                                save.set(k+1,replace);

                                rep = save_Data.get(j);
                                save_Data.set(j , save_Data.get(k+1));
                                save_Data.set(k+1,rep);
                            }
                        }
                    }
    }
    public boolean Check_For_Extension(String path){
        int i = path.lastIndexOf('.');
        String extension="";
        if (i > 0) {
            extension = path.substring(i+1);
        }
        return extension.equals("jpg") || extension.equals("mp3") || extension.equals("mp4")
                || extension.equals("pptx") || extension.equals("pdf") || extension.equals("docx")
                || extension.equals("opus") || extension.equals("jpeg") || extension.equals("txt")
                || extension.equals("m4a") || extension.equals("amr") || extension.equals("aac");

    }

    public void Check_For_Commom_Date(){
        int  count = 1;
        for (int i = 0 ; i <save.size() ; i++) {
            for (int j = i; j < save.size() - 1; j++) {

                Calendar calendar = Calendar.getInstance();
                calendar.setTime(new Date(save.get(i)));

                int last_Modified_Day = calendar.get(Calendar.DAY_OF_MONTH);
                int last_Modified_Months = calendar.get(Calendar.MONTH);

                Calendar calendar1 = Calendar.getInstance();
                calendar1.setTime(new Date(save.get(j + 1)));

                int last_Modified_Day1 = calendar1.get(Calendar.DAY_OF_MONTH);
                int last_Modified_Months1 = calendar1.get(Calendar.MONTH);
                if ((last_Modified_Months + 1) == (last_Modified_Months1 + 1)) {
                    if (last_Modified_Day == last_Modified_Day1) {
                        count++;
                        save.remove(j + 1);
                        j--;
                    }
                }
            }
            get_Common_Count.add((long)count);
            count =1;
        }
    }

    public ArrayList<File> getSave_Data() {
        return save_Data;
    }

    public ArrayList<Long> getSave() {
        return save;
    }

    public ArrayList<Long> getGet_Common_Count() {
        return get_Common_Count;
    }
}
