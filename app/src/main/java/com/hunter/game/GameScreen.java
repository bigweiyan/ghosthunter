package com.hunter.game;

import android.util.Log;

import com.hunter.game.models.GameState;
import com.hunter.game.models.Item;
import com.hunter.game.models.RoomRule;
import com.hunter.game.models.Tools;
import com.hunter.network.NetworkExample;
import com.hunter.network.NetworkException;
import com.hunter.network.NetworkSupport;
import com.hunter.sensor.SensorExample;
import com.hunter.sensor.SensorException;
import com.hunter.sensor.SensorSupport;
import com.wxyz.framework.Game;
import com.wxyz.framework.Input;
import com.wxyz.framework.gl.Camera2D;
import com.wxyz.framework.gl.SpriteBatcher;
import com.wxyz.framework.gl.math.Circle;
import com.wxyz.framework.gl.math.OverlapTest;
import com.wxyz.framework.gl.math.Vector2;
import com.wxyz.framework.impl.GLScreen;

import java.util.ArrayList;
import java.util.List;

import javax.microedition.khronos.opengles.GL10;

/**
 * 测向机屏幕.
 * Created by weiyan on 2016/11/13.
 */

public class GameScreen extends GLScreen {
    private Camera2D camera;
    private SpriteBatcher batcher;
    //present有关变量
    private float TICK = 0.04f;
    private float stateTime;
    private int networkFrame;
    private Vector2 touchPos;
    private Circle searchButton;
    private Circle freqButton;
    //游戏控制有关变量
    private GameState state;
    private int roomNumber;
    private String playerName;
    private ArrayList<String> highScores;
    private boolean onLoad;
    private int buttonState;
    private static final int BUTTON_UP = 0;
    private static final int BUTTON_DOWN = 1;
    //游戏状态有关变量
    private NetworkSupport ns;
    private SensorSupport ss;



    public GameScreen(Game game) {
        super(game);
        camera = new Camera2D(glGraphics, 1080, 1920);
        batcher = new SpriteBatcher(glGraphics, 30);
        //初始化Present有关变量
        touchPos = new Vector2();
        stateTime = 0.0f;
        networkFrame = 0;
        buttonState = BUTTON_UP;
        searchButton = new Circle(710,894,314);
        freqButton = new Circle(204,1128,224);
        //初始化update有关变量
        roomNumber = ((HuntGame)game).roomNumber;
        playerName = ((HuntGame)game).playerName;
        onLoad =  true;
        //初始化游戏信息

        ss = new SensorExample();
        ns = new NetworkExample();
    }

    @Override
    public void update(float deltaTime) {
        if (onLoad) {
            try {
                RoomRule rule = ns.getRoomRule(roomNumber);
                state = new GameState(GameState.START,rule.signals);
                highScores = ns.getHighScores(roomNumber);
                onLoad = false;
            } catch (NetworkException e) {
                Tools.showDialog(glGame,"网络异常","请检查网络连接");
            }
            return;
        }
        //游戏初始化，使用网络

        stateTime += deltaTime;
        while (stateTime >= TICK) {
            stateTime -= TICK;
            networkFrame ++;
        }
        if (networkFrame >= 25) {
            networkFrame -= 25;
            try {
                state.gameState = ns.getGameState(roomNumber);
                ArrayList<Item> items = ns.getItemsEffect(roomNumber,playerName);
                if (items != null)
                for (int i = 0; i < items.size(); i++) {
                    state.receiveAffect(items.get(i));
                }
            }catch (NetworkException e){
                Tools.showDialog(glGame,"网络异常",e.getMessage());
            }

            if(state.gameState == GameState.GAME_OVER) {
                Tools.showDialog(glGame,"游戏结束","游戏已结束");
                // TODO: 2016/12/4 跳转到下一屏幕 
            }
        }
        //更新游戏状态、道具状态、比分，使用网络

        state.updateSound(deltaTime);
        //更新声音信息，道具剩余时间

        // TODO: 2016/12/4 发出声音

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
                    try {
                        int signal = state.search(ss.getLatitude(),ss.getLongitude());
                        Item item = ns.findSignal(roomNumber,playerName,signal);
                        if (item != null) state.receiveItem(item);
                    }catch (SensorException e) {
                        Tools.showDialog(glGame,"搜索时错误","无法定位");
                    }catch (NetworkException e) {
                        Tools.showDialog(glGame,"搜索时错误","无法通信");
                    }
                }


                if (OverlapTest.pointInCircle(freqButton,touchPos)) {
                    state.addFreq();
                }

                buttonState = BUTTON_UP;
            }
            Log.d("Touch","Event!"+event.x +" "+ event.y);
        }
        //检查按钮的点击
        // TODO: 2016/12/4 增加道具的按钮点击查询
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
        if(state != null) {
            if (buttonState == BUTTON_UP) {
                if (state == null || state.isSearchButtonWake) {
                    batcher.drawSprite(710, 894, 628, 628, GameAssets.search_button_up);
                } else {
                    batcher.drawSprite(710, 894, 628, 628, GameAssets.search_button_busy);
                }
            } else {
                batcher.drawSprite(710, 894, 628, 628, GameAssets.search_button_down);
            }
            //绘制搜索按钮

            batcher.drawSprite(204, 1128, 224, 224,
                    420 - 60.0f * state.getFreq(), GameAssets.frequency_pointer);
            //绘制指针
            if (state != null) {
                for (int i = 0; i < state.isSignalsFound.size(); i++) {
                    if (state.isSignalsFound.get(i)) {
                        batcher.drawSprite(591 + 132 * (i % 4),
                                131 + (i / 4) * 119,
                                93, 96, GameAssets.fox_hunted);
                    } else {
                        batcher.drawSprite(591 + 132 * (i % 4),
                                131 + (i / 4) * 119,
                                93, 96, GameAssets.fox_remained);
                    }
                }
            }
            // TODO: 2016/12/4 绘制道具信息
        }else{
            batcher.drawSprite(540,960,1080,128,GameAssets.connecting);
        }
        //绘制提示信息
        batcher.endBatch();

        gl.glBlendFunc(GL10.GL_ONE, GL10.GL_ONE_MINUS_SRC_ALPHA);
        batcher.beginBatch(GameAssets.font_texture);
        GameAssets.font.setScale(1.5f,2.5f);
        GameAssets.font.drawText(batcher,"Mike 0",196,1740);
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
