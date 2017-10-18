package Analytic;

import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Iterator;

import static java.lang.System.out;

public class Database {
    public static void main(String args[]) throws UnknownHostException {
        MongoClient mongo = new MongoClient("localhost", 27017);
        MongoDatabase database = mongo.getDatabase("dataBase");
        MongoCollection collection = database.getCollection("collection");
        String ID = "7fe7bb0be209aa4f";
        double Lat = 10.769195117899654;
        double Long = 106.65856819599867;
        String time = "16-10-2017 09:19:30";
        Document doc = new Document()
            .append("ID", ID)
            .append("Long", Long)
            .append("Lat", Lat)
            .append("Time", time);
        collection.insertOne(doc);
        FindIterable<Document> iterDoc = collection.find();
        Iterator<Document> it = iterDoc.iterator();
        while (it.hasNext()) {
            out.println(it.next());
        }
    }
}
