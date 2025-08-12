package org.p007_fake;

public class SensorService {
    private final SensorLogger logger;

    public SensorService(SensorLogger l) {
        this.logger = l;
    }

    public void processReading(double v) {
        logger.log(v);
    }
}

interface SensorLogger {
    void log(double value);
}