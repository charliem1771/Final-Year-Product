package com.example.smarthomecontrolapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.gson.Gson;

import java.util.ArrayList;

//The unfinished deviceDetails class
public class deviceDetails extends AppCompatActivity
{
    //Creating a object to send to server
    serverComms sendData = new serverComms();
    //Creating a Gson
    Gson gson = new Gson();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_details);
        //Creating a listView
        ListView list = findViewById(R.id.deviceList);
        //Creating a arrayList and filling it with data from the mainActivity using intents
        //Note array list is set to final so it can be accessed in the inner on click method
        final ArrayList<String> nameValue = getIntent().getStringArrayListExtra("namesList");
        //Adding the data from our arrayList into a arrayAdapter
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this
                ,android.R.layout.simple_list_item_1
                ,nameValue);
        //Placing it in a list view
        list.setAdapter(arrayAdapter);
        //Setting a onClick listener for each element of the listView
        list.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Removes the clicked object from the listView
                //nameValue.remove(position);
                try
                {
                    //Creating a intent
                    Intent openMainActivity = new Intent(deviceDetails.this, MainActivity.class);
                    //Placing the name of the Device in a string
                    String deleteJson = nameValue.get(position);
                    //Sending it to the server
                    sendData.sendToServer(deleteJson, "deleteData");
                    //Placing the deleteJson object into a intent
                    openMainActivity.putExtra("deleteItem", deleteJson);
                    //Using setresult to activate the onActivityResult method in MainActivity
                    setResult(2, openMainActivity);
                    //Updates array adapter
                    //arrayAdapter.notifyDataSetChanged();
                    //Calls the MainActivity triggering onActivity result
                    finish();
                }
                catch (Exception e)
                {
                    System.out.println("Delete failed!");
                }
            }
        });
    }
}
