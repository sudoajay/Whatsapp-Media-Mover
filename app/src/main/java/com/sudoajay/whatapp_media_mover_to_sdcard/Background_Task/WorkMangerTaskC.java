package com.sudoajay.whatapp_media_mover_to_sdcard.Background_Task;


import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.provider.DocumentFile;
import android.view.View;

import com.sudoajay.whatapp_media_mover_to_sdcard.After_MainTransferFIle;
import com.sudoajay.whatapp_media_mover_to_sdcard.Copy_delete_File.Copy_The_File;
import com.sudoajay.whatapp_media_mover_to_sdcard.Copy_delete_File.Delete_The_File;
import com.sudoajay.whatapp_media_mover_to_sdcard.Copy_delete_File.Restore_The_Data;
import com.sudoajay.whatapp_media_mover_to_sdcard.Database_Classes.BackgroundTimerDataBase;
import com.sudoajay.whatapp_media_mover_to_sdcard.Main_Navigation;
import com.sudoajay.whatapp_media_mover_to_sdcard.Notification.NotifyNotification;
import com.sudoajay.whatapp_media_mover_to_sdcard.Storage_Info;
import com.sudoajay.whatapp_media_mover_to_sdcard.sharedPreferences.SdCardPathSharedPreference;
import com.sudoajay.whatapp_media_mover_to_sdcard.sharedPreferences.TraceBackgroundService;
import com.sudoajay.whatapp_media_mover_to_sdcard.sharedPreferences.WhatsappPathSharedpreferences;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class WorkMangerTaskC extends Worker {



    private Context context;
    public WorkMangerTaskC(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        this.context = context;
    }

    @NonNull
    @Override
    public Result doWork() {
            getWork(context);

        TraceBackgroundService traceBackgroundService = new TraceBackgroundService(getApplicationContext());
        traceBackgroundService.setBackgroundServiceWorking(true);



        return Result.success();
    }

    public static void getWork(final Context context){
        int value=5,hour = 0;
        try {
            // create Object
            After_MainTransferFIle after_mainTransferFIle = new After_MainTransferFIle();
            WhatsappPathSharedpreferences whatsappPathSharedpreferences = new WhatsappPathSharedpreferences(context);
            SdCardPathSharedPreference sdCardPathSharedPreference = new SdCardPathSharedPreference(context);
            BackgroundTimerDataBase backgroundTimerDataBase = new BackgroundTimerDataBase(context);


            if (!backgroundTimerDataBase.check_For_Empty()) {
                Cursor cursor = backgroundTimerDataBase.GetTheTypeFromId();
                if (cursor != null && cursor.moveToFirst()) {
                    cursor.moveToFirst();
                    switch (cursor.getInt(1)) {
                        case 0: // At Every 1/2 Day
                            hour = 12;
                            break;
                        case 1:// At Every 1 Day
                            hour = 24;
                            break;
                        case 2:
                            // At Every 2 Day
                            hour = (24 * 2);
                            break;
                        case 3:

                            Calendar calendar = Calendar.getInstance();
                            int currentDay = calendar.get(Calendar.DAY_OF_WEEK);

                            String weekdays = cursor.getString(1);
                            String[] splits = weekdays.split("");
                            List<Integer> listWeekdays = new ArrayList<>();
                            for (String ints : splits) {
                                listWeekdays.add(Integer.parseInt(ints));
                            }

                            hour = 24 * Main_Navigation.CountDay(currentDay, listWeekdays);

                            break;
                        case 4:  // At Every month(Same Date)
                            hour = (24 * 30);
                            break;
                    }
                    if(hour != 0) {
                        TraceBackgroundService traceBackgroundService = new TraceBackgroundService(context);
                        // set next date
                        traceBackgroundService.setTaskC(traceBackgroundService.NextDate(hour));
                    }
                    after_mainTransferFIle.UnneccesaryData();
                    after_mainTransferFIle.setStorage_Info(new Storage_Info(context));

                    // get data
                    String external_Path_Url = Environment.getExternalStorageDirectory().getAbsolutePath();
                    String sd_Card_Path_URL = sdCardPathSharedPreference.getSdCardPath();
                    DocumentFile sd_Card_documentFile = DocumentFile.fromTreeUri(context,
                            Uri.parse(sdCardPathSharedPreference.getStringURI()));
                    String whats_App_Media_Path = whatsappPathSharedpreferences.getWhats_App_Media_Path();

                    List<File> only_Selected_File = new ArrayList<>();

                    value = cursor.getInt(0);

                    switch (cursor.getInt(0)) {
                        case 1:
                            // Move process
                            Copy_The_File copy_the_file = new Copy_The_File(external_Path_Url, whats_App_Media_Path, sd_Card_documentFile,
                                    after_mainTransferFIle, only_Selected_File, 0,
                                    "Background", context);
                            copy_the_file.Copy_Folder_As_Per_Tick(View.VISIBLE, View.VISIBLE, View.VISIBLE, View.VISIBLE, View.VISIBLE
                                    , View.VISIBLE, View.VISIBLE, View.VISIBLE, View.VISIBLE);

                            Delete_The_File delete_the_fIle = new Delete_The_File(external_Path_Url, whats_App_Media_Path, after_mainTransferFIle,
                                    only_Selected_File, 0, "Background", context);
                            delete_the_fIle.get_File_Path(View.VISIBLE, View.VISIBLE, View.VISIBLE, View.VISIBLE, View.VISIBLE
                                    , View.VISIBLE, View.VISIBLE, View.VISIBLE, View.VISIBLE);
                            break;
                        case 2:
                            // copy process
                            after_mainTransferFIle.setWhich_Option_To_Do("move");
                            Copy_The_File copy_the_files = new Copy_The_File(external_Path_Url, whats_App_Media_Path, sd_Card_documentFile,
                                    after_mainTransferFIle, only_Selected_File, 0,
                                    "Background", context);
                            copy_the_files.Copy_Folder_As_Per_Tick(View.VISIBLE, View.VISIBLE, View.VISIBLE, View.VISIBLE, View.VISIBLE
                                    , View.VISIBLE, View.VISIBLE, View.VISIBLE, View.VISIBLE);
                            break;
                        case 3:
                            // remove Process

                            Delete_The_File delete_the_fIles = new Delete_The_File(external_Path_Url, whats_App_Media_Path, after_mainTransferFIle,
                                    only_Selected_File, 0, "Background", context);
                            delete_the_fIles.get_File_Path(View.VISIBLE, View.VISIBLE, View.VISIBLE, View.VISIBLE, View.VISIBLE
                                    , View.VISIBLE, View.VISIBLE, View.VISIBLE, View.VISIBLE);
                            break;
                        case 4:
                            // restore Process
                            Restore_The_Data restore_the_data = new Restore_The_Data(external_Path_Url, sd_Card_Path_URL, whats_App_Media_Path, after_mainTransferFIle, "Background");
                            restore_the_data.WhatsFolder_Checked();
                            restore_the_data.Restore_Folder_As_Per_Tick(View.VISIBLE, View.VISIBLE, View.VISIBLE, View.VISIBLE, View.VISIBLE
                                    , View.VISIBLE, View.VISIBLE, View.VISIBLE, View.VISIBLE);
                            break;
                        default:
                            backgroundTimerDataBase.deleteData(1 + "");
                            break;
                    }
                    if (cursor.getInt(0) != 0) {
                        // If Successfully Complete
                        NotifyNotification notify_notification = new NotifyNotification(context);
                        notify_notification.notify("Successfully Data " + GetType(value));
                    }
                }
            }

        }catch (Exception e){
            // If Error Complete
            NotifyNotification notify_notification = new NotifyNotification(context);
            notify_notification.notify( "Error on Data " + GetType(value));
        }
    }
    private static String GetType(int value){
        switch (value){
            case 0:
                return "Move";
            case 1:
                return "Copy";
            case 2:
                return "Remove";
            case 3:
                return "Restore";

        }
        return "background Process";
    }


}

