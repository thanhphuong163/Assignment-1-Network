package Analytic;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import static java.lang.System.out;

public class Database {
    public static void main(String args[]) {
        MongoClient mongo = new MongoClient("localhost",27017);
        MongoDatabase database = mongo.getDatabase("dataBase");
        out.println("Connected to database successfully.");
        MongoCollection collection = database.getCollection("collection");
        out.println("Got collection successfully.");
    }
}
