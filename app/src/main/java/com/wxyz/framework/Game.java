package com.wxyz.framework;
/**
 * 游戏主控制器，这里可以：
 * <p>获取游戏的绘图模块（含包装器），声音包装器，输入模块，文件读写模块</p>
 * <p>使用game的setScreen方法可以进行游戏屏幕的改变</p>
 * <p>在设置初始界面后</p>
 * @author ThinkPad
 *
 */
public interface Game {
	public Input getInput();
	public FileIO getFileIO();
	/**
	 * 得到图形绘制器实例，用其为屏幕绘制图形
	 * @return
	 */
	public Graphics getGraphics();
	public Audio getAudio();
	/**
	 * 置换不同的界面
	 * @param screen
	 */
	public void setScreen(Screen screen);
	public Screen getCurrentScreen();
	public Screen getStartScreen();
}
