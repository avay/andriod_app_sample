package com.eknowlabs.myapplication;
import com.parse.ParseClassName;
import com.parse.ParseObject;
/**
 * Created by Avay on 1/23/2016.
 */
@ParseClassName("Location")
public class Location extends ParseObject{
    public Location(){

    }

    public String getPlaceName(){
        return getString("placeName");
    }

    public String getParkingName(){
        return getString("parkingName");
    }

    public int getRent(){
        return getInt("rentPerHour");
    }

}