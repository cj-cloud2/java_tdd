package org.p003;

public class TemperatureMonitor {
    private double threshold;
    private boolean alert = false;

    public TemperatureMonitor(double threshold) {
        this.threshold = threshold;
    }

    public void addReading(double temp) {
        if (temp > threshold) alert = true;
    }
    public boolean isAlert() {
        return alert;
    }
    public void resetAlert() {
        alert = false;
    }
}
