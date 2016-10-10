package com.wxyz.framework.gl;

public class Font {
	public final Texture texture;
	public final int glyphWidth;
	public final int glyphHeight;
	public final TextureRegion[] glyphs = new TextureRegion[96];
	
	/**
	 * 字体文件 要求标准ASCII码可打印的96个字符，且字符等宽
	 * @param texture 纹理图集
	 * @param offsetX 文字区域左上角横坐标
	 * @param offsetY 文字区域左上角纵坐标
	 * @param glyphPerRow 一行的文字数
	 * @param glyphWidth 单位字符宽度
	 * @param glyphHeight 单位字符高度
	 */
	public Font(Texture texture, int offsetX, int offsetY, int glyphPerRow,
			int glyphWidth, int glyphHeight){
		this.texture = texture;
		this.glyphWidth = glyphWidth;
		this.glyphHeight = glyphHeight;
		int x = offsetX;
		int y = offsetY;
		for(int i = 0; i < 96; i++){
			glyphs[i] = new TextureRegion(texture,x,y,glyphWidth,glyphHeight);
			x += glyphWidth;
			if(x == offsetX + glyphPerRow * glyphWidth){
				x = offsetX;
				y += glyphHeight;
			}
		}
	}
	
	/**
	 * 绘制英文文字。注：使用此方法前，需使用batcher.beginBatch(texture);
	 * @param batcher
	 * @param text 
	 * @param x 首字符中心（左下角为0）
	 * @param y 
	 */
	public void drawText(SpriteBatcher batcher, String text, float x, float y){
		int len = text.length();
		for(int i = 0; i < len; i++){
			int c = text.charAt(i) - ' ';
			if(c<0 || c>glyphs.length - 1)
				continue;
			TextureRegion glyph = glyphs[c];
			batcher.drawSprite(x, y, glyphWidth, glyphHeight, glyph);
			x += glyphWidth;
		}
	}
}
