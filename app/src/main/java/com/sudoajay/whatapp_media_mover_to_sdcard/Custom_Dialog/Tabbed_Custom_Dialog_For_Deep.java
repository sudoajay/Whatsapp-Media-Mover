package com.sudoajay.whatapp_media_mover_to_sdcard.Custom_Dialog;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ImageView;

import com.sudoajay.whatapp_media_mover_to_sdcard.After_MainTransferFIle;
import com.sudoajay.whatapp_media_mover_to_sdcard.Fragments.Audio_Fragment;
import com.sudoajay.whatapp_media_mover_to_sdcard.CustomAdapter;
import com.sudoajay.whatapp_media_mover_to_sdcard.Fragments.Document_Fragment;
import com.sudoajay.whatapp_media_mover_to_sdcard.Fragments.Gif_Fragment;
import com.sudoajay.whatapp_media_mover_to_sdcard.Fragments.Image_Fragment;
import com.sudoajay.whatapp_media_mover_to_sdcard.Fragments.Profile_Fragment;
import com.sudoajay.whatapp_media_mover_to_sdcard.R;
import com.sudoajay.whatapp_media_mover_to_sdcard.Fragments.Sticker_Fragment;
import com.sudoajay.whatapp_media_mover_to_sdcard.Fragments.Video_Fragment;
import com.sudoajay.whatapp_media_mover_to_sdcard.Fragments.Voice_Fragment;

import java.util.Objects;

/**
 * Created by Olakunmi on 21/01/2017.
 */

public class Tabbed_Custom_Dialog_For_Deep extends DialogFragment {
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private boolean tick_Voice_ImageView, tick_Audio_ImageView, tick_Video_ImageView
            , tick_Sticker_ImageView, tick_Document_ImageView, tick_Image_ImageView
            , tick_Profile_ImageView, tick_Gif_ImageView;
    private CustomAdapter adapter;
    private ImageView imageView;
    private After_MainTransferFIle after_main_transferFIle;
    private Audio_Fragment audio_fragment = new Audio_Fragment();
    private Video_Fragment video_fragment = new Video_Fragment();
    private Document_Fragment document_fragment = new Document_Fragment();
    private Gif_Fragment gif_fragment = new Gif_Fragment();
    private Image_Fragment image_fragment = new Image_Fragment();
    private Profile_Fragment profile_fragment =new Profile_Fragment();
    private Sticker_Fragment sticker_fragment = new Sticker_Fragment();
    private Voice_Fragment voice_fragment = new Voice_Fragment();
    private String which_Option_To_Do;

