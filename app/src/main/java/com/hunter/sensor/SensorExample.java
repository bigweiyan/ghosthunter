package com.hunter.sensor;

import java.util.Random;

/**
 * 传感器接口例程.
 * Created by weiyan on 2016/11/13.
 */

public class SensorExample implements SensorSupport {
    Random rand;

    public SensorExample() {
        rand = new Random();
    }

    @Override
    public boolean checkSensor() {
        return true;
    }

    @Override
    public double getLongitude() {
        return rand.nextDouble()*180;
    }

    @Override
    public double getLatitude() {
        return rand.nextDouble()*180;
    }

    @Override
    public int getDirection() {
        return rand.nextInt(360);
    }
}
