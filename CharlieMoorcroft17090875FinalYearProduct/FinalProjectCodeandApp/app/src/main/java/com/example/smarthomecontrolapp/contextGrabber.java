package com.example.smarthomecontrolapp;

import android.app.Application;
import android.content.Context;
//A method to grab the context of the Application so any class can add to the UI
public class contextGrabber extends Application
{
    //Creating a static context
    private static Context mContext;

    //Setting the constructor to return the application context
    public static Context getContext()
    {
        return mContext;
    }
    //A on create method which gets the application context
    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
    }
}
