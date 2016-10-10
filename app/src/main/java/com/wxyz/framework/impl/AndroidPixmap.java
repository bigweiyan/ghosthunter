package com.wxyz.framework.impl;

import android.graphics.Bitmap;

import com.wxyz.framework.Graphics.PixmapFormat;
import com.wxyz.framework.Pixmap;
/**
 * 包装类
 * 每个图形资源对应一个Pixmap实例
 * 2015/2/8
 * @author ThinkPad
 *
 */
public class AndroidPixmap implements Pixmap {
	Bitmap bitmap;
	PixmapFormat format;
	/**
	 * 包装bitmap
	 * @param bitmap 被包装的位图
	 * @param format 包装格式
	 */
	public AndroidPixmap (Bitmap bitmap,PixmapFormat format){
		this.bitmap=bitmap;
		this.format = format;
	}
	@Override
	public int getWidth() {
		// TODO Auto-generated method stub
		return bitmap.getWidth();
	}

	@Override
	public int getHeight() {
		// TODO Auto-generated method stub
		return bitmap.getHeight();
	}

	@Override
	public PixmapFormat getFormat() {
		// TODO Auto-generated method stub
		return format;
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		bitmap.recycle();
	}

}
