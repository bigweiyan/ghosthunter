package com.wxyz.glbasics.gamedev2d;

public class StaticGridObject {
	public int x,y;
	public int width,height;
	/**
	 * 以世界坐标为准
	 * @param x 左上角网格x
	 * @param y 左上角网格y
	 * @param width	图像宽度
	 * @param height 图像高度
	 */
	public StaticGridObject(int x,int y,int width,int height){
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}
	
	public StaticGridObject(int x,int y){
		this.x = x;
		this.y = y;
		this.width = 1;
		this.height = 1;
	}
}
