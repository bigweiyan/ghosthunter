package com.hunter.game.models;

/**
 * 信号源对象的封装.
 * Created by weiyan on 2016/11/12.
 */

public class Signal {
    public static final int FREQ_1 = 1;
    public static final int FREQ_2 = 2;
    public static final int FREQ_3 = 3;
    public static final int FREQ_4 = 4;
    public static final int FREQ_5 = 5;
    public static final int FREQ_6 = 6;

    /**
     * 纬度.
     */
    public double latitude;
    /**
     * 经度.
     */
    public double longitude;

    /**
     * 频率.
     */
    public int frequency;

    public Signal(double latitude, double longitude, int frequency) {
        this.frequency = frequency;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Signal() {
        this.latitude = 0;
        this.longitude = 0;
        this.frequency = 1;
    }
}
