package com.hunter.game.models;

import com.wxyz.framework.gl.SpriteBatcher;
import com.wxyz.framework.gl.Texture;
import com.wxyz.framework.gl.TextureRegion;

/**
 * 提示渲染器，用于渲染提示.
 * Created by weiyan on 2016/12/10.
 */

public class ToastRenderer {
    private float positionX;
    private float positionY;
    private float width;
    private float height;
    private SpriteBatcher batcher;
    private int toast;
    private float remainTime;

    public ToastRenderer(float x, float y, float width, float height, SpriteBatcher spriteBatcher){
        positionX = x;
        positionY = y;
        this.width = width;
        this.height = height;
        batcher = spriteBatcher;
    }

    public void addToast(int toast){
        this.toast = toast;
        remainTime = 1.5f;
    }

    public void printToast(Texture texture, TextureRegion[] toastRegion, float deltaTime){
        if (remainTime <= 0) return;
        batcher.beginBatch(texture);
        batcher.drawSprite(positionX,positionY,width,height,toastRegion[toast]);
        batcher.endBatch();
        remainTime -= deltaTime;
    }
}
