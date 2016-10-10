package com.wxyz.framework;

import com.wxyz.framework.Graphics.PixmapFormat;
/**
 * 这个为封装格式类，每个图像资源对应一个Pixmap类实例 
 * <p>由Graphics中的包装方法进行创建</p>
 * @author ThinkPad
 *
 */
public interface Pixmap {
	public int getWidth();
	public int getHeight();
	public PixmapFormat getFormat();
	public void dispose();
}
