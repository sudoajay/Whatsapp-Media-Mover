package com.sudoajay.whatapp_media_mover_to_sdcard;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.arch.lifecycle.Observer;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.sudoajay.whatapp_media_mover_to_sdcard.Background_Task.WorkMangerTaskA;
import com.sudoajay.whatapp_media_mover_to_sdcard.Background_Task.WorkMangerTaskB;
import com.sudoajay.whatapp_media_mover_to_sdcard.Background_Task.WorkMangerTaskC;
import com.sudoajay.whatapp_media_mover_to_sdcard.Custom_Dialog.CustomDialogForBackgroundTimer;
import com.sudoajay.whatapp_media_mover_to_sdcard.Custom_Dialog.CustomDialogForForegroundService;
import com.sudoajay.whatapp_media_mover_to_sdcard.Custom_Dialog.Custom_Dialog_For_Choose_Your_Whatsapp_Options;
import com.sudoajay.whatapp_media_mover_to_sdcard.Database_Classes.BackgroundTimerDataBase;
import com.sudoajay.whatapp_media_mover_to_sdcard.ForegroundService.Foreground;
import com.sudoajay.whatapp_media_mover_to_sdcard.Main_Fragments.Duplication_Class;
import com.sudoajay.whatapp_media_mover_to_sdcard.Main_Fragments.Home;
import com.sudoajay.whatapp_media_mover_to_sdcard.Main_Fragments.MainTransferFIle;
import com.sudoajay.whatapp_media_mover_to_sdcard.Permission.AndroidExternalStoragePermission;
import com.sudoajay.whatapp_media_mover_to_sdcard.Permission.ForegroundService;
import com.sudoajay.whatapp_media_mover_to_sdcard.Toast.CustomToast;
import com.sudoajay.whatapp_media_mover_to_sdcard.sharedPreferences.BackgroundProcess;
import com.sudoajay.whatapp_media_mover_to_sdcard.sharedPreferences.PrefManager;
import com.sudoajay.whatapp_media_mover_to_sdcard.sharedPreferences.TraceBackgroundService;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import androidx.work.ExistingWorkPolicy;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

