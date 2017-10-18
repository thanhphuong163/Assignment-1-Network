package Analytic;

import java.util.ArrayList;

import static java.lang.System.out;

public class Computing {
    private ArrayList<GPS> data;
    private static final Integer EarthR = 6371000; // meters
    // Placeholds
    private static final Double maxMean = 3.;
    private static final Double maxVar = 9.;

    public Computing(ArrayList<GPS> data) {
        this.data = data;
    }

    private Double Degree2Radian(Double degree) {
        return degree * Math.PI / 180;
    }

    public Double velocity(GPS pos1, GPS pos2) {
        // Calculate distance
        Double Dlon = Degree2Radian(Math.abs(pos1.getLongitude() - pos2.getLongitude()));
        Double Dlat = Degree2Radian(Math.abs(pos1.getLatitude() - pos2.getLatitude()));

        Double lat1 = Degree2Radian(pos1.getLatitude());
        Double lat2 = Degree2Radian(pos2.getLatitude());

        Double a = Math.sin(Dlat / 2) * Math.sin(Dlat / 2) + Math.sin(Dlon / 2) * Math.sin(Dlon / 2) * Math.cos(lat1) * Math.cos(lat2);
        Double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        // Calculate time
        Long Dtime = Math.abs(pos2.getTime().getTime() - pos1.getTime().getTime()) / 1000;
//        out.println("Distance = " + c*EarthR);
//        out.println("Dtime = " + Dtime);
        if (Dtime == 0) {
            return c;
        }
        else {
            return c * EarthR / Dtime;
        }
    }


    public String processing() {
        String result;
        Dict items = new Dict();
        for (int i = 0; i < this.data.size(); i++) {
            items.push(this.data.get(i));
        }
        //out.println(items.getKey(0));
        //out.println(items.getValue(0));
        for (int i = 0; i < items.size(); i++) {
            Double sumVelo = 0.;
            for (int j = 0; j < items.getListGPS(i).size() - 1; j++) {
                sumVelo += this.velocity(items.getListGPS(i).get(j), items.getListGPS(i).get(j + 1));
            }
//            out.println(sumVelo);
            Double v = sumVelo / items.getListGPS(i).size();
//            out.println(v);
            items.setAvgVelocities(i, v);
        }
        out.println("Number of Vehicles: " + items.size());
//        out.println(items.mean());
//        out.println(items.variance());
        if (items.mean() <= maxMean && items.variance() <= maxVar) {
            result = "Jam";
        }
        else {
            result = "Not jam";
        }
        //out.println(result);
        return result;
    }
}
