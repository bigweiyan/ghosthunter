package com.wxyz.framework.gl;

public class TextureRegion {
	public final float u1,v1;
	public final float u2,v2;
	public final Texture texture;
	/**
	 * 纹理区域，将源文件中目标纹理的坐标（左上为0）自动转化为纹理坐标（左上为0）
	 * @param texture 纹理图集
	 * @param x 目标纹理左上点横坐标
	 * @param y 目标纹理左上点纵坐标
	 * @param width 目标纹理宽度
	 * @param height 目标纹理高度
	 */
	public TextureRegion(Texture texture,float x,float y,float width,float height){
		this.u1 = x / texture.width;
		this.v1 = y / texture.height;
		this.u2 = this.u1 + width / texture.width;
		this.v2 = this.v1 + height / texture.height;
		this.texture = texture;
	}
}
