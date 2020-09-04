package com.example.smarthomecontrolapp;

import android.content.Intent;
import android.os.StrictMode;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class androidDAO
{
    public ArrayList<String> nameValue = new ArrayList<>();
    public ArrayList<String> roomValue = new ArrayList<>();
    public ArrayList<Integer> numValue = new ArrayList<>();
    public ArrayList<String> typeValue = new ArrayList<>();
    public boolean theValue = false;
    Connection conn = null;
    Statement stmt;
   // MainActivity activityData = new MainActivity();
    public void getDBConnection()
    {

        //Getting the right username and password for the mudfoot server
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
            System.out.println("We have created our db connection");
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

   public void check() {
      try {
           ResultSet rs;
           String query = "SELECT * FROM smart_devices;";
           try {
               getDBConnection();
               System.out.println(query);
               //execute the sql query
               rs = stmt.executeQuery(query);
               if (rs != null) {
                    theValue = true;
                   System.out.println("We have data!");
               } else {
                   System.out.println("We have no data!");
               }
               rs.close();
               closeConnection();
           } catch (SQLException e) {
               // TODO: handle exception
               System.out.println(e.getMessage());
           }
       } catch(Exception e)
       {
            System.out.println("Trying are best here");
       }
   }


        public ArrayList<String> getAll () throws SQLException
        {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            ResultSet rs = null;
            //Getting all the data from smartdevices table where the serialNumber matches the input
            String query = "SELECT * FROM smart_devices;";
            deviceData dataObj = new deviceData(null, null, 0, null);
            try {
                getDBConnection();
                System.out.println(query);
                //execute the sql query
                rs = stmt.executeQuery(query);
                //using a while loop to get all of the required values
                while (rs.next()) {
                    dataObj.setDeviceName(rs.getString("deviceName"));
                    dataObj.setDeviceRoom(rs.getString("deviceRoom"));
                    dataObj.setDeviceType(rs.getString("deviceType"));
                    //Setting the correct parameter of dataObj to contain the serial number from the resultset
                    dataObj.setDeviceSerialNumber(rs.getInt("deviceSerialNumber"));
                    String dataOne = dataObj.getDeviceName();
                    System.out.println("Its data one! : "+dataOne);
                    String dataTwo = dataObj.getDeviceRoom();
                    String dataThree = dataObj.getDeviceType();
                    int dataFour = dataObj.getDeviceSerialNumber();
                    //System.out.println("The Name data: " +  activityData.nameValue2);
                    nameValue.add(dataOne);
                    roomValue.add(dataTwo);
                    typeValue.add(dataThree);
                    numValue.add(dataFour);
                    System.out.println("The values post adding" +  nameValue +  roomValue +  typeValue +  numValue);

                }


            } catch (SQLException e) {
                // TODO: handle exception
                System.out.println(e.getMessage());
            } finally {
                //end connection
                if (rs != null) {
                    rs.close();
                }
                closeConnection();
            }
            return null;
        }
    }

