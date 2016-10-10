package com.wxyz.framework.impl;

import android.view.MotionEvent;
import android.view.View;

import com.wxyz.framework.Input.TouchEvent;
import com.wxyz.framework.Pool;
import com.wxyz.framework.Pool.PoolObjectFactory;

import java.util.ArrayList;
import java.util.List;
/**
 * 多点触摸事件处理器，适用于支持多点触摸的设备
 * 2015/2/8
 * @author ThinkPad
 *
 */
public class MultiTouchHandler implements TouchHandler {
	private static final int MAX_TOUCHPOINTS = 10;
	
	boolean[] isTouched = new boolean[MAX_TOUCHPOINTS];
	int[] touchX = new int[MAX_TOUCHPOINTS];
	int[] touchY = new int[MAX_TOUCHPOINTS];
	int[] id = new int[MAX_TOUCHPOINTS];
	Pool<TouchEvent> touchEventPool;
	List<TouchEvent> touchEvents = new ArrayList<TouchEvent>();
	List<TouchEvent> touchEventsBuffer = new ArrayList<TouchEvent>();
	float scaleX;
	float scaleY;
	/**
	 * 多点触摸事件处理器 适用于支持多点触摸的设备
	 * 2015/2/8
	 * @param view 被绑定触摸事件的对象
	 * @param scaleX X轴比例因子
	 * @param scaleY Y轴比例因子
	 */
	public MultiTouchHandler(View view,float scaleX,float scaleY){
		PoolObjectFactory<TouchEvent> factory =new PoolObjectFactory<TouchEvent>(){
			@Override
			public TouchEvent createObject(){
				return new TouchEvent();
			}
		};
		touchEventPool = new Pool<TouchEvent>(factory,100);
		view.setOnTouchListener(this);
		this.scaleX=scaleX;
		this.scaleY=scaleY;
	}
	@SuppressWarnings("deprecation")
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		synchronized(this){
			int action = event.getAction()&MotionEvent.ACTION_MASK;
			int pointerIndex = (event.getAction()&MotionEvent.ACTION_POINTER_ID_MASK)
					>> MotionEvent.ACTION_POINTER_ID_SHIFT;
			int pointerCount = event.getPointerCount();
			TouchEvent touchEvent;
			for (int i =0;i<MAX_TOUCHPOINTS;i++){
				if(i>=pointerCount){
					isTouched[i]=false;
					id[i]=-1;
					continue;
				}
				int pointerId = event.getPointerId(i);
				if(event.getAction() != MotionEvent.ACTION_MOVE && i!=pointerIndex ){
					continue;
				}
				switch(action){
				case MotionEvent.ACTION_DOWN:
				case MotionEvent.ACTION_POINTER_DOWN:
					touchEvent = touchEventPool.newObject();
					touchEvent.type = TouchEvent.TOUCH_DOWN;
					touchEvent.pointer = pointerId;
					touchEvent.x = touchX[i] = (int)(event.getX()*scaleX);
					touchEvent.y = touchY[i] = (int)(event.getY()*scaleY);
					isTouched[i]=true;
					id[i] = pointerId;
					touchEventsBuffer.add(touchEvent);
					break;
				case MotionEvent.ACTION_UP:
				case MotionEvent.ACTION_POINTER_UP:
				case MotionEvent.ACTION_CANCEL:
					touchEvent = touchEventPool.newObject();
					touchEvent.type = TouchEvent.TOUCH_UP;
					touchEvent.pointer = pointerId;
					touchEvent.x = touchX[i] = (int)(event.getX()*scaleX);
					touchEvent.y = touchY[i] = (int)(event.getY()*scaleY);
					isTouched[i]=false;
					id[i] = -1;
					touchEventsBuffer.add(touchEvent);
					break;
				case MotionEvent.ACTION_MOVE:
					touchEvent = touchEventPool.newObject();
					touchEvent.type = TouchEvent.TOUCH_DRAGGED;
					touchEvent.pointer = pointerId;
					touchEvent.x = touchX[i] = (int)(event.getX()*scaleX);
					touchEvent.y = touchY[i] = (int)(event.getY()*scaleY);
					isTouched[i]=true;
					id[i] = pointerId;
					touchEventsBuffer.add(touchEvent);
					break;
				}
			}
			return true;
		}
	}
	
	private int getIndex(int pointerId){
		for(int i=0;i<MAX_TOUCHPOINTS;i++){
			if(id[i]==pointerId){
				return i;
			}
		}
		return -1;
	}
	
	@Override
	public boolean isTouchDown(int pointer) {
		synchronized(this){
			int index = getIndex(pointer);
			if(index<0||index>=MAX_TOUCHPOINTS){
				return false;
			}else{
				return isTouched[index];
			}
		}
	}

	@Override
	public int getTouchX(int pointer) {
		synchronized(this){
			int index = getIndex(pointer);
			if(index<0||index>MAX_TOUCHPOINTS){
				return 0;
			}else{
				return touchX[index];
			}
		}
	}

	@Override
	public int getTouchY(int pointer) {
		int index = getIndex(pointer);
		if(index<0||index>MAX_TOUCHPOINTS){
			return 0;
		}else{
			return touchY[index];
		}
	}

	@Override
	public List<TouchEvent> getTouchEvents() {
		synchronized(this){
			int len = touchEvents.size();
			for (int i = 0 ;i<len;i++){
				touchEventPool.free(touchEvents.get(i));
			}
			touchEvents.clear();
			touchEvents.addAll(touchEventsBuffer);
			touchEventsBuffer.clear();
			return touchEvents;
		}
	}

}
