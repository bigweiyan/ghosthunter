package com.wxyz.framework.impl;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
/**
 * AndroidGame-AndroidInput-AccelerometerHandler
 * <p>为了使实现Input接口的AndroidInput类得到加速度数据</p>
 * <p>不直接访问，通过AndroidInput实例访问</p>
 * @author ThinkPad
 *
 */
public class AccelerometerHandler implements SensorEventListener {
	float accelX;
	float accelY;
	float accelZ;
	public AccelerometerHandler(Context context){
		SensorManager manager = (SensorManager)context
				.getSystemService(Context.SENSOR_SERVICE);
		if(manager.getSensorList(Sensor.TYPE_ACCELEROMETER).size()!=0){
			Sensor accelerometer = manager.getSensorList(Sensor.TYPE_ACCELEROMETER).get(0);
			manager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_GAME);
		}
	}
	@Override
	public void onSensorChanged(SensorEvent event) {
		accelX = event.values[0];
		accelY = event.values[1];
		accelZ = event.values[2];
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		//nothing
	}
	public float getAccelX(){
		return accelX;
	}
	public float getAccelY(){
		return accelY;
	}
	public float getAccelZ(){
		return accelZ;
	}
}
