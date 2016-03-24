package com.eknowlabs.myapplication;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;

import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnSearchButtonPressListener} interface
 * to handle interaction events.
 * Use the {@link SearchFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchFragment extends Fragment implements DateTimePicker.OnDateTimeSetListener {
    public static final String TAG = "fragment_tag";
    Calendar c = Calendar.getInstance();

    // TODO: Rename and change types of parameters
    private String str;
    private String from_date, to_date, location;
    private LinearLayout linearLayout;
    private Button btn;
    private AutoCompleteTextView autoCompleteTextView;
    private LinearLayout option;
    private TextView from_field, to_field, atvPlaces ;
    private int PLACE_AUTOCOMPLETE_REQUEST_CODE = 100;

    private OnSearchButtonPressListener mListener;

    public SearchFragment() {
    }

    public static SearchFragment newInstance(String param1, String param2) {
        return new SearchFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (linearLayout == null) {
            linearLayout = (LinearLayout) inflater.inflate(R.layout.fragment_search, container, false);
            atvPlaces = (TextView) linearLayout.findViewById(R.id.textView0);
            atvPlaces.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        Intent intent =
                                new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
                                        .build((Activity) getContext());
                        startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
                    } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e) {
                    }
                }
            });
            from_field = (TextView) linearLayout.findViewById(R.id.from_date_time);
            from_field.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    str = "from";
                    showDialog(str);
                }
            });
            to_field = (TextView) linearLayout.findViewById(R.id.to_date_time);
            to_field.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    str = "to";
                    showDialog(str);
                }
            });
            btn =(Button) linearLayout.findViewById(R.id.button1);
            btn.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            try{
                                from_date = (String) from_field.getText();
                                to_date = (String) to_field.getText();
                                location = (String) atvPlaces.getText();
                                mListener = (OnSearchButtonPressListener) getContext();
                                mListener.SearchButtonPress(from_date,to_date,location);
                            }
                            catch (ClassCastException e) {
                                throw new ClassCastException("Called Activity must implement OnFragmentInteractionListener");
                            }
                        }
                    });
        }
        String strDate = "Now";
        from_field.setText(strDate);
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.HOUR, 3);
        Date currentDate = cal.getTime();
        SimpleDateFormat sdf1 = new SimpleDateFormat("dd-MMM-yyyy hh:mm");
        strDate = sdf1.format(currentDate);
        to_field.setText(strDate);
        return linearLayout;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                final Place place = PlaceAutocomplete.getPlace(getContext(), data);
                final CharSequence name = place.getName();
                atvPlaces.setText(name);
                Log.i(TAG, "Place: " + place.getName());
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(getContext(), data);
                // TODO: Handle the error.
                Log.i(TAG, status.getStatusMessage());
            } else if (resultCode == Activity.RESULT_CANCELED) {
            }
        }
    }

    private void showDialog(String str) {
        FragmentManager fm = getActivity().getSupportFragmentManager();
        SimpleDateTimePicker simpleDateTimePicker = SimpleDateTimePicker.make("Set Date & Time Title", new Date(), this, fm, str);
        simpleDateTimePicker.show();
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnSearchButtonPressListener) {
            mListener = (OnSearchButtonPressListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void DateTimeSet(Date date, String target_view) {
        DateTime mDateTime = new DateTime(date);
        String text = mDateTime.getDateString();
        if (target_view == "from") from_field.setText(text);
        else if (target_view == "to") to_field.setText(text);
    }

    /*public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        ft=ft+"&$&calledfromSF";
        void onFragmentInteraction(String ft, String tt, String lc);
    }*/

}




