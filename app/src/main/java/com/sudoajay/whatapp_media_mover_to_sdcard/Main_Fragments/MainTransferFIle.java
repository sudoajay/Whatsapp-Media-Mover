package com.sudoajay.whatapp_media_mover_to_sdcard.Main_Fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.sudoajay.whatapp_media_mover_to_sdcard.After_MainTransferFIle;
import com.sudoajay.whatapp_media_mover_to_sdcard.Main_Navigation;
import com.sudoajay.whatapp_media_mover_to_sdcard.Permission.AndroidExternalStoragePermission;
import com.sudoajay.whatapp_media_mover_to_sdcard.Permission.AndroidSdCardPermission;
import com.sudoajay.whatapp_media_mover_to_sdcard.R;
import com.sudoajay.whatapp_media_mover_to_sdcard.SdCardPath;
import com.sudoajay.whatapp_media_mover_to_sdcard.Storage_Info;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.Objects;

public class MainTransferFIle extends Fragment {
    private Button move_Button, copy_Button, remove_Button, file_Size_Text, restore_Button;
    private String string_URI = "";
    private View layout, layouts;
    private Toast toast;
    private boolean whats_App_File_Exist_Internal, whats_App_File_Exist_External;
    private Main_Navigation main_navigation;
    private AndroidExternalStoragePermission androidExternalStorage_permission;
    private AndroidSdCardPermission androidSdCardPermission;
    private Storage_Info storage_info;


    public MainTransferFIle() {
        // Required empty public constructor
    }

    public MainTransferFIle createInstance(Main_Navigation main_navigation) {
        this.main_navigation = main_navigation;
        return this;
    }

    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        layout = inflater.inflate(R.layout.fragment_main_transfer_file, container, false);


        Reference();
        if (!androidExternalStorage_permission.isExternalStorageWritable())
            androidExternalStorage_permission.call_Thread();

