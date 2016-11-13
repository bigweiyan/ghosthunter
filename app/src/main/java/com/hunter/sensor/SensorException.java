package com.hunter.sensor;

/**
 * 传感器异常类型.
 * Created by weiyan on 2016/11/13.
 */

public class SensorException extends Exception {
    /**
     * 无法得到传感器数据.
     */
    public static final String TIME_OUT = "Get sensor failed!";

    /**
     * 发生未定义错误.
     */
    public static final String UNKNOWN = "Unknown error!";

    public SensorException() {};
    public SensorException(String massage) {
        super(massage);
    }
}
