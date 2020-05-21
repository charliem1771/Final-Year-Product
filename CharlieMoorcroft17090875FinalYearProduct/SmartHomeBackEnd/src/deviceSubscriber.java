import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;

import com.google.gson.Gson;
import com.phidget22.PhidgetException;


public class deviceSubscriber 
{
	// The url for the Mqtt server
    public static final String BROKER_URL = "tcp://broker.mqttdashboard.com:1883";
    // Setting the userid
    public static final String userid = "17090875";
    // Setting the client id with the subscriber string
    String clientId = userid + "-sub";
    // Creating a empty locksensor object
    deviceData device = new deviceData("","",0);
    Gson gson = new Gson();
    // Creating a empty string for json data
    String deviceJson = "";
    // Creating a empty string for the serverData
    String serverData;
    // Declaring the serverUrl
    public static String serverURL = "http://localhost:8080/SmartHomeBackEnd/deviceDAO";
    private MqttClient mqttClient;

    // Calling the LockMover client
    public deviceSubscriber() 
    {
    	try 
    	{
            mqttClient = new MqttClient(BROKER_URL, clientId);
        } 
    	catch (MqttException e) 
    	{
            e.printStackTrace();
            System.exit(1);
        } 
    }
    
    // The method to start subscribing
    public void start() throws PhidgetException 
    {
        try 
        {
        	// Setting the LockCallBack
	        mqttClient.setCallback(new deviceCallBack());
	        // Connecting to the MQTT client
	        mqttClient.connect();
        	// Get the serial number from the phidget motormover class
        	int serialNumber = phidgetDeviceMover.getSerialNumber();
        	System.out.println(serialNumber);
        	// Seeting the room id to the serialnumber
        	device.setDeviceSerialNumber(serialNumber);
        	// converting the data to Json
        	String deviceJson = gson.toJson(device);
        	serverComms comms = new serverComms();
			comms.sendToServer(deviceJson,"serialData");	
			serverData = comms.result;
			
        	System.out.println("Lock Response: " + serverData);

            //Subscribe to correct topic
            final String topic = userid + "/"+ serverData;

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
    public static void main(String[] args) throws PhidgetException, MqttException 
    {
    	
    	// Creating a LockMover object
		deviceSubscriber newSubscriber = new deviceSubscriber();
		// Calling the start method
		newSubscriber.start();
	}
    
}
