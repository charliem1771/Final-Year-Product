
import org.eclipse.paho.client.mqttv3.*;

//The device callback method, this method sends data to move our motor
public class deviceCallBack implements MqttCallback
{
	
	@Override
	public void connectionLost(Throwable arg0) 
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deliveryComplete(IMqttDeliveryToken arg0) 
	{
		// TODO Auto-generated method stub
		
	}
	
	//The messageArrived method is used to call the phidgetDeviceMover class
	@Override
	public void messageArrived(String topic, MqttMessage message) throws Exception 
	{
		//Checking that the message has arrived from the applications front end
		System.out.println("Message arrived. Topic: " + topic + "  Message: " + message.toString());
        
        //Calling device motorMover to move the motor by a set amount
        phidgetDeviceMover.moveServoTo(180.0);
        //Wait for 2 sevonds
        Thread.sleep(2000);
        //Return to original position
        phidgetDeviceMover.moveServoTo(0.0);
        // End
        Thread.sleep(0);		
	}
	
}
