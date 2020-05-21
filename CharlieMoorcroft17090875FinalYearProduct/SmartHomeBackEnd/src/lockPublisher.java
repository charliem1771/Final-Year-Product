import org.eclipse.paho.client.mqttv3.*;
import com.google.gson.Gson;
import com.phidget22.*;

//The lockPublisher class used to publish data which we get from a RFID reader, this data is recieved by the app
public class lockPublisher 
{
	//Creating a new RFID reader
	RFID rfidReader = new RFID();
	//Creating a empty device data object
	deviceData rfidData = new deviceData("","",0,"");
	//Creating a gson
	Gson gson = new Gson();
	//Empty data object to hold server response
	public String data;
	//The requiered details for the MQTT publishing
	public static final String BROKER_URL = "tcp://mqtt.eclipse.org:1883";
    public static final String userid = "17090875";
    public static final String TOPIC_DOOR = userid + "/";
    private MqttClient client;
	
	public static void main(String[] args)throws PhidgetException,MqttException
	{
		
		lockPublisher lockManager = new lockPublisher();
	}
	
	//This method makes sure our lock exists on the database and sends its data to the server
	public lockPublisher() throws PhidgetException, MqttException
	{
		//Connecting to the MQTT broker
		try 
    	{
    		// Setting the object with the correct values
    		client = new MqttClient(BROKER_URL, userid);
            client.connect();
		} 
    	catch (MqttException e) 
    	{
    		e.printStackTrace();
            System.exit(1);
		}
		//Adding a tag listener to the rfidReader
		rfidReader.addTagListener(new RFIDTagListener() 
        {
        	//What to do when a tag is found
			public void onTag(RFIDTagEvent e) 
			{
				//A try catch surrounding the setting method for the device used  
				try 
				{
					//Getting the serial number of our device
					rfidData.setDeviceSerialNumber(rfidReader.getDeviceSerialNumber());
					System.out.println();
				} 
				catch (PhidgetException e1) 
				{
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				// Converting the rfidSensor string to json
				String RFIDJson = gson.toJson(rfidData);
				//Creating a serverComms object 
				serverComms comms = new serverComms();
				//Passing our RFID data to the server
				comms.sendToServer(RFIDJson,"rfidData");	
				//Getting the data from the server
				data = comms.result;
				System.out.println("Response data: " + data);
				
				//If the server gives a response publish the data
				if(data.equals("")) 
		        {
		        	System.out.println("ERROR INVALID LOCK");
		        }
		        else 
		        {
		        	try 
		        	{
						publishLock(data);
					} 
		        	catch (MqttException e1) 
		        	{
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
		        }
			}
        });
		//Listener for when the tag is removed
		rfidReader.addTagLostListener(new RFIDTagLostListener() 
	    {
			// What to do when a tag is lost
			public void onTagLost(RFIDTagLostEvent e) 
			{
					// optional print, used as debug here
				System.out.println("DEBUG: Tag lost: " + e.getTag());
			}
	    });
		
		//Open the RFID reader
		rfidReader.open(5000);
		//Listen for data for a while
		//Note this will time out
		try 
        {      
            System.out.println("\n\nGathering data\n\n");
            // Keeping the RFID open for 30 seconds
            pause(30000);
            rfidReader.close();
            System.out.println("\nClosed RFID Reader");      
        } 
        catch (PhidgetException ex) 
        {
            System.out.println(ex.getDescription());
        }
	}
	
	//The lock publish method
	public void publishLock(String result) throws MqttException
    {
    	// Setting the mqttTopic
    	final MqttTopic mqttTopic = client.getTopic(TOPIC_DOOR + "true");
    	System.out.println(mqttTopic);
    	//Publishing the topic to the subscriber located on the app
    	mqttTopic.publish(new MqttMessage(result.getBytes()));
    	System.out.println("Publishing data: " +mqttTopic.getName()+" message " + result);
    }
	
	//The pause method
	private void pause(int secs)
	{
        try 
        {
			Thread.sleep(secs*1000);
		} 
        catch (InterruptedException e1) 
        {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
}

