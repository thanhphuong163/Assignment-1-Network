package Database;


import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class Database {
    private static final String host = "localhost";
    private static final int port = 27017;
    private MongoCollection collection;

    public Database() {
        MongoClient mongo = new MongoClient(host,port);
        MongoDatabase database = mongo.getDatabase("dataBase");
        collection = database.getCollection("collection");
    }

    public MongoCollection getCollection() {
        return collection;
    }
}