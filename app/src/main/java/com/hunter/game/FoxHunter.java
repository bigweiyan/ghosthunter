package com.hunter.game;

import android.os.Bundle;

import com.wxyz.framework.Screen;
import com.wxyz.framework.impl.GLGame;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by weiyan on 2016/10/29.
 */

public class FoxHunter extends GLGame {
    private boolean firstCreated;
    public FoxHunter() {
        super();
        firstCreated = true;
    }
    @Override
    public Screen getStartScreen() {
        return new MainScreen(this);
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume(){
        super.onResume();
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        super.onSurfaceCreated(gl,config);
        if (firstCreated){
            firstCreated = false;
            Assets.load(this);
        }
        else{
            Assets.reload();
        }
    }
}
