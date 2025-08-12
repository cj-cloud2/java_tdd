package org.p008_dummy;


import java.util.ArrayList;
import java.util.List;

public class TemperatureSensor {
    private Authenticator authenticator;
    private List<Integer> readings = new ArrayList<>();
    public TemperatureSensor(Authenticator auth) {
        this.authenticator = auth;
    }
    public void record(int temp) {
        if (temp < -100 || temp > 200) throw new IllegalArgumentException();
        readings.add(temp);
    }
    public int getReadingsCount() { return readings.size(); }
}

interface Authenticator {
    boolean authenticate(String token);
}