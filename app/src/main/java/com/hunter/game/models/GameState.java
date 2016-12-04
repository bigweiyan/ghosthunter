package com.hunter.game.models;

import java.util.ArrayList;

/**
 * 存储游戏的状态，如是否开始，道具效果等.
 * Created by weiyan on 2016/11/13.
 */

public class GameState {

    /**
     * 没有这个游戏房间.
     */
    public static final int NO_SUCH_ROOM = 0;
    /**
     * 房间建立，未全部准备.
     */
    public static final int NOT_READY_YET = 1;
    /**
     * 房间建立且全部准备.
     */
    public static final int READY_TO_START = 2;
    /**
     * 游戏开始.
     */
    public static final int START = 3;
    /**
     * 游戏结束.
     */
    public static final int GAME_OVER = 4;

    /**
     * 存储游戏的状态：NO_SUCH_ROOM;NOT_READY_YET;READY_TO_START;START;GAME_OVER.
     */
    public int gameState;

    /**
     * 是否可以进行采集周围信号源的操作。否代表正在冷却.
     */
    public boolean isSearchButtonWake;


    /**
     * 以下三个变量分别表示信号的数据.
     * 对应是否被自己找到.
     * 以及当前探测器感受信号的大小(范围0.0f-1.0f)
     * 对于团队模式，还可以设置信号源的归属(未发现0,红队1,蓝队2)
     */
    public ArrayList<Signal> signals;
    public ArrayList<Boolean> isSignalsFound;
    public ArrayList<Float> signalSound;
    public ArrayList<Integer> signalBelong;

    /**
     * 当前获得的道具.可能为null(无道具).
     * 当前生效的道具.
     */
    public Item item;
    public ArrayList<Item> workingItems;//包括别人的debuff

    /**
     * 当前测向机的频率.取值范围1-6,参见Signal类.
     */
    private int presentFreq;

    public GameState(int gameState,ArrayList<Signal> signals) {
        this.gameState = gameState;
        isSearchButtonWake = true;
        presentFreq = Signal.FREQ_1;
        workingItems = new ArrayList<>();

        this.signals = signals;
        isSignalsFound = new ArrayList<>();
        signalSound = new ArrayList<>();
        signalBelong = new ArrayList<>();
        for (int i = 0; i < signals.size(); i++) {
            isSignalsFound.add(false);
            signalSound.add(0.0f);
            signalBelong.add(0);
        }
    }

    /**
     * 更新生效道具的剩余时间，更新信号源的声音大小.
     * @param deltaTime
     * signalSound
     * 同时负责统计已进行的时间
     * 更新状态
     */
    public void updateSound(float deltaTime) {
        // TODO: 2016/12/4  
    }

    /**
     * 从服务器获得了新的道具.
     * @param item
     */
    public void receiveItem(Item item) {
        // TODO: 2016/12/4  
    }

    /**
     * 从服务器获得了新的附加状态.
     * @param item
     *
     */
    public void receiveAffect(Item item) {
        // TODO: 2016/12/4  
    }

    /**
     * 用户使用了道具.
     * 只有一个道具所以没有参数
     */
    public void useItem() {
        // TODO: 2016/12/4  
    }

    /**
     * 用户点击了采集.更新采集状态并设searchButtonWake为false
     * @param latitude 当前纬度
     * @param longitude 当前经度
     * @return 返回采集成功的信号源的id,如果采集失败返回-1.
     */
    public int search(double latitude, double longitude) {
        return -1;
    }

    public void addFreq() {
        presentFreq ++;
        if (presentFreq > 6) presentFreq = 1;
    }

    public int getFreq() {
        return presentFreq;
    }
}
