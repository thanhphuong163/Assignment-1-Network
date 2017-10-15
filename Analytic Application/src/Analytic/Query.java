package Analytic;

import com.mongodb.client.MongoCollection;

import java.util.ArrayList;

public class Query {
    private ArrayList<GPS> data;
    private GPS pos;
    private MongoCollection collection;

    public Query(GPS pos, MongoCollection collection) {
        this.pos = pos;
        this.collection = collection;
    }

    public ArrayList<GPS> getData() {
        return null;
    }
}