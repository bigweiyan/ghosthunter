package com.hunter.game;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;

import com.hunter.game.models.RoomRule;
import com.wxyz.framework.Screen;
import com.wxyz.framework.impl.GLGame;

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
}