public class Main_Navigation extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private boolean doubleBackToExitPressedOnce;
    private String rating_link = "https://play.google.com/store/apps/details?id=com.sudoajay.whatapp_media_mover_to_sdcard";
    private Fragment fragment;
    private Home home = new Home();
    private MainTransferFIle mainTransferFIle = new MainTransferFIle();
    private Duplication_Class duplication_class = new Duplication_Class();
    private NavigationView navigationView;
    private TraceBackgroundService traceBackgroundService;
    private BackgroundProcess backgroundProcess;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main_navigation);
        // local variable
        String value = null;
        // get data from intent
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            value = extras.getString("passing");
        }

        // Create Object
        backgroundProcess = new BackgroundProcess(getApplicationContext());

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        @SuppressLint("CutPasteId") DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        if (value == null) {
            setTitle("Transfer Data");
            //  change the navigation item to main transfer data
            navigationView.getMenu().getItem(1).setChecked(true);
            onNavigationItemSelected(navigationView.getMenu().getItem(1));
        } else if (value.equalsIgnoreCase("DuplicateData")) {
            setTitle("Duplicate Data");
            //  change the navigation item to main transfer data
            navigationView.getMenu().getItem(1).setChecked(true);
            onNavigationItemSelected(navigationView.getMenu().getItem(2));
        }

        if (getIntent().getAction() != null) {
            if (Objects.requireNonNull(getIntent().getAction()).equalsIgnoreCase("Stop_Foreground(Setting)")) {
                navigationView.getMenu().getItem(1).setChecked(true);
                onNavigationItemSelected(navigationView.getMenu().getItem(5));
            }

        }

        AndroidExternalStoragePermission androidExternalStoragePermission = new AndroidExternalStoragePermission(getApplicationContext(),
                Main_Navigation.this);
        if (androidExternalStoragePermission.isExternalStorageWritable()) {

            PrefManager prefManager = new PrefManager(getApplicationContext());
            traceBackgroundService = new TraceBackgroundService(getApplicationContext());

            // check for first time and there is no restriction in background service
            if (!prefManager.isFirstTimeLaunch() && traceBackgroundService.isBackgroundServiceWorking()) {
                if (!TraceBackgroundService.CheckForBackground(traceBackgroundService.getTaskA())) {
                    traceBackgroundService.setBackgroundServiceWorking(false);
                } else if (!TraceBackgroundService.CheckForBackground(traceBackgroundService.getTaskB())) {
                    traceBackgroundService.setBackgroundServiceWorking(false);
                } else if (traceBackgroundService.getTaskC() != null &&
                        !TraceBackgroundService.CheckForBackground(traceBackgroundService.getTaskC())) {
                    traceBackgroundService.setBackgroundServiceWorking(false);
                }
            }

            // first time check
            if (traceBackgroundService.isBackgroundServiceWorking()) {


                // regular background Process
                // showing size of whatsApp Data
                if (backgroundProcess.isTaskADone())
                    TypeATask();

                // showing duplication of whatsapp Data.
                if (backgroundProcess.isTaskBDone())
                    TypeBTask();

                // doing background Process
                // New Feature
                if (backgroundProcess.isTaskCDone())
                    TypeCTask();
            } else {

                if (traceBackgroundService.isForegroundServiceWorking()) {

                    if (!isServiceRunningInForeground(getApplicationContext(), Foreground.class)) {
                        ForegroundService foregroundService =
                                new ForegroundService(Main_Navigation.this, Main_Navigation.this);
                        // call Custom dialog for Thread
                        foregroundService.call_Thread();
                    }
                }
            }


        }
    }

    public void On_Click_Process(View view) {
        switch (view.getId()) {
            case R.id.media_Mover_Image_View:
            case R.id.media_Mover_Text_View:
                navigationView.getMenu().getItem(0).setChecked(true);
                onNavigationItemSelected(navigationView.getMenu().getItem(0));
                break;
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_navigation, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_Home) {
            setTitle("Home");
            fragment = home.createInstance(Main_Navigation.this);
            // Handle the camera action
        } else if (id == R.id.nav_Transfer_Data) {
            setTitle("Transfer Data");
            fragment = mainTransferFIle.createInstance(Main_Navigation.this);

        } else if (id == R.id.nav_Duplicate_Data) {
            setTitle("Duplicate Data");
            fragment = duplication_class.createInstance(Main_Navigation.this);

        } else if (id == R.id.nav_Timer) CallCustomDailogBackgroundTimer();

        else if (id == R.id.nav_Whatsapp_Setting) Call_Custom_Dailog_Setting();

        else if (id == R.id.nav_Foreground_Setting) CallCustomDailogForeground();

        else if (id == R.id.nav_share) Share();

        else if (id == R.id.nav_Rate_Us) Rating();

        else if (id == R.id.nav_Contact_Me) Open_Email();

        Replace_Fragments();

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void Call_Custom_Dailog_Setting() {

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Custom_Dialog_For_Choose_Your_Whatsapp_Options custom_dialog_for_changes_options
                = new Custom_Dialog_For_Choose_Your_Whatsapp_Options(this);
        custom_dialog_for_changes_options.show(ft, "dialog");
    }

    public void CallCustomDailogBackgroundTimer() {

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        CustomDialogForBackgroundTimer customDialogForBackgroundTimer
                = new CustomDialogForBackgroundTimer(this);
        customDialogForBackgroundTimer.show(ft, "dialog");
    }

    public void CallCustomDailogForeground() {

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        CustomDialogForForegroundService custom_dialog_for_changes_options
                = new CustomDialogForForegroundService();
        custom_dialog_for_changes_options.show(ft, "dialog");
    }

    private void TypeATask() {

        // set the Task is started
        backgroundProcess.setTaskADone(false);

        // this task for Regular Show Size
        OneTimeWorkRequest morning_Work =
                new OneTimeWorkRequest.Builder(WorkMangerTaskA.class).addTag("Regular Data Size").setInitialDelay(1
                        , TimeUnit.DAYS).build();


        WorkManager.getInstance().enqueueUniqueWork("Regular Data Size", ExistingWorkPolicy.KEEP, morning_Work);

        WorkManager.getInstance().getWorkInfoByIdLiveData(morning_Work.getId())
                .observe(this, new Observer<WorkInfo>() {
                    @Override
                    public void onChanged(@Nullable WorkInfo workInfo) {
                        // Do something with the status
                        if (workInfo != null && workInfo.getState().isFinished()) {


                            // Recursive
                            TypeATask();
                        }
                    }
                });


    }

    private void TypeBTask() {

        // set the Task is started

        backgroundProcess.setTaskBDone(false);

        // this task for weekly Duplicate Size
        OneTimeWorkRequest morning_Work =
                new OneTimeWorkRequest.Builder(WorkMangerTaskB.class).addTag("Weekly Duplicate Size").setInitialDelay(7
                        , TimeUnit.DAYS).build();
        WorkManager.getInstance().enqueueUniqueWork("Weekly Duplicate Size", ExistingWorkPolicy.KEEP, morning_Work);

        WorkManager.getInstance().getWorkInfoByIdLiveData(morning_Work.getId())
                .observe(this, new Observer<WorkInfo>() {
                    @Override
                    public void onChanged(@Nullable WorkInfo workInfo) {
                        // Do something with the status
                        if (workInfo != null && workInfo.getState().isFinished()) {


                            // Recursive
                            TypeBTask();
                        }
                    }
                });

    }

    public void TypeCTask() {

        // set the Task is started
        backgroundProcess.setTaskCDone(false);

        // this task for cleaning and show today task
        int hour = 0;
        String endles = "No Date";

        // grab the data From Database
        BackgroundTimerDataBase backgroundTimerDataBase = new BackgroundTimerDataBase(getApplicationContext());

        if (!backgroundTimerDataBase.check_For_Empty()) {
            Cursor cursor = backgroundTimerDataBase.GetTheHourFromId();
            if (cursor != null && cursor.moveToFirst()) {
                cursor.moveToFirst();

                endles = cursor.getString(2);
                try {

                    switch (cursor.getInt(0)) {
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

                            hour = 24 * CountDay(currentDay, listWeekdays);

                            break;
                        case 4:  // At Every month(Same Date)
                            hour = (24 * 30);
                            break;
                    }

                } catch (Exception e) {
                }
            }

            // delete database if not endlesss
            if (endles.equals("No Date"))
                backgroundTimerDataBase.deleteData(1 + "");


        }

        if (hour != 0) {


            OneTimeWorkRequest morning_Work =
                    new OneTimeWorkRequest.Builder(WorkMangerTaskC.class).addTag(" Duplication Size").setInitialDelay(hour, TimeUnit.HOURS)
                            .build();
            WorkManager.getInstance().enqueueUniqueWork(" Duplication Size", ExistingWorkPolicy.KEEP, morning_Work);


            final String finalEndles = endles;
            WorkManager.getInstance().getWorkInfoByIdLiveData(morning_Work.getId())
                    .observe(this, new Observer<WorkInfo>() {
                        @Override
                        public void onChanged(@Nullable WorkInfo workInfo) {
                            // Do something with the status
                            if (workInfo != null && workInfo.getState().isFinished()) {
                                try {
                                    if (!finalEndles.equals("No Date")) {

                                        // current or today date
                                        Calendar calendars = Calendar.getInstance();
                                        Date curDate = calendars.getTime();

                                        // specific date from database
                                        DateFormat format = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
                                        Date date = null;

                                        date = format.parse(finalEndles);

                                        if (date.after(curDate)) {
                                            // Recursive
                                            TypeCTask();
                                        }
                                    }
                                } catch (Exception e) {
                                }

                            }
                        }
                    });
        }
    }

    // Replace Fragments
    public void Replace_Fragments() {

        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.frame_Layout, fragment);
            ft.commit();
        }
    }

    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            Finish();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        CustomToast.ToastIt(getApplicationContext(), "Click Back Again To Exit");

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }

    public void Finish() {
        Intent homeIntent = new Intent(Intent.ACTION_MAIN);
        homeIntent.addCategory(Intent.CATEGORY_HOME);
        homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(homeIntent);

    }

    public void Share() {
        Intent i = new Intent(android.content.Intent.ACTION_SEND);
        i.setType("text/plain");
        i.putExtra(android.content.Intent.EXTRA_SUBJECT, "Link-Share");
        i.putExtra(android.content.Intent.EXTRA_TEXT, R.string.share_info + rating_link);
        startActivity(Intent.createChooser(i, "Share via"));
    }

    public void Rating() {
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(rating_link));
        startActivity(i);
    }

    public void Open_Email() {

        try {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("mailto:" + "sudoajay@gmail.com"));
            intent.putExtra(Intent.EXTRA_SUBJECT, "");
            intent.putExtra(Intent.EXTRA_TEXT, "");
            startActivity(intent);
        } catch (Exception e) {
            CustomToast.ToastIt(getApplicationContext(), "There is no Email App");
        }

    }


    public NavigationView getNavigationView() {
        return navigationView;
    }

    public static int CountDay(int day, List<Integer> week_Days) {
        int temp = day, count = 0;
        do {
            count++;
            temp++;
            if (temp == 8) temp = 1;

            for (Integer week : week_Days) {
                if (temp == week) return count;
            }
        } while (temp != day);
        return 0;
    }

    public static boolean isServiceRunningInForeground(Context context, Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                if (service.foreground) {
                    return true;
                }
            }
        }
        return false;
    }
}
