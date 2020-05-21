

import java.io.*;
import java.sql.*;
import java.util.*;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import com.google.gson.Gson;
//Setting a servlet so we can get the class
@WebServlet("/deviceDAO")
//The deviceDAO a class used for querying the database allowing for the creation and activation of devices
public class deviceDAO extends HttpServlet
{
	//Setting up gson and connection objects
	private static final long serialVersionUID = 1L;
	Connection conn = null;
	Statement stmt;
	Gson gson = new Gson();
	//Creating a configuration for the servlet
	public void init(ServletConfig config) throws ServletException
	{
		super.init(config);
		System.out.println("Up and running!");
	}
	//A method to get the databases connection
	private void  getDBConnection() 
	{
		//Getting the right username and oassiwrd for the mudfoot server
		String user = "moorcroc";
	    String password = "Exdrangl3";
	    //The mudfoot server where our SQL table is stored
	    String url = "jdbc:mysql://mudfoot.doc.stu.mmu.ac.uk:6306/"+user;
		// Load the database driver
		try 
		{  
			Class.forName("com.mysql.jdbc.Driver").newInstance();
		} 
		catch (Exception e) 
		{
			System.out.println(e);        
		}
		// get a connection with the user/pass
	    try 
	    {
	    	//Setting the connection object to match the password user and correct URL
	        conn = DriverManager.getConnection(url, user, password);
	        //Creating a statement object for database queries
	        stmt = conn.createStatement();
	    } 
	    catch (SQLException se) 
	    {
	    	System.out.println(se);
	        System.out.println("\nDid you alter the lines to set user/password in the server code?");
	    }
	}
	
