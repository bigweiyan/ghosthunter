package com.hunter.sensor;

/**
 * 传感器接口.
 * Created by weiyan on 2016/11/13.
 */

public interface SensorSupport {

    /**
     * 检查是否可以获得位置数据.
     * @return true：可以获得;false：无法获得.
     */
    public boolean checkSensor();

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
