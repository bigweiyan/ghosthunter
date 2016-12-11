package com.hunter.game;

import com.hunter.game.models.GameState;
import com.hunter.game.models.Item;
import com.hunter.game.models.RoomRule;
import com.hunter.game.models.Signal;
import com.hunter.game.models.ToastRenderer;
import com.hunter.game.models.Tools;
import com.hunter.sensor.SensorException;
import com.hunter.sensor.SensorSupport;
import com.wxyz.framework.Game;
import com.wxyz.framework.Input;
import com.wxyz.framework.gl.Camera2D;
import com.wxyz.framework.gl.SpriteBatcher;
import com.wxyz.framework.gl.math.Circle;
import com.wxyz.framework.gl.math.OverlapTest;
import com.wxyz.framework.gl.math.Rectangle;
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
    private ToastRenderer toastRenderer;
    //present有关变量
    private Vector2 touchPos;
    private Circle searchButton;
    private Circle freqButton;
    private Rectangle itemButton;
    //游戏控制有关变量
    private GameState state;
    private int mode;
    private ArrayList<String> highScores;
    private boolean onLoad;
    private int buttonState;
    private static final int BUTTON_UP = 0;
    private static final int BUTTON_DOWN = 1;
    //游戏状态有关变量
    private SensorSupport ss;

    public GameScreen(Game game) {
        super(game);
        camera = new Camera2D(glGraphics, 1080, 1920);
        batcher = new SpriteBatcher(glGraphics, 50);
        mode = ((HuntGame)glGame).mode;
        toastRenderer = new ToastRenderer(540,960,884,160,batcher);
        //初始化Present有关变量
        touchPos = new Vector2();
        buttonState = BUTTON_UP;
        searchButton = new Circle(710,894,314);
        freqButton = new Circle(204,1128,224);
        itemButton = new Rectangle(78,557,194,194);
        //初始化update有关变量
        ss = ((HuntGame)game).getSs();
        onLoad =  true;
        //初始化游戏信息
    }

    @Override
    public void update(float deltaTime) {
        if (onLoad) {
            ArrayList<Signal> signals = ((HuntGame)glGame).getSignals();
            state = new GameState(GameState.START,signals);
            highScores = ((HuntGame)glGame).getHighScores();
            onLoad = false;
            return;
        }
        //游戏初始化，使用网络

        if(state.gameState == GameState.GAME_OVER) {
            ((HuntGame)glGame).gameOver();
        }
        //更新游戏状态

        if (((HuntGame)glGame).isNewData()) {
            Item item = ((HuntGame) glGame).getItem();
            if (item != null) toastRenderer.addToast(GameAssets.TOAST_ITEM_GET);
            state.receiveItem(item);
            //获得服务器返回的道具
            highScores = ((HuntGame) glGame).getHighScores();
            //获得最高分列表
            if (mode == RoomRule.MODE_TEAM)
                state.signalBelong = ((HuntGame) glGame).getSignalBelong();
            //获得信号源状态
            ArrayList<Item> items = ((HuntGame) glGame).getItems();
            if (items != null) {
                for (int i = 0; i < items.size(); i++) {
                    Item temp = items.get(i);
                    state.receiveAffect(temp);
                    toastRenderer.addToast(GameAssets.TOAST_UNDER_ITEM);
                }
            }
            //获得道具的附加状态
            ((HuntGame)glGame).dataReceived();
        }
        //检查服务器返回的消息

        try {
            state.updateSound((float)ss.getLatitude(),(float)ss.getLongitude(),deltaTime,ss.getDirection());
        }catch (SensorException e) {
            Tools.showDialog(glGame, "搜索时错误", "无法定位");
        }

        //更新声音信息，道具剩余时间，搜索按键的冷却时间

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
                        if (signal == -1) {
                            toastRenderer.addToast(GameAssets.TOAST_NO_SIG_GET);
                        }else {
                            ((HuntGame)glGame).findSignal(signal);
                            toastRenderer.addToast(GameAssets.TOAST_SIG_GET);
                        }
                    }catch (SensorException e) {
                        Tools.showDialog(glGame, "搜索时错误", "无法定位");
                    }
                }


                if (OverlapTest.pointInCircle(freqButton,touchPos)) {
                    state.addFreq();
                }

                if (OverlapTest.pointInRectangle(itemButton,touchPos)) {
                    if(state.item != null) {
                        ((HuntGame) glGame).useItem(state.item);
                        state.useItem();
                    }
                }

                buttonState = BUTTON_UP;
            }
        }
        //检查按钮的点击
    }

    @Override
    public void present(float deltaTime) {
        GL10 gl = glGraphics.getGL();
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
        camera.setViewportAndMatrics();

        gl.glEnable(GL10.GL_TEXTURE_2D);

        if(mode == RoomRule.MODE_BATTLE) batcher.beginBatch(GameAssets.battle_back);
        else batcher.beginBatch(GameAssets.team_back);
        batcher.drawSprite(540,960,1080,1920,GameAssets.page_region);
        batcher.endBatch();

        gl.glEnable(GL10.GL_BLEND);
        gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);


        if(mode == RoomRule.MODE_BATTLE) batcher.beginBatch(GameAssets.battle_back);
        else batcher.beginBatch(GameAssets.team_back);
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
            if (state != null && mode == RoomRule.MODE_BATTLE) {
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
            } else if (state != null && mode == RoomRule.MODE_TEAM) {
                for (int i = 0; i < state.signalBelong.size(); i++) {
                    if (state.signalBelong.get(i) == 1) {
                        batcher.drawSprite(591 + 132 * (i % 4),
                                131 + (i / 4) * 119,
                                93, 96, GameAssets.fox_red);
                    } else if (state.signalBelong.get(i) == 0) {
                        batcher.drawSprite(591 + 132 * (i % 4),
                                131 + (i / 4) * 119,
                                93, 96, GameAssets.fox_remained);
                    } else {
                        batcher.drawSprite(591 + 132 * (i % 4),
                                131 + (i / 4) * 119,
                                93, 96, GameAssets.fox_blue);
                    }
                }
            }

        }else{
            batcher.drawSprite(540,960,1080,128,GameAssets.connecting);
        }
        //绘制屏幕UI
        batcher.endBatch();

        toastRenderer.printToast(GameAssets.item_texture,GameAssets.toasts,deltaTime);
        //绘制提示信息

        int itemLen = state.workingItems.size();
        if(state.item != null || itemLen != 0) {
            batcher.beginBatch(GameAssets.item_texture);
            if (state.item != null) {
                batcher.drawSprite(195, 674, 194, 194, GameAssets.items_region[state.item.getItemType()]);
                batcher.drawSprite(195, 500, 188, 40, GameAssets.items_descript[state.item.getItemType()]);
            }
            for(int i = 0; i < state.workingItems.size(); i++) {
                batcher.drawSprite(80+i*80,40,60,60,GameAssets.items_region[state.workingItems.get(i).getItemType()]);
            }
            batcher.endBatch();
        }
        //绘制道具

        gl.glBlendFunc(GL10.GL_ONE, GL10.GL_ONE_MINUS_SRC_ALPHA);
        batcher.beginBatch(GameAssets.font_texture);
        if(mode == RoomRule.MODE_BATTLE) {
            GameAssets.font.setScale(1.5f, 2.5f);
            GameAssets.font.drawText(batcher, highScores.get(0), 196, 1740);
            GameAssets.font.setScale(1.5f,2.0f);
            GameAssets.font.drawText(batcher, highScores.get(1), 196, 1566);
            GameAssets.font.drawText(batcher, highScores.get(2), 700, 1566);
        }else {
            GameAssets.font.setScale(1.5f, 2.5f);
            GameAssets.font.drawText(batcher,highScores.get(0),340,1620);
            GameAssets.font.drawText(batcher,highScores.get(1),700,1620);
        }
        batcher.endBatch();
        //绘制最高分

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
