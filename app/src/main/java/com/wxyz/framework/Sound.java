package com.wxyz.framework;
/**
 * 这个为封装格式类，每个音效资源对应一个Sound类实例
 * <p>由Audio中的包装方法进行创建</p>
 * @author ThinkPad
 *
 */
public interface Sound {
	public void play(float volume);
	public void dispose();
}
