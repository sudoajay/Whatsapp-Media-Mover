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
import com.sudoajay.whatapp_media_mover_to_sdcard.Notification.NotifyNotification;
import com.sudoajay.whatapp_media_mover_to_sdcard.Storage_Info;
import com.sudoajay.whatapp_media_mover_to_sdcard.sharedPreferences.SdCardPathSharedPreference;
import com.sudoajay.whatapp_media_mover_to_sdcard.sharedPreferences.WhatsappPathSharedpreferences;

import java.io.File;
import java.util.ArrayList;
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
        int value=5;
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
                                    "Background", getApplicationContext());
                            copy_the_file.Copy_Folder_As_Per_Tick(View.VISIBLE, View.VISIBLE, View.VISIBLE, View.VISIBLE, View.VISIBLE
                                    , View.VISIBLE, View.VISIBLE, View.VISIBLE, View.VISIBLE);

                            Delete_The_File delete_the_fIle = new Delete_The_File(external_Path_Url, whats_App_Media_Path, after_mainTransferFIle,
                                    only_Selected_File, 0, "Background", getApplicationContext());
                            delete_the_fIle.get_File_Path(View.VISIBLE, View.VISIBLE, View.VISIBLE, View.VISIBLE, View.VISIBLE
                                    , View.VISIBLE, View.VISIBLE, View.VISIBLE, View.VISIBLE);
                            break;
                        case 2:
                            // copy process
                            after_mainTransferFIle.setWhich_Option_To_Do("move");
                            Copy_The_File copy_the_files = new Copy_The_File(external_Path_Url, whats_App_Media_Path, sd_Card_documentFile,
                                    after_mainTransferFIle, only_Selected_File, 0,
                                    "Background", getApplicationContext());
                            copy_the_files.Copy_Folder_As_Per_Tick(View.VISIBLE, View.VISIBLE, View.VISIBLE, View.VISIBLE, View.VISIBLE
                                    , View.VISIBLE, View.VISIBLE, View.VISIBLE, View.VISIBLE);
                            break;
                        case 3:
                            // remove Process

                            Delete_The_File delete_the_fIles = new Delete_The_File(external_Path_Url, whats_App_Media_Path, after_mainTransferFIle,
                                    only_Selected_File, 0, "Background", getApplicationContext());
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
        return Result.success();
    }
    private String GetType(int value){
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

