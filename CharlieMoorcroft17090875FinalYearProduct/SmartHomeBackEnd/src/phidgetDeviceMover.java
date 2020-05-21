
import com.phidget22.*;

//A method to call the connected motor
public class phidgetDeviceMover 
{
	//Creating a RCServo
	static RCServo servo = null;
	
	public static RCServo getInstance() 
	{
		System.out.println("In singleton constructor");
	    if(servo == null) 
	    {
	       servo = phidgetDeviceMover();
	    }
	    return servo;
	}
	
	private static  RCServo phidgetDeviceMover() 
	{
		//Create new instance of servo board and start listening for motor changes
		//This method should only be called once when first constructing a servo instance
		try 
		{
			System.out.println("Constructing DeviceMover");
			//Create a new servo
			servo = new RCServo();
			//Start listening for motor interaction
			servo.open(2000);
		} 
		catch (PhidgetException e) 
		{
			e.printStackTrace();
		}
        return servo;
	}
	
	public static void moveServoTo(double motorPosition) 
	{
        try 
        {
        	// Get the servo that is available
        	phidgetDeviceMover.getInstance();
        	//Setting a maximum position our motor can move too
        	servo.setMaxPosition(210.0);
        	//Setting the target position using the data passed in the device callback
			servo.setTargetPosition(motorPosition);
			servo.setEngaged(true);
		}
        catch (PhidgetException e) 
        {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static int getSerialNumber() 
	{
		try 
		{
			//Getting the current instance of the device
			phidgetDeviceMover.getInstance();
			//Getting the serial number and storing it as a int
			int serialNumber = servo.getDeviceSerialNumber();
			//Returning the int
			return serialNumber;
		} 
		catch (Exception e) 
		{
			// TODO: handle exception
			return 0;
		}
	}
	
}
