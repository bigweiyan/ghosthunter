package com.hunter.network;

import com.hunter.game.models.Item;
import com.hunter.game.models.RoomRule;
import com.hunter.game.models.Signal;

import java.util.ArrayList;

/**
 * 网络接口.
 * Created by weiyan on 2016/11/12.
 */

public interface NetworkSupport {

    /**
     * 没有这个游戏房间.
     */
    public static final int NO_SUCH_ROOM = 0;
    /**
     * 房间建立，未全部准备.
     */
    public static final int NOT_READY_YET = 1;
    /**
     * 房间建立且全部准备.
     */
    public static final int READY_TO_START = 2;
    /**
     * 游戏开始.
     */
    public static final int START = 3;
    /**
     * 游戏结束.
     */
    public static final int GAME_OVER = 4;

    /**
     * 检测是否有网络.
     * @return 是：有网络 否：无网络
     */
    public boolean checkLink();


    /**
     * 创建新的游戏.
     * @param mode 游戏模式
     * @param hostName 房主昵称
     * @param useItem 使用道具
     * @param autoReady 自动准备
     * @param signals 信号源列表
     * @return 服务器产生的房间号
     * @throws NetworkException
     */
    public int createRoom(int mode, String hostName, boolean useItem,
                          boolean autoReady, ArrayList<Signal> signals) throws NetworkException;

    /**
     * 加入已创建的房间。加入成功时返回true，否则抛出异常NetworkException，在异常中标明错误类型.
     * <p>如果未发生异常但没有加入成功，则返回false
     * @param roomNumber 房间号
     * @param playerName 玩家昵称
     * @param isBlue 是否为蓝队。此参数仅当团队模式时有效，目标房间是混战模式时，此参数默认为false
     * @return 加入成功与否。是：成功；否：失败（不返回）
     * @throws NetworkException
     */
    public boolean checkIn(int roomNumber, String playerName, boolean isBlue) throws NetworkException;

    /**
     * 得到蓝方队员的列表（团队模式），即列表1.
     * @param roomNumber 房间号码
     * @return 蓝方队员列表
     * @throws NetworkException
     */
    public ArrayList<String> getMembersBlue(int roomNumber) throws  NetworkException;

    /**
     * 离开已创建的房间。离开成功时返回true，否则抛出异常，在异常中标明错误类型.
     * <p>如果未发生异常但没有离开成功，则返回false
     * @param roomNumber 房间号
     * @param playerName 玩家昵称
     * @return 是否退出成功
     * @throws NetworkException
     */
    public boolean checkOut(int roomNumber, String playerName) throws NetworkException;


    /**
     * 得到红方队员的列表（团队模式）/所有玩家的列表（混战模式），即列表2.
     * @param roomNumber 房间号码
     * @return 红方队员列表
     * @throws NetworkException
     */
    public ArrayList<String> getMembersRed(int roomNumber) throws  NetworkException;

    /**
     * 得到游戏的规则
     * @param roomNumber 房间号码
     * @return 游戏规则
     * @throws NetworkException
     */
    public RoomRule getRoomRule(int roomNumber) throws NetworkException;

    /**
     * 发出准备/取消准备信号。成功则返回是\否，失败则抛出异常
     * @param roomNumber 发出信号玩家所在的房间
     * @param playerName 发出信号的玩家
     * @return 是：准备 否：取消准备
     * @throws NetworkException
     */
    public boolean gameReady(int roomNumber, String playerName) throws NetworkException;

    /**
     * 房主发出开始游戏信号
     * @param roomNumber 待查房间号
     * @return 游戏是否开始，开始则返回true，如果未全准备好，返回false，有异常抛出
     * @throws NetworkException
     */
    public boolean gameStart(int roomNumber) throws NetworkException;

    /**
     * 当前游戏的状态.
     * @param roomNumber 待查房间号
     * @return 游戏状态，详见接口变量
     * @throws NetworkException
     */
    public int getGameState(int roomNumber) throws NetworkException;


    /**
     * 设置当前游戏的状态.
     * @param roomNumber 房间号，改变之后的状态
     * @return 是否设置成功
     * @throws NetworkException
     */
    public boolean setGameState(int roomNumber, int gameState) throws NetworkException;
    /**
     * 查询房主.
     * @param roomNumber 房间号码
     * @return 房主昵称
     * @throws NetworkException
     */
    public String getHostName(int roomNumber) throws NetworkException;

    /**
     * 获得最高分列表。其中对于混战模式，获得前三人的分数和名字（分数降序）,例如：
     * <br>"Name 2","Name 1","Name 0"
     * <br>对于团队模式，获得两个团队总分（分数降序）,例如
     * <br>"Blue 5","Red 4"
     * @param roomNumber 房间号
     * @return 排行榜
     * @throws NetworkException
     */
    public ArrayList<String> getHighScores(int roomNumber) throws NetworkException;

    /**
     * 获得服务器上用户当前收到的自身/对手的状态影响.
     * @param roomNumber
     * @param playerName
     * @return
     * @throws NetworkException
     */
    public ArrayList<Item> getItemsEffect(int roomNumber, String playerName) throws NetworkException;

    /**
     * 用户找到的信号源，服务器返回一个道具(50%几率)
     * @param roomNumber
     * @param playerName
     * @param signal 信号源的编号
     * @return
     * @throws NetworkException
     */
    public Item findSignal(int roomNumber, String playerName, int signal) throws NetworkException;
}
