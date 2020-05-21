import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

//This is the serverComms class for the device back end it has the exact same methods and functionality
//As the serverComms class used in the device front end
public class serverComms {

	String result = "";
	public static String serverURL = "http://localhost:8080/SmartHomeBackEnd/deviceDAO";

	public String sendToServer(String deviceSensor,String typeParams)
    {
    	// Declaring a url
    	URL url;
    	// Creating the connection
        HttpURLConnection conn;
        BufferedReader rd;
        
        // Putting the appropriate encoding on the rfidsensor string
    	try 
    	{
    		deviceSensor = URLEncoder.encode(deviceSensor, "UTF-8");
		} 
    	catch (UnsupportedEncodingException e1) 
    	{
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
    	// Adding the correct request and the data to the serverUrl
        String fullURL = serverURL + "?"+typeParams+"="+deviceSensor;
        System.out.println("Sending data to: "+fullURL);  // DEBUG confirmation message
        String line;
       // String result = "";
        // Wrapping the code in a try catch
        try 
        {
           url = new URL(fullURL);
           conn = (HttpURLConnection) url.openConnection();
           conn.setRequestMethod("GET");
           rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
           // Request response from server to enable URL to be opened
           while ((line = rd.readLine()) != null) 
           {
        	  // Setting the result to be a response from the server
              result += line;
           }
           rd.close();
        } 
        catch (Exception e) 
        {
           e.printStackTrace();
        }
        System.out.println(result);
        // Returning the result 
        return result;    	    	
    }
}
