package com.sudoajay.whatsapp_media_mover_to_sdcard;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.provider.DocumentFile;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;

import com.sudoajay.whatsapp_media_mover_to_sdcard.Copy_delete_File.Copy_The_File;
import com.sudoajay.whatsapp_media_mover_to_sdcard.Copy_delete_File.Delete_The_File;
import com.sudoajay.whatsapp_media_mover_to_sdcard.Copy_delete_File.Restore_The_Data;
import com.sudoajay.whatsapp_media_mover_to_sdcard.Custom_Dialog.Custom_Dialog_For_Changes_Options;
import com.sudoajay.whatsapp_media_mover_to_sdcard.Custom_Dialog.Custom_Dialog_For_Normal_Changes;
import com.sudoajay.whatsapp_media_mover_to_sdcard.Custom_Dialog.Tabbed_Custom_Dialog_For_Deep;
import com.sudoajay.whatsapp_media_mover_to_sdcard.Permission.AndroidSdCardPermission;
import com.sudoajay.whatsapp_media_mover_to_sdcard.Permission.Notification_Permission_Check;
import com.sudoajay.whatsapp_media_mover_to_sdcard.Toast.CustomToast;
import com.sudoajay.whatsapp_media_mover_to_sdcard.sharedPreferences.TickOnButtonSharedPreference;
import com.sudoajay.whatsapp_media_mover_to_sdcard.sharedPreferences.WhatsappPathSharedpreferences;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

