package com.wxyz.framework.impl;

import android.media.SoundPool;

import com.wxyz.framework.Sound;

public class AndroidSound implements Sound {
	int soundID;
	SoundPool soundPool;
	public AndroidSound(SoundPool soundPool, int soundID) {
		this.soundID=soundID;
		this.soundPool=soundPool;
	}

	@Override
	public void play(float volume) {
		soundPool.play(soundID, volume, volume, 0, 0, 1);
	}

	@Override
	public void dispose() {
		soundPool.unload(soundID);
	}

}
