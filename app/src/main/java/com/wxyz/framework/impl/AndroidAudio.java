package com.wxyz.framework.impl;

import android.app.Activity;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioManager;
import android.media.SoundPool;

import com.wxyz.framework.Audio;
import com.wxyz.framework.Music;
import com.wxyz.framework.Sound;

import java.io.IOException;

public class AndroidAudio implements Audio {
	AssetManager assets;
	SoundPool soundPool;
	@SuppressWarnings("deprecation")
	public AndroidAudio(Activity activity){
		activity.setVolumeControlStream(AudioManager.STREAM_MUSIC);
		this.assets = activity.getAssets();
		this.soundPool = new SoundPool(20,AudioManager.STREAM_MUSIC,0);
	}
	@Override
	/**
	 * 包装音乐
	 */
	public Music newMusic(String fileName) {
		try{
			AssetFileDescriptor assetFileDescriptor = assets.openFd(fileName);
			return new AndroidMusic(assetFileDescriptor);
		}catch(IOException e){
			throw new RuntimeException("Couldn't load music '"+fileName+"'");
		}
	}
	/**
	 * 包装音效
	 */
	@Override
	public Sound newSound(String fileName) {
		try{
			AssetFileDescriptor assetFileDescriptor = assets.openFd(fileName);
			int soundID = soundPool.load(assetFileDescriptor, 0);
			return new AndroidSound(soundPool,soundID);
		}catch(IOException e){
			throw new RuntimeException("Couldn't load sound '"+fileName+"'");
		}
	}

}
