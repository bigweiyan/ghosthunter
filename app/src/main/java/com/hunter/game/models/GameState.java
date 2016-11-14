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
     * 判断信号源是否被找到.
     * 每一个成员对应RoomRule中signals是否被找到.
     */
    public ArrayList<Boolean> isSignalsFound;

    /**
     * 当前获得的道具.可能为null(无道具).
     */
    public Item item;

    /**
     * 当前生效的道具.
     */
    public ArrayList<Item> workingItems;

    /**
     * 当前的频率.取值范围1-6,参见Signal类.
     */
    public int presentFreq;

    public GameState(int gameState,int signalNumber) {
        this.gameState = gameState;
        isSignalsFound = new ArrayList<>();
        for (int i = 0; i < signalNumber; i++) {
            isSignalsFound.add(false);
        }
    }
}
