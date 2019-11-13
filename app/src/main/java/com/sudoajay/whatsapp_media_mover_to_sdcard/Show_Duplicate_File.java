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
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.NotificationCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.sudoajay.lodinganimation.LoadingAnimation;
import com.sudoajay.whatsapp_media_mover_to_sdcard.AdFolder.InterstitialAds;
import com.sudoajay.whatsapp_media_mover_to_sdcard.Custom_Dialog.Dialog_InformationData;
import com.sudoajay.whatsapp_media_mover_to_sdcard.HelperClass.CustomToast;
import com.sudoajay.whatsapp_media_mover_to_sdcard.Permission.AndroidExternalStoragePermission;
import com.sudoajay.whatsapp_media_mover_to_sdcard.Permission.AndroidSdCardPermission;
import com.sudoajay.whatsapp_media_mover_to_sdcard.Permission.Notification_Permission_Check;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;


public class Show_Duplicate_File extends AppCompatActivity {
    private ImageView refresh_Image_View;
    private ExpandableListView expandableListView;
    private List<Integer> arrow_Image_Resource = new ArrayList<>();
    private Expandable_Duplicate_List_Adapter expandableDuplicateListAdapter;
    private List<String> list_Header = new ArrayList<>(), save = new ArrayList<>();
    private List<Boolean> checkPath = new ArrayList<>();
    private HashMap<String, List<String>> list_Header_Child = new LinkedHashMap<>();
    private HashMap<String, List<Boolean>> checkDeletedPath = new LinkedHashMap<>();
    private MultiThreading_Task2 multiThreadingtask2;
    private Button deleteDuplicateButton;
    private long total_Size;
    private ConstraintLayout deleteDuplicate;
    private ConstraintLayout nothingToShow_ConstraintsLayout;
    private RemoteViews contentView;
    private Notification notification;
    private NotificationManager notificationManager;
    private Notification_Permission_Check notification_permission_check;
    private InterstitialAds interstitialAds;
    private int internalCheck, externalCheck;
    private ArrayList<String> Data = new ArrayList<>();
    private BottomSheetDialog mBottomSheetDialog;
    private String onClickPath;
    private LinearLayout fragment_history_bottom_sheet_openFile;

//    public enum DataHolder {
//        INSTANCE;
//
//        private ArrayList<String> mObjectList;
//
//        public static boolean hasData() {
//            return INSTANCE.mObjectList != null;
//        }
//
//        public static void setData(final ArrayList<String> objectList) {
//            INSTANCE.mObjectList = objectList;
//        }
//
//        public static ArrayList<String> getData() {
//            final ArrayList<String> retList = INSTANCE.mObjectList;
//            INSTANCE.mObjectList = null;
//            return retList;
//        }
//    }
//


    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_duplicate_file);


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        if (intent != null) {
            if (Objects.requireNonNull(getIntent().getExtras()).get("internalCheck") != null
                    && Objects.requireNonNull(getIntent().getExtras()).get("externalCheck") != null) {
                internalCheck = getIntent().getExtras().getInt("internalCheck");
                externalCheck = getIntent().getExtras().getInt("externalCheck");
            }
        }

        MultiThreadingTask1 multiThreadingTask1 = new MultiThreadingTask1();
        multiThreadingTask1.execute();
    }

    @SuppressLint("StaticFieldLeak")
    public class MultiThreadingTask1 extends AsyncTask<String, String, String> {


        private Duplication_Data duplication_data;
        private AndroidExternalStoragePermission androidExternalStoragePermission;
        private AndroidSdCardPermission androidSdCardPermission;
        private LoadingAnimation loadingAnimation;
        private Storage_Info storage_info;

        @Override
        protected void onPreExecute() {

            loadingAnimation = findViewById(R.id.loadingAnimation);
            loadingAnimation.start();
            duplication_data = new Duplication_Data();
            androidExternalStoragePermission = new
                    AndroidExternalStoragePermission(getApplicationContext(), Show_Duplicate_File.this);

            androidSdCardPermission = new AndroidSdCardPermission(getApplicationContext());
            storage_info = new Storage_Info(getApplicationContext());

            super.onPreExecute();
        }

        @SuppressLint("WrongThread")
        @Override
        protected String doInBackground(String... strings) {
            duplication_data.Duplication(getApplicationContext(), new File(androidExternalStoragePermission.getExternal_Path() + storage_info.getWhatsapp_Path() + "/" + ""),
                    new File(androidSdCardPermission.getSd_Card_Path_URL() + storage_info.getWhatsapp_Path() + "/"),
                    internalCheck, externalCheck);

            return null;
        }

        @Override
        protected void onProgressUpdate(String... values) {

            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(String s) {
            loadingAnimation.stop();
            Data = duplication_data.getList();
            AfterLoading();
            super.onPostExecute(s);


        }

    }

    @SuppressLint("SetTextI18n")
    private void AfterLoading() {

        Reference();

        assert Data != null;
        if (Data.isEmpty()) {
            nothingToShow_ConstraintsLayout.setVisibility(View.VISIBLE);

        } else {
            deleteDuplicateButton.setVisibility(View.VISIBLE);
            deleteDuplicate.setVisibility(View.VISIBLE);

        }
        int i = 0;
        for (String get : Data) {
            if (get.equalsIgnoreCase("And")) {
                i++;
                list_Header.add("Duplicate " + i);
                arrow_Image_Resource.add(R.drawable.arrow_up_icon);
            }
        }
        i = 0;
        for (String get : Data) {
            if (get.equalsIgnoreCase("And")) {
                list_Header_Child.put(list_Header.get(i), new ArrayList<>(save));
                checkDeletedPath.put(list_Header.get(i), new ArrayList<>(checkPath));
                i++;
                save.clear();
                checkPath.clear();
            } else {
                save.add(get);
                if (checkPath.size() == 0) {
                    checkPath.add(false);
                } else {
                    checkPath.add(true);
                }

            }
        }


        expandableDuplicateListAdapter = new Expandable_Duplicate_List_Adapter(this, list_Header, list_Header_Child, checkDeletedPath, arrow_Image_Resource);
        expandableListView.setAdapter(expandableDuplicateListAdapter);

        for (i = 0; i < list_Header.size(); i++) {
            expandableListView.collapseGroup(i);
            for (int j = 0; j < Objects.requireNonNull(list_Header_Child.get(list_Header.get(i))).size(); j++) {
                if (Objects.requireNonNull(checkDeletedPath.get(list_Header.get(i))).get(j))
                    total_Size += new File(Objects.requireNonNull(list_Header_Child.get(list_Header.get(i))).get(j)).length();
            }
        }

        // Listview Group click listener
        expandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {

            @Override
            public boolean onGroupClick(ExpandableListView parent, View v,
                                        int groupPosition, long id) {
                return false;
            }
        });

        // Listview Group expanded listener
        expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

            @Override
            public void onGroupExpand(int groupPosition) {
                arrow_Image_Resource.set(groupPosition, R.drawable.arrow_up_icon);

            }
        });

        // Listview Group collasped listener
        expandableListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {

            @Override
            public void onGroupCollapse(int groupPosition) {
                arrow_Image_Resource.set(groupPosition, R.drawable.arrow_down_icon);
            }
        });

        // Listview on child click listener
        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                // TODO Auto-generated method stub

