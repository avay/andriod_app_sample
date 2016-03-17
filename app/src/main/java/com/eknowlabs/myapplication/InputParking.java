package com.eknowlabs.myapplication;

import android.Manifest;
import android.content.Context;
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
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
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
        OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        ActivityCompat.OnRequestPermissionsResultCallback,
        LocationListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    private static final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 123;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    private CoordinatorLayout coordinatorLayout;
    private EditText inputName, inputEmail, inputPassword, inputSpaceCount;
    private TextInputLayout inputLayoutName, inputLayoutEmail, inputLayoutPassword, inputLayoutSpaceCount;
    private Button btnSignUp;
    private Spinner Parking_types_spinner, Property_types_spinner;
    private OnSubmitButtonPressListener mListener;
    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    private GoogleMap googleMap;
    private MapView mapView;
    private boolean mapsSupported = true;
    public static final String TAG = MainActivity.class.getSimpleName();
    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    private LocationRequest mLocationRequest;

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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (coordinatorLayout == null) {
            coordinatorLayout = (CoordinatorLayout) inflater.inflate(R.layout.fragment_input_parking, container, false);
            Parking_types_spinner = (Spinner) coordinatorLayout.findViewById(R.id.parking_types);
            ArrayAdapter<CharSequence> Parking_types_adapter = ArrayAdapter.createFromResource(getContext(), R.array.parking_types, android.R.layout.simple_spinner_item);
            Parking_types_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            Parking_types_spinner.setAdapter(Parking_types_adapter);
            Parking_types_spinner.setOnItemSelectedListener(this);
            Property_types_spinner = (Spinner) coordinatorLayout.findViewById(R.id.property_types);
            ArrayAdapter<CharSequence> Property_types_adapter = ArrayAdapter.createFromResource(getContext(), R.array.parking_types, android.R.layout.simple_spinner_item);
            Property_types_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            Property_types_spinner.setAdapter(Property_types_adapter);
            Property_types_spinner.setOnItemSelectedListener(this);
            inputLayoutSpaceCount = (TextInputLayout) coordinatorLayout.findViewById(R.id.input_layout_no_of_parking_space);

            //View v = inflater.inflate(R.layout.map_fragment, container, false);
            mapView = (MapView) coordinatorLayout.findViewById(R.id.mapView);
            mapView.onCreate(savedInstanceState);
            mapView.getMapAsync(this);
            //mapView.onResume();//needed to get the map to display immediately

            try {
                MapsInitializer.initialize(getActivity().getApplicationContext());
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (mGoogleApiClient == null) {
                mGoogleApiClient = new GoogleApiClient.Builder(getContext())
                        .addConnectionCallbacks(this)
                        .addOnConnectionFailedListener(this)
                        .addApi(LocationServices.API)
                        .build();
            }

            // Create the LocationRequest object
            mLocationRequest = LocationRequest.create()
                    .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                    .setInterval(10 * 1000)        // 10 seconds, in milliseconds
                    .setFastestInterval(1 * 1000); // 1 second, in milliseconds
            //googleMap = mapView.getMapAsync(OnMapReadyCallback);

            //Perform any camera updates here

            //return v;


            inputLayoutName = (TextInputLayout) coordinatorLayout.findViewById(R.id.input_layout_name);
            inputLayoutEmail = (TextInputLayout) coordinatorLayout.findViewById(R.id.input_layout_email);
            inputLayoutPassword = (TextInputLayout) coordinatorLayout.findViewById(R.id.input_layout_password);
            inputSpaceCount = (EditText) coordinatorLayout.findViewById(R.id.input_no_of_parking_space);
            inputName = (EditText) coordinatorLayout.findViewById(R.id.input_name);
            inputEmail = (EditText) coordinatorLayout.findViewById(R.id.input_email);
            inputPassword = (EditText) coordinatorLayout.findViewById(R.id.input_password);
            btnSignUp = (Button) coordinatorLayout.findViewById(R.id.btn_signup);
            inputSpaceCount.addTextChangedListener(new MyTextWatcher(inputSpaceCount));
            inputName.addTextChangedListener(new MyTextWatcher(inputName));
            inputEmail.addTextChangedListener(new MyTextWatcher(inputEmail));
            inputPassword.addTextChangedListener(new MyTextWatcher(inputPassword));
            btnSignUp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    submitForm();

                }
            });
        }
        return inflater.inflate(R.layout.fragment_input_parking, container, false);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            String teststr = "input parking fragment";
            mListener.SubmitButtonPress(teststr);
        }
    }

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
    public void onResume() {
        super.onResume();
        mGoogleApiClient.connect();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
            mGoogleApiClient.disconnect();
        }
        mapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
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

        Toast.makeText(getContext(), "Thank You!", Toast.LENGTH_SHORT).show();
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
    public void onMapReady(GoogleMap googleMap) {
        googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        googleMap.setMyLocationEnabled(true);
        googleMap.setTrafficEnabled(true);
        googleMap.setIndoorEnabled(true);
        googleMap.setBuildingsEnabled(true);
        googleMap.getUiSettings().setZoomControlsEnabled(true);
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.i(TAG, "Location services connected.");
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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

        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (mLastLocation != null) {
            //handleNewLocation(mLastLocation);
            // mLatitudeText.setText(String.valueOf(mLastLocation.getLatitude()));
            // mLongitudeText.setText(String.valueOf(mLastLocation.getLongitude()));
        }
        else mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
    }

    private void handleNewLocation(Location mLastLocation) {
        double currentLatitude = mLastLocation.getLatitude();
        double currentLongitude = mLastLocation.getLongitude();
        LatLng latLng = new LatLng(currentLatitude, currentLongitude);
        String latlong = String.valueOf(mLastLocation.getLatitude()) + String.valueOf(mLastLocation.getLongitude());
        //Toast.makeText(MainActivity.this, latlong, Toast.LENGTH_SHORT).show();
        if (latLng != null) {
            MarkerOptions options = new MarkerOptions().position(latLng).title("I am here!");
            googleMap.addMarker(options);
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        }
        //String latlong = String.valueOf(mLastLocation.getLatitude()) + String.valueOf(mLastLocation.getLongitude());
        //Toast.makeText(MainActivity.this, latlong, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }
            // other 'case' lines to check for other
            // permissions this app might request
        }
    }


    @Override
    public void onConnectionSuspended(int i) {
        Log.i(TAG, "Location services suspended. Please reconnect.");

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        if (connectionResult.hasResolution()) {
            try {
                // Start an Activity that tries to resolve the error
                connectionResult.startResolutionForResult(getActivity(), CONNECTION_FAILURE_RESOLUTION_REQUEST);
            } catch (IntentSender.SendIntentException e) {
                e.printStackTrace();
            }
        } else {
            Log.i(TAG, "Location services connection failed with code " + connectionResult.getErrorCode());
        }

    }
    /*private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }*/

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
       // handleNewLocation(location);
    }


}


