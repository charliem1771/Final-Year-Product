package com.example.smarthomecontrolapp;

import android.os.AsyncTask;
import android.widget.Toast;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

//A class to listen for Input from RFID readers
public class deviceSubscriber implements MqttCallback
{
    //Getting data for the MQTT broker
    public static final String BROKER_URL = "tcp://mqtt.eclipse.org:1883";
    public static final String userid = "17090875";
    //Notice that this has a sub attached to the end to allow the client to subscribe
    public static final String clientId = userid + "-sub";
    private MqttClient mqttClient;
    //Creating a empty string for data that is recieved
    public String data;
    public deviceSubscriber()
    {
        //Connecting to the MQTT broker
        try
        {
            mqttClient = new MqttClient(BROKER_URL, clientId,null);
        }
        catch (MqttException e)
        {
            e.printStackTrace();
            System.exit(1);
        }
    }

    public void start()
    {
        try
        {
            // Setting the LockCallBack to be this class
            mqttClient.setCallback( this);
            mqttClient.connect();
            //Subscribe to correct topic
            final String topic = userid + "/true";
            //Setting the client to subscribe to the topic
            mqttClient.subscribe(topic);
            System.out.println("Listening for: " + topic);
        }
        catch (MqttException e)
        {
            e.printStackTrace();
            System.exit(1);
        }
    }
    // Main method
    public static void main(String[] args)
    {
        // Creating a LockMover object
        deviceSubscriber newSubscriber = new deviceSubscriber();
        // Calling the start method
        newSubscriber.start();
    }

    //The MQTT callback methods
    public void connectionLost(Throwable arg0)
    {
        // TODO Auto-generated method stub

    }

    public void deliveryComplete(IMqttDeliveryToken arg0)
    {
        // TODO Auto-generated method stub

    }

    public void messageArrived(String topic, MqttMessage message) throws Exception
    {
        //Getting the message sent by the RFID reader and storing it in the data string
        data = message.toString();
        //Starting and executing our async task
        AsyncTasks async = new AsyncTasks();
        async.execute();
    }

    //The async task used for interacting with the UI of the MainActviity
    public class AsyncTasks extends AsyncTask<String, String, String>
    {

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params)
        {
            String current = "";
            return  current;
        }

        @Override
        protected void onPostExecute(String s)
        {
            //Creating a toast message, this toast message will appear every time a RFID registered to our database
            //Is activated it will tell us the RFIDReaders name and say its activated
            Toast.makeText(contextGrabber.getContext(), data + " Is Activated", Toast.LENGTH_LONG).show();
        }
    }
}