public class After_MainTransferFIle extends AppCompatActivity {
    private Toolbar toolbar;
    private TextView file_Size_TextView;
    private Button changes_Button, doit_Button;
    private ImageView refresh_Image_View;
    private ImageView tick_Voice_ImageView;
    private ImageView tick_Audio_ImageView;
    private ImageView tick_Video_ImageView;
    private ImageView tick_Sticker_ImageView;
    private ImageView tick_Document_ImageView;
    private ImageView tick_Image_ImageView;
    private ImageView tick_Profile_ImageView;
    private ImageView tick_Database_ImageView;
    private ImageView tick_Gif_ImageView;
    private String which_Option_To_Do;
    private String external_Path_Url;
    private String sd_Card_Path_URL;
    private String title_Notification = "Title";
    private String save_Exact_Size;
    private String after_Notification;
    private long get_File_Size;
    private boolean whats_App_File_Exist_Internal, checking_Folder, whats_App_File_Exist_External;
    private View layout;
    private Toast toast;
    private String whats_App_Media_Path;
    private Delete_The_File delete_the_fIle;
    private Copy_The_File copy_the_file;
    private Restore_The_Data restore_the_data;
    private DocumentFile sd_Card_documentFile;
    private Uri sd_Card_URL;
    private RemoteViews contentView;
    private MultiThreading_Task multiThreading_task = new MultiThreading_Task();
    private NotificationManager notificationManager;
    private Notification notification;
    private int count_Data, count_Folder;
    private List<File> only_Selected_File = new ArrayList<>();
    private boolean stop_The_Process;
    private Notification_Permission_Check notification_permission_check;
    private int normal_Changes = 0;
    private String whatsapp_Path, string_URI;
    private Storage_Info storage_Info;
    private TickOnButtonSharedPreference tickOnButtonSharedPreference;
    private AndroidSdCardPermission android_sdCard_permission;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_after_main_transfer_file_);
        which_Option_To_Do = getIntent().getStringExtra("move_copy_remove_restore");
        sd_Card_Path_URL = getIntent().getStringExtra("sd_card_path");
        Reference();
        File get_File_path = Environment.getExternalStorageDirectory();
        external_Path_Url = get_File_path.getAbsolutePath();

        // setup and instalization of sharedprefernece
        tickOnButtonSharedPreference = new TickOnButtonSharedPreference(getApplicationContext());
        int no = 0;
        while (no < 9) {
            if (!tickOnButtonSharedPreference.getTickArray(no)) {
                return_Id(no).setVisibility(View.GONE);
            }
            no++;
        }

        WhatsappPathSharedpreferences whatsappPathSharedpreferences = new WhatsappPathSharedpreferences(getApplicationContext());
        whatsapp_Path = whatsappPathSharedpreferences.getWhatsapp_Path();
        whats_App_Media_Path = whatsappPathSharedpreferences.getWhats_App_Media_Path();

        setSupportActionBar(toolbar);

        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
        LayoutInflater inflater = getLayoutInflater();
        layout = inflater.inflate(R.layout.activity_custom_toast,
                (ViewGroup) findViewById(R.id.toastcustom));
        Checked_For_Check_The_Icon();
        File_Size_Checked_The_Main_Thing();

    }

    public void Reference() {
        toolbar = findViewById(R.id.toolbar);
        TextView toolbar_Title = toolbar.findViewById(R.id.toolbar_title);

        file_Size_TextView = findViewById(R.id.file_Size_TextView);
        refresh_Image_View = toolbar.findViewById(R.id.refresh_Image_View);

        changes_Button = findViewById(R.id.normal_Changes_Button);
        doit_Button = findViewById(R.id.doit_Button);

        ImageView audio_ImageView = findViewById(R.id.audio_ImageView);


        tick_Audio_ImageView = findViewById(R.id.tick_audio_ImageView);
        tick_Video_ImageView = findViewById(R.id.tick_Video_ImageView);
        tick_Document_ImageView = findViewById(R.id.tick_Document_ImageView);
        tick_Image_ImageView = findViewById(R.id.tick_Image_ImageView);
        tick_Gif_ImageView = findViewById(R.id.tick_Gif_ImageView);
        tick_Database_ImageView = findViewById(R.id.tick_Database_ImageView);
        tick_Profile_ImageView = findViewById(R.id.tick_Profile_ImageView);
        tick_Voice_ImageView = findViewById(R.id.tick_Voice_ImageView);
        tick_Sticker_ImageView = findViewById(R.id.tick_Sticker_ImageView);
        ImageView back_Image_View = findViewById(R.id.back_Image_View);

        // create class object
        notification_permission_check = new Notification_Permission_Check(this, this);
        storage_Info = new Storage_Info(sd_Card_Path_URL, this);

        // create and instalization of sd card permission
        android_sdCard_permission = new AndroidSdCardPermission(After_MainTransferFIle.this
                , getApplicationContext(), this);
    }

    @SuppressLint("SetTextI18n")
    public void On_Click_Process(View view) {
        Checked_The_Main_Thing();
        String rating_link = "https://play.google.com/store/apps/details?id=com.sudoajay.whatsapp_media_mover";
        switch (view.getId()) {
            case R.id.back_Image_View:
                onBackPressed();
                break;
            case R.id.share_ImageView:
                Intent i = new Intent(android.content.Intent.ACTION_SEND);
                i.setType("text/plain");
                i.putExtra(android.content.Intent.EXTRA_SUBJECT, "Link-Share");
                i.putExtra(android.content.Intent.EXTRA_TEXT, "Check This App WhatsApp Media Mover (Easy Way To Move Your WhatsApp Data To Sd Card) - " + rating_link);
                startActivity(Intent.createChooser(i, "Share via"));
                break;
            case R.id.normal_Changes_Button:
                if (changes_Button.getAlpha() == 0.5 || (changes_Button.getAlpha() == 0.5 && tick_Database_ImageView.getVisibility() == View.VISIBLE))
                    Toast_It("You Supposed To Select Something");
                else {
                    Call_Custom_Dailog_Option_Changes();
                }
                break;
            case R.id.doit_Button:
                if (!whats_App_File_Exist_Internal && !which_Option_To_Do.equalsIgnoreCase("restore"))
                    Toast_It("No WhatsApp Data Present");
                else if (which_Option_To_Do.equalsIgnoreCase("restore") && !whats_App_File_Exist_External)
                    Toast_It("No WhatsApp Data Present");
                else if (doit_Button.getAlpha() == 0.6) {
                    Toast_It("Select The Sd Card");
                    android_sdCard_permission.call_Thread();
                } else if (doit_Button.getAlpha() == 0.5)
                    Toast_It("You Supposed To Select Something");
                else {
                    Save_In_Database();
                    if (!notification_permission_check.check_Notification_Permission()) {
                        notification_permission_check.Custom_AertDialog();
                    } else {
                        Send_Permission_To_Transfer();
                    }

                }
                break;
            case R.id.refresh_Image_View:
                if (refresh_Image_View.getRotation() % 360 == 0)
                    refresh_Image_View.animate().rotationBy(360f).setDuration(1000);
                break;
            case R.id.audio_TextView:
            case R.id.audio_ImageView:
                int visibility1 = tick_Audio_ImageView.getVisibility();
                if (!whats_App_File_Exist_Internal && !which_Option_To_Do.equalsIgnoreCase("restore"))
                    Toast_It("No WhatsApp Data Present");
                else if (which_Option_To_Do.equalsIgnoreCase("restore") && !whats_App_File_Exist_External)
                    Toast_It("No WhatsApp Data Present");
                else if (visibility1 == View.VISIBLE) {
                    tick_Audio_ImageView.setVisibility(View.GONE);
                    if (!which_Option_To_Do.equalsIgnoreCase("restore"))
                        get_File_Size -= storage_Info.getFileSizeInBytes(external_Path_Url + whats_App_Media_Path + "/" + whatsapp_Path.substring(1, whatsapp_Path.length() - 1) + " Audio");
                    else {
                        get_File_Size -= storage_Info.getFileSizeInBytes(sd_Card_Path_URL + whats_App_Media_Path + "/" + whatsapp_Path.substring(1, whatsapp_Path.length() - 1) + " Audio");
                    }
                } else {
                    tick_Audio_ImageView.setVisibility(View.VISIBLE);
                    if (!which_Option_To_Do.equalsIgnoreCase("restore"))
                        get_File_Size += storage_Info.getFileSizeInBytes(external_Path_Url + whats_App_Media_Path + "/" + whatsapp_Path.substring(1, whatsapp_Path.length() - 1) + " Audio");
                    else {
                        get_File_Size += storage_Info.getFileSizeInBytes(sd_Card_Path_URL + whats_App_Media_Path + "/" + whatsapp_Path.substring(1, whatsapp_Path.length() - 1) + " Audio");
                    }
                }
                break;
            case R.id.video_TextView:
            case R.id.video_ImageView:
                int visibility2 = tick_Video_ImageView.getVisibility();
                if (!whats_App_File_Exist_Internal && !which_Option_To_Do.equalsIgnoreCase("restore"))
                    Toast_It("No WhatsApp Data Present");
                else if (which_Option_To_Do.equalsIgnoreCase("restore") && !whats_App_File_Exist_External)
                    Toast_It("No WhatsApp Data Present");
                else if (visibility2 == View.VISIBLE) {
                    tick_Video_ImageView.setVisibility(View.GONE);
                    if (!which_Option_To_Do.equalsIgnoreCase("restore"))
                        get_File_Size -= storage_Info.getFileSizeInBytes(external_Path_Url + whats_App_Media_Path + "/" + whatsapp_Path.substring(1, whatsapp_Path.length() - 1) + " Video");
                    else {
                        get_File_Size -= storage_Info.getFileSizeInBytes(sd_Card_Path_URL + whats_App_Media_Path + "/" + whatsapp_Path.substring(1, whatsapp_Path.length() - 1) + " Video");
                    }

                } else {
                    tick_Video_ImageView.setVisibility(View.VISIBLE);
                    if (!which_Option_To_Do.equalsIgnoreCase("restore"))
                        get_File_Size += storage_Info.getFileSizeInBytes(external_Path_Url + whats_App_Media_Path + "/" + whatsapp_Path.substring(1, whatsapp_Path.length() - 1) + " Video");
                    else {
                        get_File_Size += storage_Info.getFileSizeInBytes(sd_Card_Path_URL + whats_App_Media_Path + "/" + whatsapp_Path.substring(1, whatsapp_Path.length() - 1) + " Video");
                    }
                }
                break;
            case R.id.document_TextView:
            case R.id.document_ImageView:
                int visibility3 = tick_Document_ImageView.getVisibility();
                if (!whats_App_File_Exist_Internal && !which_Option_To_Do.equalsIgnoreCase("restore"))
                    Toast_It("No WhatsApp Data Present");
                else if (which_Option_To_Do.equalsIgnoreCase("restore") && !whats_App_File_Exist_External)
                    Toast_It("No WhatsApp Data Present");
                else if (visibility3 == View.VISIBLE) {
                    tick_Document_ImageView.setVisibility(View.GONE);
                    if (!which_Option_To_Do.equalsIgnoreCase("restore"))
                        get_File_Size -= storage_Info.getFileSizeInBytes(external_Path_Url + whats_App_Media_Path + "/" + whatsapp_Path.substring(1, whatsapp_Path.length() - 1) + " Documents");
                    else {
                        get_File_Size -= storage_Info.getFileSizeInBytes(sd_Card_Path_URL + whats_App_Media_Path + "/" + whatsapp_Path.substring(1, whatsapp_Path.length() - 1) + " Documents");
                    }

                } else {
                    tick_Document_ImageView.setVisibility(View.VISIBLE);
                    if (!which_Option_To_Do.equalsIgnoreCase("restore"))
                        get_File_Size += storage_Info.getFileSizeInBytes(external_Path_Url + whats_App_Media_Path + "/" + whatsapp_Path.substring(1, whatsapp_Path.length() - 1) + " Documents");
                    else {
                        get_File_Size += storage_Info.getFileSizeInBytes(sd_Card_Path_URL + whats_App_Media_Path + "/" + whatsapp_Path.substring(1, whatsapp_Path.length() - 1) + " Documents");
                    }
                }
                break;
            case R.id.image_TextView:
            case R.id.image_ImageView:
                int visibility4 = tick_Image_ImageView.getVisibility();
                if (!whats_App_File_Exist_Internal && !which_Option_To_Do.equalsIgnoreCase("restore"))
                    Toast_It("No WhatsApp Data Present");
                else if (which_Option_To_Do.equalsIgnoreCase("restore") && !whats_App_File_Exist_External)
                    Toast_It("No WhatsApp Data Present");
                else if (visibility4 == View.VISIBLE) {
                    tick_Image_ImageView.setVisibility(View.GONE);
                    if (!which_Option_To_Do.equalsIgnoreCase("restore"))
                        get_File_Size -= storage_Info.getFileSizeInBytes(external_Path_Url + whats_App_Media_Path + "/" + whatsapp_Path.substring(1, whatsapp_Path.length() - 1) + " Images");
                    else {
                        get_File_Size -= storage_Info.getFileSizeInBytes(sd_Card_Path_URL + whats_App_Media_Path + "/" + whatsapp_Path.substring(1, whatsapp_Path.length() - 1) + " Images");
                    }

                } else {
                    tick_Image_ImageView.setVisibility(View.VISIBLE);
                    if (!which_Option_To_Do.equalsIgnoreCase("restore"))
                        get_File_Size += storage_Info.getFileSizeInBytes(external_Path_Url + whats_App_Media_Path + "/" + whatsapp_Path.substring(1, whatsapp_Path.length() - 1) + " Images");
                    else {
                        get_File_Size += storage_Info.getFileSizeInBytes(sd_Card_Path_URL + whats_App_Media_Path + "/" + whatsapp_Path.substring(1, whatsapp_Path.length() - 1) + " Images");
                    }
                }
                break;
            case R.id.gif_TextView:
            case R.id.gif_ImageView:
                int visibility5 = tick_Gif_ImageView.getVisibility();
                if (!whats_App_File_Exist_Internal && !which_Option_To_Do.equalsIgnoreCase("restore"))
                    Toast_It("No WhatsApp Data Present");
                else if (which_Option_To_Do.equalsIgnoreCase("restore") && !whats_App_File_Exist_External)
                    Toast_It("No WhatsApp Data Present");
                else if (visibility5 == View.VISIBLE) {
                    tick_Gif_ImageView.setVisibility(View.GONE);
                    if (!which_Option_To_Do.equalsIgnoreCase("restore"))
                        get_File_Size -= storage_Info.getFileSizeInBytes(external_Path_Url + whats_App_Media_Path + "/" + whatsapp_Path.substring(1, whatsapp_Path.length() - 1) + " Animated Gifs");
                    else {
                        get_File_Size -= storage_Info.getFileSizeInBytes(sd_Card_Path_URL + whats_App_Media_Path + "/" + whatsapp_Path.substring(1, whatsapp_Path.length() - 1) + " Animated Gifs");
                    }

                } else {
                    tick_Gif_ImageView.setVisibility(View.VISIBLE);
                    if (!which_Option_To_Do.equalsIgnoreCase("restore"))
                        get_File_Size += storage_Info.getFileSizeInBytes(external_Path_Url + whats_App_Media_Path + "/" + whatsapp_Path.substring(1, whatsapp_Path.length() - 1) + " Animated Gifs");
                    else {
                        get_File_Size += storage_Info.getFileSizeInBytes(sd_Card_Path_URL + whats_App_Media_Path + "/" + whatsapp_Path.substring(1, whatsapp_Path.length() - 1) + " Animated Gifs");
                    }
                }
                break;
            case R.id.voice_TextView:
            case R.id.voice_ImageView:
                int visibility6 = tick_Voice_ImageView.getVisibility();
                if (!whats_App_File_Exist_Internal && !which_Option_To_Do.equalsIgnoreCase("restore"))
                    Toast_It("No WhatsApp Data Present");
                else if (which_Option_To_Do.equalsIgnoreCase("restore") && !whats_App_File_Exist_External)
                    Toast_It("No WhatsApp Data Present");
                else if (visibility6 == View.VISIBLE) {
                    tick_Voice_ImageView.setVisibility(View.GONE);
                    if (!which_Option_To_Do.equalsIgnoreCase("restore"))
                        get_File_Size -= storage_Info.getFileSizeInBytes(external_Path_Url + whats_App_Media_Path + "/" + whatsapp_Path.substring(1, whatsapp_Path.length() - 1) + " Voice Notes");
                    else {
                        get_File_Size -= storage_Info.getFileSizeInBytes(sd_Card_Path_URL + whats_App_Media_Path + "/" + whatsapp_Path.substring(1, whatsapp_Path.length() - 1) + " Voice Notes");
                    }
                } else {
                    tick_Voice_ImageView.setVisibility(View.VISIBLE);
                    if (!which_Option_To_Do.equalsIgnoreCase("restore"))
                        get_File_Size += storage_Info.getFileSizeInBytes(external_Path_Url + whats_App_Media_Path + "/" + whatsapp_Path.substring(1, whatsapp_Path.length() - 1) + " Voice Notes");
                    else {
                        get_File_Size += storage_Info.getFileSizeInBytes(sd_Card_Path_URL + whats_App_Media_Path + "/" + whatsapp_Path.substring(1, whatsapp_Path.length() - 1) + " Voice Notes");
                    }
                }
                break;
            case R.id.profile_TextView:
            case R.id.profile_ImageView:
                int visibility7 = tick_Profile_ImageView.getVisibility();
                if (!whats_App_File_Exist_Internal && !which_Option_To_Do.equalsIgnoreCase("restore"))
                    Toast_It("No WhatsApp Data Present");
                else if (which_Option_To_Do.equalsIgnoreCase("restore") && !whats_App_File_Exist_External)
                    Toast_It("No WhatsApp Data Present");
                else if (visibility7 == View.VISIBLE) {
                    tick_Profile_ImageView.setVisibility(View.GONE);

                    if (!which_Option_To_Do.equalsIgnoreCase("restore")) {
                        get_File_Size -= storage_Info.getFileSizeInBytes(external_Path_Url + whats_App_Media_Path + "/" + whatsapp_Path.substring(1, whatsapp_Path.length() - 1) + " Profile Photos");
                        get_File_Size -= storage_Info.getFileSizeInBytes(external_Path_Url + whats_App_Media_Path + "/.Statuses");
                        get_File_Size -= storage_Info.getFileSizeInBytes(external_Path_Url + whats_App_Media_Path + "/WallPaper");
                    } else {
                        get_File_Size -= storage_Info.getFileSizeInBytes(sd_Card_Path_URL + whats_App_Media_Path + "/" + whatsapp_Path.substring(1, whatsapp_Path.length() - 1) + " Profile Photos");
                        get_File_Size -= storage_Info.getFileSizeInBytes(sd_Card_Path_URL + whats_App_Media_Path + "/.Statuses");
                        get_File_Size -= storage_Info.getFileSizeInBytes(sd_Card_Path_URL + whats_App_Media_Path + "/WallPaper");
                    }
                } else {

                    tick_Profile_ImageView.setVisibility(View.VISIBLE);
                    if (!which_Option_To_Do.equalsIgnoreCase("restore")) {
                        get_File_Size += storage_Info.getFileSizeInBytes(external_Path_Url + whats_App_Media_Path + "/" + whatsapp_Path.substring(1, whatsapp_Path.length() - 1) + " Profile Photos");
                        get_File_Size += storage_Info.getFileSizeInBytes(external_Path_Url + whats_App_Media_Path + "/.Statuses");
                        get_File_Size += storage_Info.getFileSizeInBytes(external_Path_Url + whats_App_Media_Path + "/WallPaper");
                    } else {
                        get_File_Size += storage_Info.getFileSizeInBytes(sd_Card_Path_URL + whats_App_Media_Path + "/WallPaper");
                        get_File_Size += storage_Info.getFileSizeInBytes(sd_Card_Path_URL + whats_App_Media_Path + "/" + whatsapp_Path.substring(1, whatsapp_Path.length() - 1) + " Profile Photos");
                        get_File_Size += storage_Info.getFileSizeInBytes(sd_Card_Path_URL + whats_App_Media_Path + "/.Statuses");
                    }
                }
                break;
            case R.id.sticker_TextView:
            case R.id.sticker_ImageView:
                int visibility8 = tick_Sticker_ImageView.getVisibility();
                if (!whats_App_File_Exist_Internal && !which_Option_To_Do.equalsIgnoreCase("restore"))
                    Toast_It("No WhatsApp Data Present");
                else if (which_Option_To_Do.equalsIgnoreCase("restore") && !whats_App_File_Exist_External)
                    Toast_It("No WhatsApp Data Present");
                else if (visibility8 == View.VISIBLE) {
                    tick_Sticker_ImageView.setVisibility(View.GONE);
                    if (!which_Option_To_Do.equalsIgnoreCase("restore"))
                        get_File_Size -= storage_Info.getFileSizeInBytes(external_Path_Url + whats_App_Media_Path + "/" + whatsapp_Path.substring(1, whatsapp_Path.length() - 1) + " Stickers");
                    else {
                        get_File_Size -= storage_Info.getFileSizeInBytes(sd_Card_Path_URL + whats_App_Media_Path + "/" + whatsapp_Path.substring(1, whatsapp_Path.length() - 1) + " Stickers");
                    }

                } else {
                    tick_Sticker_ImageView.setVisibility(View.VISIBLE);
                    if (!which_Option_To_Do.equalsIgnoreCase("restore"))
                        get_File_Size += storage_Info.getFileSizeInBytes(external_Path_Url + whats_App_Media_Path + "/" + whatsapp_Path.substring(1, whatsapp_Path.length() - 1) + " Stickers");
                    else {
                        get_File_Size += storage_Info.getFileSizeInBytes(sd_Card_Path_URL + whats_App_Media_Path + "/" + whatsapp_Path.substring(1, whatsapp_Path.length() - 1) + " Stickers");
                    }
                }
                break;
            case R.id.database_TextView:
            case R.id.database_ImageView:
                int visibility9 = tick_Database_ImageView.getVisibility();
                if (!whats_App_File_Exist_Internal && !which_Option_To_Do.equalsIgnoreCase("restore"))
                    Toast_It("No WhatsApp Data Present");
                else if (which_Option_To_Do.equalsIgnoreCase("restore") && !whats_App_File_Exist_External)
                    Toast_It("No WhatsApp Data Present");
                else if (visibility9 == View.VISIBLE) {
                    tick_Database_ImageView.setVisibility(View.GONE);
                    if (!which_Option_To_Do.equalsIgnoreCase("restore")) {
                        get_File_Size -= storage_Info.getFileSizeInBytes(external_Path_Url + whatsapp_Path + "Databases");
                        get_File_Size -= storage_Info.getFileSizeInBytes(external_Path_Url + whatsapp_Path + ".Shared");
                        get_File_Size -= storage_Info.getFileSizeInBytes(external_Path_Url + whatsapp_Path + ".Trash");
                        get_File_Size -= storage_Info.getFileSizeInBytes(external_Path_Url + whatsapp_Path + "cache");
                        get_File_Size -= storage_Info.getFileSizeInBytes(external_Path_Url + whatsapp_Path + "Theme");
                    } else {
                        get_File_Size -= storage_Info.getFileSizeInBytes(sd_Card_Path_URL + whatsapp_Path + "Databases");
                    }

                } else {
                    tick_Database_ImageView.setVisibility(View.VISIBLE);
                    if (!which_Option_To_Do.equalsIgnoreCase("restore")) {
                        get_File_Size += storage_Info.getFileSizeInBytes(external_Path_Url + whatsapp_Path + "Databases");
                        get_File_Size += storage_Info.getFileSizeInBytes(external_Path_Url + whatsapp_Path + ".Shared");
                        get_File_Size += storage_Info.getFileSizeInBytes(external_Path_Url + whatsapp_Path + ".Trash");
                        get_File_Size += storage_Info.getFileSizeInBytes(external_Path_Url + whatsapp_Path + "cache");
                        get_File_Size += storage_Info.getFileSizeInBytes(external_Path_Url + whatsapp_Path + "Theme");
                    } else {
                        get_File_Size += storage_Info.getFileSizeInBytes(sd_Card_Path_URL + whatsapp_Path + "Databases");
                    }
                }
                break;
        }
        Checked_For_Check_The_Icon();
        file_Size_TextView.setText("Data Size - " + storage_Info.Convert_It(get_File_Size));
    }

    public void Checked_The_Main_Thing() {
        whats_App_File_Exist_Internal = new File(external_Path_Url + whats_App_Media_Path).exists();
        File file1 = new File(sd_Card_Path_URL);
        if (!which_Option_To_Do.equalsIgnoreCase("remove")) {

            if (!file1.exists() || isSamePath()) {
                doit_Button.animate().alpha(0.6f);
            }
        }

        whats_App_File_Exist_External = (new File(sd_Card_Path_URL + whats_App_Media_Path)).exists();
    }


    public void Checked_For_Check_The_Icon() {
        if (tick_Audio_ImageView.getVisibility() == View.VISIBLE || tick_Video_ImageView.getVisibility() == View.VISIBLE ||
                tick_Document_ImageView.getVisibility() == View.VISIBLE || tick_Image_ImageView.getVisibility() == View.VISIBLE ||
                tick_Gif_ImageView.getVisibility() == View.VISIBLE ||
                tick_Profile_ImageView.getVisibility() == View.VISIBLE || tick_Sticker_ImageView.getVisibility() == View.VISIBLE ||
                tick_Voice_ImageView.getVisibility() == View.VISIBLE) {
            doit_Button.animate().alpha(1f);
            changes_Button.animate().alpha(1f);

        } else {
            if (tick_Database_ImageView.getVisibility() == View.GONE)
                doit_Button.animate().alpha(0.5f);
            else {
                doit_Button.animate().alpha(1f);
            }
            changes_Button.animate().alpha(0.5f);


        }

    }

    public boolean onCreateOptionsMenu(Menu menu) {
// Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_bar, menu);
        return true;
    }

    // Activity's overrided method used to perform click events on menu items
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    public void Toast_It(String message) {
        TextView toast_TextView = layout.findViewById(R.id.text);
        if (toast == null || toast.getView().getWindowVisibility() != View.VISIBLE) {
            toast = new Toast(getApplicationContext());
            toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
            toast.setDuration(Toast.LENGTH_LONG);
            toast.setView(layout);
            toast_TextView.setText(message);
            toast.show();
        } else {
            toast.cancel();
        }

    }

    public ImageView return_Id(int no) {
        switch (no) {
            case 1:
                return tick_Audio_ImageView;
            case 2:
                return tick_Video_ImageView;
            case 3:
                return tick_Document_ImageView;
            case 4:
                return tick_Image_ImageView;
            case 5:
                return tick_Gif_ImageView;
            case 6:
                return tick_Voice_ImageView;
            case 7:
                return tick_Profile_ImageView;
            case 8:
                return tick_Sticker_ImageView;
            default:
                return tick_Database_ImageView;
        }
    }

    public void Save_In_Database() {
        int no = 0;
        boolean get[] = new boolean[9];
        while (no < 9) {
            get[no] = return_Id(no).getVisibility() == View.VISIBLE;
            no++;
        }
        tickOnButtonSharedPreference.setTickArray(get);

    }

    @SuppressLint("SetTextI18n")
    public void File_Size_Checked_The_Main_Thing() {
        Checked_The_Main_Thing();
        if (whats_App_File_Exist_Internal && !which_Option_To_Do.equalsIgnoreCase("Restore")) {
            if (tick_Audio_ImageView.getVisibility() == View.VISIBLE)
                get_File_Size += storage_Info.getFileSizeInBytes(external_Path_Url + whats_App_Media_Path + "/" + whatsapp_Path.substring(1, whatsapp_Path.length() - 1) + " Audio");
            if (tick_Video_ImageView.getVisibility() == View.VISIBLE)
                get_File_Size += storage_Info.getFileSizeInBytes(external_Path_Url + whats_App_Media_Path + "/" + whatsapp_Path.substring(1, whatsapp_Path.length() - 1) + " Video");
            if (tick_Document_ImageView.getVisibility() == View.VISIBLE)
                get_File_Size += storage_Info.getFileSizeInBytes(external_Path_Url + whats_App_Media_Path + "/" + whatsapp_Path.substring(1, whatsapp_Path.length() - 1) + " Documents");
            if (tick_Image_ImageView.getVisibility() == View.VISIBLE)
                get_File_Size += storage_Info.getFileSizeInBytes(external_Path_Url + whats_App_Media_Path + "/" + whatsapp_Path.substring(1, whatsapp_Path.length() - 1) + " Images");
            if (tick_Gif_ImageView.getVisibility() == View.VISIBLE)
                get_File_Size += storage_Info.getFileSizeInBytes(external_Path_Url + whats_App_Media_Path + "/" + whatsapp_Path.substring(1, whatsapp_Path.length() - 1) + " Animated Gifs");
            if (tick_Voice_ImageView.getVisibility() == View.VISIBLE)
                get_File_Size += storage_Info.getFileSizeInBytes(external_Path_Url + whats_App_Media_Path + "/" + whatsapp_Path.substring(1, whatsapp_Path.length() - 1) + " Voice Notes");
            if (tick_Profile_ImageView.getVisibility() == View.VISIBLE) {
                get_File_Size += storage_Info.getFileSizeInBytes(external_Path_Url + whats_App_Media_Path + "/" + whatsapp_Path.substring(1, whatsapp_Path.length() - 1) + " Profile Photos");
                get_File_Size += storage_Info.getFileSizeInBytes(external_Path_Url + whats_App_Media_Path + "/.Statuses");
                get_File_Size += storage_Info.getFileSizeInBytes(external_Path_Url + whats_App_Media_Path + "/WallPaper");

            }
            if (tick_Sticker_ImageView.getVisibility() == View.VISIBLE)
                get_File_Size += storage_Info.getFileSizeInBytes(external_Path_Url + whats_App_Media_Path + "/" + whatsapp_Path.substring(1, whatsapp_Path.length() - 1) + " Stickers");
            if (tick_Database_ImageView.getVisibility() == View.VISIBLE) {
                get_File_Size += storage_Info.getFileSizeInBytes(external_Path_Url + whatsapp_Path + "Databases");
                get_File_Size += storage_Info.getFileSizeInBytes(external_Path_Url + whatsapp_Path + ".Shared");
                get_File_Size += storage_Info.getFileSizeInBytes(external_Path_Url + whatsapp_Path + ".Trash");
                get_File_Size += storage_Info.getFileSizeInBytes(external_Path_Url + whatsapp_Path + "cache");
                get_File_Size += storage_Info.getFileSizeInBytes(external_Path_Url + whatsapp_Path + "Theme");

            }

            file_Size_TextView.setText("Data Size - " + storage_Info.Convert_It(get_File_Size));

        } else {
            if (tick_Audio_ImageView.getVisibility() == View.VISIBLE)
                get_File_Size += storage_Info.getFileSizeInBytes(sd_Card_Path_URL + whats_App_Media_Path + "/" + whatsapp_Path.substring(1, whatsapp_Path.length() - 1) + " Audio");
            if (tick_Video_ImageView.getVisibility() == View.VISIBLE)
                get_File_Size += storage_Info.getFileSizeInBytes(sd_Card_Path_URL + whats_App_Media_Path + "/" + whatsapp_Path.substring(1, whatsapp_Path.length() - 1) + " Video");
            if (tick_Document_ImageView.getVisibility() == View.VISIBLE)
                get_File_Size += storage_Info.getFileSizeInBytes(sd_Card_Path_URL + whats_App_Media_Path + "/" + whatsapp_Path.substring(1, whatsapp_Path.length() - 1) + " Documents");
            if (tick_Image_ImageView.getVisibility() == View.VISIBLE)
                get_File_Size += storage_Info.getFileSizeInBytes(sd_Card_Path_URL + whats_App_Media_Path + "/" + whatsapp_Path.substring(1, whatsapp_Path.length() - 1) + " Images");
            if (tick_Gif_ImageView.getVisibility() == View.VISIBLE)
                get_File_Size += storage_Info.getFileSizeInBytes(sd_Card_Path_URL + whats_App_Media_Path + "/" + whatsapp_Path.substring(1, whatsapp_Path.length() - 1) + " Animated Gifs");
            if (tick_Voice_ImageView.getVisibility() == View.VISIBLE)
                get_File_Size += storage_Info.getFileSizeInBytes(sd_Card_Path_URL + whats_App_Media_Path + "/" + whatsapp_Path.substring(1, whatsapp_Path.length() - 1) + " Voice Notes");
            if (tick_Profile_ImageView.getVisibility() == View.VISIBLE) {
                get_File_Size += storage_Info.getFileSizeInBytes(sd_Card_Path_URL + whats_App_Media_Path + "/" + whatsapp_Path.substring(1, whatsapp_Path.length() - 1) + " Profile Photos");
                get_File_Size += storage_Info.getFileSizeInBytes(sd_Card_Path_URL + whats_App_Media_Path + "/.Statuses");
                get_File_Size += storage_Info.getFileSizeInBytes(sd_Card_Path_URL + whats_App_Media_Path + "/WallPaper");
            }
            if (tick_Sticker_ImageView.getVisibility() == View.VISIBLE)
                get_File_Size += storage_Info.getFileSizeInBytes(sd_Card_Path_URL + whats_App_Media_Path + "/" + whatsapp_Path.substring(1, whatsapp_Path.length() - 1) + " Stickers");
            if (tick_Database_ImageView.getVisibility() == View.VISIBLE)
                get_File_Size += storage_Info.getFileSizeInBytes(sd_Card_Path_URL + whatsapp_Path + "Databases");
            file_Size_TextView.setText("Data Size - " + storage_Info.Convert_It(get_File_Size));
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent resultData) {
        if (resultCode != RESULT_OK)
            return;

        sd_Card_URL = resultData.getData();
        grantUriPermission(getPackageName(), sd_Card_URL, Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        getContentResolver().takePersistableUriPermission(sd_Card_URL, Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        sd_Card_Path_URL = SdCardPath.getFullPathFromTreeUri(sd_Card_URL, this);

        if (new File(sd_Card_Path_URL).exists()) string_URI = Split_The_URI(sd_Card_URL.toString());
        android_sdCard_permission.setSd_Card_Path_URL(sd_Card_Path_URL);
        android_sdCard_permission.setString_URI(string_URI);

        File file = new File(sd_Card_Path_URL);
        if (!isSamePath() && file.exists()) {
            sd_Card_Path_URL = Split_The_URI(sd_Card_URL.toString());
            sd_Card_URL = Uri.parse(sd_Card_Path_URL);
            Check_For_WhatsApp_Folder();
        }

    }

    public String Split_The_URI(String url) {
        String save[] = url.split("%3A");
        return save[0] + "%3A";
    }

    public String check_For_Duplicate(DocumentFile file, String name) {
        DocumentFile[] Files = file.listFiles();
        for (DocumentFile files : Files) {
            if (Objects.requireNonNull(files.getName()).equalsIgnoreCase(name)) {
                return files.getName();
            }
        }
        return name;
    }

    public void Check_For_WhatsApp_Folder() {
        try {
            sd_Card_documentFile = DocumentFile.fromTreeUri(this, sd_Card_URL);
            while (!Get_Path()) {
            }
        } catch (Exception e) {
            Log.e("Exceptionaaaasdasdasa ", e.toString());
        }
    }

    public boolean Get_Path() {

        DocumentFile whatsApp_dir = sd_Card_documentFile.findFile(check_For_Duplicate(sd_Card_documentFile, (whatsapp_Path.substring(1, whatsapp_Path.length() - 1))));
        try {
            if (whatsApp_dir == null) {
                sd_Card_documentFile.createDirectory(whatsapp_Path.substring(1, whatsapp_Path.length() - 1));
                return false;
            } else {
                count_Folder = 1;
                multiThreading_task.onProgressUpdate();
            }
            DocumentFile database_dir = whatsApp_dir.findFile(check_For_Duplicate(whatsApp_dir, "Databases"));
            if (database_dir == null) {
                whatsApp_dir.createDirectory("Databases");
                return false;
            } else {
                count_Folder = 2;
                multiThreading_task.onProgressUpdate();
            }
            DocumentFile media_dir = whatsApp_dir.findFile(check_For_Duplicate(whatsApp_dir, "Media"));
            if (media_dir == null) {
                whatsApp_dir.createDirectory("Media");
                return false;
            } else {
                count_Folder = 3;
                multiThreading_task.onProgressUpdate();
            }

            for (int i = 1; i <= 10; i++) {
                if (!make_SubFolder(Return_Path(i), media_dir))
                    return false;
                else {
                    count_Folder = i + 3;
                    multiThreading_task.onProgressUpdate();
                }
            }
        } catch (Exception e) {
            Log.e("Exceptionaaaa ", e.toString());
        }
        return true;
    }

    public boolean make_SubFolder(String subFolder, DocumentFile childFile) {
        DocumentFile media_SubFolder_File, media_SubsFolder_File;
        try {
            media_SubFolder_File = childFile.findFile(check_For_Duplicate(childFile, subFolder));
            if (media_SubFolder_File == null) {
                childFile.createDirectory(subFolder);
                return false;
            }
            media_SubsFolder_File = media_SubFolder_File.findFile(check_For_Duplicate(media_SubFolder_File, "Sent"));

            if (media_SubsFolder_File == null) {
                media_SubFolder_File.createDirectory("Sent");

            }
            media_SubsFolder_File = media_SubFolder_File.findFile(check_For_Duplicate(media_SubFolder_File, "Private"));

            if (media_SubsFolder_File == null) {
                media_SubFolder_File.createDirectory("Private");

            }


        } catch (Exception e) {
            Log.e("Exceptionasdasd ", e.getMessage());
        }
        return true;
    }

    public boolean isSamePath() {
        return external_Path_Url.equals(sd_Card_Path_URL);
    }

    public String Return_Path(int no) {
        switch (no) {
            case 1:
                return whatsapp_Path.substring(1, whatsapp_Path.length() - 1) + " Audio";
            case 2:
                return whatsapp_Path.substring(1, whatsapp_Path.length() - 1) + " Video";
            case 3:
                return whatsapp_Path.substring(1, whatsapp_Path.length() - 1) + " Documents";
            case 4:
                return whatsapp_Path.substring(1, whatsapp_Path.length() - 1) + " Images";
            case 5:
                return whatsapp_Path.substring(1, whatsapp_Path.length() - 1) + " Animated Gifs";
            case 6:
                return "WallPaper";
            case 7:
                return whatsapp_Path.substring(1, whatsapp_Path.length() - 1) + " Profile Photos";
            case 8:
                return ".Statuses";
            case 9:
                return whatsapp_Path.substring(1, whatsapp_Path.length() - 1) + " Stickers";
            case 10:
                return whatsapp_Path.substring(1, whatsapp_Path.length() - 1) + " Voice Notes";
            default:
                return "Databases";
        }
    }

    public void Call_Custom_Dailog(String Message) {

        final Dialog dialog = new Dialog(this);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.activity_custom_dialog);
        TextView text_Message = dialog.findViewById(R.id.text_Message);
        text_Message.setText(Message);
        TextView button_No = dialog.findViewById(R.id.button_No);
        TextView button_Yes = dialog.findViewById(R.id.button_Yes);
        // if button is clicked, close the custom dialog

        button_Yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (which_Option_To_Do.equals("remove"))
                    CustomToast.ToastIt(getApplicationContext(),"Removing....");
                 else if (which_Option_To_Do.equals("copy"))
                    CustomToast.ToastIt(getApplicationContext(),"Copying....");
                 else if (which_Option_To_Do.equals("move"))
                    CustomToast.ToastIt(getApplicationContext(),"Moving....");
                 else {
                    CustomToast.ToastIt(getApplicationContext(),"Restoring....");
                }
                Push_Back();
                multiThreading_task.execute();


                dialog.dismiss();
            }
        });
        button_No.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public void Copy_The_File() {
        sd_Card_URL = Uri.parse(getIntent().getStringExtra("Sd_Card_URI"));


        Notification();
        Check_For_WhatsApp_Folder();
        transFer_File_Changes();
        checking_Folder = false;
        copy_the_file = new Copy_The_File(external_Path_Url, whats_App_Media_Path, sd_Card_documentFile,
                this, only_Selected_File, convert_To_Days(), "No Background", getApplicationContext());
        copy_the_file.Copy_Folder_As_Per_Tick(tick_Database_ImageView.getVisibility(), tick_Audio_ImageView.getVisibility(), tick_Video_ImageView.getVisibility(),
                tick_Document_ImageView.getVisibility(), tick_Image_ImageView.getVisibility(),
                tick_Gif_ImageView.getVisibility(), tick_Voice_ImageView.getVisibility(),
                tick_Profile_ImageView.getVisibility(), tick_Sticker_ImageView.getVisibility());
        stop_The_Process = copy_the_file.isStop();

    }

    public void Remove_The_File() {
        checking_Folder = false;
        if (!stop_The_Process) {
            delete_the_fIle = new Delete_The_File(external_Path_Url, whats_App_Media_Path, this,
                    only_Selected_File, convert_To_Days(), "No Background", getApplicationContext());
            delete_the_fIle.get_File_Path(tick_Database_ImageView.getVisibility(), tick_Audio_ImageView.getVisibility(), tick_Video_ImageView.getVisibility(),
                    tick_Document_ImageView.getVisibility(), tick_Image_ImageView.getVisibility(),
                    tick_Gif_ImageView.getVisibility(), tick_Voice_ImageView.getVisibility(),
                    tick_Profile_ImageView.getVisibility(), tick_Sticker_ImageView.getVisibility());
        } else {
            default_Notification(2);
        }
    }

    public void Restore_The_Data() {

        restore_the_data = new Restore_The_Data(external_Path_Url, sd_Card_Path_URL, whats_App_Media_Path, this, "No Background");
        Notification();
        restore_the_data.WhatsFolder_Checked();
        checking_Folder = false;

        restore_the_data.Restore_Folder_As_Per_Tick(tick_Database_ImageView.getVisibility(), tick_Audio_ImageView.getVisibility(), tick_Video_ImageView.getVisibility(),
                tick_Document_ImageView.getVisibility(), tick_Image_ImageView.getVisibility(),
                tick_Gif_ImageView.getVisibility(), tick_Voice_ImageView.getVisibility(),
                tick_Profile_ImageView.getVisibility(), tick_Sticker_ImageView.getVisibility());
        stop_The_Process = restore_the_data.isStop();
        if (stop_The_Process) default_Notification(2);

    }


    @SuppressLint("StaticFieldLeak")
    public class MultiThreading_Task extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            count_Data = Count_The_Data();
            checking_Folder = true;
            UnneccesaryData();
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            if (which_Option_To_Do.equals("remove")) {
                Notification();
                transFer_File_Changes();
                Remove_The_File();
            } else if (which_Option_To_Do.equals("copy")) {
                Copy_The_File();
            } else if (which_Option_To_Do.equals("move")) {
                Copy_The_File();
                Remove_The_File();
            } else {
                Restore_The_Data();
            }

            return null;

        }

        public void onProgressUpdate(String... values) {

            if (!checking_Folder) {
                String size = "";
                int exact_Data = 0;
                int percentage = 00;
                try {
                    if (which_Option_To_Do.equals("remove")) {
                        size = storage_Info.Convert_It(delete_the_fIle.getGetSize());
                        exact_Data = delete_the_fIle.getGet_Data_Count();
                        percentage = ((exact_Data * 100) / count_Data);
                    } else if (which_Option_To_Do.equals("copy")) {


                        size = storage_Info.Convert_It(copy_the_file.getGetSize());
                        exact_Data = copy_the_file.getGet_Data_Count();
                        percentage = ((exact_Data * 100) / count_Data);
                    } else if (which_Option_To_Do.equals("move")) {
                        size = storage_Info.Convert_It(copy_the_file.getGetSize());
                        exact_Data = copy_the_file.getGet_Data_Count();
                        percentage = ((exact_Data * 100) / count_Data);
                        if (copy_the_file.isCopy_Done()) {
                            size = storage_Info.Convert_It(delete_the_fIle.getGetSize());
                            exact_Data = delete_the_fIle.getGet_Data_Count();
                            percentage = ((exact_Data * 100) / count_Data);
                        }
                    } else {
                        size = storage_Info.Convert_It(restore_the_data.getGetSize());
                        exact_Data = restore_the_data.getGet_Data_Count();
                        percentage = ((exact_Data * 100) / count_Data);
                    }

                } catch (ArithmeticException e) {
                    percentage = 100;

                } catch (Exception f) {
                    Log.d("Exceptionsasas", f.getStackTrace() + "");
                }
                contentView.setTextViewText(R.id.size_Title, size + "/" + save_Exact_Size);
                contentView.setProgressBar(R.id.progressBar, count_Data, exact_Data, false);
                contentView.setTextViewText(R.id.percent_Text, percentage + "%");

            } else if (which_Option_To_Do.equals("restore")) {
                contentView.setProgressBar(R.id.progressBar, 11, restore_the_data.getGet_Data_Count(), false);
                contentView.setTextViewText(R.id.percent_Text, ((restore_the_data.getGet_Data_Count() * 100) / 11) + "%");
            } else {

                contentView.setProgressBar(R.id.progressBar, 13, count_Folder, false);
                contentView.setTextViewText(R.id.percent_Text, ((count_Folder * 100) / 13) + "%");
            }
            contentView.setTextViewText(R.id.time_Tittle, get_Current_Time());
            notificationManager.notify(1, notification);
        }

        protected void onPostExecute(String s) {
            try {
                Thread.sleep(2000);
            } catch (Exception ignored) {

            }
            notificationManager.cancel(1);
            if (!stop_The_Process)
                default_Notification(1);
            super.onPostExecute(s);
        }
    }

    public void transFer_File_Changes() {
        contentView.setTextViewText(R.id.title, title_Notification);
        contentView.setProgressBar(R.id.progressBar, count_Data, 0, false);
        contentView.setTextViewText(R.id.size_Title, "0.0 KB/" + save_Exact_Size);
        contentView.setTextViewText(R.id.percent_Text, "00%");
        notificationManager.notify(1, notification);
    }

    public void Notification() {

        String id = this.getString(R.string.transfer_Id); // default_channel_id
        String title = this.getString(R.string.transfer_title); // Default Channel
        NotificationCompat.Builder builder;


        save_Exact_Size = storage_Info.Convert_It(get_File_Size);
        contentView = new RemoteViews(getPackageName(), R.layout.activity_custom_notification);
        contentView.setImageViewResource(R.id.image, R.drawable.copy_intro_icon);
        contentView.setTextViewText(R.id.title, "Data Folder");
        contentView.setTextViewText(R.id.time_Tittle, get_Current_Time());
        contentView.setProgressBar(R.id.progressBar, 0, 0, false);
        contentView.setTextViewText(R.id.size_Title, " Folder.....");
        contentView.setTextViewText(R.id.percent_Text, "00%");

        if (notificationManager == null) {
            notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            assert notificationManager != null;
            NotificationChannel mChannel = notificationManager.getNotificationChannel(id);
            if (mChannel == null) {
                mChannel = new NotificationChannel(id, title, importance);
                notificationManager.createNotificationChannel(mChannel);
            }
        }
        builder = new NotificationCompat.Builder(this, id)// required
                .setSmallIcon(R.drawable.copy_intro_icon)   // required
                .setColor(ContextCompat.getColor(After_MainTransferFIle.this,R.color.colorPrimary))
                .setVibrate(new long[]{0L}) // Passing null here silently fails
                .setAutoCancel(false)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setOngoing(true)
                .setContent(contentView)
                .setLights(Color.parseColor("#075e54"), 3000, 3000);
        notification = builder.build();
        notification.flags |= Notification.FLAG_AUTO_CANCEL;

        notificationManager.notify(1, notification);

    }

    public String get_Current_Time() {

        Calendar calendar = Calendar.getInstance();
        int hours = calendar.get(Calendar.HOUR_OF_DAY);
        int minutes = calendar.get(Calendar.MINUTE);

        if (hours < 12) {
            return hours + ":" + minutes + " AM";
        } else {

            return (hours - 12) + ":" + minutes + " PM";
        }
    }

    public MultiThreading_Task getMultiThreading_task() {
        return multiThreading_task;
    }

    public int Count_The_Data() {
        int count = 0;
        for (int i = 1; i < 9; i++) {

            if (return_Id(i).getVisibility() == View.VISIBLE) {
                if (i == 7) {
                    if (!which_Option_To_Do.equals("restore")) {
                        count += Data_Count(new File(external_Path_Url + whats_App_Media_Path + "/" + Return_Path(i)));
                        count += Data_Count(new File(external_Path_Url + whats_App_Media_Path + "/" + Return_Path(i + 1)));
                        count += Data_Count(new File(external_Path_Url + Return_Path(i + 2)));
                    } else {
                        count += Data_Count(new File(sd_Card_Path_URL + whats_App_Media_Path + "/" + Return_Path(i)));
                        count += Data_Count(new File(sd_Card_Path_URL + whats_App_Media_Path + "/" + Return_Path(i + 1)));
                        count += Data_Count(new File(sd_Card_Path_URL + Return_Path(i + 2)));
                    }
                } else if (i > 7) {
                    if (!which_Option_To_Do.equals("restore"))
                        count += Data_Count(new File(external_Path_Url + whats_App_Media_Path + "/" + Return_Path(i + 2)));
                    else {
                        count += Data_Count(new File(sd_Card_Path_URL + whats_App_Media_Path + "/" + Return_Path(i + 2)));

                    }
                } else {
                    if (!which_Option_To_Do.equals("restore"))
                        count += Data_Count(new File(external_Path_Url + whats_App_Media_Path + "/" + Return_Path(i)));
                    else {
                        count += Data_Count(new File(sd_Card_Path_URL + whats_App_Media_Path + "/" + Return_Path(i)));

                    }
                }
            }
        }

        return count;
    }

    public int Data_Count(File file) {
        int count = 0;
        if (file.isDirectory())
            for (File child : file.listFiles())
                count += Data_Count(child);
        if (Check_For_Extension(file.getAbsolutePath())) {
            count++;
            return count;
        } else {
            return count;
        }
    }

    public boolean Check_For_Extension(String path) {
        int i = path.lastIndexOf('.');
        String extension = "";
        if (i > 0) {
            extension = path.substring(i + 1);
        }
        return extension.equals("jpg") || extension.equals("mp3") || extension.equals("mp4")
                || extension.equals("pptx") || extension.equals("pdf") || extension.equals("docx")
                || extension.equals("opus") || extension.equals("m4a")
                || extension.equals("amr") || extension.equals("aac");

    }

    public void default_Notification(int type) {
        String id = this.getString(R.string.transfer_Done_Id); // default_channel_id
        String title = this.getString(R.string.transfer_Done_title); // Default Channel
        NotificationCompat.Builder builder;
        Uri uri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        String get = "Transfer";
        if (which_Option_To_Do.equals("remove"))
            get = "Remove";


        if (notificationManager == null) {
            notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            assert notificationManager != null;
            NotificationChannel mChannel = notificationManager.getNotificationChannel(id);
            if (mChannel == null) {
                mChannel = new NotificationChannel(id, title, importance);
                notificationManager.createNotificationChannel(mChannel);
            }
        }
        builder =
                new NotificationCompat.Builder(this, id)
                        .setSmallIcon(R.drawable.copy_intro_icon)
                        .setColor(ContextCompat.getColor(After_MainTransferFIle.this,R.color.colorPrimary))
                        .setVibrate(new long[]{1000})
                        .setOngoing(false)
                        .setSound(uri)
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                        .setContentTitle(after_Notification)
                        .setAutoCancel(true)
                        .setLights(Color.parseColor("#075e54"), 3000, 3000);

        if (type == 1) {
            builder.setContentText("Successfully Data " + get);
        } else {
            builder.setContentText("Error : No Space Left ");
        }
        Intent notificationIntent = new Intent(this, Main_Navigation.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(contentIntent);

        notification = builder.build();
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        notificationManager.notify(1, notification);
    }

    public void Call_Custom_Dailog_Option_Changes() {

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Custom_Dialog_For_Changes_Options custom_dialog_for_changes_options = new Custom_Dialog_For_Changes_Options(this);
        custom_dialog_for_changes_options.show(ft, "dialog");
    }

    public void Call_Custom_Dailog_Normal_Changes() {

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Custom_Dialog_For_Normal_Changes custom_dialog_for_normal_changes = new Custom_Dialog_For_Normal_Changes(this, normal_Changes);
        custom_dialog_for_normal_changes.show(ft, "dialog");
    }

    public void Call_Custom_Dailog_Deep_Changes() {

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Tabbed_Custom_Dialog_For_Deep dialogFragment = new Tabbed_Custom_Dialog_For_Deep(this, return_Value(tick_Audio_ImageView.getVisibility()), return_Value(tick_Video_ImageView.getVisibility()),
                return_Value(tick_Document_ImageView.getVisibility()), return_Value(tick_Image_ImageView.getVisibility()), return_Value(tick_Gif_ImageView.getVisibility())
                , return_Value(tick_Profile_ImageView.getVisibility()), return_Value(tick_Sticker_ImageView.getVisibility()),
                return_Value(tick_Voice_ImageView.getVisibility()), which_Option_To_Do);
        dialogFragment.show(ft, "dialog");
    }

    public boolean return_Value(int views) {
        return views == View.VISIBLE;
    }

    public String getExternal_Path_Url() {
        return external_Path_Url;
    }


    public List<File> getOnly_Selected_File() {
        return only_Selected_File;
    }

    public String getSd_Card_Path_URL() {
        return sd_Card_Path_URL;
    }

    public void Push_Back() {
        onBackPressed();
    }

    public void Delete_Particular_Data(String path) {
        File[] files = new File(path).listFiles();
        for (File data : files) {
            if (!data.isDirectory()) {
                data.delete();
            } else {
                Delete_Particular_Data(data.getAbsolutePath());
            }
        }

    }

    public void Send_Permission_To_Transfer() {
        if (which_Option_To_Do.equals("remove")) {
            Call_Custom_Dailog("     Are You Sure To Remove Data ? ");
            title_Notification = "Data Removing";
            after_Notification = "Data Removed";
        } else if (which_Option_To_Do.equals("copy")) {
            Call_Custom_Dailog("     Are You Sure To Copy Data ? ");
            title_Notification = "Data Copying";
            after_Notification = "Data Coped";
        } else if (which_Option_To_Do.equalsIgnoreCase("move")) {
            Call_Custom_Dailog("     Are You Sure To Move Data ? ");
            title_Notification = "Data Moving";
            after_Notification = "Data Moved";
        } else {
            Call_Custom_Dailog("     Are You Sure To restore Data ? ");
            title_Notification = "Data Restore";
            after_Notification = "Data Restore";
        }
    }

    public  void UnneccesaryData() {
        if (new File(external_Path_Url + whatsapp_Path + ".Shared/").exists())
            Delete_Particular_Data(external_Path_Url + whatsapp_Path + ".Shared/");
        if (new File(external_Path_Url + whatsapp_Path + ".Trash").exists())
            Delete_Particular_Data(external_Path_Url + whatsapp_Path + ".Trash/");
        if (new File(external_Path_Url + whatsapp_Path + "cache").exists())
            Delete_Particular_Data(external_Path_Url + whatsapp_Path + "cache/");
        if (new File(external_Path_Url + whatsapp_Path + "Theme").exists())
            Delete_Particular_Data(external_Path_Url + whatsapp_Path + "Theme/");
    }

    public void setNormal_Changes(int normal_Changes) {
        this.normal_Changes = normal_Changes;
    }

    private int convert_To_Days() {
        if (normal_Changes == 1) return 1;
        else if (normal_Changes == 2) return 5;
        else if (normal_Changes == 3) return 10;
        else if (normal_Changes == 4) return 15;
        else if (normal_Changes == 5) return 30;
        else if (normal_Changes == 6) return 60;
        else if (normal_Changes == 7) return 180;

        return 0;
    }

    public Storage_Info getStorage_Info() {
        return storage_Info;
    }

    public void setStorage_Info(Storage_Info storage_Info) {
        this.storage_Info = storage_Info;
    }

    public void setWhich_Option_To_Do(String which_Option_To_Do) {
        this.which_Option_To_Do = which_Option_To_Do;
    }
}

