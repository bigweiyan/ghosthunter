package com.hunter.game.models;

import java.util.ArrayList;

/**
 * 游戏规则的封装.
 * Created by weiyan on 2016/11/12.
 */

public class RoomRule {
    /**
     * 团队模式.
     */
    public static final int MODE_TEAM = 0;
    /**
     * 混战模式.
     */
    public static final int MODE_BATTLE = 1;
    public boolean useItem;
    public boolean autoReady;
    public int mode;
    public ArrayList<Signal> signals;

}
