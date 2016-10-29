package com.hunter.game;

import com.wxyz.framework.Screen;
import com.wxyz.framework.impl.GLGame;

/**
 * Created by weiyan on 2016/10/29.
 */

public class FoxHunter extends GLGame {
    @Override
    public Screen getStartScreen() {
        Assets.load(this);
        return new MainScreen(this);
    }
}