        LayoutInflater inflaters = getLayoutInflater();
        layouts = inflaters.inflate(R.layout.activity_custom_toast,
                (ViewGroup) layout.findViewById(R.id.toastcustom));
        call_Thread();
        return layout;

    }

    public void Reference() {

        move_Button = layout.findViewById(R.id.move_Button);
        copy_Button = layout.findViewById(R.id.copy_Button);
        remove_Button = layout.findViewById(R.id.remove_Button);
        file_Size_Text = layout.findViewById(R.id.file_Size_Text);
        restore_Button = layout.findViewById(R.id.restore_Button);

        // on click process
        OnClick_Class onClick_class = new OnClick_Class();
        move_Button.setOnClickListener(onClick_class);
        copy_Button.setOnClickListener(onClick_class);
        remove_Button.setOnClickListener(onClick_class);
        restore_Button.setOnClickListener(onClick_class);

        // Setting scaled drawable in code

        Drawable img = ContextCompat.getDrawable(Objects.requireNonNull(getContext()), R.drawable.copy_intro_icon);
        assert img != null;
        img.setBounds(0, 0, 80, 80);
        copy_Button.setCompoundDrawables(img, null, null, null);

        img = ContextCompat.getDrawable(Objects.requireNonNull(getContext()), R.drawable.restore_intro_icon);
        assert img != null;
        img.setBounds(0, 0, 80, 80);
        restore_Button.setCompoundDrawables(img, null, null, null);

        img = ContextCompat.getDrawable(Objects.requireNonNull(getContext()), R.drawable.remove_intro_icon);
        assert img != null;
        img.setBounds(0, 0, 80, 80);
        remove_Button.setCompoundDrawables(img, null, null, null);

        img = ContextCompat.getDrawable(Objects.requireNonNull(getContext()), R.drawable.move_intro_icon);
        assert img != null;
        img.setBounds(0, 0, 80, 80);
        move_Button.setCompoundDrawables(img, null, null, null);

        // create object classes

        androidExternalStorage_permission = new AndroidExternalStoragePermission(main_navigation, main_navigation);
        androidSdCardPermission = new AndroidSdCardPermission(main_navigation, main_navigation, this);
        storage_info = new Storage_Info(androidSdCardPermission.getSd_Card_Path_URL(), main_navigation);

    }


    public void checked_For_Not_Get_Error() {
        File file = new File(androidExternalStorage_permission.getExternal_Path() + storage_info.getWhatsapp_Path());
        if (!file.exists() || !androidExternalStorage_permission.isExternalStorageWritable()) {
            whats_App_File_Exist_Internal = false;
            move_Button.animate().alpha(0.5f);
            copy_Button.animate().alpha(0.5f);
            remove_Button.animate().alpha(0.5f);
        } else {
            whats_App_File_Exist_Internal = true;
            remove_Button.animate().alpha(1f);
            move_Button.animate().alpha(1f);
            copy_Button.animate().alpha(1f);
        }

        if (!(new File(androidSdCardPermission.getSd_Card_Path_URL() + storage_info.getWhatsapp_Path())).exists()) {
            restore_Button.animate().alpha(0.5f);
            whats_App_File_Exist_External = false;

        } else {
            restore_Button.animate().alpha(1f);
            whats_App_File_Exist_External = true;
        }


        if (androidSdCardPermission.getSd_Card_Path_URL().equals("") || isSamePath() || androidSdCardPermission.isGetting()) {
            move_Button.animate().alpha(0.5f);
            copy_Button.animate().alpha(0.5f);
            restore_Button.animate().alpha(0.5f);

        }
    }

    //
    @SuppressLint("SetTextI18n")
    public void Enter_Whats_App_Folder() {

        if (androidExternalStorage_permission.isExternalStorageWritable())
            file_Size_Text.setText("Data Size - " + storage_info.getWhatsAppInternalMemorySize());
    }

    // Activity's overrided method used to perform click events on menu items
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK)
            return;
        Uri sdCard_Uri = data.getData();
        main_navigation.grantUriPermission(main_navigation.getPackageName(), sdCard_Uri, Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        main_navigation.getContentResolver().takePersistableUriPermission(sdCard_Uri, Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        String sd_Card_Path_URL = SdCardPath.getFullPathFromTreeUri(sdCard_Uri, main_navigation);

        if (new File(sd_Card_Path_URL).exists())
            string_URI = Split_The_URI(sdCard_Uri.toString());


        androidSdCardPermission.setSd_Card_Path_URL(sd_Card_Path_URL);
        androidSdCardPermission.setString_URI(string_URI);

    }

    public String Split_The_URI(String url) {
        String save[] = url.split("%3A");
        return save[0] + "%3A";
    }

    public void Toast_It(String Message) {
        TextView toast_TextView = layouts.findViewById(R.id.text);
        if (toast == null || toast.getView().getWindowVisibility() != View.VISIBLE) {
            toast = new Toast(main_navigation.getApplicationContext());
            toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
            toast.setDuration(Toast.LENGTH_LONG);
            toast.setView(layouts);
            toast_TextView.setText(Message);
            toast.show();
        } else {
            toast.cancel();
        }

    }


    public boolean isSamePath() {
        return androidExternalStorage_permission.getExternal_Path().equals(androidSdCardPermission.getSd_Card_Path_URL());
    }

    public void call_Thread() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                try {
                    Enter_Whats_App_Folder();
                    checked_For_Not_Get_Error();
                } catch (Exception ignored) {

                }

                call_Thread();

            }
        }, 1000);
    }

    public class OnClick_Class implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            try {
                Enter_Whats_App_Folder();
            } catch (Exception ignored) {

            }
            checked_For_Not_Get_Error();
            switch (v.getId()) {
                case R.id.move_Button:
                    if (!androidExternalStorage_permission.isExternalStorageWritable()) {
                        Toast_It("No Permission Granted");
                        androidExternalStorage_permission.call_Thread();
                        call_Thread();
                    } else if (!whats_App_File_Exist_Internal)
                        Toast_It("No WhatsApp Data Present");
                    else if (move_Button.getAlpha() == 0.5f && androidSdCardPermission.isGetting()) {
                        Toast_It("Select The Sd Card  ");
                        androidSdCardPermission.call_Thread();
                        call_Thread();
                    } else {
                        Intent move_Intent = new Intent(main_navigation.getApplicationContext(), After_MainTransferFIle.class);
                        move_Intent.putExtra("move_copy_remove_restore", "move");
                        move_Intent.putExtra("sd_card_path", androidSdCardPermission.getSd_Card_Path_URL());
                        move_Intent.putExtra("Sd_Card_URI", androidSdCardPermission.getString_URI());
                        startActivity(move_Intent);
                    }
                    break;
                case R.id.copy_Button:
                    if (!androidExternalStorage_permission.isExternalStorageWritable()) {
                        Toast_It("No Permission Granted");
                        androidExternalStorage_permission.call_Thread();
                        call_Thread();
                    } else if (!whats_App_File_Exist_Internal)
                        Toast_It("No WhatsApp Data Present");
                    else if (copy_Button.getAlpha() == 0.5f && androidSdCardPermission.isGetting()) {
                        Toast_It("Select The Sd Card  ");
                        androidSdCardPermission.call_Thread();
                        call_Thread();
                    } else {
                        Intent copy_Intent = new Intent(main_navigation.getApplicationContext(), After_MainTransferFIle.class);
                        copy_Intent.putExtra("move_copy_remove_restore", "copy");
                        copy_Intent.putExtra("sd_card_path", androidSdCardPermission.getSd_Card_Path_URL());
                        copy_Intent.putExtra("Sd_Card_URI", androidSdCardPermission.getString_URI());
                        startActivity(copy_Intent);
                    }
                    break;
                case R.id.remove_Button:
                    if (!androidExternalStorage_permission.isExternalStorageWritable()) {
                        Toast_It("No Permission Granted");
                        androidExternalStorage_permission.call_Thread();
                        call_Thread();
                    } else if (!whats_App_File_Exist_Internal)
                        Toast_It("No WhatsApp Data Present");
                    else {
                        Intent remove_Intent = new Intent(main_navigation.getApplicationContext(), After_MainTransferFIle.class);
                        remove_Intent.putExtra("move_copy_remove_restore", "remove");
                        remove_Intent.putExtra("sd_card_path", androidSdCardPermission.getSd_Card_Path_URL());
                        startActivity(remove_Intent);
                    }
                    break;
                case R.id.restore_Button:
                    if (!androidExternalStorage_permission.isExternalStorageWritable()) {
                        Toast_It("No Permission Granted");
                        androidExternalStorage_permission.call_Thread();
                        call_Thread();
                    } else if (restore_Button.getAlpha() == 0.5f && androidSdCardPermission.isGetting()) {
                        Toast_It("Select The Sd Card  ");
                        androidSdCardPermission.call_Thread();
                        call_Thread();
                    } else if (!whats_App_File_Exist_External)
                        Toast_It("No WhatsApp Data Present");
                    else {
                        Intent restore_Intent = new Intent(main_navigation.getApplicationContext(), After_MainTransferFIle.class);
                        restore_Intent.putExtra("move_copy_remove_restore", "restore");
                        restore_Intent.putExtra("sd_card_path", androidSdCardPermission.getSd_Card_Path_URL());
                        restore_Intent.putExtra("Sd_Card_URI", androidSdCardPermission.getString_URI());
                        startActivity(restore_Intent);
                    }
                    break;
            }


        }
    }

}
