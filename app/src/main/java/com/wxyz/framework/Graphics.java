package com.wxyz.framework;

public interface Graphics {
	public static enum PixmapFormat{
		ARGB8888,ARGB4444,RGB565
	}
	/**
	 * 包装图像
	 * @param fileName 文件名（放在assets目录下）
	 * @param format 包装格式
	 * @return 包装后的Pixmap
	 */
	public Pixmap newPixmap(String fileName,PixmapFormat format);
	public void clear(int color);
	public void drawPixel(int x,int y,int color);
	public void drawLine(int x,int y,int x2,int y2,int color);
	public void drawRect(int x,int y,int width,int height,int color);
	/**
	 * 绘制Pixmap（包装过的）到buffer
	 * @param pixmap：被绘制图像
	 * @param x：绘制在屏幕的坐标x
	 * @param y：绘制在屏幕的坐标x
	 */
	public void drawPixmap(Pixmap pixmap,int x,int y);
	/**
	 * 绘制裁剪后的Pixmap（包装过的）到buffer
	 * <p>（x，y）为绘制在画布上左上角坐标</p>
	 * <p>srcX/Y为裁剪区的左上角，srcWidth/Height为裁剪区矩形的宽/高</p>
	 */
	public void drawPixmap(Pixmap pixmap,int x,int y,int srcX,int srcY
			,int srcWidth,int srcHeight);
	public int getWidth();
	public int getHeight();//帧缓冲区
}
