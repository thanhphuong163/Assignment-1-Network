package Analytic;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.text.SimpleDateFormat;

public class GPS {
    private String ID;
    private Double Longitude;
    private Double Latitude;
    private Date time;

    public GPS(String ID, double Longitude, double Latitude, Date time) {
        this.ID = ID;
        this.Longitude = Longitude;
        this.Latitude = Latitude;
        this.time = time;
    }

    public void GPS(String ID, Double Longitude, Double Latitude, Date time) {
        this.ID = ID;
        this.Longitude = Longitude;
        this.Latitude = Latitude;
        this.time = time;
    }

    public String getID() {
        return this.ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public Double getLongitude() {
        return this.Longitude;
    }

    public void setLongitude(Double Longitude) {
        this.Longitude = Longitude;
    }

    public Double getLatitude() {
        return this.Latitude;
    }

    public void setLatitude(Double Latitude) {
        this.Latitude = Latitude;
    }

    public Date getTime() {
        return this.time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public JSONObject toJson() {
        SimpleDateFormat df = new SimpleDateFormat("DD-MM-YYYY hh:mm:ss");
        JSONObject json = new JSONObject();
        try {
            json.put("ID", this.ID);
            json.put("Long", this.Longitude);
            json.put("Lat", this.Latitude);
            json.put("Time", df.format(this.time));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json;
    }
}
