package com.eknowlabs.myapplication;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseObject;
import com.parse.ParseUser;

/**
 * Created by Avay on 1/24/2016.
 */
public class SlipBeep extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        // Enable Local Datastore.
        Parse.enableLocalDatastore(this);
        // Add your initialization code here
        //Parse.initialize(this);
        Parse.initialize(new Parse.Configuration.Builder(getApplicationContext())
                .applicationId("QlpG1uupgRm8ZwO4QY1keqgO0nLRYKFL2ECz10qU")
                .clientKey("07vvto4wsd37f38GkhSuQ2sLh7RhmVzO5oURRrpc")
                .server("http://api.slipbeep.com/1/").build()
        );
        ParseObject.registerSubclass(SearchParking.class);

        ParseUser.enableAutomaticUser();
        ParseACL defaultACL = new ParseACL();
        // Optionally enable public read access.
        // defaultACL.setPublicReadAccess(true);
        ParseACL.setDefaultACL(defaultACL, true);
        }
}
