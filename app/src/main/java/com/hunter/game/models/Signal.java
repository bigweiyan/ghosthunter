package com.hunter.game.models;

import com.hunter.game.GameAssets;

/**
 * 信号源对象的封装.
 * Created by weiyan on 2016/11/12.
 */

public class Signal {
    public static final int FREQ_1 = 1;
    public static final int FREQ_2 = 2;
    public static final int FREQ_3 = 3;
    public static final int FREQ_4 = 4;
    public static final int FREQ_5 = 5;
    public static final int FREQ_6 = 6;

    /**
     * 纬度.
     */
    public double latitude;
    /**
     * 经度.
     */
    public double longitude;

    /**
     * 频率.
     */
    public int frequency;

    /**
     * 播放声音的格式、当前播放的时间、序号和长度
     */
    public int[] soundMap;
    int soundIndex=-1;
    float time;
    float volume;

    public Signal(double latitude, double longitude, int frequency) {
        this.frequency = frequency;
        this.latitude = latitude;
        this.longitude = longitude;
        time = 0.5f;
    }

    public void play(float dtime)
    {
        time-=dtime;
        if (time < 0)
        {
            soundIndex = (soundIndex+1)%8;
            int NowId=soundMap[soundIndex];
            if(NowId==0)
            {
                GameAssets.short_sound.play(volume);
            }
            if(NowId==1)
            {
                GameAssets.long_sound.play(volume);
            }
            if(NowId==0)
            {
                GameAssets.empty_sound.play(volume);
            }
            time=0.5f;
        }
    }

    public void setVolume(float volume) {
        this.volume = volume;
    }

    public void setSoundMap(int[] soundMap) {
        this.soundMap = soundMap;
    }
}