    public Tabbed_Custom_Dialog_For_Deep(){

    }
    @SuppressLint("ValidFragment")
    public Tabbed_Custom_Dialog_For_Deep(After_MainTransferFIle after_main_transferFIle, boolean tick_Audio_ImageView, boolean tick_Video_ImageView, boolean tick_Document_ImageView
            , boolean tick_Image_ImageView, boolean tick_Gif_ImageView,
                                         boolean tick_Profile_ImageView, boolean tick_Sticker_ImageView,
                                         boolean tick_Voice_ImageView, String which_Option_To_Do){
        this.after_main_transferFIle = after_main_transferFIle;
        this.tick_Audio_ImageView = tick_Audio_ImageView;
        this.tick_Video_ImageView = tick_Video_ImageView;
        this.tick_Document_ImageView = tick_Document_ImageView;
        this.tick_Image_ImageView = tick_Image_ImageView;
        this.tick_Profile_ImageView = tick_Profile_ImageView;
        this.tick_Gif_ImageView = tick_Gif_ImageView;
        this.tick_Sticker_ImageView = tick_Sticker_ImageView;
        this.tick_Voice_ImageView = tick_Voice_ImageView;
        this.which_Option_To_Do=which_Option_To_Do;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootview = inflater.inflate(R.layout.activity_custom_dialog_changes,container,false);

        tabLayout =  rootview.findViewById(R.id.tabLayout);
        viewPager =  rootview.findViewById(R.id.masterViewPager);

        imageView = rootview.findViewById(R.id.back_Image_View_Change);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                after_main_transferFIle.Call_Custom_Dailog_Option_Changes();
                dismiss();
            }
        });

        adapter = new CustomAdapter(getChildFragmentManager(), after_main_transferFIle);
        show_Tab();

        this.setCancelable(true);

        viewPager.setAdapter(adapter);

        tabLayout.setupWithViewPager(viewPager);

        return rootview;
    }
    public void onStart() {
        // This MUST be called first! Otherwise the view tweaking will not be present in the displayed Dialog (most likely overriden)
        super.onStart();

        forceWrapContent(this.getView());
    }

    protected void forceWrapContent(View v) {
        // Start with the provided view
        View current = v;
        DisplayMetrics  dm = new DisplayMetrics();
        Objects.requireNonNull(getActivity()).getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        // Travel up the tree until fail, modifying the LayoutParams
        do {
            // Get the parent
            ViewParent parent = current.getParent();

            // Check if the parent exists
            if (parent != null) {
                // Get the view
                try {
                    current = (View) parent;
                } catch (ClassCastException e) {
                    // This will happen when at the top view, it cannot be cast to a View
                    break;
                }

                // Modify the layout
                current.getLayoutParams().width = width-((10*width)/100);
                current.getLayoutParams().height = height-((13*height)/100);
            }
        } while (current.getParent() != null);

        // Request a layout to be re-done
        current.requestLayout();
    }
    public void show_Tab(){

        if(tick_Audio_ImageView)
            adapter.addFragment("Audio",audio_fragment.createInstance(after_main_transferFIle,which_Option_To_Do,
                    after_main_transferFIle.getOnly_Selected_File(),getContext()));
        if(tick_Video_ImageView)
            adapter.addFragment("Video",video_fragment.createInstance(after_main_transferFIle,which_Option_To_Do,
                    after_main_transferFIle.getOnly_Selected_File(),getContext()));
        if(tick_Document_ImageView)
            adapter.addFragment("Document", document_fragment.createInstance(after_main_transferFIle,which_Option_To_Do,
                    after_main_transferFIle.getOnly_Selected_File(),getContext()));
        if(tick_Image_ImageView)
            adapter.addFragment("Image",image_fragment.createInstance(after_main_transferFIle,which_Option_To_Do,
                    after_main_transferFIle.getOnly_Selected_File(),getContext()));
        if(tick_Gif_ImageView)
            adapter.addFragment("Gif",gif_fragment.createInstance(after_main_transferFIle,which_Option_To_Do,
                    after_main_transferFIle.getOnly_Selected_File(),getContext()));
        if(tick_Voice_ImageView)
            adapter.addFragment("Voice",voice_fragment.createInstance(after_main_transferFIle,which_Option_To_Do,
                    after_main_transferFIle.getOnly_Selected_File(),getContext()));
        if(tick_Profile_ImageView)
            adapter.addFragment("Profile",profile_fragment.createInstance(after_main_transferFIle,which_Option_To_Do,
                    after_main_transferFIle.getOnly_Selected_File(),getContext()));
        if(tick_Sticker_ImageView)
            adapter.addFragment("Sticker",sticker_fragment.createInstance(after_main_transferFIle,which_Option_To_Do,
                    after_main_transferFIle.getOnly_Selected_File(),getContext()));

        if(adapter.getCount() <=3)
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        else {
            tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        }

    }

    @Override
    public void onDismiss(DialogInterface dialog) {

        after_main_transferFIle.getOnly_Selected_File().clear();

        if(tick_Audio_ImageView)
            after_main_transferFIle.getOnly_Selected_File().addAll(audio_fragment.getListAdapter().getSelected_File());
        if(tick_Video_ImageView)
            after_main_transferFIle.getOnly_Selected_File().addAll(video_fragment.getListAdapter().getSelected_File());
        if(tick_Document_ImageView)
            after_main_transferFIle.getOnly_Selected_File().addAll(document_fragment.getListAdapter().getSelected_File());
        if(tick_Image_ImageView)
             after_main_transferFIle.getOnly_Selected_File().addAll(image_fragment.getListAdapter().getSelected_File());
        if(tick_Gif_ImageView)
            after_main_transferFIle.getOnly_Selected_File().addAll(gif_fragment.getListAdapter().getSelected_File());
        if(tick_Voice_ImageView)
            after_main_transferFIle.getOnly_Selected_File().addAll(voice_fragment.getListAdapter().getSelected_File());
        if(tick_Profile_ImageView)
            after_main_transferFIle.getOnly_Selected_File().addAll(profile_fragment.getListAdapter().getSelected_File());
        if(tick_Sticker_ImageView)
            after_main_transferFIle.getOnly_Selected_File().addAll(sticker_fragment.getListAdapter().getSelected_File());
        super.onDismiss(dialog);
    }

    public void Dissmiss(){

        this.dismiss();
    }
}