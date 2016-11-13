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
    public int checkSensor() {
        return SensorSupport.OK;
    }

    @Override
    public double getLongitude() throws SensorException {

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
