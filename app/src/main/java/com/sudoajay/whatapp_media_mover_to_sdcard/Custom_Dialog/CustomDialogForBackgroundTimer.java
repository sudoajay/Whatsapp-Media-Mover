package com.sudoajay.whatapp_media_mover_to_sdcard.Custom_Dialog;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.dpro.widgets.WeekdaysPicker;
import com.sudoajay.whatapp_media_mover_to_sdcard.Database_Classes.BackgroundTimerDataBase;
import com.sudoajay.whatapp_media_mover_to_sdcard.Main_Navigation;
import com.sudoajay.whatapp_media_mover_to_sdcard.Permission.ForegroundService;
import com.sudoajay.whatapp_media_mover_to_sdcard.R;
import com.sudoajay.whatapp_media_mover_to_sdcard.sharedPreferences.TraceBackgroundService;

import org.angmarch.views.NiceSpinner;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class CustomDialogForBackgroundTimer extends DialogFragment implements AdapterView.OnItemSelectedListener, View.OnClickListener {

    private NiceSpinner repeatedlySpinner, chooseSpinner;
    private WeekdaysPicker weekdaysPicker;
    private ImageView choose_ImageView;
    private EditText endlesslyEditText;
    private String getSelectedEndlesslyDate = null;
    private BackgroundTimerDataBase backgroundTimerDataBase;
    private Main_Navigation main_navigation;

    public CustomDialogForBackgroundTimer() {

    }

    @SuppressLint("ValidFragment")
    public CustomDialogForBackgroundTimer(Main_Navigation main_navigation) {
        this.main_navigation = main_navigation;
    }

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        View rootview = inflater.inflate(R.layout.activity_background_timer, container, false);

        Reference(rootview);

        // configure and setup custom spinner
        setCustomChooserSpinner();
        setCustomRepeatSpinner();

        if (!backgroundTimerDataBase.check_For_Empty()) {
            Cursor cursor = backgroundTimerDataBase.GetTheValueFromId();
            if (cursor != null) {
                cursor.moveToFirst();

                chooseSpinner.setSelectedIndex(cursor.getInt(1));
                repeatedlySpinner.setSelectedIndex(cursor.getInt(2));
                if (cursor.getInt(2) == 3) {
                    Fill_The_Selected_Weekdays(cursor.getString(3));
                    weekdaysPicker.setVisibility(View.VISIBLE);
                }
                try {

                    Calendar calendar = Calendar.getInstance();
                    @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
                    simpleDateFormat.setCalendar(calendar);

                    if (cursor.getString(4).equalsIgnoreCase(simpleDateFormat.format(calendar.getTime()))) {
                        endlesslyEditText.setText(getResources().getText(R.string.today_Date));
                    } else {
                        endlesslyEditText.setText(cursor.getString(4));
                    }
                } catch (Exception e) {
                    Toast.makeText(getContext(), "Something Wrong", Toast.LENGTH_LONG).show();
                }

            }
        }

        // setup choose_ImageView
        setupChooseImageView(chooseSpinner.getSelectedIndex());

        this.setCancelable(true);
        return rootview;
    }

    // reference the object
    private void Reference(View view) {
        // global variable
        Button cancelButton = view.findViewById(R.id.cancelButton);
        Button okButton = view.findViewById(R.id.okButton);
        chooseSpinner = view.findViewById(R.id.chooseSpinner);
        repeatedlySpinner = view.findViewById(R.id.repeatedlySpinner);
        weekdaysPicker = view.findViewById(R.id.weekdaysPicker);
        choose_ImageView = view.findViewById(R.id.choose_ImageView);
        endlesslyEditText = view.findViewById(R.id.endlesslyEditText);

        // local variable
        ImageView back_Image_View_Change = view.findViewById(R.id.back_Image_View_Change);
        ImageView repeat_Off_Image_View = view.findViewById(R.id.repeat_Off_Image_View);

        backgroundTimerDataBase = new BackgroundTimerDataBase(getContext());

        // default weekdays
        final Calendar c = Calendar.getInstance();
        weekdaysPicker.selectDay(c.get(Calendar.DAY_OF_WEEK));

        // setup for listener
        okButton.setOnClickListener(this);
        cancelButton.setOnClickListener(this);
        back_Image_View_Change.setOnClickListener(this);
        repeat_Off_Image_View.setOnClickListener(this);
        endlesslyEditText.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.okButton:

                // checking for foreground Service
                CheckingAndSetting();

                //Save To Database
                SaveToDatabase();
                main_navigation.TypeCTask();
            case R.id.cancelButton:
            case R.id.back_Image_View_Change:
                Dissmiss();
                break;
            case R.id.repeat_Off_Image_View:
            case R.id.endlesslyEditText:
                // Get Current Date
                GetEndlesslyDate();
                break;
        }
    }

    // spinner for choose
    // set spinner list for Choose
    private void setCustomChooserSpinner() {

        List<String> repeat_Array = new LinkedList<>(Arrays.asList(getResources().getStringArray(R.array.customChooserSetup)));
        chooseSpinner.attachDataSource(repeat_Array);

        // custom listener with

        chooseSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                setupChooseImageView(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    // spinner for choose
    // set spinner list for repeat
    private void setCustomRepeatSpinner() {

        List<String> repeat_Array = new LinkedList<>(Arrays.asList(getResources().getStringArray(R.array.customWeekdaysSetup)));
        repeatedlySpinner.attachDataSource(repeat_Array);

        // custom listener with

        repeatedlySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (position == 3) {
                    weekdaysPicker.setVisibility(View.VISIBLE);
                } else {
                    weekdaysPicker.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    // setup choose_ImageView
    private void setupChooseImageView(final int pos) {

        // local variable
        Bitmap largeIcon;
        try {
            switch (pos) {
                case 1:
                    largeIcon = BitmapFactory.decodeResource(getResources(), R.drawable.move_intro_icon);
                    break;
                case 2:
                    largeIcon = BitmapFactory.decodeResource(getResources(), R.drawable.copy_intro_icon);
                    break;
                case 3:
                    largeIcon = BitmapFactory.decodeResource(getResources(), R.drawable.remove_intro_icon);
                    break;
                default:
                    largeIcon = BitmapFactory.decodeResource(getResources(), R.drawable.restore_intro_icon);
                    break;
            }
            choose_ImageView.setImageBitmap(Bitmap.createScaledBitmap(largeIcon, 55, 55, false));
        } catch (Exception e) {
            Toast.makeText(getContext(), "Something Wrong - " + e.getMessage(), Toast.LENGTH_LONG).show();

        }
    }

    private void GetEndlesslyDate() {
        final Calendar c = Calendar.getInstance();
        final int cYear = c.get(Calendar.YEAR);
        final int cMonth = c.get(Calendar.MONTH);
        final int cDay = c.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog datePickerDialog;
        // Theme
        int theme1 = android.R.style.Theme_Material_Light_Dialog;

        datePickerDialog = new DatePickerDialog(Objects.requireNonNull(getContext()), theme1,
                new DatePickerDialog.OnDateSetListener() {
                    @SuppressLint({"SetTextI18n", "SimpleDateFormat"})
                    @Override
                    public void onDateSet(DatePicker view1, int year, int monthOfYear, int dayOfMonth) {
                        endlesslyEditText.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                        Calendar calendar = Calendar.getInstance();
                        calendar.set(year, monthOfYear, dayOfMonth);
                        SimpleDateFormat getEndlesslyDate = new SimpleDateFormat("dd-MM-yyyy");
                        getSelectedEndlesslyDate = getEndlesslyDate.format(calendar.getTime());

                        // check for today date
                        try {

                            Calendar calendars = Calendar.getInstance();
                            @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
                            simpleDateFormat.setCalendar(calendars);

                            if (getSelectedEndlesslyDate.equalsIgnoreCase(simpleDateFormat.format(calendars.getTime()))) {
                                endlesslyEditText.setText(getResources().getText(R.string.today_Date));
                            } else {
                                endlesslyEditText.setText(getSelectedEndlesslyDate);
                            }
                        } catch (Exception e) {
                            Toast.makeText(getContext(), "Something Wrong", Toast.LENGTH_LONG).show();
                        }

                        // if date already done then show the user
                        String[] split_Date = getSelectedEndlesslyDate.split("-");
                        if (((Integer.parseInt(split_Date[2]) < c.get(Calendar.YEAR)) ||
                                ((Integer.parseInt(split_Date[2]) <= c.get(Calendar.YEAR)) && (Integer.parseInt(split_Date[1]) < c.get(Calendar.MONTH)))
                                || ((Integer.parseInt(split_Date[2]) <= c.get(Calendar.YEAR)) && (Integer.parseInt(split_Date[1]) <= c.get(Calendar.MONTH)) && (Integer.parseInt(split_Date[0]) < c.get(Calendar.DAY_OF_MONTH)))))
                            Toast.makeText(CustomDialogForBackgroundTimer.this.getContext(), "Oops... The Date You selected is Already gone", Toast.LENGTH_SHORT).show();
                        else {
                            // print the endlessly_Edit_Text text
                            Toast.makeText(CustomDialogForBackgroundTimer.this.getContext(), endlesslyEditText.getText().toString(), Toast.LENGTH_LONG).show();
                        }

                    }
                }, cYear, cMonth, cDay);

        if (android.os.Build.VERSION.SDK_INT <= android.os.Build.VERSION_CODES.LOLLIPOP) {
            datePickerDialog.setIcon(R.drawable.check_icon);
            datePickerDialog.setTitle("Please select Date.");
        }

        datePickerDialog.show();
    }

    private void SaveToDatabase() {
        // save to Database
        if (getSelectedEndlesslyDate == null)
            getSelectedEndlesslyDate = "No Date";

        if (backgroundTimerDataBase.check_For_Empty()) {
            backgroundTimerDataBase.FillIt(chooseSpinner.getSelectedIndex(),
                    repeatedlySpinner.getSelectedIndex(), get_Repeat(), getSelectedEndlesslyDate);

        } else {
            backgroundTimerDataBase.UpdateTheTable("1", chooseSpinner.getSelectedIndex(),
                    repeatedlySpinner.getSelectedIndex(), get_Repeat(), getSelectedEndlesslyDate);
        }

        Toast.makeText(getContext(), getResources().getText(R.string.setting_Updated), Toast.LENGTH_LONG).show();

    }

    // week days selected
    private void Fill_The_Selected_Weekdays(String week) {
        String[] split = week.split("");
        List<Integer> list = new ArrayList<>();
        for (String weeks_Days : split) {
            try {
                list.add(Integer.parseInt(weeks_Days));
            } catch (Exception ignored) {

            }
        }
        weekdaysPicker.setSelectedDays(list);
    }

    public String get_Repeat() {
        List<Integer> weekday = weekdaysPicker.getSelectedDays();
        StringBuilder join = new StringBuilder();
        for (Integer week : weekday) {
            join.append(week);
        }
        return join.toString();
    }

    public void onStart() {
        // This MUST be called first! Otherwise the view tweaking will not be present in the displayed Dialog (most likely overriden)
        super.onStart();

        forceWrapContent(this.getView());
    }

    private void forceWrapContent(View v) {
        // Start with the provided view
        View current = v;
        DisplayMetrics dm = new DisplayMetrics();
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
                current.getLayoutParams().width = width - ((10 * width) / 100);
                current.getLayoutParams().height = height - ((25 * height) / 100);
            }
        } while (current.getParent() != null);

        // Request a layout to be re-done
        current.requestLayout();
    }

    @Override
    public void onDismiss(DialogInterface dialog) {

        super.onDismiss(dialog);
    }

    public void Dissmiss() {

        this.dismiss();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
    // only for forground service
    private void CheckingAndSetting() {
        TraceBackgroundService traceBackgroundService =
                new TraceBackgroundService(Objects.requireNonNull(getContext()));
//        if (!traceBackgroundService.isBackgroundServiceWorking()
//                && !traceBackgroundService.isForegroundServiceWorking()){

                // call thread and dilog to run foreground service
        ForegroundService foregroundService = new ForegroundService(getContext(),getActivity());
        foregroundService.call_Thread();
    //    }
    }

}
