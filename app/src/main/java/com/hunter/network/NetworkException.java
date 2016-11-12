package com.hunter.network;

/**
 * 网络异常的封装.
 * Created by weiyan on 2016/11/12.
 */

public class NetworkException extends Exception {
    /**
     * 连接超时.
     */
    public static final String TIME_OUT = "Time Out!";
    /**
     * 超出人数上限.
     */
    public static final String OUT_OF_RANGE = "Out Of Range!";
    /**
     * 未定义异常.
     */
    public static final String UNKNOWN = "Unknown exception!";
    /**
     * 房间号错误（无此房间）.
     */
    public static final String WRONG_NUM = "Wrong room number!";
    /**
     * 使用了重复的昵称.
     */
    public static final String SAME_NAME = "There's a same name guy!";

    public NetworkException() {};

    public NetworkException(String message) {
        super(message);
    }
}
