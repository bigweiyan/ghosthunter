package com.wxyz.framework;
/**
 * 该类有两个包装方法，分别包装为Music和Sound
 * @author ThinkPad
 *
 */
public interface Audio {
	public Music newMusic(String fielName);
	public Sound newSound(String fileName);
}
