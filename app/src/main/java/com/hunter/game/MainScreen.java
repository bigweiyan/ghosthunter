package com.hunter.game;

import android.content.Intent;
import android.view.KeyEvent;

import com.wxyz.framework.Game;
import com.wxyz.framework.Input;
import com.wxyz.framework.gl.Camera2D;
import com.wxyz.framework.gl.SpriteBatcher;
import com.wxyz.framework.gl.math.OverlapTest;
import com.wxyz.framework.gl.math.Rectangle;
import com.wxyz.framework.gl.math.Vector2;
import com.wxyz.framework.impl.GLScreen;

import java.util.List;

import javax.microedition.khronos.opengles.GL10;

/**
 * Created by weiyan on 2016/10/29.
 */

public class MainScreen extends GLScreen {
    private Camera2D camera;
    private SpriteBatcher batcher;
    private Rectangle upButton;
    private boolean upPressed;
    private boolean downPressed;
    private Rectangle downButton;
    private Vector2 touchPos;
    int mode;
    private static final int MODE_NONE = 0;
    private static final int MODE_BATTLE = 1;
    private static final int MODE_TEAM = 2;
    public MainScreen(Game game) {
        super(game);
        camera = new Camera2D(glGraphics, 1080, 1920);
        batcher = new SpriteBatcher(glGraphics, 10);
        touchPos = new Vector2();

        upButton = new Rectangle(278, 394, 524, 164);
        upPressed = false;
        downButton = new Rectangle(278, 160, 524, 164);
        downPressed = false;
        mode = MODE_NONE;
    }

    @Override
    public void update(float deltaTime) {
        List<Input.KeyEvent> keyEvents = game.getInput().getKeyEvents();
        List<Input.TouchEvent> events =  game.getInput().getTouchEvents();
        int len = events.size();

        for (int i = 0; i < len; i++) {
            Input.TouchEvent event = events.get(i);
            touchPos.set(event.x,event.y);
            camera.touchToWorld(touchPos);

            if (event.type == Input.TouchEvent.TOUCH_UP) {
                if (OverlapTest.pointInRectangle(upButton, touchPos)) {
                    if (mode == MODE_NONE) mode = MODE_BATTLE;
                    else if (mode == MODE_BATTLE) {
                        Intent intent = new Intent();
                        intent.putExtra("mode",EnterRoom.MODE_BATTLE);
                        intent.setClass(glGame, com.hunter.game.EnterRoom.class);
                        glGame.startActivity(intent);
                    } else if (mode == MODE_TEAM) {
                        Intent intent = new Intent();
                        intent.putExtra("mode",EnterRoom.MODE_TEAM);
                        intent.setClass(glGame, com.hunter.game.EnterRoom.class);
                        glGame.startActivity(intent);
                    }
                    upPressed = false;
                }else if (OverlapTest.pointInRectangle(downButton, touchPos)) {
                    if (mode == MODE_NONE) mode = MODE_TEAM;
                    else if (mode == MODE_BATTLE) {
                        Intent intent = new Intent();
                        intent.putExtra("mode",RoomSetting.MODE_BATTLE);
                        intent.setClass(glGame, com.hunter.game.RoomSetting.class);
                        glGame.startActivity(intent);
                    } else if (mode == MODE_TEAM) {
                        Intent intent = new Intent();
                        intent.putExtra("mode", RoomSetting.MODE_TEAM);
                        intent.setClass(glGame, com.hunter.game.RoomSetting.class);
                        glGame.startActivity(intent);
                    }
                    downPressed = false;
                }else {
                    mode = MODE_NONE;
                }
            }

            if (event.type == Input.TouchEvent.TOUCH_DRAGGED
                    || event.type == Input.TouchEvent.TOUCH_DOWN) {

                if (OverlapTest.pointInRectangle(upButton, touchPos)) {
                    upPressed = true;
                }else if (OverlapTest.pointInRectangle(downButton, touchPos)) {
                    downPressed = true;
                }else {
                    upPressed = false;
                    downPressed = false;
                }
            }
        }

        len = keyEvents.size();
        for (int i = 0; i < len; i++) {
            Input.KeyEvent event = keyEvents.get(i);
            if (event.type == Input.KeyEvent.KEY_UP) {
                if (event.keyCode == KeyEvent.KEYCODE_BACK) {
                    if (mode == MODE_NONE) {
                        glGame.finish();
                    }else{
                        mode = MODE_NONE;
                    }
                }
            }
        }
    }

    @Override
    public void present(float deltaTime) {
        GL10 gl = glGraphics.getGL();
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
        camera.setViewportAndMatrics();

        gl.glEnable(GL10.GL_TEXTURE_2D);

        batcher.beginBatch(Assets.main_page);
        batcher.drawSprite(540, 960, 1080, 1920, Assets.page_region);
        batcher.endBatch();

        gl.glEnable(GL10.GL_BLEND);
        gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);

        if (mode == MODE_NONE) {
            batcher.beginBatch(Assets.main_page);
            batcher.drawSprite(540, 242, 524, 164,
                    downPressed?Assets.main_team_button_pressed:Assets.main_team_button_released);
            batcher.drawSprite(540, 476, 524, 164,
                    upPressed?Assets.main_battle_button_pressed:Assets.main_battle_button_released);
            batcher.endBatch();
        } else if (mode == MODE_BATTLE) {
            batcher.beginBatch(Assets.battle_main_page);
            batcher.drawSprite(540, 960, 1080, 1920, Assets.page_region);
            batcher.drawSprite(540, 242, 524, 164,
                    downPressed?Assets.main_team_button_pressed:Assets.main_team_button_released);
            batcher.drawSprite(540, 476, 524, 164,
                    upPressed?Assets.main_battle_button_pressed:Assets.main_battle_button_released);
            batcher.endBatch();
        } else if (mode == MODE_TEAM) {
            batcher.beginBatch(Assets.team_main_page);
            batcher.drawSprite(540, 960, 1080, 1920, Assets.page_region);
            batcher.drawSprite(540, 242, 524, 164,
                    downPressed?Assets.main_team_button_pressed:Assets.main_team_button_released);
            batcher.drawSprite(540, 476, 524, 164,
                    upPressed?Assets.main_battle_button_pressed:Assets.main_battle_button_released);
            batcher.endBatch();
        }

        gl.glDisable(GL10.GL_BLEND);

    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void dispose() {

    }
}
