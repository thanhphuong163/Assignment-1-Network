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
import java.util.List;


public class Query {
    private ArrayList<GPS> data;
    private GPS pos;
    private MongoCollection collection;
    private static final double r = 1.8e-4;

    public Query(GPS pos, MongoCollection collection) {
        this.pos = pos;
        this.collection = collection;
    }

    public ArrayList<GPS> getData() throws ParseException {
        SimpleDateFormat df = new SimpleDateFormat("DD-MM-YYYY hh:mm:ss");
        double LongE = pos.getLongitude() + r;
        double LongW = pos.getLongitude() - r;
        double LatN = pos.getLatitude() + r;
        double LatS = pos.getLatitude() - r;
        Date time0 = pos.getTime();
        BasicDBObject areaLong = new BasicDBObject();
        BasicDBObject areaLat = new BasicDBObject();
        areaLong.put("Long", new BasicDBObject("$gte",LongW).append("$lte",LongE));
        areaLat.put("Lat", new BasicDBObject("$gte",LatS).append("$lte",LatN));
        List<BasicDBObject> obj = new ArrayList<>();
        obj.add(areaLat);
        obj.add(areaLong);
        BasicDBObject andQuery = new BasicDBObject();
        andQuery.put("$and",obj);
        FindIterable<Document> iterDoc = collection.find(andQuery);
        Iterator<Document> it = iterDoc.iterator();
        ArrayList<GPS> data = new ArrayList<>();
        while (it.hasNext()) {
            GPS item;
            Document iter = it.next();
            String ID = iter.getString("ID");
            double Long = iter.getDouble("Long");
            double Lat = iter.getDouble("Lat");
            Date time = df.parse(iter.getString("Time"));
            if (time0.getTime() - time.getTime() <= 3*60*1000) {
                item = new GPS(ID, Long, Lat, time);
                data.add(item);
            }
        }
        return data;
    }
}