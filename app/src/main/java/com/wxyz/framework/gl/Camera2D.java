package com.wxyz.framework.gl;

import com.wxyz.framework.gl.math.Vector2;
import com.wxyz.framework.impl.GLGraphics;

import javax.microedition.khronos.opengles.GL10;

public class Camera2D {
	public final Vector2 position;
	public float zoom;
	public final float frustumWidth;
	public final float frustumHeight;
	final GLGraphics glGraphics;
	
	/**
	 * 
	 * @param glGraphics
	 * 		GLGraphics实例
	 * @param frustumWidth
	 * 		视锥体宽度
	 * @param frustumHeight
	 * 		视锥体高度
	 */
	public Camera2D(GLGraphics glGraphics,float frustumWidth,float frustumHeight){
		this.glGraphics = glGraphics;
		this.frustumHeight = frustumHeight;
		this.frustumWidth = frustumWidth;
		this.position = new Vector2(frustumWidth/2,frustumHeight/2);
		this.zoom = 1.0f;
	}
	
	/**
	 * 自动处理：设置屏幕上渲染宽高度（像素）和照相机位置（世界坐标） 
	 */
	public void setViewportAndMatrics(){
		GL10 gl = glGraphics.getGL();
		gl.glViewport(0, 0, glGraphics.getWidth(), glGraphics.getHeight());
		gl.glMatrixMode(GL10.GL_PROJECTION);
		gl.glLoadIdentity();
		gl.glOrthof(position.x - frustumWidth * zoom / 2, 
				position.x + frustumWidth * zoom / 2, 
				position.y - frustumHeight * zoom / 2,
				position.y + frustumHeight * zoom / 2, 1, -1);
		gl.glMatrixMode(GL10.GL_MODELVIEW);
		gl.glLoadIdentity();
	}
	
	/**
	 * 注：此处数值以960*540的屏幕为准,左下角原点坐标系
	 * @param x 左下角x
	 * @param y	左下角y
	 * @param width
	 * @param height
	 */
	public void setViewportAndMatrics(int x,int y,int width,int height){
		GL10 gl = glGraphics.getGL();
		gl.glViewport(x*glGraphics.getWidth()/960,
				y*glGraphics.getHeight()/540, 
				width*glGraphics.getWidth()/960 ,
				height*glGraphics.getHeight() / 540);
		gl.glMatrixMode(GL10.GL_PROJECTION);
		gl.glLoadIdentity();
		gl.glOrthof(position.x - frustumWidth * zoom / 2, 
				position.x + frustumWidth * zoom / 2, 
				position.y - frustumHeight * zoom / 2,
				position.y + frustumHeight * zoom / 2, 1, -1);
		gl.glMatrixMode(GL10.GL_MODELVIEW);
		gl.glLoadIdentity();
	}
	
	/**
	 * 将触摸点屏幕坐标转化为世界坐标
	 * @param touch 屏幕坐标向量
	 */
	public void touchToWorld(Vector2 touch){
		touch.x = (touch.x / (float)glGraphics.getWidth()) * frustumWidth * zoom;
		touch.y = (1 - touch.y / (float)glGraphics.getHeight()) * frustumHeight * zoom;
		touch.add(position).sub(frustumWidth * zoom / 2, frustumHeight * zoom / 2);
	}
	/*
	 * 未考虑zoom
	 */
	/**
	 * 
	 * @param touch
	 * @param x
	 * @param y
	 * @param scalarX 屏幕轴单位长度/世界轴单位长度
	 */
	public void touchToWorld(Vector2 touch, int x,int y,float scalarX, float scalarY){
		touch.y = (glGraphics.getHeight() - touch.y);
		touch.x -= x * glGraphics.getWidth() / 960.0f;
		touch.y -= y * glGraphics.getHeight() / 540.0f;
		touch.x = touch.x *zoom / scalarX ;
		touch.y = touch.y *zoom / scalarY;
		touch.add(position).sub(frustumWidth * zoom / 2, frustumHeight * zoom/ 2);
	}
}
