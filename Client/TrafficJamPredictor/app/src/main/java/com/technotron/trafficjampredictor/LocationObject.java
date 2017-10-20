package com.technotron.trafficjampredictor;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by lephu on 10/14/2017.
 */

public class LocationObject {
    private JSONObject obj = null;
    private String android_id;
    private Double myLatitude;
    private Double myLongitude;

    LocationObject (String id, Double latitude, Double longitude){
        this.android_id = id;
        this.myLatitude = latitude;
        this.myLongitude = longitude;

        obj = new JSONObject();

        String currentTime = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss")
                .format(Calendar.getInstance().getTime());
        try {
            obj.put("ID", android_id);
            obj.put("Lat", myLatitude);
            obj.put("Long", myLongitude);
            obj.put("Time",currentTime);
        }
        catch (JSONException e){
            e.printStackTrace();
        }
    }
    public byte[] convertToBytes(){
        return this.obj.toString().getBytes();
    }

    public  String convertToStrings() {return this.obj.toString();}
}
