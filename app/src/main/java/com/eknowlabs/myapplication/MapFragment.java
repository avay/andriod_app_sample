package com.eknowlabs.myapplication;


import android.os.Bundle;
import android.support.v4.app.Fragment;
//import android.support.v4.app.FragmentManager;
//import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


/**
 * A simple {@link Fragment} subclass.
 */
public class MapFragment extends SupportMapFragment {

    private GoogleMap map;
    private SupportMapFragment mSupportMapFragment;
    private LatLng mPosition;
    private String pName;
    //private SupportMapFragment mSupportMapFragment;



    public MapFragment() {
        // Required empty public constructor
        //this.pName = "test";
    }

    public static MapFragment newInstance(LatLng position) {
        MapFragment frag = new MapFragment();
        frag.mPosition = position;
        return frag;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //super.onCreateView(inflater, container, savedInstanceState);
        //View v = super.onCreateView(arg0, arg1, arg2);
        super.onCreateView(inflater, container, savedInstanceState);
        View v =  inflater.inflate(R.layout.fragment_map, container, false);
        initMap();
        return v;
    }

    private void initMap() {
        mSupportMapFragment = (SupportMapFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.map_fragment);
        if (mSupportMapFragment == null) {
            //supportFragmentManager fragmentManager1 = getFragmentManager();
            //FragmentTransaction mp_ftr = getActivity().getSupportFragmentManager().beginTransaction();
            mSupportMapFragment = SupportMapFragment.newInstance();
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.map_fragment, mSupportMapFragment).commit();
        }
        else {
            mSupportMapFragment.getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(GoogleMap map) {
                    map.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                    map.moveCamera(CameraUpdateFactory.newLatLngZoom(mPosition, 15));
                    map.addMarker(new MarkerOptions().position(mPosition));
                    map.getUiSettings().setAllGesturesEnabled(true);
                    map.getUiSettings().setCompassEnabled(true);
                    map.getUiSettings().setZoomControlsEnabled(true);
                }
            });
        }
    }

    /*public void changeMap(LatLng position) {
       // mSupportMapFragment = (SupportMapFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.map_fragment);
        //MarkerOptions options = new MarkerOptions().position(latLng).title("I am here!");
        this.mPosition = position;
        this.pName = "test";
        mSupportMapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap map) {
                map.animateCamera(CameraUpdateFactory.newLatLngZoom(mPosition, 17), 2000, null);
                map.addMarker(new MarkerOptions().position(mPosition).title(pName));
            }
        });
        //map.moveCamera(CameraUpdateFactory.newLatLngZoom(position, 15));
    }*/

}
