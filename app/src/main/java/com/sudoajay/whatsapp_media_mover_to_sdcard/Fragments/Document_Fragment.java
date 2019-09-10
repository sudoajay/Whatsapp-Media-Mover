package com.sudoajay.whatsapp_media_mover_to_sdcard.Fragments;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.ExpandableListView;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import com.sudoajay.whatsapp_media_mover_to_sdcard.After_MainTransferFIle;
import com.sudoajay.whatsapp_media_mover_to_sdcard.BuildConfig;
import com.sudoajay.whatsapp_media_mover_to_sdcard.ExpandableListAdapter;
import com.sudoajay.whatsapp_media_mover_to_sdcard.Make_Changes;
import com.sudoajay.whatsapp_media_mover_to_sdcard.R;
import com.sudoajay.whatsapp_media_mover_to_sdcard.Toast.CustomToast;
import com.sudoajay.whatsapp_media_mover_to_sdcard.sharedPreferences.WhatsappPathSharedpreferences;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

public class Document_Fragment extends Fragment {
    private static After_MainTransferFIle after_main_transferFIle;
    private static Make_Changes make_changes=new Make_Changes();
    private List<String> listDataHeader;
    private HashMap<String, List<String>> listDataChild;
    private List<Integer> arrow_Image_Resource = new ArrayList<>(), check_Close_Image_Resource = new ArrayList<>(), count_The_Size = new ArrayList<>();
    private String which_Option_To_Do;
    private List<Boolean> check_Array = new ArrayList<>();
    private ExpandableListAdapter listAdapter;
    private List<File> selected_List,selected_File=new ArrayList<>();
    private String whatsapp_Path,whats_App_Media_Path;

    public Document_Fragment() {
        // Required empty public constructor
    }

    public Document_Fragment createInstance(After_MainTransferFIle after_main_transferFIle, String which_Option_To_Do, List<File> selected_List
    , Context context) {
        Document_Fragment.after_main_transferFIle = after_main_transferFIle;
        this.which_Option_To_Do= which_Option_To_Do;
        this.selected_List= selected_List;

        // configuration or setup the sharedPreferences
        WhatsappPathSharedpreferences whatsappPathSharedpreferences = new WhatsappPathSharedpreferences(context);
        whatsapp_Path = whatsappPathSharedpreferences.getWhatsapp_Path();
        whats_App_Media_Path = whatsappPathSharedpreferences.getWhats_App_Media_Path();

        run_Selected();
        listAdapter = new ExpandableListAdapter(selected_File);
        return this;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_blank, container, false);

        prepareListData();
        if (make_changes.getSave_Data().isEmpty()) {
            ConstraintLayout nothingToShow_ConstraintsLayout = v.findViewById(R.id.nothingToShow_ConstraintsLayout);
            nothingToShow_ConstraintsLayout.setVisibility(View.VISIBLE);
        } else {

            final ExpandableListView expListView = v.findViewById(R.id.lvExp);
             listAdapter = new ExpandableListAdapter(after_main_transferFIle, listDataHeader, listDataChild, arrow_Image_Resource
                    , count_The_Size, make_changes.getGet_Common_Count(), make_changes.getSave_Data(),check_Array,selected_File);

            // setting list adapter
            expListView.setAdapter(listAdapter);


            // Listview Group click listener
            expListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {

                @Override
                public boolean onGroupClick(ExpandableListView parent, View v,
                                            int groupPosition, long id) {
                    return false;
                }
            });

            // Listview Group expanded listener
            expListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

