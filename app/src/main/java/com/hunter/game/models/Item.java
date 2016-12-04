package com.hunter.game.models;

/**
 * 道具类.用于在游戏中获得.
 * Created by weiyan on 2016/11/14.
 */

public class Item {
    public static final int ITEM_KNOW_FREQ = 1;
    public static final int ITEM_COMPASS_LOSS = 2;
    public static final int ITEM_FREQ_LOSS = 3;
    public static final int ITEM_ENLARGE_FREQ = 4;
    public static final int ITEM_DIRECT_REVERT = 5;

    /**
     * 道具的种类.
     */
    private int itemType;
    /**
     * 道具生效的剩余时间.
     */
    private float remainTime;
    public Item(int itemType) {

    }

    public void setRemainTime(float remainTime) {
        this.remainTime = remainTime;
    }
    public float getRemainTime() {
        return remainTime;
    }
    public int getItemType() {
        return itemType;
    }
}
