package org.p010_spy;


public class TemperatureMonitor {
    private double threshold;
    private boolean alert = false;
    private final AlertNotifier notifier;

    public TemperatureMonitor(double threshold, AlertNotifier notifier) {
        this.threshold = threshold;
        this.notifier = notifier;
    }

    public void addReading(double temp) {
        if (temp > threshold && !alert) {
            alert = true;
            notifier.sendAlert();
        }
    }

    public boolean isAlert() {
        return alert;
    }

    public void resetAlert() {
        alert = false;
    }
}


interface AlertNotifier {
    void sendAlert();
}
