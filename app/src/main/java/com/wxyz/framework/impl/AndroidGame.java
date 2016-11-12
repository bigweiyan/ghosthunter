package com.wxyz.framework.impl;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.view.Window;
import android.view.WindowManager;

import com.wxyz.framework.Audio;
import com.wxyz.framework.FileIO;
import com.wxyz.framework.Game;
import com.wxyz.framework.Graphics;
import com.wxyz.framework.Input;
import com.wxyz.framework.Screen;
/**
 * <p>由此类派生出游戏的入口，此类集成四个模块，开启了渲染图形的线程，同时控制屏幕的切换</p>
 * <p>继承此类时需要覆盖getStartScreen方法</p>
 * 2015/2/8
 * @author ThinkPad
 *
 */
public abstract class AndroidGame extends Activity implements Game {
	AndroidFastRenderView renderView;
	Graphics graphics;
	Audio audio;
	Input input;
	FileIO fileIO;
	Screen screen;//这里作为形式参数，传递正在进行的Screen，开始为getStartScreen，之后每次更改Screen都会传递到screen实例
	WakeLock wakeLock;
	
	@SuppressWarnings("deprecation")
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		boolean isLandscape = getResources().getConfiguration().orientation==Configuration.ORIENTATION_LANDSCAPE;
		int frameBufferWidth = isLandscape ? 1920:1080;//这里修改默认分辨率
		int frameBufferHeight = isLandscape ? 1080:1920;
		//这里新建了一个屏幕大小的帧缓冲区，以其作为画布，并在Graphics类中完成绘画
		Bitmap frameBuffer = Bitmap.createBitmap(frameBufferWidth, frameBufferHeight, Config.RGB_565);
		
		float scaleX = (float)frameBufferWidth
				/getWindowManager().getDefaultDisplay().getWidth();
		float scaleY = (float)frameBufferHeight
				/getWindowManager().getDefaultDisplay().getHeight();
		
		renderView = new AndroidFastRenderView(this,frameBuffer);
		graphics = new AndroidGraphics(getAssets(),frameBuffer);
		fileIO = new AndroidFileIO(this);//wy略作修改
		audio = new AndroidAudio(this);
		input = new AndroidInput(this, renderView, scaleX, scaleY);
		//这里对画布进行缩放以适配屏幕
		screen = getStartScreen();
		setContentView(renderView);
		
		PowerManager powerManager = (PowerManager)getSystemService(Context.POWER_SERVICE);
		wakeLock = powerManager.newWakeLock(PowerManager.FULL_WAKE_LOCK, "Game");
	}
	
	@Override
	public void onResume(){
		super.onResume();
		wakeLock.acquire();
		screen.resume();
		renderView.resume();
	}
	
	@Override
	public void onPause(){
		super.onPause();
		wakeLock.release();
		renderView.pause();
		screen.pause();
	}
	/**
	 * 获得触摸键盘传感器的输入事件，可直接访问下层get方法，也可以由此获得input引用
	 */
	@Override
	public Input getInput() {
		return input;
	}
	/**
	 * <p>获得输入输出流，可以直接调用下层方法得到流，或者由此方法获得fileIO引用</p>
	 */
	@Override
	public FileIO getFileIO() {
		return fileIO;
	}
	/**
	 * <p>获得绘图器，可以直接调用下层绘图/包装方法，或由此方法获得graphics引用</p>
	 * <p>注：下层的包装方法并不是绘图，而是得到图像的引用，方便绘图</p>
	 */
	@Override
	public Graphics getGraphics() {
		return graphics;
	}
	/**
	 * <p>获得音乐、音效包装器，可以直接调用下层new包装方法。包装后的实例自带播放功能</p>
	 */
	@Override
	public Audio getAudio() {
		return audio;
	}
	/**
	 * 屏幕的切换，在一个屏幕的update中调用，切换到另一个屏幕
	 */
	@Override
	public void setScreen(Screen screen) {
		if(screen==null){
			throw new IllegalArgumentException("Screen must not be null");
		}
		this.screen.pause();
		this.screen.dispose();
		screen.resume();
		screen.update(0);
		this.screen = screen;
	}
	/**
	 * 获得当前屏幕（用于在FastRenderView中渲染）
	 */
	@Override
	public Screen getCurrentScreen() {
		return screen;
	}


}
