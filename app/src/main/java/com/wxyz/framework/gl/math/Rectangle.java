package com.wxyz.framework.gl.math;


public class Rectangle {
	public final Vector2 lowerLeft;
	public float width,height;
	/**
	 * 
	 * @param x 左下角横坐标
	 * @param y 左下角纵坐标
	 * @param width
	 * @param height
	 */
	public Rectangle(float x,float y,float width,float height){
		this.lowerLeft = new Vector2(x,y);
		this.width = width;
		this.height = height;
	}
}
