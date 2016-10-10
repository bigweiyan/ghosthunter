package com.wxyz.framework.impl;

import android.content.Context;
import android.os.Build.VERSION;
import android.view.View;

import com.wxyz.framework.Input;

import java.util.List;
/**
 * AndroidGame-AndroidInput
 * <p>汇总AccelerometerHandler，KeyboardHandler，TouchHandler的数据</p>
 * @author ThinkPad
 *
 */
public class AndroidInput implements Input {
	AccelerometerHandler accelHandler;
	KeyboardHandler keyHandler;
	TouchHandler touchHandler;
	
	@SuppressWarnings("deprecation")
	public AndroidInput(Context context,View view,float scaleX,float scaleY){
		accelHandler = new AccelerometerHandler(context);
		keyHandler = new KeyboardHandler(view);
		if (Integer.parseInt(VERSION.SDK)<5){
			touchHandler = new SingleTouchHandler(view,scaleX,scaleY);
		}else{
			touchHandler = new MultiTouchHandler(view,scaleX,scaleY);
		}
	}
	
	@Override
	public boolean isKeyPressed(int keyCode) {
		return keyHandler.isKeyPressed(keyCode);
	}

	@Override
	public boolean isTouchDown(int pointer) {
		return touchHandler.isTouchDown(pointer);
	}

	@Override
	public int getTouchX(int pointer) {
		return touchHandler.getTouchX(pointer);
	}

	@Override
	public int getTouchY(int pointer) {
		return touchHandler.getTouchY(pointer);
	}

	@Override
	public float getAccelX() {
		return accelHandler.accelX;
	}

	@Override
	public float getAccelY() {
		return accelHandler.accelY;
	}

	@Override
	public float getAccelZ() {
		return accelHandler.accelZ;
	}

	@Override
	public List<KeyEvent> getKeyEvents() {
		return keyHandler.getKeyEvents();
	}

	@Override
	public List<TouchEvent> getTouchEvents() {
		return touchHandler.getTouchEvents();
	}

}
