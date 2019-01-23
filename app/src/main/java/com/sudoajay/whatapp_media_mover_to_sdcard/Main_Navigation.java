package com.sudoajay.whatapp_media_mover_to_sdcard;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.sudoajay.whatapp_media_mover_to_sdcard.Custom_Dialog.CustomDialogForBackgroundTimer;
import com.sudoajay.whatapp_media_mover_to_sdcard.Custom_Dialog.Custom_Dialog_For_Choose_Your_Whatsapp_Options;
import com.sudoajay.whatapp_media_mover_to_sdcard.Main_Fragments.Duplication_Class;
import com.sudoajay.whatapp_media_mover_to_sdcard.Main_Fragments.Home;
import com.sudoajay.whatapp_media_mover_to_sdcard.Main_Fragments.MainTransferFIle;

import java.io.File;
import java.util.ArrayList;

public class Main_Navigation extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private boolean doubleBackToExitPressedOnce;
    private String rating_link = "https://play.google.com/store/apps/details?id=com.sudoajay.whatapp_media_mover_to_sdcard";
    private Fragment fragment;
    private Home home = new Home();
    private MainTransferFIle mainTransferFIle= new MainTransferFIle();
    private Duplication_Class duplication_class = new Duplication_Class();
    private  NavigationView navigationView;
    private ArrayList<String> whats_App_Path = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main_navigation);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        setTitle("Transfer Data");
        //  change the navigation item to main transfer data
        navigationView.getMenu().getItem(1).setChecked(true);
        onNavigationItemSelected(navigationView.getMenu().getItem(1));

    }

    public void On_Click_Process (View view){
        switch (view.getId()){
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

        else if(id == R.id.nav_Whatsapp_Setting) Call_Custom_Dailog_Setting();

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
                = new CustomDialogForBackgroundTimer();
        customDialogForBackgroundTimer.show(ft, "dialog");
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
        Toast.makeText(this, " Click Back Again To Exit", Toast.LENGTH_SHORT).show();

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
            Toast.makeText(this, "There is no Email App", Toast.LENGTH_LONG).show();
        }

    }

    public Home getHome() {
        return home;
    }


    public NavigationView getNavigationView() {
        return navigationView;
    }
}
