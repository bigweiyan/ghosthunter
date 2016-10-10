package com.wxyz.framework.impl;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;

import com.wxyz.framework.Graphics;
import com.wxyz.framework.Pixmap;

import java.io.IOException;
import java.io.InputStream;
/**
 * 绘图器 通常只使用一个实例 绘制到buffer
 * 获取屏幕作为帧缓冲区及画板（构造）-在画板上绘画并显示（方法）
 * 2015/2/8
 * @author ThinkPad
 *
 */
public class AndroidGraphics implements Graphics {
	AssetManager assets;
	Bitmap frameBuffer;
	Canvas canvas;
	Paint paint;
	Rect srcRect = new Rect();
	Rect dstRect = new Rect();
	/**
	 * 绘制帧缓冲区
	 * 2015/2/8
	 * @param assets 资源管理器
	 * @param frameBuffer 需要传递的frameBuffer参数（屏幕大小）
	 */
	public AndroidGraphics (AssetManager assets,Bitmap frameBuffer){
		this.assets=assets;
		this.frameBuffer = frameBuffer;
		//将Game中获取的屏幕参数传递给Canvas用于绘画
		this.canvas = new Canvas(frameBuffer);
		this.paint = new Paint();
	}
	
	/**
	 * 把png文件包装为对象
	 * format使用Pixmap中的PixmapFormat枚举类
	 */
	@Override
	public Pixmap newPixmap(String fileName, PixmapFormat format) {
		Config config = null;
		if (format==PixmapFormat.RGB565){
			config = Config.RGB_565;
		}else if(format == PixmapFormat.ARGB4444){
			config = Config.ARGB_4444;
		}else{
			config = Config.ARGB_8888;
		}
		Options options = new Options();
		options.inPreferredConfig = config;
		InputStream in = null;
		Bitmap bitmap = null;
		try{
			in = assets.open(fileName);
			//wy做了稍许修改
			bitmap = BitmapFactory.decodeStream(in, null, options);
			if (bitmap==null){
				throw new RuntimeException("Couldn't load bitmap from assets'"
						+fileName+"'");
			}
		}catch(IOException e){
			throw new RuntimeException("Couldn't load bitmap from assets'"
					+fileName+"'");
		}finally{
			if(in!=null){
				try{
					in.close();
				}catch(IOException e){
					
				}
			}
		}
		if(bitmap.getConfig()==Config.RGB_565){
			format=PixmapFormat.RGB565;
		}else if(bitmap.getConfig()==Config.ARGB_4444){
			format=PixmapFormat.ARGB4444;
		}else{
			format=PixmapFormat.ARGB8888;
		}
		return new AndroidPixmap(bitmap,format);
	}

	@Override
	public void clear(int color) {
		canvas.drawRGB((color & 0xff0000)>>16, (color & 0xff00)>>8, (color & 0xff));
	}

	@Override
	public void drawPixel(int x, int y, int color) {
		paint.setColor(color);
		canvas.drawPoint(x, y, paint);
	}

	@Override
	public void drawLine(int x, int y, int x2, int y2, int color) {
		paint.setColor(color);
		canvas.drawLine(x, y, x2, y2, paint);
	}

	@Override
	public void drawRect(int x, int y, int width, int height, int color) {
		paint.setColor(color);
		paint.setStyle(Style.FILL);
		canvas.drawRect(x, y, x+width-1, y+height-1, paint);
	}

	@Override
	public void drawPixmap(Pixmap pixmap, int x, int y) {
		canvas.drawBitmap(((AndroidPixmap)pixmap).bitmap, x, y, null);
	}

	@Override
	public void drawPixmap(Pixmap pixmap, int x, int y, int srcX, int srcY,
			int srcWidth, int srcHeight) {
		srcRect.left = srcX;
		srcRect.top = srcY;
		srcRect.right = srcX + srcWidth - 1;
		srcRect.bottom = srcY + srcHeight - 1;
		
		dstRect.left = x;
		dstRect.right = x + srcWidth - 1;
		dstRect.top = y;
		dstRect.bottom = y + srcHeight - 1;
		
		canvas.drawBitmap(((AndroidPixmap)pixmap).bitmap, srcRect, dstRect, paint);
	}

	@Override
	public int getWidth() {
		return frameBuffer.getWidth();
	}

	@Override
	public int getHeight() {
		return frameBuffer.getHeight();
	}

}
