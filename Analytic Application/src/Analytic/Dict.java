package Analytic;

import java.util.ArrayList;

public class Dict {
    private ArrayList<String> keys;
    private ArrayList<ArrayList<GPS>> values;
    private ArrayList<Double> avgVelocities;
    private int count;

    public Dict() {
        this.keys = new ArrayList<>();
        this.values = new ArrayList<>();
        this.avgVelocities = new ArrayList<>();
        this.count = 0;
    }

    public Dict(GPS value) {
        this.keys.add(value.getID());
        this.count++;
        ArrayList<GPS> valueList = new ArrayList<>();
        valueList.add(value);
        this.values.add(valueList);
        this.avgVelocities.add(0.);
    }

    public int size() {
        return this.count;
    }

    public int exists(GPS value) {
        for (int i = 0; i < this.keys.size(); i++) {
            if (this.keys.get(i).equals(value.getID())) {
                return i;
            }
        }
        return -1;
    }

    public ArrayList<GPS> getListGPS(int index) {
        return this.values.get(index);
    }

    public void setAvgVelocities(int index, Double v) {
        this.avgVelocities.set(index, v);
    }

    public void push(GPS value) {
        int index = this.exists(value);
        if (index >= 0) {
            this.values.get(index).add(value);
        } else {
            this.keys.add(value.getID());
            this.count++;
            ArrayList<GPS> valueList = new ArrayList<>();
            valueList.add(value);
            this.values.add(valueList);
            this.avgVelocities.add(0.);
        }
    }

    public Double mean() {
        Double sum = 0.;
        for (int i = 0; i < this.count; i++) {
            sum += this.avgVelocities.get(i);
        }
        return sum / this.count;
    }

    public Double variance() {
        Double sum = 0.;
        for (int i = 0; i < this.count; i++) {
            sum += Math.pow(this.avgVelocities.get(i) - this.mean(), 2.);
        }
        return sum / this.count;
    }
}