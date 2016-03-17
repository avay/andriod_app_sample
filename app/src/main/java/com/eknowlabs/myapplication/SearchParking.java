package com.eknowlabs.myapplication;
import com.parse.ParseClassName;
import com.parse.ParseObject;
/**
 * Created by Avay on 1/23/2016.
 */
@ParseClassName("SearchParking")
public class SearchParking extends ParseObject{
    public SearchParking(){
    }

    public String getLocation(){
        return getString("Location");
    }

    public String getParkingName(){
        return getString("parkingName");
    }

}