package com.eknowlabs.myapplication;

import android.Manifest;
import android.app.Activity;
import android.app.Application;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.NestedScrollView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnSubmitButtonPressListener} interface
 * to handle interaction events.
 * Use the {@link InputParking#newInstance} factory method to
 * create an instance of this fragment.
 */
public class InputParking extends Fragment implements AdapterView.OnItemSelectedListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        ActivityCompat.OnRequestPermissionsResultCallback,
        LocationListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    private static final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 123;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final int REQUEST_CHECK_SETTINGS = 101;
    private String mParam1;
    private String mParam2;
    private CoordinatorLayout coordinatorLayout;
    private NestedScrollView nestedScrollView;
    private LinearLayout MapLayout;
    private EditText inputName, inputEmail, inputPassword, inputSpaceCount;
    private TextInputLayout inputLayoutName, inputLayoutEmail, inputLayoutPassword, inputLayoutSpaceCount;
    private Button btnSignUp;
    private Spinner Parking_types_spinner, Property_types_spinner;
    //private OnSubmitButtonPressListener mListener;
    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    private GoogleMap googleMap;
    private MapView mapView;
    private boolean mapsSupported = true;
    private boolean access_location_checked = true;
    public static final String TAG = MainActivity.class.getSimpleName();
    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    private LocationRequest mLocationRequest;
    private Location lastLocation;
    private Location currentLocation;
    private MapFragment mMapFragment;
    private OnSubmitButtonPressListener mListener;


    public InputParking() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment InputParking.
     */
    // TODO: Rename and change types and number of parameters
    public static InputParking newInstance(String param1, String param2) {
        InputParking fragment = new InputParking();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }

        // Create the LocationRequest object
        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(1000)        // 10 seconds, in milliseconds
                .setFastestInterval(1000); // 1 second, in milliseconds
        //googleMap = mapView.getMapAsync(OnMapReadyCallback);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (nestedScrollView == null) {
            nestedScrollView = (NestedScrollView) inflater.inflate(R.layout.fragment_input_parking, container, false);

            Parking_types_spinner = (Spinner) nestedScrollView.findViewById(R.id.parking_types);
            ArrayAdapter<CharSequence> Parking_types_adapter = ArrayAdapter.createFromResource(getContext(), R.array.parking_types, android.R.layout.simple_spinner_item);
            Parking_types_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            Parking_types_spinner.setAdapter(Parking_types_adapter);
            Parking_types_spinner.setOnItemSelectedListener(this);
            Property_types_spinner = (Spinner) nestedScrollView.findViewById(R.id.property_types);
            ArrayAdapter<CharSequence> Property_types_adapter = ArrayAdapter.createFromResource(getContext(), R.array.parking_types, android.R.layout.simple_spinner_item);
            Property_types_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            Property_types_spinner.setAdapter(Property_types_adapter);
            Property_types_spinner.setOnItemSelectedListener(this);
            inputLayoutSpaceCount = (TextInputLayout) nestedScrollView.findViewById(R.id.input_layout_no_of_parking_space);
            inputLayoutName = (TextInputLayout) nestedScrollView.findViewById(R.id.input_layout_name);
            inputLayoutEmail = (TextInputLayout) nestedScrollView.findViewById(R.id.input_layout_email);
            inputLayoutPassword = (TextInputLayout) nestedScrollView.findViewById(R.id.input_layout_password);
            inputSpaceCount = (EditText) nestedScrollView.findViewById(R.id.input_no_of_parking_space);
            inputName = (EditText) nestedScrollView.findViewById(R.id.input_name);
            inputEmail = (EditText) nestedScrollView.findViewById(R.id.input_email);
            inputPassword = (EditText) nestedScrollView.findViewById(R.id.input_password);
            btnSignUp = (Button) nestedScrollView.findViewById(R.id.btn_signup);
            inputSpaceCount.addTextChangedListener(new MyTextWatcher(inputSpaceCount));
            inputName.addTextChangedListener(new MyTextWatcher(inputName));
            inputEmail.addTextChangedListener(new MyTextWatcher(inputEmail));
            inputPassword.addTextChangedListener(new MyTextWatcher(inputPassword));
            //MapLayout = (LinearLayout) nestedScrollView.findViewById(R.id.my_map_fragment);
            btnSignUp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //submitForm();
                    if (mListener != null && currentLocation != null) {
                        double currentLatitude = currentLocation.getLatitude();
                        double currentLongitude = currentLocation.getLongitude();
                        LatLng latLng = new LatLng(currentLatitude, currentLongitude);
                        String latlong = String.valueOf(currentLocation.getLatitude()) + String.valueOf(currentLocation.getLongitude());
                        Toast.makeText(getContext(), latlong, Toast.LENGTH_SHORT).show();
                        //String teststr = "input parking fragment";
                        mListener.SubmitButtonPress(latLng);
                    }
                    //handleNewLocation(currentLocation);

                }
            });

                FragmentManager fm1 = getChildFragmentManager();
                mMapFragment = MapFragment.newInstance(new LatLng(0, 0));
                FragmentTransaction ip_ftr = fm1.beginTransaction();
                ip_ftr.add(R.id.my_map_fragment, mMapFragment);
                ip_ftr.commit();
                //fm.executePendingTransactions();

        }
        //return inflater.inflate(R.layout.fragment_input_parking, container, false);
        return nestedScrollView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    /*public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            String teststr = "input parking fragment";
            mListener.SubmitButtonPress(teststr);
        }
    }*/

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnSubmitButtonPressListener) {
            mListener = (OnSubmitButtonPressListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onStart() {
        //mGoogleApiClient.connect();
        mGoogleApiClient.connect();
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        mGoogleApiClient.connect();
//        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
            mGoogleApiClient.disconnect();
        }
       // mapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
       // mapView.onLowMemory();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    private void submitForm() {

        if (!validateSpaceCount()) {
            return;
        }

        if (!validateName()) {
            return;
        }

        if (!validateEmail()) {
            return;
        }

        if (!validatePassword()) {
            return;
        }

        Toast.makeText(getActivity(), "Thank You!", Toast.LENGTH_SHORT).show();
    }

    private boolean validateSpaceCount() {
        if (inputSpaceCount.getText().toString().trim().isEmpty()) {
            inputLayoutSpaceCount.setError(getString(R.string.err_msg_name));
            //requestFocus(inputName);
            //inputName.requestFocus();
            return false;
        } else {
            inputLayoutSpaceCount.setErrorEnabled(false);
        }
        return true;
    }

    private boolean validateName() {
        if (inputName.getText().toString().trim().isEmpty()) {
            inputLayoutName.setError(getString(R.string.err_msg_name));
            //requestFocus(inputName);
            //inputName.requestFocus();
            return false;
        } else {
            inputLayoutName.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validateEmail() {
        String email = inputEmail.getText().toString().trim();

        if (email.isEmpty() || !isValidEmail(email)) {
            inputLayoutEmail.setError(getString(R.string.err_msg_email));
            //requestFocus(inputEmail);
            //inputEmail.requestFocus();
            return false;
        } else {
            inputLayoutEmail.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validatePassword() {
        if (inputPassword.getText().toString().trim().isEmpty()) {
            inputLayoutPassword.setError(getString(R.string.err_msg_password));
            //inputPassword.requestFocus(inputPassword);
            //inputPassword.requestFocus();
            return false;
        } else {
            inputLayoutPassword.setErrorEnabled(false);
        }

        return true;
    }

    private static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String item = parent.getItemAtPosition(position).toString();
        // Showing selected spinner item
        Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (SlipBeep.APPDEBUG) {
            Log.i(SlipBeep.APPTAG, "inside on connected");
        }
        getLocationSettings();
    }

    private void getLocationSettings() {

        // If Google Play Services is available
        if (servicesConnected()) {

            Log.i(SlipBeep.APPTAG, "google api services available");
            LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                    .addLocationRequest(mLocationRequest);
            PendingResult<LocationSettingsResult> result =
                    LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient, builder.build());
            result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
                @Override
                public void onResult(@NonNull LocationSettingsResult locationSettingsResult) {
                    final Status status = locationSettingsResult.getStatus();
                    //final LocationSettingsStates states = locationSettingsResult.getLocationSettingsStates();
                    switch (status.getStatusCode()) {
                        case LocationSettingsStatusCodes.SUCCESS:
                            // All location settings are satisfied. The client can
                            // initialize location requests here.
                            getLocation();
                            break;
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                            // Location settings are not satisfied, but this can be fixed
                            // by showing the user a dialog.
                            try {
                                // Show the dialog by calling startResolutionForResult(),
                                // and check the result in onActivityResult().
                                Log.i(SlipBeep.APPTAG, "calling the location settings window");
                                status.startResolutionForResult(
                                        getActivity(), REQUEST_CHECK_SETTINGS);
                                Log.i(SlipBeep.APPTAG, "after location settings result activity callback");


                            } catch (IntentSender.SendIntentException e) {
                                // Ignore the error.
                            }
                            break;
                        case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                            // Location settings are not satisfied. However, we have no way
                            // to fix the settings so we won't show the dialog.
                            break;
                    }
                }
            });
        }
    }


        private void getLocation() {

            // Get the current location
            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ) {
                Log.i(SlipBeep.APPTAG, "permission to access fine location is not mentioned in manifest");
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
                return;
            }
            PendingResult<Status> pendingResult = LocationServices.FusedLocationApi.requestLocationUpdates(
                    mGoogleApiClient, mLocationRequest, this);
            Log.d(TAG, "Location update started ..............: ");
            //currentLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            if (currentLocation == null){
                Log.i(SlipBeep.APPTAG, "currentlocation is still null after fused location api settings");
            }
        }

    private boolean servicesConnected() {
        GoogleApiAvailability googleAPI = GoogleApiAvailability.getInstance();
        int resultCode = googleAPI.isGooglePlayServicesAvailable(getActivity());

        if (ConnectionResult.SUCCESS == resultCode) {
            Log.i(SlipBeep.APPTAG, "google play services is available");
            return true;
        } else {
            Dialog dialog = googleAPI.getErrorDialog(getActivity(), resultCode, 0);
            if (dialog != null) {
                ErrorDialogFragment errorFragment = new ErrorDialogFragment();
                errorFragment.setDialog(dialog);
                //FragmentManager fm_err = getActivity().getSupportFragmentManager();
                errorFragment.show(getActivity().getSupportFragmentManager(), "errorfragment");
            }
            return false;
        }
    }

    public static class ErrorDialogFragment extends DialogFragment {
        // Global field to contain the error dialog
        private Dialog mDialog;
        /**
         * Default constructor. Sets the dialog field to null
         */
        public ErrorDialogFragment() {
            super();
            mDialog = null;
        }
        /*
         * Set the dialog to display
         *
         * @param dialog An error dialog
         */
        public void setDialog(Dialog dialog) {
            mDialog = dialog;
        }
        /*
         * This method must return a Dialog to the DialogFragment.
         */
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            return mDialog;
        }
        /*public void show(FragmentManager fm_err, String errorfragment) {
        }*/
    }

    /*private void handleNewLocation(Location mLastLocation) {
        //if (mLastLocation == null) {
            ////String error_msg = " no location data recived";
            //Toast.makeText(getContext(), error_msg, Toast.LENGTH_SHORT).show();
        //}
        //else if (mLastLocation != null) {
            *//*double currentLatitude = mLastLocation.getLatitude();
            double currentLongitude = mLastLocation.getLongitude();
            LatLng latLng = new LatLng(currentLatitude, currentLongitude);
            String latlong = String.valueOf(mLastLocation.getLatitude()) + String.valueOf(mLastLocation.getLongitude());
            Toast.makeText(getContext(), latlong, Toast.LENGTH_SHORT).show();*//*
            //String parking_name = "test";
            //if (latLng != null) {
                //MapFragment newMapFragment = new MapFragment();
                mMapFragment.changeMap(latLng);
                //FragmentManager fm1 = getChildFragmentManager();
            *//*MapFragment newMapFragment = MapFragment.newInstance(latLng);
            FragmentTransaction ip_ftr = getChildFragmentManager().beginTransaction();
            ip_ftr.replace(R.id.my_map_fragment, newMapFragment);
            ip_ftr.commit();
            transaction.replace(R.id.fragment_container, fragment);*//*
                //MarkerOptions options = new MarkerOptions().position(latLng).title("I am here!");
                //googleMap.addMarker(options);
                //googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
           // }
        //}
        //String latlong = String.valueOf(mLastLocation.getLatitude()) + String.valueOf(mLastLocation.getLongitude());
        //Toast.makeText(MainActivity.this, latlong, Toast.LENGTH_SHORT).show();
    }*/

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        currentLocation = null;
                        return;
                    }
                    currentLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                } else {
                    currentLocation = null;
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
            }
        }
    }


    @Override
    public void onConnectionSuspended(int i) {
        if (SlipBeep.APPDEBUG) {
            // Log the result
            Log.i(SlipBeep.APPTAG, "Location services suspended. Please reconnect.");
        }

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        if (connectionResult.hasResolution()) {
            try {
                // Start an Activity that tries to resolve the error
                connectionResult.startResolutionForResult(getActivity(), CONNECTION_FAILURE_RESOLUTION_REQUEST);
            } catch (IntentSender.SendIntentException e) {
                if (SlipBeep.APPDEBUG) {
                    // Thrown if Google Play services canceled the original PendingIntent
                    Log.d(SlipBeep.APPTAG, "An error occurred when connecting to location services.", e);
                }
                e.printStackTrace();
            }
        } else {
            showErrorDialog(connectionResult.getErrorCode());
           // Log.i(TAG, "Location services connection failed with code " + connectionResult.getErrorCode());
        }
    }

    private void showErrorDialog(int errorCode) {
        // Get the error dialog from Google Play services
        GoogleApiAvailability googleAPI = GoogleApiAvailability.getInstance();
        Dialog errorDialog =
                googleAPI.getErrorDialog(getActivity(), errorCode,
                        CONNECTION_FAILURE_RESOLUTION_REQUEST);
        // If Google Play services can provide an error dialog
        if (errorDialog != null) {
            // Create a new DialogFragment in which to show the error dialog
            ErrorDialogFragment errorFragment = new ErrorDialogFragment();
            // Set the dialog in the DialogFragment
            errorFragment.setDialog(errorDialog);
            // Show the error dialog in the DialogFragment
            errorFragment.show(getActivity().getSupportFragmentManager(), SlipBeep.APPTAG);
        }
    }
    /*private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }*/

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        // Choose what to do based on the request code
        switch (requestCode) {
            // If the request code matches the code sent in onConnectionFailed
            case CONNECTION_FAILURE_RESOLUTION_REQUEST:
                switch (resultCode) {
                    // If Google Play services resolved the problem
                    case Activity.RESULT_OK:
                        if (SlipBeep.APPDEBUG) {
                            // Log the result
                            Log.d(SlipBeep.APPTAG, "Connected to Google Play services");
                        }
                        break;
                    // If any other result was returned by Google Play services
                    default:
                        if (SlipBeep.APPDEBUG) {
                            // Log the result
                            Log.d(SlipBeep.APPTAG, "Could not connect to Google Play services");
                        }
                        break;
                }
            case REQUEST_CHECK_SETTINGS:
                switch (resultCode) {
                    // If location settings fixed
                    case Activity.RESULT_OK:
                        if (SlipBeep.APPDEBUG) {
                            // Log the result
                            Log.i(SlipBeep.APPTAG, "In activity result callback after location settings enabled");
                            mGoogleApiClient.connect();
                            //getLocation();
                        }
                        break;
                    // If any other result was returned by Google Play services
                    default:
                        if (SlipBeep.APPDEBUG) {
                            // Log the result
                            Log.i(SlipBeep.APPTAG, "In activity result callback and setting location to null");
                            currentLocation = null;
                        }
                        break;
                }
                // If any other request code was received
            default:
                if (SlipBeep.APPDEBUG) {
                    // Report that this Activity received an unknown requestCode
                    Log.d(SlipBeep.APPTAG, "Unknown request code received by the mainactivity");
                }
                break;
        }
    }

    private class MyTextWatcher implements TextWatcher {

        private View view;

        private MyTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void afterTextChanged(Editable editable) {
            switch (view.getId()) {
                case R.id.input_no_of_parking_space:
                    validateSpaceCount();
                    break;
                case R.id.input_name:
                    validateName();
                    break;
                case R.id.input_email:
                    validateEmail();
                    break;
                case R.id.input_password:
                    validatePassword();
                    break;
            }
        }
    }

    @Override
    public void onLocationChanged(Location location) {
       //handleNewLocation(location);
        Log.d(SlipBeep.APPTAG, "getting current location on event - locationChanged");
        currentLocation = location;
    }


}


