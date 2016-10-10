package com.wxyz.framework.impl;

import com.wxyz.framework.Game;
import com.wxyz.framework.Screen;

public abstract class GLScreen extends Screen {
	protected final GLGame glGame;
	protected final GLGraphics glGraphics;
	public GLScreen(Game game) {
		super(game);
		glGame = (GLGame)game;
		glGraphics = ((GLGame)game).getGLGraphics();
	}
}
