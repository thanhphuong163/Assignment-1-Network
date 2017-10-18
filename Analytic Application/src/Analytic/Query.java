package Analytic;

import com.mongodb.BasicDBObject;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import org.bson.Document;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

import static java.lang.System.out;

public class Query {
    private ArrayList<GPS> data;
    private GPS pos;
    private MongoCollection collection;
    private static final double r = 1.8e-4;
    ArrayList<String> IDs = new ArrayList<>();

    public Query(GPS pos, MongoCollection collection) {
        this.pos = pos;
        this.collection = collection;
    }

    private boolean inIDs(String s) {
        for (int i = 0; i < IDs.size(); i++) {
            if (s.equals(IDs.get(i))) {
                return true;
            }
        }
        return false;
    }

    public ArrayList<GPS> getData() throws ParseException {
        SimpleDateFormat df = new SimpleDateFormat("DD-MM-YYYY hh:mm:ss");
        double LongE = pos.getLongitude() + r;
        double LongW = pos.getLongitude() - r;
        double LatN = pos.getLatitude() + r;
        double LatS = pos.getLatitude() - r;
        Date time0 = pos.getTime();
//        BasicDBObject areaLong = new BasicDBObject();
//        BasicDBObject areaLat = new BasicDBObject();
//        areaLong.put("Long", new BasicDBObject("$gte",LongW).append("$lte",LongE));
//        areaLat.put("Lat", new BasicDBObject("$gte",LatS).append("$lte",LatN));
//        List<BasicDBObject> obj = new ArrayList<>();
//        obj.add(areaLat);
//        obj.add(areaLong);
//        BasicDBObject andQuery = new BasicDBObject();
//        andQuery.put("$and",obj);
        FindIterable<Document> iterDoc = collection.find();
        Iterator<Document> it = iterDoc.iterator();
//        while (it.hasNext()) {
//            out.println(it.next().toString());
//        }
//        ArrayList<GPS> data = new ArrayList<>();
//        it = iterDoc.iterator();
        while (it.hasNext()) {
            Document iter = it.next();
            String ID = iter.getString("ID");
            double Long = iter.getDouble("Long");
            double Lat = iter.getDouble("Lat");
            if (Long >= LongW && Long <= LongE && Lat >= LatS && Lat <= LatN) {
                if (!inIDs(ID)) IDs.add(ID);
            }
        }
        out.println("Number of vehicles: "+IDs.size());
        it = iterDoc.iterator();
        while (it.hasNext()) {
            GPS item;
            Document iter = it.next();
            String ID = iter.getString("ID");
            double Long = iter.getDouble("Long");
            double Lat = iter.getDouble("Lat");
            Date time = df.parse(iter.getString("Time"));
            if (time0.getTime() - time.getTime() <= 3*60*1000 && inIDs(ID)) {
                item = new GPS(ID, Long, Lat, time);
                data.add(item);
            }
        }
        out.println("Size of data: " + data.size());
        return data;
    }
}