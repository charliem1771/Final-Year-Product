//Exactly the same as the deviceData class on the front end
public class deviceData 
{
	String deviceName;
	String deviceRoom;
	String deviceType;
	int deviceSerialNumber;
	
	public deviceData(String deviceName, String deviceRoom, int deviceSerialNumber, String deviceType) 
	{
		super();
		this.deviceName = deviceName;
		this.deviceRoom = deviceRoom;
		this.deviceType = deviceType;
		this.deviceSerialNumber = deviceSerialNumber;
	}

	public deviceData(String deviceName, String deviceRoom, Integer integer) {
	}
	
	public String getDeviceType() 
	{
		return deviceType;
	}

	public void setDeviceType(String deviceType) 
	{
		this.deviceType = deviceType;
	}
	

	public String getDeviceName() 
	{
		return deviceName;
	}
	
	public void setDeviceName(String deviceName) 
	{
		this.deviceName = deviceName;
	}
	
	public String getDeviceRoom() 
	{
		return deviceRoom;
	}
	
	public void setDeviceRoom(String deviceRoom) 
	{
		this.deviceRoom = deviceRoom;
	}
	
	public int getDeviceSerialNumber() 
	{
		return deviceSerialNumber;
	}
	
	public void setDeviceSerialNumber(int deviceSerialNumber) 
	{
		this.deviceSerialNumber = deviceSerialNumber;
	}
	
	@Override
	public String toString() 
	{
		return "deviceData [deviceName=" + deviceName + ", deviceRoom=" + deviceRoom + ", deviceSerialNumber="
				+ deviceSerialNumber + "deviceType="+deviceType+"]";
	}
}
