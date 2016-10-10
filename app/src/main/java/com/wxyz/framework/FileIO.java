package com.wxyz.framework;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
/**
 * 用于返回对应文件夹的输入输出流，参见AndroidFileIO
 * @author ThinkPad
 *
 */
public interface FileIO {
	public InputStream readAsset(String fileName) throws IOException;
	public InputStream readFile(String fileName) throws IOException;
	public OutputStream writeFile(String fileName) throws IOException;
}