                @Override
                public void onGroupExpand(int groupPosition) {


                    arrow_Image_Resource.set(groupPosition, R.drawable.arrow_up_icon);
                }
            });

            // Listview Group collasped listener
            expListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {

                @Override
                public void onGroupCollapse(int groupPosition) {

                    arrow_Image_Resource.set(groupPosition, R.drawable.arrow_down_icon);
                }
            });

            // Listview on child click listener
            expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

                @Override
                public boolean onChildClick(ExpandableListView parent, View v,
                                            int groupPosition, int childPosition, long id) {
                    // TODO Auto-generated method stub
                    if (groupPosition == 0) {

                        open_With(make_changes.getSave_Data().get(childPosition));
                    } else {
                        open_With(make_changes.getSave_Data().get(count_The_Size.get(groupPosition - 1) + childPosition));
                    }

                    listAdapter.getChildView(groupPosition, childPosition, false, v, parent);

                    return false;
                }

            });
            expListView.invalidate();


        }
        return v;
    }

    private void prepareListData() {
        listDataHeader = new ArrayList<>();
        listDataChild = new LinkedHashMap<>();
        List<String> send_Data_Path = new ArrayList<>();


        List<Integer> count_The_Data = new ArrayList<>();
        int count = 0;
        for (int i = 0; i < make_changes.getGet_Common_Count().size(); i++) {
            count += make_changes.getGet_Common_Count().get(i);
            count_The_Data.add(count);
        }

        for (int i = 0; i < make_changes.getSave().size(); i++) {
            Calendar last_Modified_calendar = Calendar.getInstance();
            last_Modified_calendar.setTime(new Date(make_changes.getSave().get(i)));

            Calendar current_Time = Calendar.getInstance();

            listDataHeader.add(check_For_Date(current_Time, last_Modified_calendar));

            long j;
            if (i != 0) {
                j = count_The_Data.get(i - 1);

            } else {
                j = 0;
            }
            for (int k = (int) j; k < count_The_Data.get(i); k++)
                send_Data_Path.add(make_changes.getSave_Data().get(k).getName());
            listDataChild.put(listDataHeader.get(i), new ArrayList<>(send_Data_Path)); // Header, Child data
            send_Data_Path.clear();
        }


        int counts = 0;

        for (List<String> s : listDataChild.values()) {
            for (int i = 0; i < s.size(); i++) {
                counts++;
            }
            count_The_Size.add(counts);
        }

        for (String s : listDataHeader)
            arrow_Image_Resource.add(R.drawable.arrow_down_icon);

    }

    public String check_For_Date(Calendar current_Time, Calendar last_Modified_calendar) {
        int current_Day = current_Time.get(Calendar.DAY_OF_MONTH);
        int current_Month = current_Time.get(Calendar.MONTH);

        int last_Modified_Day = last_Modified_calendar.get(Calendar.DAY_OF_MONTH);
        int last_Modified_Months = last_Modified_calendar.get(Calendar.MONTH);

        if ((current_Month + 1) == (last_Modified_Months + 1)) {
            if (current_Day == last_Modified_Day)
                return "Today";

            else if (current_Day == (last_Modified_Day - 1))
                return "Yesterday";
            else {
                return last_Modified_Day + " " + Get_Months(last_Modified_Months + 1);
            }
        }
        return last_Modified_Day + " " + Get_Months(last_Modified_Months + 1);
    }

    public String Get_Months(int no) {

        switch (no) {
            case 1:
                return "Jan";
            case 2:
                return "Feb";
            case 3:
                return "Mar";
            case 4:
                return "Apr";
            case 5:
                return "May";
            case 6:
                return "June";
            case 7:
                return "July";
            case 8:
                return "Aug";
            case 9:
                return "Sept";
            case 10:
                return "Oct";
            case 11:
                return "Nov";
            default:
                return "Dec";
        }
    }

    public void open_With(File file){
        MimeTypeMap myMime = MimeTypeMap.getSingleton();
        Intent newIntent = new Intent(Intent.ACTION_VIEW);

        String mimeType = myMime.getMimeTypeFromExtension(fileExt(file.getAbsolutePath()).substring(1));
        Uri URI = FileProvider.getUriForFile(after_main_transferFIle,
                BuildConfig.APPLICATION_ID + ".provider",
                file);
        newIntent.setDataAndType(URI,mimeType);
        newIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        try {
            after_main_transferFIle.startActivity(newIntent);
        } catch (Exception e) {
            CustomToast.ToastIt(getContext(),"No handler for this type of file.");

        }
    }
    private String fileExt(String url) {
        if (url.indexOf("?") > -1) {
            url = url.substring(0, url.indexOf("?"));
        }
        if (url.lastIndexOf(".") == -1) {
            return null;
        } else {
            String ext = url.substring(url.lastIndexOf(".") + 1);
            if (ext.indexOf("%") > -1) {
                ext = ext.substring(0, ext.indexOf("%"));
            }
            if (ext.indexOf("/") > -1) {
                ext = ext.substring(0, ext.indexOf("/"));
            }
            return ext.toLowerCase();

        }
    }

    public ExpandableListAdapter getListAdapter() {
        prepareListData();
        return listAdapter;
    }
    public void run_Selected(){

        if(which_Option_To_Do.equals("restore"))
            make_changes = new Make_Changes(after_main_transferFIle.getSd_Card_Path_URL() + whats_App_Media_Path +
                    whatsapp_Path.substring(1, whatsapp_Path.length()-1)+" Documents/");
        else
            make_changes = new Make_Changes(after_main_transferFIle.getExternal_Path_Url() + whats_App_Media_Path +
                    whatsapp_Path.substring(1, whatsapp_Path.length()-1)+" Documents/");

        for (int i = 0 ; i < make_changes.getSave_Data().size();i++){
            check_Array.add(true);
            for (int j = 0 ; j< selected_List.size();j++){
                if(make_changes.getSave_Data().get(i).equals(selected_List.get(j))){
                    check_Array.set(i,false);
                    selected_File.add(selected_List.get(j));
                    break;
                }else {
                    check_Array.set(i,true);
                }
            }
        }
    }
}

