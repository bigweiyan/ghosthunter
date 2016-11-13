package com.hunter.sensor;

/**
 * 传感器接口.
 * Created by weiyan on 2016/11/13.
 */

public interface SensorSupport {
    /**
     * 没有传感器可以使用.
     */
    public static final int NO_START = 0;
    /**
     * 传感器正在初始化.
     */
    public static final int BUSY = 1;
    /**
     * 传感器准备就绪.
     */
    public static final int OK = 2;

    /**
     * 检查是否可以获得位置数据.
     * @return 参见静态变量NO_START,BUSY,OK.
     */
    public int checkSensor();

    /**
     * 得到经度.
     * @return 经度.
     * @exception SensorException
     */
    public double getLongitude() throws SensorException;

    /**
     * 得到纬度.
     * @return 纬度.
     * @exception SensorException
     */
    public double getLatitude() throws SensorException;

    /**
     * 得到方向.
     * @return 以0为正北，90为正东的方向数据（角度制）.
     * @exception SensorException
     */
    public int getDirection() throws SensorException;
}
