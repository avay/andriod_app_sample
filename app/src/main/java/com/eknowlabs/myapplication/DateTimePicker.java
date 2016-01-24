package com.eknowlabs.myapplication;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Date;


public class DateTimePicker extends DialogFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    public static final String TAG_FRAG_DATE_TIME = "fragDateTime";
    private static final String KEY_DIALOG_TITLE = "dialogTitle";
    private static final String KEY_INIT_DATE = "initDate";
    private static final String TAG_DATE = "date";
    private static final String TAG_TIME = "time";
    private Context mContext;
    //private ButtonClickListener mButtonClickListener;
    private OnDateTimeSetListener mOnDateTimeSetListener;
    private Bundle mArgument;
    private DatePicker mDatePicker;
    private TimePicker mTimePicker;
    private View mView;
    private static final String ARG_PARAM1 = "param1";

    private OnDateTimeSetListener callback;

    public DateTimePicker() {
        // Required empty public constructor
    }



    /**
     *
     * @param dialogTitle Title of the DateTimePicker DialogFragment
     * @param initDate Initial Date and Time set to the Date and Time Picker
     * @return Instance of the DateTimePicker DialogFragment
     */
    public static DateTimePicker newInstance(CharSequence dialogTitle, Date initDate, String param1) {
        // Create a new instance of DateTimePicker
        DateTimePicker mDateTimePicker = new DateTimePicker();
        // Setup the constructor parameters as arguments
        Bundle mBundle = new Bundle();
        mBundle.putCharSequence(KEY_DIALOG_TITLE, dialogTitle);
        mBundle.putSerializable(KEY_INIT_DATE, initDate);
        mBundle.putString(ARG_PARAM1, param1);
        mDateTimePicker.setArguments(mBundle);
        // Return instance with arguments
        return mDateTimePicker;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            mOnDateTimeSetListener = (OnDateTimeSetListener) getTargetFragment();
        } catch (ClassCastException e) {
            throw new ClassCastException("Calling Fragment must implement OnDateTimeSetListener");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Retrieve Argument passed to the constructor
        mArgument = getArguments();
        // Use an AlertDialog Builder to initially create the Dialog
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(getContext());
        // Setup the Dialog
        mBuilder.setTitle(mArgument.getCharSequence(KEY_DIALOG_TITLE));
        //mBuilder.setNegativeButton(R.string.no, mButtonClickListener);
        mBuilder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (DialogInterface.BUTTON_POSITIVE == i) {
                            DateTime mDateTime = new DateTime(
                                    mDatePicker.getYear(),
                                    mDatePicker.getMonth(),
                                    mDatePicker.getDayOfMonth(),
                                    mTimePicker.getCurrentHour(),
                                    mTimePicker.getCurrentMinute()
                            );
                            //String text = mDateTime.getDateString();
                            //Toast.makeText(getContext(), text, Toast.LENGTH_LONG).show();
                            try{
                                mOnDateTimeSetListener = (OnDateTimeSetListener) getTargetFragment();
                                mOnDateTimeSetListener.DateTimeSet(mDateTime.getDate(),mArgument.getString(ARG_PARAM1) );
                            }
                            catch (ClassCastException e) {
                                throw new ClassCastException("Calling Fragment must implement OnDateTimeSetListener");
                            }
                            //mOnDateTimeSetListener.DateTimeSet(mDateTime.getDate());
                        }
                    }
                });
        mBuilder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(DialogInterface.BUTTON_NEGATIVE == i) {
                    DateTimePicker.this.getDialog().cancel();
                }
            }
        });
        // Create the Alert Dialog
        AlertDialog mDialog = mBuilder.create();
        // Set the View to the Dialog
        mDialog.setView(createDateTimeView(mDialog.getLayoutInflater()));
        // Return the Dialog created
        return mDialog;
    }
    private View createDateTimeView(LayoutInflater layoutInflater) {
        // Inflate the XML Layout using the inflater from the created Dialog
        mView = layoutInflater.inflate(R.layout.date_time_picker,null);
        // Extract the TabHost
        TabHost mTabHost = (TabHost) mView.findViewById(R.id.tab_host);
        mTabHost.setup();
        // Create Date Tab and add to TabHost
        TabHost.TabSpec mDateTab = mTabHost.newTabSpec(TAG_DATE);
        mDateTab.setIndicator(getString(R.string.tab_date));
        mDateTab.setContent(R.id.date_content);
        mTabHost.addTab(mDateTab);
        // Create Time Tab and add to TabHost
        TabHost.TabSpec mTimeTab = mTabHost.newTabSpec(TAG_TIME);
        mTimeTab.setIndicator(getString(R.string.tab_time));
        mTimeTab.setContent(R.id.time_content);
        mTabHost.addTab(mTimeTab);
        // Retrieve Date from Arguments sent to the Dialog
        DateTime mDateTime = new DateTime((Date) mArgument.getSerializable(KEY_INIT_DATE));
        // Initialize Date and Time Pickers
        mDatePicker = (DatePicker) mView.findViewById(R.id.date_picker);
        mTimePicker = (TimePicker) mView.findViewById(R.id.time_picker);
        mDatePicker.init(mDateTime.getYear(), mDateTime.getMonthOfYear(),
                mDateTime.getDayOfMonth(), null);
        //if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            mTimePicker.setCurrentHour(mDateTime.getHourOfDay());
        //}
        //if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            mTimePicker.setCurrentMinute(mDateTime.getMinuteOfHour());
        //}
        // Return created view
        return mView;
    }

    public void setOnDateTimeSetListener(OnDateTimeSetListener onDateTimeSetListener) {
        mOnDateTimeSetListener = onDateTimeSetListener;
    }
    /*private class ButtonClickListener implements DialogInterface.OnClickListener {
        @Override
        public void onClick(DialogInterface dialogInterface, int result) {
            // Determine if the user selected Ok
            mDatePicker = (DatePicker) mView.findViewById(R.id.date_picker);
            mTimePicker = (TimePicker) mView.findViewById(R.id.time_picker);
            if(DialogInterface.BUTTON_POSITIVE == result) {
                //DateTime mDateTime = null;
                DateTime mDateTime;
                *//*if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                    mDateTime = new DateTime(
                            mDatePicker.getYear(),
                            mDatePicker.getMonth(),
                            mDatePicker.getDayOfMonth(),
                            mTimePicker.getHour(),
                            mTimePicker.getMinute()
                    );
                }
                else {*//*
                    mDateTime = new DateTime(
                            mDatePicker.getYear(),
                            mDatePicker.getMonth(),
                            mDatePicker.getDayOfMonth(),
                            mTimePicker.getCurrentHour(),
                            mTimePicker.getCurrentMinute()
                    );
               // }
                mOnDateTimeSetListener.DateTimeSet(mDateTime.getDate());
            }
            if(DialogInterface.BUTTON_NEGATIVE == result) {
                DateTimePicker.this.getDialog().cancel();
            }
        }
    }*/

    /**
     * Interface for sending the Date and Time to the calling object
     */
    public interface OnDateTimeSetListener {
        void DateTimeSet(Date date, String string);
    }

    public void setTargetFragment(SearchFragment searchFragment, int i) {
    }

}
