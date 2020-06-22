package com.example.smarthomecontrolapp;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;


//A method to save the Arrays used in the MainActivity
public class arrayManager
{
    //Creating the shredprefences and a object to edit them
    static SharedPreferences prefs;
    static SharedPreferences.Editor editor;

    //A method to set data to the prefs
    public static void setArrayPrefs(String arrayName, ArrayList<String> array, Context mContext)
    {
        //Setting the prefs object to equal a blank context
        prefs = mContext.getSharedPreferences("preferencename", 0);
        //Setting the editor to equal the prefs
        editor = prefs.edit();
        //Storing the arrays size
        editor.putInt(arrayName +"_size", array.size());
        //Iterating through the array and adding the string data
        for(int i = 0; i < array.size(); i++)
        {
            editor.putString(arrayName + "_" + i, array.get(i));
        }
        editor.apply();
    }
    //Exactly the same as the above method but utilising a Integer instead arrayList instead of a string
    public static void setArrayInt(String arrayName, ArrayList<Integer> array, Context mContext)
    {
        prefs = mContext.getSharedPreferences("preferencename", 0);
        editor = prefs.edit();
        editor.putInt(arrayName +"_size", array.size());
        for(int i = 0; i < array.size(); i++)
        {
            editor.putInt(arrayName + "_" + i, array.get(i));
        }
        editor.apply();
    }

    //A method to get the data stored in the arrayList
    public static ArrayList<String> getArrayPrefs(String arrayName, Context mContext)
    {
        //Setting prefs to equal blank context
        prefs = mContext.getSharedPreferences("preferencename", 0);
        //Getting the array data
        int size = prefs.getInt(arrayName + "_size", 0);
        //Creating a empty arrayList
        ArrayList<String> array = new ArrayList<>(size);
        //Iterating through the arrayList
        for(int i = 0; i < size; i++)
        {
            //Adding the data we have grabbed to the new array
            array.add(prefs.getString(arrayName + "_" + i, null));
        }
        //Retruning the arrayList
        return array;
    }
    //The same as the above method bu passing a ineteger to the arrayList
    public static ArrayList<Integer> getArrayInt(String arrayName, Context mContext)
    {
        prefs = mContext.getSharedPreferences("preferencename", 0);
        int size = prefs.getInt(arrayName + "_size", 0);
        ArrayList<Integer> array = new ArrayList<>(size);
        for(int i = 0; i < size; i++)
        {
            //Note we get int instead of string from prefs here
            array.add(prefs.getInt(arrayName + "_" + i, 0));
        }
        return array;
    }

    public static void removeArrayPrefs(String arrayName, ArrayList<String> array, Context mContext)
    {
        //Setting the prefs object to equal a blank context
        prefs = mContext.getSharedPreferences("preferencename", 0);
        //Setting the editor to equal the prefs
        editor = prefs.edit();
        //Storing the arrays size
        editor.putInt(arrayName +"_size", array.size());
        //Iterating through the array and adding the string data
        for(int i = 0; i < array.size(); i++)
        {
            editor.remove(arrayName + "_" + 0);
        }
        editor.apply();
    }
}
