package com.wxyz.framework.gl;

import com.wxyz.framework.gl.math.Vector2;
import com.wxyz.framework.impl.GLGraphics;
import com.wxyz.glbasics.gamedev2d.GameObject;

import javax.microedition.khronos.opengles.GL10;

public class SpriteBatcher {
	final float[] verticesBuffer;
	int bufferIndex;
	final Vertices vertices;
	int numSprites;
	
	public SpriteBatcher(GLGraphics glGraphics,int maxSprites){
		this.verticesBuffer = new float[maxSprites * 4 * 4];
		this.vertices = new Vertices(glGraphics,maxSprites*4,maxSprites*6,false,true);
		this.bufferIndex = 0;
		this.numSprites = 0;
		
		short[] indices = new short[maxSprites * 6];
		int len = indices.length;
		short j = 0;
		for(int i=0;i<len;i+=6,j+=4){
			indices[i] = j;
			indices[i + 1] = (short)(j + 1);
			indices[i + 2] = (short)(j + 2);
			indices[i + 3] = (short)(j + 2);
			indices[i + 4] = (short)(j + 3);
			indices[i + 5] = j;
		}
		vertices.setIndices(indices, 0, indices.length);
	}
	
	public void beginBatch(Texture texture){
		texture.bind();
		numSprites = 0;
		bufferIndex = 0;
	}
	
	public void endBatch(){
		vertices.setVertices(verticesBuffer, 0, bufferIndex);
		vertices.bind();
		vertices.draw(GL10.GL_TRIANGLES, 0, numSprites*6);
		vertices.unbind();
	}
	
	/**
	 * 渲染图形的通用方法，无论边界矩形是否与图像矩形相同
	 * @param x 纹理中心横坐标（视锥体坐标，坐下角为0）
	 * @param y 纹理中心纵坐标
	 * @param width 纹理渲染宽度
	 * @param height 纹理渲染高度
	 * @param region 渲染对象的纹理
	 */
	public void drawSprite(float x,float y,float width,float height,TextureRegion region){
		float halfWidth = width / 2;
		float halfHeight = height / 2;
		float x1 = x - halfWidth;
		float x2 = x + halfWidth;
		float y1 = y - halfHeight;
		float y2 = y + halfHeight;
		
		verticesBuffer[bufferIndex++] = x1;
		verticesBuffer[bufferIndex++] = y1;
		verticesBuffer[bufferIndex++] = region.u1;
		verticesBuffer[bufferIndex++] = region.v2;
		
		verticesBuffer[bufferIndex++] = x2;
		verticesBuffer[bufferIndex++] = y1;
		verticesBuffer[bufferIndex++] = region.u2;
		verticesBuffer[bufferIndex++] = region.v2;
		
		verticesBuffer[bufferIndex++] = x2;
		verticesBuffer[bufferIndex++] = y2;
		verticesBuffer[bufferIndex++] = region.u2;
		verticesBuffer[bufferIndex++] = region.v1;
		
		verticesBuffer[bufferIndex++] = x1;
		verticesBuffer[bufferIndex++] = y2;
		verticesBuffer[bufferIndex++] = region.u1;
		verticesBuffer[bufferIndex++] = region.v1;
		numSprites++;
	}

	public void drawSprite(float x,float y,float width,float height,float angle,TextureRegion region){
		float halfWidth = width / 2;
		float halfHeight = height / 2;
		float rad = angle * Vector2.TO_RADIANS;
		float cos = (float)Math.cos(rad);
		float sin = (float)Math.sin(rad);
		
		float x1 = -halfWidth * cos + halfHeight * sin;
		float y1 = -halfWidth * sin - halfHeight * cos;
		float x2 = halfWidth * cos + halfHeight * sin;
		float y2 = halfWidth * sin - halfHeight * cos;
		float x3 = halfWidth * cos - halfHeight * sin;
		float y3 = halfWidth * sin + halfHeight * cos;
		float x4 = -halfWidth * cos - halfHeight * sin;
		float y4 = -halfWidth * sin + halfHeight * cos;
		
		x1 += x;
		x2 += x;
		x3 += x;
		x4 += x;
		y1 += y;
		y2 += y;
		y3 += y;
		y4 += y;
		
		verticesBuffer[bufferIndex++] = x1;
		verticesBuffer[bufferIndex++] = y1;
		verticesBuffer[bufferIndex++] = region.u1;
		verticesBuffer[bufferIndex++] = region.v2;
		
		verticesBuffer[bufferIndex++] = x2;
		verticesBuffer[bufferIndex++] = y2;
		verticesBuffer[bufferIndex++] = region.u2;
		verticesBuffer[bufferIndex++] = region.v2;
		
		verticesBuffer[bufferIndex++] = x3;
		verticesBuffer[bufferIndex++] = y3;
		verticesBuffer[bufferIndex++] = region.u2;
		verticesBuffer[bufferIndex++] = region.v1;
		
		verticesBuffer[bufferIndex++] = x4;
		verticesBuffer[bufferIndex++] = y4;
		verticesBuffer[bufferIndex++] = region.u1;
		verticesBuffer[bufferIndex++] = region.v1;
		numSprites++;
	}
	
	/**
	 * 当碰撞边界与图像边界相同是，使用此方法
	 * @param object 渲染对象
	 * @param region 渲染对象的纹理
	 */
	public void drawSprite(GameObject object,TextureRegion region){
		float x = object.position.x;
		float y = object.position.y;
		float halfWidth = object.bounds.width / 2;
		float halfHeight = object.bounds.height / 2;
		float x1 = x - halfWidth;
		float x2 = x + halfWidth;
		float y1 = y - halfHeight;
		float y2 = y + halfHeight;
		
		verticesBuffer[bufferIndex++] = x1;
		verticesBuffer[bufferIndex++] = y1;
		verticesBuffer[bufferIndex++] = region.u1;
		verticesBuffer[bufferIndex++] = region.v2;
		
		verticesBuffer[bufferIndex++] = x2;
		verticesBuffer[bufferIndex++] = y1;
		verticesBuffer[bufferIndex++] = region.u2;
		verticesBuffer[bufferIndex++] = region.v2;
		
		verticesBuffer[bufferIndex++] = x2;
		verticesBuffer[bufferIndex++] = y2;
		verticesBuffer[bufferIndex++] = region.u2;
		verticesBuffer[bufferIndex++] = region.v1;
		
		verticesBuffer[bufferIndex++] = x1;
		verticesBuffer[bufferIndex++] = y2;
		verticesBuffer[bufferIndex++] = region.u1;
		verticesBuffer[bufferIndex++] = region.v1;
		numSprites++;
	}
}
