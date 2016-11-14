package com.hunter.network;

import android.util.Log;

import com.hunter.game.models.RoomRule;
import com.hunter.game.models.Signal;
import com.hunter.network.NetworkExample;

import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Properties;
import java.util.concurrent.ExecutionException;

import static java.net.HttpURLConnection.HTTP_OK;

/**
 * 网络类的实现
 * Created by abgnwl on 2016-11-13.
 */

public class NetworkImplement implements NetworkSupport
{
    final private String SEVER = "10.0.2.2";
    //final String SEVER = "172.17.25.216";
    final private String DRIVER="com.mysql.jdbc.Driver";
    final private String URL="jdbc:mysql://"+SEVER+"/foxhunter?user=root&password=foxhunter";

    final private String TIME_OUT = "3000";


    private Connection getConnection()
    {
        try
        {
            Class.forName(DRIVER);
            Log.d("getConnection","driver ok");

            Properties properties = new Properties();
            properties.put("connectTimeout", TIME_OUT);

            // this cannot set time_out
            //Connection connection = DriverManager.getConnection(url,username,password,properties);


            Log.d("getConnection","SET properties ok");

            //try new way to getConnection
            Connection connection = DriverManager.getConnection(URL,properties);


            Log.d("getConnection","Connection OK!");

            return connection;
        }
        catch (Exception e)
        {
            Log.d("getConnection","Connection ERROR!");
            return null;
        }
    }