	//Method to close the connection to the mudfoot server 
	private void closeConnection() 
	{
	    try 
	    {
	    	conn.close();
	    } 
	    catch (Exception e) 
	    {
	    	System.out.println(e);
	    }
	}
	//The do get method we use to get data sent by the server commms class both on the app front end and backend
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException,IOException
	{
		response.setStatus(HttpServletResponse.SC_OK);
		//Creating a empty device object
		deviceData devices = new deviceData(null,null,0,null);
		
		//Below are four strings each of which requesting a different parameter
		String rfidJson = request.getParameter("rfidData");
		String newDeviceJson =request.getParameter("newDevice");
		String serialJson = request.getParameter("serialData");
		String delete = request.getParameter("deleteData");
		
		//checking if the parameters carries data
		//Note for some reason passing the data in Json for this specific operation caused a lot of problems
		//Not sure why
		if(delete != null) 
		{
			try 
			{
				//Placing data in a string
				String data = delete.toString();
				//Passing to our delete method
				String result = DeleteByName(data);
				//Creating a writer and getting the result
				PrintWriter out = response.getWriter();
				out.println(result);
			} 
			catch (SQLException e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		//The below three conditional statements follow the same structure as the initial one
		if(serialJson != null) 
		{
			serialJson.toString();	
			//The object must be converted back from Json for use doing that here
			devices = gson.fromJson(serialJson,deviceData.class);
			try 
			{
				String data = String.valueOf(devices.getDeviceSerialNumber());
				String result = SelectBySerialNumber(data);
				PrintWriter out = response.getWriter();
				out.println(result);
			} 
			catch (SQLException e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		if(rfidJson != null) 
		{
			rfidJson.toString();
			devices = gson.fromJson(rfidJson,deviceData.class);
			try 
			{
				String result = GetDeviceName(devices);
				PrintWriter out = response.getWriter();
				out.println(result);
			} 
			catch (SQLException e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		if(newDeviceJson != null) 
		{
			newDeviceJson.toString();
			devices = gson.fromJson(newDeviceJson, deviceData.class);
			try 
			{
				boolean result = InsertNewDevice(devices);
				PrintWriter out = response.getWriter();
				out.println(result);
			} 
			catch (SQLException e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	//A method to get the deviceBased off its serial number, this device is called if serialJson is not null
	public String SelectBySerialNumber(String data) throws SQLException
	{
		//Creating a empty string to store the data
		String sensorJson = "";
		ResultSet rs = null;
		//Getting all the data from smartdevices table where the serialNumber matches the input
		String query = "SELECT * FROM smart_devices WHERE deviceSerialNumber ='" +data+"';";
		deviceData dataObj = new deviceData(null,null,null);
		try 
		{
			getDBConnection();
			System.out.println(query);
			//execute the sql query
			rs = stmt.executeQuery(query);
			//using a while loop to get all of the required values
			while(rs.next()) 
			{
				//Setting the correct parameter of dataObj to contain the serial number from the resultset
				dataObj.setDeviceSerialNumber(rs.getInt("deviceSerialNumber")); 
				//Convert the deviceObj to Json
				sensorJson = gson.toJson(dataObj.getDeviceSerialNumber());
			}
		} 
		catch (SQLException e) 
		{
			// TODO: handle exception
			System.out.println(e.getMessage());
		}
		finally 
		{
			//end connection
			if(rs != null) 
			{
				rs.close();
			}
			closeConnection();
		}
		System.out.println(sensorJson);
		return sensorJson;
	}
	
	//The below method is used to get the names of RFID readers, it has the same structure as the selectBySerialNumber method
	//It returns the name of the device and not its serialnumber
	public String GetDeviceName(deviceData data) throws SQLException
	{
		String sensorJson = "";
		ResultSet rs = null;
		String query = "SELECT * FROM smart_devices WHERE deviceSerialNumber ='" +data.getDeviceSerialNumber()+"';";
		try 
		{
			getDBConnection();
			System.out.println(query);
			rs = stmt.executeQuery(query);
			while(rs.next()) 
			{
				data.setDeviceName(rs.getString("deviceName")); 
				sensorJson = gson.toJson(data.getDeviceName());
			}
		} 
		catch (SQLException e) 
		{
			// TODO: handle exception
			System.out.println(e.getMessage());
		}
		finally 
		{
			if(rs != null) 
			{
				rs.close();
			}
			closeConnection();
		}
		System.out.println(sensorJson);
		return sensorJson;
	}
	//A method to delete values from the database
	public String DeleteByName(String name) throws SQLException
	{
		//A query to delete database entries based of there name
		String query = "DELETE FROM smart_devices WHERE deviceName = '"+name+"';";
		//Getting the connection and performing the delete operartion
		try 
		{
			getDBConnection();
			System.out.println("Performing delete operation");
			stmt.executeUpdate(query);
			System.out.println("Delete Operation successful");
		} 
		catch(Exception e) 
		{
			System.err.println(e.getClass().getName()+": "+e.getMessage());
		}
		finally 
		{
			closeConnection();
		}
		
		return name;
	}
	
	//This method inserts new devices into the database, it is of type boolean so if the insert succeeds and returns true
	//The app frontend will know this and react acccordingly
	public boolean InsertNewDevice(deviceData createNew) throws SQLException
	{
		System.out.println("Entering insert");
		
		//setting up the prepared statement which will be used to insert data
		String insertDb = "INSERT INTO smart_devices(deviceName,deviceRoom,deviceSerialNumber,deviceType)VALUES('"+createNew.getDeviceName()+"','"+createNew.getDeviceRoom()+"','"+createNew.getDeviceSerialNumber()+"','"+createNew.getDeviceType()+"');";
		
		boolean check = false;
		//Executing the statement and opening connection
		try 
		{
			getDBConnection();
			stmt.executeUpdate(insertDb);
			System.out.println(insertDb);
			//Setting check to true
			check = true;
		}
		catch (Exception e) 
		{
			System.out.println(e.getMessage());
		}
		finally 
		{
			//Closing the connection
			closeConnection();
		}
		System.out.println(check);
		//Returning our boolean
		return check;
	}
}
