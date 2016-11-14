package com.hunter.game.models;

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
}
