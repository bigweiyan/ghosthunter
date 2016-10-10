package com.wxyz.framework.impl;

import android.view.View.OnTouchListener;

import com.wxyz.framework.Input.TouchEvent;

import java.util.List;
/**
 * 触摸事件处理器
 * 2015/2/8
 * @author ThinkPad
 * 
 */
public interface TouchHandler extends OnTouchListener {
	public boolean isTouchDown(int pointer);
	public int getTouchX(int pointer);
	public int getTouchY(int pointer);
	public List<TouchEvent> getTouchEvents();
}
