package com.wxyz.framework.impl;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
/**
 * 渲染图像
 * 2015/2/8
 * @author ThinkPad
 *
 */
public class AndroidFastRenderView extends SurfaceView implements Runnable {
	AndroidGame game;
	Bitmap framebuffer;
	Thread renderThread = null;
	SurfaceHolder holder;
	volatile boolean running = false;
	/**
	 * 渲染图像
	 * @param game 游戏类的实例
	 * @param framebuffer 帧缓冲区
	 */
	public AndroidFastRenderView(AndroidGame game,Bitmap framebuffer){
		super(game);
		this.game = game;
		this.framebuffer = framebuffer;//这里实际上是一个绑定
		this.holder = getHolder();
	}
	@Override
	public void run() {
		Rect dstRect = new Rect();
		long startTime = System.nanoTime();
		while(running){
			if(!holder.getSurface().isValid()){
				continue;
			}
			float deltaTime = (System.nanoTime()-startTime)/1000000000.0f;
			startTime = System.nanoTime();
			
			game.getCurrentScreen().update(deltaTime);
			game.getCurrentScreen().present(deltaTime);
			
			Canvas canvas = holder.lockCanvas();
			canvas.getClipBounds(dstRect);
			canvas.drawBitmap(framebuffer, null, dstRect, null);
			holder.unlockCanvasAndPost(canvas);
		}
	}
	
	public void resume(){
		running = true;
		renderThread = new Thread(this);
		renderThread.start();
	}
	
	public void pause(){
		running = false;
		while(true){
			try{
				renderThread.join();
				break;
			}catch(InterruptedException e){
				//retry
			}
		}
	}

}
