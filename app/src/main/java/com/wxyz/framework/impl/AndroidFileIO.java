package com.wxyz.framework.impl;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.os.Environment;
import android.preference.PreferenceManager;

import com.wxyz.framework.FileIO;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class AndroidFileIO implements FileIO{
	Context context;
	AssetManager assets;
	String externalStoragePath;
	
	public AndroidFileIO(Context context){
		this.context=context;
		this.assets=context.getAssets();
		this.externalStoragePath = Environment.getExternalStorageDirectory()
				.getAbsolutePath()+File.separator;
	}
	@Override
	/**
	 * 读取游戏资源（图像，声音）
	 * <p>注一：资源放在项目的assets目录</p>
	 * <p>注二：此部分文件不可写入，即存档使用read/writefile方法</p>
	 */
	public InputStream readAsset(String fileName) throws IOException{
		return assets.open(fileName);
	}
	
	@Override
	/**
	 * 读取文件，一般用来读取配置，位置为外部存储（SD卡）的根目录
	 */
	public InputStream readFile(String fileName) throws IOException{
		return new FileInputStream(externalStoragePath+fileName);
	}
	
	@Override
	/**
	 * 写入文件，一般用来写入配置，位置为外部存储（SD卡）的根目录
	 */
	public OutputStream writeFile(String fileName) throws IOException{
		return new FileOutputStream(externalStoragePath+fileName);
	}
	public SharedPreferences getPreferences(){
		return PreferenceManager.getDefaultSharedPreferences(context);
	}
}
