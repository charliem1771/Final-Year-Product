package com.example.smarthomecontrolapp;
//Standard data class with getters and setters for utilisation in other classes
public class deviceData 
{
	String deviceName;
	String deviceRoom;
	int deviceSerialNumber;
	String deviceType;
	
	public deviceData(String deviceName, String deviceRoom, int deviceSerialNumber, String deviceType)
	{
		super();
		this.deviceName = deviceName;
		this.deviceRoom = deviceRoom;
		this.deviceSerialNumber = deviceSerialNumber;
		this.deviceType = deviceType;
	}

	public String getDeviceType()
	{
		return deviceType;
	}

	public void setDeviceType()
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
				+ deviceSerialNumber + "]";
	}
}
