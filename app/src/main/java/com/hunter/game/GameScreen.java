package com.hunter.game;

import android.util.Log;

import com.hunter.game.models.GameState;
import com.hunter.game.models.RoomRule;
import com.hunter.game.models.Signal;
import com.hunter.network.NetworkExample;
import com.hunter.network.NetworkException;
import com.hunter.network.NetworkSupport;
import com.hunter.sensor.SensorExample;
import com.hunter.sensor.SensorSupport;
import com.wxyz.framework.Game;
import com.wxyz.framework.Input;
import com.wxyz.framework.gl.Camera2D;
import com.wxyz.framework.gl.SpriteBatcher;
import com.wxyz.framework.gl.math.Circle;
import com.wxyz.framework.gl.math.OverlapTest;
import com.wxyz.framework.gl.math.Vector2;
import com.wxyz.framework.impl.GLScreen;

import java.util.List;

import javax.microedition.khronos.opengles.GL10;

/**
 * 测向机屏幕.
 * Created by weiyan on 2016/11/13.
 */

public class GameScreen extends GLScreen {
    private Camera2D camera;
    private SpriteBatcher batcher;
    private Vector2 touchPos;

    private Circle searchButton;
    private Circle freqButton;

    private GameState state;
    private RoomRule rule;
    private int roomNumber;
    private String playerName;
    private boolean onLoad;
    private NetworkSupport ns;
    private SensorSupport ss;

    private int buttonState;
    private static final int BUTTON_UP = 0;
    private static final int BUTTON_DOWN = 1;
    private static final int BUTTON_BUSY = 2;

    public GameScreen(Game game) {
        super(game);
        camera = new Camera2D(glGraphics, 1080, 1920);
        batcher = new SpriteBatcher(glGraphics, 30);
        touchPos = new Vector2();

        buttonState = BUTTON_UP;
        searchButton = new Circle(710,894,314);
        freqButton = new Circle(204,1128,224);

        state = new GameState(GameState.START,0);
        rule = new RoomRule(false,false,((HuntGame)game).mode);
        roomNumber = ((HuntGame)game).roomNumber;
        playerName = ((HuntGame)game).playerName;
        onLoad =  true;

        ss = new SensorExample();
        ns = new NetworkExample();
    }

    @Override
    public void update(float deltaTime) {
        if (onLoad) {
            try {
                rule = ns.getRoomRule(roomNumber);
                onLoad = false;
            } catch (NetworkException e) {
                e.printStackTrace();
            }
            return;
        }

        game.getInput().getKeyEvents();
        List<Input.TouchEvent> events =  game.getInput().getTouchEvents();

        int len = events.size();

        for (int i = 0; i < len; i++) {
            Input.TouchEvent event = events.get(i);
            touchPos.set(event.x,event.y);
            camera.touchToWorld(touchPos);

            if (event.type == Input.TouchEvent.TOUCH_DRAGGED
                    || event.type == Input.TouchEvent.TOUCH_DOWN) {
                if (OverlapTest.pointInCircle(searchButton,touchPos)) {
                    if (state.isSearchButtonWake) buttonState = BUTTON_DOWN;
                } else {
                    if (state.isSearchButtonWake) buttonState = BUTTON_UP;
                }
            }

            if (event.type == Input.TouchEvent.TOUCH_UP) {
                if (OverlapTest.pointInCircle(searchButton,touchPos)) {
                    buttonState = BUTTON_BUSY;
                    // TODO: 2016/11/14 dealSearch
                } else {
                    if (state.isSearchButtonWake) buttonState = BUTTON_UP;
                }

                if (OverlapTest.pointInCircle(freqButton,touchPos)) {
                    // TODO: 2016/11/14 dealFreq
                    state.presentFreq ++;
                    if(state.presentFreq > Signal.FREQ_6) state.presentFreq = Signal.FREQ_1;
                }
            }
            Log.d("Touch","Event!"+event.x +" "+ event.y);
        }
    }

    @Override
    public void present(float deltaTime) {
        GL10 gl = glGraphics.getGL();
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
        camera.setViewportAndMatrics();

        gl.glEnable(GL10.GL_TEXTURE_2D);

        batcher.beginBatch(GameAssets.battle_back);
        batcher.drawSprite(540,960,1080,1920,GameAssets.page_region);
        batcher.endBatch();

        gl.glEnable(GL10.GL_BLEND);
        gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);

        batcher.beginBatch(GameAssets.battle_back);
        if (buttonState == BUTTON_UP) {
            batcher.drawSprite(710,894,628,628,GameAssets.search_button_up);
        }else if (buttonState == BUTTON_BUSY){
            batcher.drawSprite(710,894,628,628,GameAssets.search_button_busy);
        }else {
            batcher.drawSprite(710,894,628,628,GameAssets.search_button_down);
        }
        if (onLoad) {
            batcher.drawSprite(540,960,1080,128,GameAssets.connecting);
        }
        batcher.drawSprite(204,1128,224,224,
                420 - 60.0f*state.presentFreq,GameAssets.frequency_pointer);
        batcher.endBatch();

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
