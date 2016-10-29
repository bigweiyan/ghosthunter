package com.hunter.game;

import com.wxyz.framework.Game;
import com.wxyz.framework.gl.Camera2D;
import com.wxyz.framework.gl.SpriteBatcher;
import com.wxyz.framework.gl.Texture;
import com.wxyz.framework.gl.TextureRegion;
import com.wxyz.framework.impl.GLScreen;

import javax.microedition.khronos.opengles.GL10;

/**
 * Created by weiyan on 2016/10/29.
 */

public class MainScreen extends GLScreen {
    Camera2D camera;
    SpriteBatcher batcher;
    Texture mainPage;
    TextureRegion mainPageRegion;
    public MainScreen(Game game) {
        super(game);
        camera = new Camera2D(glGraphics, 1080, 1920);
        batcher = new SpriteBatcher(glGraphics, 2);
    }

    @Override
    public void update(float deltaTime) {
        game.getInput().getKeyEvents();
        game.getInput().getTouchEvents();
    }

    @Override
    public void present(float deltaTime) {
        GL10 gl = glGraphics.getGL();
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
        camera.setViewportAndMatrics();

        gl.glEnable(GL10.GL_TEXTURE_2D);

        batcher.beginBatch(mainPage);
        batcher.drawSprite(540, 960, 1080, 1920, mainPageRegion);
        batcher.endBatch();
    }

    @Override
    public void pause() {
        mainPage.dispose();
    }

    @Override
    public void resume() {
        mainPage = Assets.main_page;
        mainPageRegion = Assets.page_region;
    }

    @Override
    public void dispose() {

    }
}
