package com.wxyz.framework.gl;

public class Animation {
	public static final int ANIMATION_LOOPING = 1;
	public static final int ANIMATION_NONLOOPING = 0;
	final TextureRegion[] keyFrames;
	final float frameDuration;
	
	/**
	 * 动画实例
	 * @param frameDuration 关键帧之间时间间隔
	 * @param keyFrames 关键帧
	 */
	public Animation(float frameDuration,TextureRegion ... keyFrames){
		this.frameDuration = frameDuration;
		this.keyFrames = keyFrames;
	}
	/**
	 * 取出对应时间应当得到的图形
	 * @param stateTime 对象的时间
	 * @param mode 是否循环
	 * @return 时间对应的关键帧
	 */
	public TextureRegion getKeyFrame(float stateTime, int mode){
		int frameNumber = (int)(stateTime / frameDuration);
		
		if(mode == ANIMATION_NONLOOPING){
			frameNumber = Math.min(keyFrames.length-1, frameNumber);
		}else{
			frameNumber = frameNumber % keyFrames.length;
		}
		return keyFrames[frameNumber];
		
	}
}
