package com.example.smarthomecontrolapp;

import android.os.Bundle;
import android.os.StrictMode;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
//This class exists as a helper class to pass data to the webserver
public class serverComms
{
    public String result = "";
    //Creating a public string to return result
    //Url of the webserver
    public static String serverURL = "http://10.0.2.2:8080/SmartHomeBackEnd/deviceDAO";

    //Two parameters are passed the deviceData and the type of parameters we need, different types of parameters make
    //The backend do different things
    public String sendToServer(String deviceData,String typeParams)
    {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        // Declaring a url
        URL url;
        // Creating the connection
        HttpURLConnection conn;
        BufferedReader rd;
        // Putting the appropriate encoding on the deviceData string
        try
        {
            deviceData = URLEncoder.encode(deviceData, "UTF-8");
        }
        catch (UnsupportedEncodingException e1)
        {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        // Adding the correct request and the data to the serverUrl
        String fullURL = serverURL + "?"+typeParams+"="+deviceData;
        //Checking our URL is okay
        System.out.println("Sending data to: "+fullURL);
        //Setting result to null
        result = "";
        String line;
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
