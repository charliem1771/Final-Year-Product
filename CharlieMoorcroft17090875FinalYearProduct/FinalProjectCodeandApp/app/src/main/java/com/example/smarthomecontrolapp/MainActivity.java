package com.example.smarthomecontrolapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;

import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.google.gson.Gson;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttTopic;

import java.util.ArrayList;

import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity
{
    //Int to identify which button is which
    public int id = 0;
    //Arrays to hold the values for UI
    public ArrayList<String> nameValue = new ArrayList<>();
    public ArrayList<String> roomValue = new ArrayList<>();
    public ArrayList<Integer> numValue = new ArrayList<>();
    public ArrayList<String> typeValue = new ArrayList<>();

    //Arrays to hold values for UI after on destroy
    public ArrayList<String> nameValue2 = new ArrayList<>();
    public ArrayList<String> roomValue2 = new ArrayList<>();
    public ArrayList<Integer> numValue2 = new ArrayList<>();
    public ArrayList<String> typeValue2 = new ArrayList<>();

    //Getting the device subscriber and server comms from a non static context
    public deviceSubscriber rfid = new deviceSubscriber();
    public serverComms sendData = new serverComms();

    //Connection details for the MQTT broker
    String userId = "17090875";
    String topicName = userId + "/";
    public static final String BROKER_URL = "tcp://broker.mqttdashboard.com:1883";
    private MqttClient mqttClient;

    //Gson Obj for conversions
    Gson gson = new Gson();

    //Layout stuff
    public LinearLayout linearLayoutOne,linearLayoutTwo,linearLayoutThree;
    public Button newBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        //Creating three intents and setting them to final
        final Intent intentOne = new Intent(MainActivity.this, newDevice.class);
        final Intent intentTwo = new Intent(MainActivity.this, deviceDetails.class);
        final Intent intentThree = new Intent(MainActivity.this, InfoActivity.class);
        //Grabbing two buttons
        Button buttonOne = findViewById(R.id.buttonAddDevice);
        Button buttonTwo = findViewById(R.id.deviceDetails);
        //Grabbing a text view
        TextView infoClick = findViewById(R.id.infoView);
        //Connecting to MQTT broker
        try
        {
            mqttClient = new MqttClient(BROKER_URL,userId,null);
            mqttClient.connect();
        }
        catch (MqttException e)
        {
            e.printStackTrace();
            System.exit(1);
        }
        //Calling the device Subscriber class
        rfid.start();
        infoClick.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                startActivityForResult(intentThree,1);
            }
        });
        //Creating two onclick listeners for our buttons, once clicked they will switch to the activity
        //Specified in the intents
        buttonOne.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                startActivityForResult(intentOne,1);
            }
        });

        buttonTwo.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                Bundle data = new Bundle();
                data.putStringArrayList("namesList",nameValue);
                intentTwo.putExtras(data);
                startActivityForResult(intentTwo,1);
            }
        });

        //Checking if the preferences editor in array manager has any data
        //It will only have data stored if the activity has been destroyed
        if(arrayManager.editor != null)
        {
            //Filling our second set of arrays with data,the methods are stored in the arrayManager class
            nameValue2 = arrayManager.getArrayPrefs("names",contextGrabber.getContext());
            roomValue2 = arrayManager.getArrayPrefs("rooms",contextGrabber.getContext());
            numValue2 = arrayManager.getArrayInt("numbers",contextGrabber.getContext());
            typeValue2 = arrayManager.getArrayPrefs("types",contextGrabber.getContext());
            //Iterating through the nameValue2 arrayList till all of our buttons our recreated
            for(int i = 0; i < nameValue2.size(); i++)
            {
                generateButton(nameValue2.get(i),roomValue2.get(i),numValue2.get(i),typeValue2.get(i));
                System.out.println("Redrawing buttons");
            }
        }
        else {
            System.out.println("We have no stored data");
        }
    }

    //Used to pass data for the dynamic UI between classes
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode,resultCode,data);
        //Checking if the request and result code are correct
        if(requestCode == 1 && resultCode == RESULT_OK)
        {
            //Getting all of the required data to generate the button
            //This data has been passed from the newDevice class
            String messageReturn = data.getStringExtra("bool");
            String dataName = data.getStringExtra("dataName");
            String dataRoom = data.getStringExtra("dataRoom");
            String dataNum = data.getStringExtra("dataNum");
            String dataType = data.getStringExtra("dataType");
            int dataNumber = Integer.parseInt(dataNum);
            //Making sure the device has been added to the server and its not
            //An RFID reader
            if (messageReturn.contains("true") && !dataType.contains("rfid"))
            {
                //Generate the button
                generateButton(dataName, dataRoom, dataNumber, dataType);
                Toast.makeText(contextGrabber.getContext(), "An " + dataType + " called " + dataName + " has been created", Toast.LENGTH_SHORT).show();
            }
            else
            {
                //Toast to tell us we have create a RFID device
                Toast.makeText(contextGrabber.getContext(), "An RFID by the name of " + dataName + " has been created", Toast.LENGTH_SHORT).show();
            }
        }
        //Checking if result code is true
        else if(resultCode == 2)
        {
            //The below code is non functional
            //It is as attempt to remove the Button from the dynamic UI
            String toDelete = data.getStringExtra("deleteItem");
            for(int i = 0; i < nameValue.size(); i++) {
                System.out.println("We deleting: " + nameValue.size());
                String buttonText = newBtn.getText().toString();
                if (buttonText == toDelete) {
                    linearLayoutOne.removeView(newBtn);
                }
            }
            nameValue.remove(toDelete);
            nameValue2.remove(toDelete);
        }
    }
    //The generateButton class
    public String generateButton(String nameVal, final String roomVal, final int numVal,String typeVal)
    {
        //Placing the data we need in arrayLists
        nameValue.add(nameVal);
        roomValue.add(roomVal);
        numValue.add(numVal);
        typeValue.add(typeVal);
        //Using the arrayManager class to fill our new arrayLists with data
        arrayManager.setArrayPrefs("names",nameValue,contextGrabber.getContext());
        arrayManager.setArrayPrefs("rooms",roomValue,contextGrabber.getContext());
        arrayManager.setArrayInt("numbers",numValue,contextGrabber.getContext());
        arrayManager.setArrayPrefs("types",typeValue,contextGrabber.getContext());
        //Creating a onClickListener
        View.OnClickListener btnclick = new View.OnClickListener()
        {
            //Using switch cases to allow for up to 12 dynamically created buttons
            @Override
            public void onClick(View view) {

                switch (view.getId()) {
                    case 1:
                //A method to send data to the web server, which also calls the MQTT publisher
                        publishAndSend(0);
                        break;
                    case 2:
                        publishAndSend(1);
                        break;
                    case 3:
                        publishAndSend(2);
                        break;
                    case 4:
                        publishAndSend(3);
                        break;
                    case 5:
                        publishAndSend(4);
                        break;
                    case 6:
                        publishAndSend(5);
                        break;
                    case 7:
                        publishAndSend(6);
                        break;
                    case 8:
                        publishAndSend(7);
                        break;
                    case 9:
                        publishAndSend(8);
                        break;
                    case 10:
                        publishAndSend(9);
                        break;
                    case 11:
                        publishAndSend(10);
                        break;
                    case 12:
                        publishAndSend(11);
                        break;
                    default:
                        throw new IllegalStateException("Unexpected value: " + view.getId());
                }
            }
        };
        //Getting the linear layouts from the XML
        linearLayoutOne = findViewById(R.id.linearLayoutOne);
        linearLayoutTwo = findViewById(R.id.linearLayoutTwo);
        linearLayoutThree = findViewById(R.id.linearLayoutThree);
        //Secondary check to prevent button generation for RFID devices
        if(!typeVal.contains("rfid")) {
                id += 1;
                //Creating a button and giving it an onclick listener
                newBtn = new Button(this);
                newBtn.setText(nameVal);
                newBtn.setId(id);
                newBtn.setOnClickListener(btnclick);
                newBtn.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                //A series of statements placing our buttons in seperate layouts based
                //Off of the device type
                if (linearLayoutOne != null && typeVal.contains("door") || typeVal.contains("blinds")) {
                    linearLayoutOne.addView(newBtn);
                }
                if (linearLayoutTwo != null && typeVal.contains("switch")) {
                    linearLayoutTwo.addView(newBtn);
                }
                if (linearLayoutThree != null && typeVal.contains("light")) {
                    linearLayoutThree.addView(newBtn);
                }
        }
        return nameVal;
    }
    //The MQTT publish method to publish data to motors and other devices
    public String publishDevice(String data)
    {
        try
        {
            //Getting the topic
            final MqttTopic dataTopic = mqttClient.getTopic(topicName+data);
            System.out.println(dataTopic);
            //Publishing the topic
            dataTopic.publish(new MqttMessage(data.getBytes()));
            System.out.println("Published data. Topic: " + dataTopic.getName() + " Message: " + data);
        }
        catch (MqttException e)
        {
            e.printStackTrace();
            System.exit(1);
        }
        return data;
    }

    //Method to get the data required to call the server
    public void publishAndSend(int currentVal)
    {
        //Creating temp variables
        String tempVal = "";
        String tempVal2 = "";
        int tempVal3;
        String tempVal4 = "";
        String deviceJson;
        //Creating a device object
        deviceData newData;
        //Setting the tempvalues, based off the position in the arrayList we are
        //Interacting with
        tempVal = nameValue.get(currentVal);
        tempVal2 = roomValue.get(currentVal);
        tempVal3 = numValue.get(currentVal);
        tempVal4 = typeValue.get(currentVal);
        System.out.println(tempVal2);
        //Placing the temp values in the data object
        newData = new deviceData(tempVal,tempVal2,tempVal3,tempVal4);
        //Convert to Json
        deviceJson = gson.toJson(newData);
        //Pass to the server
        sendData.sendToServer(deviceJson,"serialData");
        String send = "";
        System.out.println(send);
        send = sendData.result;
        System.out.println(send);
        //Send the data returned from the server to the MQTT publisher
        publishDevice(send);
    }

}