//                open_With(new File(Objects.requireNonNull(list_Header_Child.get(list_Header.get(groupPosition))).get(childPosition)));
//                expandableDuplicateListAdapter.getChildView(groupPosition, childPosition, false, v, parent);
                onClickPath = Objects.requireNonNull(list_Header_Child.get(list_Header.get(groupPosition))).get(childPosition);
                if (new File(onClickPath).isDirectory())
                    fragment_history_bottom_sheet_openFile.setVisibility(View.GONE);
                else {
                    fragment_history_bottom_sheet_openFile.setVisibility(View.VISIBLE);

                }
                mBottomSheetDialog.show();


                return false;
            }

        });
        expandableListView.invalidate();

        deleteDuplicateButton.setText("Delete (" + Convert_It(total_Size) + ")");

        // Initialize the Mobile Ads SDK.
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });


        interstitialAds = new InterstitialAds(getApplicationContext());


    }

    public void Reference() {
        refresh_Image_View = findViewById(R.id.refresh_Image_View);
        expandableListView = findViewById(R.id.duplicate_Expandable_List_View);
        nothingToShow_ConstraintsLayout = findViewById(R.id.nothingToShow_ConstraintsLayout);
        deleteDuplicateButton = findViewById(R.id.deleteDuplicateButton);
        deleteDuplicate = findViewById(R.id.deleteDuplicate);

        // create object
        multiThreadingtask2 = new MultiThreading_Task2();
        notification_permission_check = new Notification_Permission_Check(this, this);

        mBottomSheetDialog = new BottomSheetDialog(Show_Duplicate_File.this);
        @SuppressLint("InflateParams") View sheetView = getLayoutInflater().inflate(R.layout.layout_dialog_moreoption, null);
        mBottomSheetDialog.setContentView(sheetView);

        fragment_history_bottom_sheet_openFile = sheetView.findViewById(R.id.fragment_history_bottom_sheet_openFile);
        fragment_history_bottom_sheet_openFile.setOnClickListener(new Onclick());
        LinearLayout fragment_history_bottom_sheet_viewFolder = sheetView.findViewById(R.id.fragment_history_bottom_sheet_viewFolder);
        fragment_history_bottom_sheet_viewFolder.setOnClickListener(new Onclick());
        LinearLayout fragment_history_bottom_sheet_moreInfo = sheetView.findViewById(R.id.fragment_history_bottom_sheet_moreInfo);
        fragment_history_bottom_sheet_moreInfo.setOnClickListener(new Onclick());

    }

    public void On_Click_Process(View view) {
        String rating_link = "https://play.google.com/store/apps/details?id=com.sudoajay.whatsapp_media_mover";
        switch (view.getId()) {
            case R.id.back_Image_View:
                onBackPressed();
                break;
            case R.id.share_ImageView:
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("text/plain");
                i.putExtra(Intent.EXTRA_SUBJECT, "Link-Share");
                i.putExtra(Intent.EXTRA_TEXT, "Check This App WhatsApp Media Mover (Easy Way To Move Your WhatsApp Data To Sd Card) - " + rating_link);
                startActivity(Intent.createChooser(i, "Share via"));
                break;
            case R.id.refresh_Image_View:
                if (refresh_Image_View.getRotation() % 360 == 0)
                    refresh_Image_View.animate().rotationBy(360f).setDuration(1000);
                break;
            case R.id.deleteDuplicateButton:
            case R.id.deleteDuplicate:
                if (notification_permission_check.check_Notification_Permission()) {
                    notification_permission_check.Custom_AertDialog();
                } else {
                    Call_Custom_Dailog("   Are You Sure To Delete ?");
                }
        }
    }

    public void open_With(File file) {
        MimeTypeMap myMime = MimeTypeMap.getSingleton();
        Intent newIntent = new Intent(Intent.ACTION_VIEW);
        String mimeType = myMime.getMimeTypeFromExtension(Objects.requireNonNull(fileExt(file.getAbsolutePath())).substring(1));
        Uri URI = FileProvider.getUriForFile(this,
                BuildConfig.APPLICATION_ID + ".provider",
                file);
        newIntent.setDataAndType(URI, mimeType);
        newIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        try {
            this.startActivity(newIntent);
        } catch (Exception e) {
            CustomToast.ToastIt(getApplicationContext(), "No handler for this type of file.");
        }
    }

    private String fileExt(String url) {
        if (url.contains("?")) {
            url = url.substring(0, url.indexOf("?"));
        }
        if (url.lastIndexOf(".") == -1) {
            return null;
        } else {
            String ext = url.substring(url.lastIndexOf(".") + 1);
            if (ext.contains("%")) {
                ext = ext.substring(0, ext.indexOf("%"));
            }
            if (ext.contains("/")) {
                ext = ext.substring(0, ext.indexOf("/"));
            }
            return ext.toLowerCase();

        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), Main_Navigation.class);
        intent.putExtra("passing", "DuplicateData");
        startActivity(intent);
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

                Toast.makeText(getApplicationContext(),"Gets Here",Toast.LENGTH_LONG).show();
                OpenAds();
                onBackPressed();
                multiThreadingtask2.execute();

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

    public void OpenAds() {

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                if (interstitialAds.isLoaded())
                    interstitialAds.getmInterstitialAd().show();

            }
        }, 3000);
    }



    public void call_Thread() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                notificationManager.cancel(1);
                default_Notification();
                CustomToast.ToastIt(getApplicationContext(), "Process Done");

            }
        }, 2000);
    }

    public void default_Notification() {

        String id = this.getString(R.string.transfer_Done_Id); // default_channel_id
        String title = this.getString(R.string.transfer_Done_title); // Default Channel
        NotificationCompat.Builder builder;


        if (notificationManager == null) {
            notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_HIGH;
            assert notificationManager != null;
            NotificationChannel mChannel = notificationManager.getNotificationChannel(id);
            if (mChannel == null) {
                mChannel = new NotificationChannel(id, title, importance);
                notificationManager.createNotificationChannel(mChannel);
            }
        }
        builder =
                new NotificationCompat.Builder(this, "")
                        .setSmallIcon(R.drawable.remove_intro_icon)
                        .setContentTitle("Data Deleted")
                        .setAutoCancel(true)
                        .setOngoing(false)
                        .setLights(Color.parseColor("#075e54"), 3000, 3000);
        builder.setContentText("You Have Saved " + Convert_It(total_Size) + " Of Data ");
        CustomToast.ToastIt(getApplicationContext(), "Successfully Duplicate Data Deleted");

        Intent notificationIntent = new Intent(this, Main_Navigation.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(contentIntent);

        notification = builder.build();
        notification.flags |= Notification.FLAG_AUTO_CANCEL;

        notificationManager.notify(1, notification);
    }

    public void Notification() {
        String id = this.getString(R.string.duplicate_Id); // default_channel_id
        String title = this.getString(R.string.duplicate_title); // Default Channel

        Intent closeButton = new Intent();
        closeButton.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

        NotificationCompat.Builder mBuilder;

       contentView = new RemoteViews(getPackageName(), R.layout.activity_custom_notification);
        contentView.setImageViewResource(R.id.image, R.mipmap.ic_launcher);
        contentView.setTextViewText(R.id.title, "Deletion...");
        contentView.setTextViewText(R.id.time_Tittle, get_Current_Time());
        contentView.setProgressBar(R.id.progressBar, 100, 0, false);
        contentView.setTextViewText(R.id.size_Title, "0/" + list_Header.size());
        contentView.setTextViewText(R.id.percent_Text, "00%");

        if (notificationManager == null) {
            notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_LOW;
            assert notificationManager != null;
            NotificationChannel mChannel = notificationManager.getNotificationChannel(id);
            if (mChannel == null) {
                mChannel = new NotificationChannel(id, title, importance);
                notificationManager.createNotificationChannel(mChannel);
            }
        }
        mBuilder = new NotificationCompat.Builder(this, id)
                .setSmallIcon(R.mipmap.ic_launcher)   // required
                .setContent(contentView)
                .setAutoCancel(false)
                .setOngoing(true)
                .setLights(Color.parseColor("#075e54"), 3000, 3000);

        mBuilder.setContentIntent(
                PendingIntent.getActivity(
                        getApplicationContext(),
                        0,
                        closeButton,
                        PendingIntent.FLAG_UPDATE_CURRENT));


        notification = mBuilder.build();
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

    public MultiThreading_Task2 getMultiThreading_task() {
        return multiThreadingtask2;
    }

    @SuppressLint("StaticFieldLeak")
    public class MultiThreading_Task2 extends AsyncTask<String, String, String> {
        int progress = 0;

        @Override
        protected void onPreExecute() {

            CustomToast.ToastIt(getApplicationContext(), "Progress Shown In Notification Bar");
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            call_Thread();

            super.onPostExecute(s);
        }

        @Override
        protected void onProgressUpdate(String... values) {
            progress++;
            contentView.setTextViewText(R.id.size_Title, progress + "/" + list_Header.size());
            contentView.setTextViewText(R.id.percent_Text, ((progress * 100) / list_Header.size()) + "%");
            contentView.setTextViewText(R.id.time_Tittle, get_Current_Time());
            contentView.setProgressBar(R.id.progressBar, list_Header.size(), progress, false);
            notificationManager.notify(1, notification);
            super.onProgressUpdate(values);
        }

        @Override
        protected String doInBackground(String... strings) {
            Notification();
            String[] destPath = Objects.requireNonNull(getApplicationContext().getExternalCacheDir())
                            .getAbsolutePath().split("/Android/data/com");

            new Delete_Duplicate_Data(list_Header, list_Header_Child, expandableDuplicateListAdapter.getCheckDeletedPath(), Show_Duplicate_File.this, destPath[0]);
            return null;
        }


    }
    public static String Convert_It(long size) {
        if (size > (1024 * 1024 * 1024)) {
            // GB
            return Convert_To_Decimal((float) size / (1024 * 1024 * 1024)) + " GB";
        } else if (size > (1024 * 1024)) {
            // MB
            return Convert_To_Decimal((float) size / (1024 * 1024)) + " MB";

        } else {
            // KB
            return Convert_To_Decimal((float) size / (1024)) + " KB";
        }

    }

    public static String Convert_To_Decimal(float value) {
        String size = value + "";
        if (value >= 1000) {
            return size.substring(0, 4);
        } else if (value >= 100) {
            return size.substring(0, 3);
        } else {
            if (size.length() == 2 || size.length() == 3) {
                return size.substring(0, 1);
            }
            return size.substring(0, 4);

        }

    }
    @SuppressLint("SetTextI18n")
    public void setTotal_Size(final String type, final long size) {
        if (type.equals("add")) {
            total_Size += size;
        } else {
            total_Size -= size;
        }
        deleteDuplicateButton.setText("Delete (" + Convert_It(total_Size) + ")");
    }

    public void Dialog_InformationData() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Dialog_InformationData information_data = new Dialog_InformationData(onClickPath, Show_Duplicate_File.this);
        information_data.show(ft, "dialog");
    }

    private void SpecificFolder() {
        String getPath = onClickPath.replace("/" + new File(onClickPath).getName(), "");
        Uri selectedUri = Uri.parse(getPath);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(selectedUri, "resource/folder");

        if (intent.resolveActivityInfo(getPackageManager(), 0) != null) {
            startActivity(intent);
        } else {
            CustomToast.ToastIt(getApplicationContext(), "No file explorer found");
        }
    }
    class Onclick implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.fragment_history_bottom_sheet_openFile:
                    open_With(new File(onClickPath));
                    break;
                case R.id.fragment_history_bottom_sheet_viewFolder:
                    SpecificFolder();
                    break;
                case R.id.fragment_history_bottom_sheet_moreInfo:
                    Dialog_InformationData();
                    mBottomSheetDialog.dismiss();
                    break;
            }
        }
    }
}
