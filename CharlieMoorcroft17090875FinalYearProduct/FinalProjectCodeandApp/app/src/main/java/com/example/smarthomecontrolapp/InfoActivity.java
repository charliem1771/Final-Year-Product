package com.example.smarthomecontrolapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class InfoActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        TextView info = findViewById(R.id.info);
        info.setText(
                "When the application initially loads it will display an activity with three buttons. The most important of these buttons is the add new devices button, when this button is" +
                        "clicked it will open a new activity which contains four text boxes. These textViews are used to add new data to the database for devices, the most important of these four" +
                        "text boxes are the device serial number text box and the device type text box. The device serial number can be found  \n " +
                        "the phidget manager when inputing the device type there is 5 types of device which can be utilised. \n"+
                    "They are strictly typed as follows: \n" +
                        "   1. door\n" +
                        "   2. blinds\n" +
                        "   3. switch\n" +
                        "   4. light\n" +
                        "   5. rfid \n" +
                        "When a device is added it will allow for interaction with the connected phidget devices up to 12 devices can be added. There is also the device details page which is currently mostly non functional."
        );
    }
}
