package com.wxyz.framework;
/**
 * 使用该类的子类表示不同的屏幕
 * 是游戏的前端及控制板块
 * @author ThinkPad
 *
 */
public abstract class Screen {
	protected final Game game;
	/**
	 * 传递游戏主框架game
	 * @param game
	 */
	public Screen(Game game){
		this.game=game;
	}
	/**
	 * 这个方法处理事件和场景切换
	 * @param deltaTime
	 */
	public abstract void update(float deltaTime);
	/**
	 * 这个方法生成屏幕中的图像
	 * @param deltaTime
	 */
	public abstract void present(float deltaTime);
	/**
	 * 这个方法伴随着屏幕活动的暂停过程onPause进行
	 */
	public abstract void pause();
	/**
	 * 这个方法伴随着屏幕的准备过程onResume进行
	 */
	public abstract void resume();
	/**
	 * 这个方法伴随着屏幕的内存回收onDestroy进行
	 */
	public abstract void dispose();
}
