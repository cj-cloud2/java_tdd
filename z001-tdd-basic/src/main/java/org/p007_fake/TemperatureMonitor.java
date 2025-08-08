package org.p007_fake;

public class TemperatureMonitor {
    private final double threshold;
    private final NotificationDispatcher dispatcher;

    public TemperatureMonitor(double threshold, NotificationDispatcher d) {
        this.threshold = threshold;
        this.dispatcher = d;
    }

    public void addReading(double value) {
        if (value > threshold) dispatcher.dispatch("ALERT: " + value + " above " + threshold);
    }
}

interface NotificationDispatcher {
    void dispatch(String message);
}