    /**
     * 检测是否有网络.
     * @return 是：有网络 否：无网络
     */
    public boolean checkLink()
    {
        if(getConnection()==null)
            return false;
        else
            return true;
    }

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
                          boolean autoReady, ArrayList<Signal> signals) throws NetworkException
    {
        Connection connection = getConnection();
        if(connection==null)
        {
            Log.d("createRoom", "TIME OUT");
            throw new NetworkException(NetworkException.TIME_OUT);
        }

        final int iUseItem = useItem?1:0;
        final int iAutoReady = autoReady?1:0;

        try
        {
            Statement stmt = connection.createStatement();
            String roomCreate = "insert into room values(null," + mode + ",'" + hostName + "'," + iUseItem + "," + iAutoReady + "," + NOT_READY_YET + ");";

            Log.d("createRoom", roomCreate);

            if (stmt.executeUpdate(roomCreate) == 1)
            {
                String roomIDQuery = "select max(roomid) from room;";
                ResultSet rst = stmt.executeQuery(roomIDQuery);
                Log.d("createRoom",roomIDQuery);
                if(rst.next())
                {
                    final int roomID = rst.getInt("max(roomid)");
                    for(Signal signal:signals)
                    {
                        String signalInsert = "insert into signal values("+roomID+","+signal.longitude+","+signal.latitude+","+signal.frequency+");";
                        //Log.d("createRoom",signalInsert);
                        if(stmt.executeUpdate(signalInsert)!=1)
                        {
                            rst.close();
                            stmt.close();
                            connection.close();
                            throw new NetworkException(NetworkException.UNKNOWN);
                        }
                    }
                    rst.close();
                    stmt.close();
                    connection.close();
                    return roomID;
                }
                else
                {
                    rst.close();
                    stmt.close();
                    connection.close();
                    throw new NetworkException(NetworkException.UNKNOWN);
                }
            }
            else
            {
                stmt.close();
                connection.close();
                throw new NetworkException(NetworkException.UNKNOWN);
            }
        }
        catch (Exception e)
        {
            Log.d("createRoom","catch exception "+e);
            throw new NetworkException(NetworkException.UNKNOWN);
        }
    }


    /**
     * 加入已创建的房间。加入成功时返回true，否则抛出异常NetworkException，在异常中标明错误类型.
     * <p>如果未发生异常但没有加入成功，则返回false
     * @param roomNumber 房间号
     * @param playerName 玩家昵称
     * @param isBlue 是否为蓝队。此参数仅当团队模式时有效，目标房间是混战模式时，此参数默认为false
     * @return 加入成功与否。是：成功；否：失败（不返回）
     * @throws NetworkException
     */
    public boolean checkIn(int roomNumber, String playerName, boolean isBlue) throws NetworkException
    {
        Connection connection = getConnection();
        if(connection==null)
        {
            Log.d("checkIn", "TIME OUT");
            throw new NetworkException(NetworkException.TIME_OUT);
        }

        final int iIsBlue = isBlue?1:0;

        try
        {
            Statement stmt = connection.createStatement();

            String roomQuery = "select * from room where roomid="+roomNumber+";";
            ResultSet roomRst = stmt.executeQuery(roomQuery);
            if(!roomRst.next())
            {
                roomRst.close();
                stmt.close();
                connection.close();
                throw new NetworkException(NetworkException.WRONG_NUM);
            }

            String userQuery = "select * from user where roomid="+roomNumber+" and username='"+playerName+"';";
            ResultSet userRst = stmt.executeQuery(userQuery);
            if(userRst.next())
            {
                userRst.close();
                stmt.close();
                connection.close();
                throw new NetworkException(NetworkException.SAME_NAME);
            }

            String userUpdate = "insert into user values("+roomNumber+",'"+playerName+"',"+iIsBlue+","+"0);";
            if(stmt.executeUpdate(userUpdate)==1)
            {
                stmt.close();
                connection.close();
                return true;
            }
            else
            {
                stmt.close();
                connection.close();
                return false;
            }
        }
        catch (NetworkException e)
        {
            Log.d("checkIn","Network Exception "+e);
            throw e;
        }
        catch (Exception e)
        {
            Log.d("checkIn","other Exception "+e);
            throw new NetworkException(NetworkException.UNKNOWN);
        }
    }


    /**
     * 离开已创建的房间。离开成功时返回true，否则抛出异常，在异常中标明错误类型.
     * <p>如果未发生异常但没有离开成功，则返回false
     * @param roomNumber 房间号
     * @param playerName 玩家昵称
     * @return 是否退出成功
     * @throws NetworkException
     */
    public boolean checkOut(int roomNumber, String playerName) throws NetworkException
    {
        Connection connection = getConnection();
        if(connection==null)
        {
            Log.d("checkOut", "TIME OUT");
            throw new NetworkException(NetworkException.TIME_OUT);
        }

        try
        {
            Statement stmt = connection.createStatement();

            String roomQuery = "select * from room where roomid="+roomNumber+";";
            ResultSet roomRst = stmt.executeQuery(roomQuery);
            if(!roomRst.next())
            {
                roomRst.close();
                stmt.close();
                connection.close();
                throw new NetworkException(NetworkException.WRONG_NUM);
            }

            String userUpdate = "delete from user where roomid="+roomNumber+" and username='"+playerName+"';";
            if(stmt.executeUpdate(userUpdate)==1)
            {
                stmt.close();
                connection.close();
                return true;
            }
            else
            {
                stmt.close();
                connection.close();
                return false;
            }
        }
        catch (NetworkException e)
        {
            Log.d("checkOut","Network Exception "+e);
            throw e;
        }
        catch (Exception e)
        {
            Log.d("checkOut","other Exception "+e);
            throw new NetworkException(NetworkException.UNKNOWN);
        }
    }



    /**
     * 得到蓝方队员的列表（团队模式），即列表1.
     * @param roomNumber 房间号码
     * @return 蓝方队员列表
     * @throws NetworkException
     */
    public ArrayList<String> getMembersBlue(int roomNumber) throws  NetworkException
    {
        throw new NetworkException(NetworkException.TIME_OUT);
    }

    /**
     * 得到红方队员的列表（团队模式）/所有玩家的列表（混战模式），即列表2.
     * @param roomNumber 房间号码
     * @return 红方队员列表
     * @throws NetworkException
     */
    public ArrayList<String> getMembersRed(int roomNumber) throws  NetworkException
    {
        throw new NetworkException(NetworkException.TIME_OUT);
    }

    /**
     * 得到游戏的规则
     * @param roomNumber 房间号码
     * @return 游戏规则
     * @throws NetworkException
     */
    public RoomRule getRoomRule(int roomNumber) throws NetworkException
    {
        throw new NetworkException(NetworkException.TIME_OUT);
    }

    /**
     * 发出准备/取消准备信号。成功则返回是\否，失败则抛出异常
     * @param roomNumber 发出信号玩家所在的房间
     * @param playerName 发出信号的玩家
     * @return 是：准备 否：取消准备
     * @throws NetworkException
     */
    public boolean gameReady(int roomNumber, String playerName) throws NetworkException
    {
        throw new NetworkException(NetworkException.TIME_OUT);
    }



    /**
     * 房主发出开始游戏信号
     * @param roomNumber 待查房间号
     * @return 游戏是否开始，开始则返回true，如果未全准备好，返回false，有异常抛出
     * @throws NetworkException
     */
    public boolean gameStart(int roomNumber) throws NetworkException
    {
        return true;
    }


    /**
     * 当前游戏的状态.
     * @param roomNumber 待查房间号
     * @return 游戏状态，详见接口变量
     * @throws NetworkException
     */
    public int getGameState(int roomNumber) throws NetworkException
    {
        return 0;
    }

    /**
     * 查询房主.
     * @param roomNumber 房间号码
     * @return 房主昵称
     * @throws NetworkException
     */
    public String getHostName(int roomNumber) throws NetworkException
    {
        throw new NetworkException(NetworkException.TIME_OUT);
    }

}
