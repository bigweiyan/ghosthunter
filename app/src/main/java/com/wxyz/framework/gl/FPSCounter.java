package com.wxyz.framework.gl;

import android.util.Log;

public class FPSCounter {
	long startTime = System.nanoTime();
	int frames = 0;
	public FPSCounter(){
		Log.d("FPSCounter", "Start counting.");
	}
	public void logFrame(){
		frames++;
		if(System.nanoTime()-startTime>=1000000000){
			Log.d("FPSCounter", "FPS:"+frames);
			frames =0;
			startTime = System.nanoTime();
		}
	}
}
