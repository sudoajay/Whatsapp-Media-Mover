package com.sudoajay.whatapp_media_mover_to_sdcard;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
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
import android.os.Handler;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;

import com.sudoajay.whatapp_media_mover_to_sdcard.Permission.Notification_Permission_Check;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import dmax.dialog.SpotsDialog;


public class Show_Duplicate_File extends AppCompatActivity {
    private final String rating_link = "https://play.google.com/store/apps/details?id=com.sudoajay.whatsapp_media_mover";
    private ImageView refresh_Image_View;
    private ExpandableListView expandableListView;
    private List<Integer> arrow_Image_Resource = new ArrayList<>();
    private Expandable_Duplicate_List_Adapter expandable_duplicate_list_adapter;
    private List<String> list_Header = new ArrayList<>(), save = new ArrayList<>();
    private HashMap<String, List<String>> list_Header_Child = new LinkedHashMap<>();
    private MultiThreading_Task multiThreading_task=new MultiThreading_Task();
    private long total_Size;
    private Button delete_Duplicate_Button;
    private TextView text_View_Nothing;
    private RemoteViews contentView;
    private AlertDialog alertDialog ;
    private Notification notification;
    private NotificationManager notificationManager;
    private Notification_Permission_Check notification_permission_check;


    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_duplicate_file);

        Reference();

        Bundle extra = getIntent().getBundleExtra("Duplication_Class_Data");
        List<String> Data = (List<String>) extra.getSerializable("Duplication_Class_Data");
        if(Data.isEmpty()) {
            delete_Duplicate_Button.setVisibility(View.INVISIBLE);
            text_View_Nothing.setVisibility(View.VISIBLE);

        }
        int i = 0;
        for (String get : Data) {
            if (get.equalsIgnoreCase("And")) {
                i++;
                list_Header.add("Group " + i);
                arrow_Image_Resource.add(R.drawable.arrow_up_icon);
            }
        }
        i = 0;
        for (String get : Data) {
            if (get.equalsIgnoreCase("And")) {
                list_Header_Child.put(list_Header.get(i), new ArrayList<>(save));
                i++;
                save.clear();
            } else {
                save.add(get);
            }
        }


        expandable_duplicate_list_adapter = new Expandable_Duplicate_List_Adapter(this, list_Header, list_Header_Child, arrow_Image_Resource);
        expandableListView.setAdapter(expandable_duplicate_list_adapter);
        for (i = 0; i < list_Header.size(); i++) {
            expandableListView.expandGroup(i);
            total_Size+=new File(list_Header_Child.get(list_Header.get(i)).get(0)).length();
        }

        delete_Duplicate_Button.setText("Delete ("+Convert_It(total_Size)+")");
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

                open_With(new File(list_Header_Child.get(list_Header.get(groupPosition)).get(childPosition)));
                expandable_duplicate_list_adapter.getChildView(groupPosition, childPosition, false, v, parent);

                return false;
            }

        });
        expandableListView.invalidate();
    }

    public void Reference() {
        refresh_Image_View = findViewById(R.id.refresh_Image_View);
        expandableListView = findViewById(R.id.duplicate_Expandable_List_View);
        delete_Duplicate_Button = findViewById(R.id.delete_Duplicate_Button);
        text_View_Nothing = findViewById(R.id.text_View_Nothing);

        notification_permission_check = new Notification_Permission_Check(this,this);
    }

    public void On_Click_Process(View view) {
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
            case R.id.delete_Duplicate_Button:
                if(!notification_permission_check.check_Notification_Permission()){
                    notification_permission_check.Custom_AertDialog();
                }else {
                    Call_Custom_Dailog("   Are You Sure To Delete ?");
                }
        }
    }

    public void open_With(File file) {
        MimeTypeMap myMime = MimeTypeMap.getSingleton();
        Intent newIntent = new Intent(Intent.ACTION_VIEW);

        String mimeType = myMime.getMimeTypeFromExtension(fileExt(file.getAbsolutePath()).substring(1));
        Uri URI = FileProvider.getUriForFile(this,
                BuildConfig.APPLICATION_ID + ".provider",
                file);
        newIntent.setDataAndType(URI, mimeType);
        newIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        try {
            this.startActivity(newIntent);
        } catch (Exception e) {
            Toast.makeText(this, "No handler for this type of file.", Toast.LENGTH_LONG).show();

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
       super.onBackPressed();
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

    @SuppressLint("StaticFieldLeak")
    public  class MultiThreading_Task extends AsyncTask<String, String, String> {
        int progress = 0 ;

        @Override
        protected void onPreExecute() {
            alertDialog = new SpotsDialog.Builder()
                    .setContext(Show_Duplicate_File.this)
                    .setMessage("Deletion....")
                    .setCancelable(false)
                    .setTheme(R.style.Custom)
                    .build();

            alertDialog.show();
            onBackPressed();
            Toast.makeText(Show_Duplicate_File.this,"Deletion" , Toast.LENGTH_LONG).show();
                super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            call_Thread();

            super.onPostExecute(s);
        }

        @Override
        protected void onProgressUpdate(String... values)
        {
            progress++;
            contentView.setTextViewText(R.id.size_Title, progress + "/" + list_Header.size());
            contentView.setTextViewText(R.id.percent_Text, ((progress*100)/list_Header.size())+"%");
            contentView.setTextViewText(R.id.time_Tittle, get_Current_Time());
            contentView.setProgressBar(R.id.progressBar, list_Header.size(), progress, false);
            notificationManager.notify(1, notification);
            super.onProgressUpdate(values);
        }

        @Override
        protected String doInBackground(String... strings) {
            Notification();
            new Delete_Duplicate_Data(list_Header,list_Header_Child,Show_Duplicate_File.this);
            return null;
        }
    }

    public String Convert_It(long size) {
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

    public String Convert_To_Decimal(float value) {
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
    public void call_Thread() {
       Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                notificationManager.cancel(1);
                default_Notification();
                Toast.makeText(Show_Duplicate_File.this, "Successfully Data Deleted", Toast.LENGTH_LONG).show();

            }
        }, 2000);
    }
    public void default_Notification() {

        String id = this.getString(R.string.transfer_Done_Id); // default_channel_id
        String title = this.getString(R.string.transfer_Done_title); // Default Channel
        NotificationCompat.Builder builder;


        if (notificationManager == null) {
            notificationManager = (NotificationManager)this.getSystemService(Context.NOTIFICATION_SERVICE);
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
                new NotificationCompat.Builder(this,"")
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle("Data Deleted")
                        .setAutoCancel(true)
                        .setOngoing(false)
                        .setPriority(Notification.PRIORITY_DEFAULT)
                        .setLights(Color.parseColor("#075e54"), 3000, 3000);
        builder.setContentText("You Have Saved " + Convert_It(total_Size) + " Of Data ");

        Intent notificationIntent = new Intent(this, Main_Navigation.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(contentIntent);

        notification = builder.build();
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        notification.defaults |= Notification.DEFAULT_LIGHTS;

        notificationManager.notify(1, notification);
    }
    public void Notification() {
        String id = this.getString(R.string.duplicate_Id); // default_channel_id
        String title = this.getString(R.string.duplicate_title); // Default Channel
        NotificationCompat.Builder mBuilder;

        contentView = new RemoteViews(getPackageName(), R.layout.activity_custom_notification);
        contentView.setImageViewResource(R.id.image, R.mipmap.ic_launcher);
        contentView.setTextViewText(R.id.title, "Deletion...");
        contentView.setTextViewText(R.id.time_Tittle, get_Current_Time());
        contentView.setProgressBar(R.id.progressBar, 100, 0, false);
        contentView.setTextViewText(R.id.size_Title, "0/"+ list_Header.size());
        contentView.setTextViewText(R.id.percent_Text, "00%");

        if (notificationManager == null) {
            notificationManager = (NotificationManager)this.getSystemService(Context.NOTIFICATION_SERVICE);
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
        mBuilder = new NotificationCompat.Builder(this, id)
                .setSmallIcon(R.mipmap.ic_launcher)   // required
                .setPriority(Notification.PRIORITY_DEFAULT)
                .setContent(contentView)
                .setAutoCancel(false)
                .setOngoing(true)
                .setLights(Color.parseColor("#075e54"), 3000, 3000);

        notification = mBuilder.build();
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        notification.defaults |= Notification.DEFAULT_LIGHTS;

        notificationManager.notify(1, notification);
    }
    public String get_Current_Time() {
        Date currentTime = Calendar.getInstance().getTime();

        if (currentTime.getHours() < 12) {
            return currentTime.getHours() + ":" + currentTime.getMinutes() + " AM";
        } else {

            return (currentTime.getHours() - 12) + ":" + currentTime.getMinutes() + " PM";
        }
    }

    public MultiThreading_Task getMultiThreading_task() {
        return multiThreading_task;
    }

    public void setMultiThreading_task(MultiThreading_Task multiThreading_task) {
        this.multiThreading_task = multiThreading_task;
    }
}
