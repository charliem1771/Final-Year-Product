package com.example.smarthomecontrolapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.google.gson.Gson;
//A class to add new device data to the database, along with telling the MainActvity its okay to generate a button
public class newDevice extends AppCompatActivity
{
    //Creating empty strings we will fill with data from the get text
    public String nameVal;
    public String roomVal;
    public String numVal;
    public String typeVal;
    //Creating the GSON object
    Gson gson = new Gson();
    //Creating a serverComms object
    public serverComms sendData = new serverComms();
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_device);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        //Getting the editTexts from the XML
        final EditText name = findViewById(R.id.nameVal);
        final EditText room = findViewById(R.id.roomVal);
        final EditText number = findViewById(R.id.numVal);
        final EditText type = findViewById(R.id.typeVal);
        //Getting the button from the XML
        final Button button = findViewById(R.id.buttonVal);
        //Setting a onClickListener for our button
        button.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                //Getting the values from the editTexts and putting them in strings
                nameVal = name.getText().toString();
                roomVal = room.getText().toString();
                numVal = number.getText().toString();
                typeVal = type.getText().toString();
                //Integer conversion
                int numberVal = Integer.parseInt(numVal);
                //Placing the required values in a deviceData object,converting them to Json and sending them to the server
                deviceData newData =  new deviceData(nameVal,roomVal,numberVal,typeVal);
                String deviceJson = gson.toJson(newData);
                sendData.sendToServer(deviceJson,"newDevice");
                //If the data has been inserted into a database
                if(sendData.result.contains("true"))
                {
                    //Creating a intent and filling it with data we will use to generate buttons
                    Intent openMainActivity = new Intent(newDevice.this, MainActivity.class);
                    openMainActivity.putExtra("bool",sendData.result);
                    openMainActivity.putExtra("dataName",nameVal);
                    openMainActivity.putExtra("dataRoom",roomVal);
                    openMainActivity.putExtra("dataNum",numVal);
                    openMainActivity.putExtra("dataType",typeVal);
                    //Calling onActivityResult in the MainActivity, which will generate a button with our passed values
                    setResult(RESULT_OK,openMainActivity);
                    finish();
                }
            }
        });
    }

}
