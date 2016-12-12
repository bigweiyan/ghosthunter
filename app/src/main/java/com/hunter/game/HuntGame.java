package com.hunter.game;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;

import com.gps.Sensor_If;
import com.hunter.game.models.Item;
import com.hunter.game.models.RoomRule;
import com.hunter.game.models.Signal;
import com.hunter.game.models.Tools;
import com.hunter.network.NetworkException;
import com.hunter.network.NetworkImplement;
import com.hunter.network.NetworkSupport;
import com.hunter.sensor.SensorSupport;
import com.wxyz.framework.Screen;
import com.wxyz.framework.impl.GLGame;

import java.util.ArrayList;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * 测向机界面.
 * Created by weiyan on 2016/11/13.
 */

public class HuntGame extends GLGame {
    private boolean firstCreated = true;
    public String playerName;
    public int roomNumber;
    public int mode;
    private NetworkSupport ns;
    private SensorSupport ss;
    private ArrayList<Item> items;
    private ArrayList<Integer> signalBelong;
    private ArrayList<String> highScores;
    private ArrayList<Signal> signals;
    private Item itemGet; //服务器赋予的道具
    private boolean newData;
    private boolean useItemBoolean;
    //服务器中的同步内容 其中newData标志服务器是否更新


    /**
     * 用户找到了一个信号源
     */
    private static final int MASSAGE_SIGNAL = 0x1;
    /**
     * 用户使用了一个道具
     */
    private static final int MASSAGE_USE_ITEM = 0x2;
    private int signalFound;
    private Item itemUse;
    private int massage;
    //用户的请求 massage用来标志请求的类型

    private Handler mHandler = new Handler();
    private Runnable timerTask = new Runnable() {
        @Override
        public void run() {
            if (state == GLGameState.Running) {
                try {
                    newData = false;
                    items = ns.getItemsEffect(roomNumber, playerName);
                    //获得道具效果
                    if (mode == RoomRule.MODE_TEAM) {
                        signalBelong = ns.getSignalBelong(roomNumber);
                    }
                    //获得信号源归属
                    highScores = ns.getHighScores(roomNumber);
                    //获得最高分
                    newData = true;
                    //同步服务器

                    if (massage%2 == 1) {
                        itemGet = ns.findSignal(roomNumber,playerName,signalFound);
                    }
                    //相应信号源查找操作
                    if ((massage >> 1) % 2 == 1 ) {
                        ns.useItem(roomNumber,playerName,itemUse);
                    }
                    //相应使用道具操作
                    massage = 0;
                    //处理本地消息
                } catch (NetworkException e) {
                    Tools.showDialog(HuntGame.this, "网络异常", e.getMessage());
                }
            }
            mHandler.postDelayed(timerTask,1000);
        }
    };

    public HuntGame() {
        super();
    }

    @Override
    public void onCreate(Bundle savedInstanceState){

        StrictMode.ThreadPolicy policy=new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        super.onCreate(savedInstanceState);

        Intent intent =  getIntent();
        playerName = intent.getStringExtra("name");
        roomNumber = intent.getIntExtra("roomNumber",0);
        mode = intent.getIntExtra("mode", RoomRule.MODE_BATTLE);
        massage = 0;
        newData = false;

        ns = new NetworkImplement();
        ss = new Sensor_If(this);

        try {
            RoomRule rule = ns.getRoomRule(roomNumber);
            signals = rule.signals;
            useItemBoolean = rule.useItem;
            highScores = ns.getHighScores(roomNumber);

        } catch (NetworkException e) {
            Tools.showDialog(this,"网络异常","请检查网络连接");
        }
    }
    @Override
    public Screen getStartScreen() {
        return new GameScreen(this);
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        super.onSurfaceCreated(gl,config);
        if (firstCreated) {
            firstCreated = false;
            GameAssets.load(this);
        }else {
            GameAssets.reload();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mHandler.post(timerTask);
    }

    @Override
    public void onPause() {
        super.onPause();
        mHandler.removeCallbacks(timerTask);
    }

    public SensorSupport getSs() {
        return ss;
    }

    /**
     * 获得受影响的道具组合.
     * @return
     */
    public ArrayList<Item> getItems() {
        return items;
    }

    public ArrayList<String> getHighScores() {
        return highScores;
    }

    public ArrayList<Integer> getSignalBelong() {
        return signalBelong;
    }

    public ArrayList<Signal> getSignals() {
        return signals;
    }

    private void setMassage(int massage){
        this.massage = this.massage | massage;
    }

    /**
     * 传递找到信号源的消息。信号源不许为负数
     * @param signal
     */
    public void findSignal(int signal) {
        setMassage(MASSAGE_SIGNAL);
        signalFound = signal;
    }

    /**
     * 传递使用道具的消息。道具不许为空
     * @param item
     */
    public void useItem(Item item) {
        setMassage(MASSAGE_USE_ITEM);
        itemUse = item;
    }

    public Item getItem() {
        return itemGet;
    }

    public void dataReceived(){
        newData = false;
        itemGet = null;
    }

    public boolean isNewData(){
        return newData;
    }

    public void gameOver() {
        Intent intent = new Intent();
        intent.setClass(this,HighScoreActivity.class);
        intent.putExtra("roomNumber",roomNumber);
        startActivity(intent);
        finish();
    }

    public boolean getUseItem(){
        return useItemBoolean;
    }
}
