package com.hunter.Player;

/**
 * Created by BG2CYR on 2016/11/22.
 */

public class player {
    float Latitude;
    float Longitude;
    void getPlayerLocation(float lat,float longt)
    {
        this.Latitude=lat;
        this.Longitude=longt;
    }
    public float getLatitude()
    {
        return Latitude;
    }
    public float getLongitude()
    {
        return Longitude;
    }
}
