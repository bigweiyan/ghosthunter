package com.wxyz.framework;
/**
 * 这个为封装格式类，每个音乐资源对应一个Music类实例
 * <p>由Audio中的包装方法进行创建</p>
 * 封装为音乐资源
 * @author ThinkPad
 *
 */
public interface Music {
	public void play();
	public void stop();
	public void pause();
	public void setLooping(boolean looping);
	public void setVolume(float volume);
	public boolean isPlaying();
	public boolean isLooping();
	public boolean isStopped();
	public void dispose();
}
