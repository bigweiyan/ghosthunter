package com.hunter.network;

import android.util.Log;

import com.hunter.game.models.Item;
import com.hunter.game.models.RoomRule;
import com.hunter.game.models.Signal;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.TreeMap;

import static com.hunter.game.models.Item.ITEM_COMPASS_LOSS;
import static com.hunter.game.models.Item.ITEM_DIRECT_REVERT;
import static com.hunter.game.models.Item.ITEM_ENLARGE_FREQ;
import static com.hunter.game.models.Item.ITEM_FREQ_LOSS;
import static com.hunter.game.models.Item.ITEM_SHORTEN_COLD;
import static com.hunter.game.models.RoomRule.MODE_BATTLE;
import static com.hunter.game.models.RoomRule.MODE_TEAM;

/**
 * 网络类的实现
 * Created by abgnwl on 2016-11-13.
 */

public class NetworkImplement implements NetworkSupport
{
    //final private String SEVER = "10.0.2.2";
    final private String SEVER = "192.168.191.1";
    final private String DRIVER="com.mysql.jdbc.Driver";
    final private String URL="jdbc:mysql://"+SEVER+":3306/foxhunter?user=root&password=foxhunter";

    final private String TIME_OUT = "3000";

    /**
     * 建立数据库链接
     * @return 未连接返回null，否则返回Connection
     */
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
            Log.d("getConnection","Connection ERROR!"+e.toString());
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
        Log.d("createRoom","hostName="+hostName);
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
                    int signalId = 0;
                    for(Signal signal:signals)
                    {
                        String signalInsert = "insert into signal values("+roomID+","+signal.longitude+","+signal.latitude+","+signal.frequency+",0,"+signalId+");";
                        signalId++;
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
            roomRst.close();

            String userQuery = "select * from user where roomid="+roomNumber+" and username='"+playerName+"';";
            ResultSet userRst = stmt.executeQuery(userQuery);
            if(userRst.next())
            {
                userRst.close();
                stmt.close();
                connection.close();
                throw new NetworkException(NetworkException.SAME_NAME);
            }
            userRst.close();

            String userUpdate = "insert into user values("+roomNumber+",'"+playerName+"',"+iIsBlue+","+"0,0);";
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
            roomRst.close();

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
        Connection connection = getConnection();
        if(connection==null)
        {
            Log.d("getMembersBlue", "TIME OUT");
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
            roomRst.close();

            ArrayList<String> arrayList = new ArrayList<>();
            String userQuery = "select * from user where roomid="+roomNumber+" and isblue=1;";
            ResultSet rst = stmt.executeQuery(userQuery);

            while(rst.next())
            {
                arrayList.add(rst.getString("username"));
            }
            rst.close();
            stmt.close();
            connection.close();
            return arrayList;
        }
        catch (NetworkException e)
        {
            Log.d("getMembersBlue","Network Exception "+e);
            throw e;
        }
        catch (Exception e)
        {
            Log.d("getMembersBlue","other Exception "+e);
            throw new NetworkException(NetworkException.UNKNOWN);
        }
    }


    /**
     * 得到红方队员的列表（团队模式）/所有玩家的列表（混战模式），即列表2.
     * @param roomNumber 房间号码
     * @return 红方队员列表
     * @throws NetworkException
     */
    public ArrayList<String> getMembersRed(int roomNumber) throws  NetworkException
    {
        Connection connection = getConnection();
        if(connection==null)
        {
            Log.d("getMembersRed", "TIME OUT");
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
            roomRst.close();

            ArrayList<String> arrayList = new ArrayList<>();
            String userQuery = "select * from user where roomid="+roomNumber+" and isblue=0;";
            ResultSet rst = stmt.executeQuery(userQuery);

            while(rst.next())
            {
                arrayList.add(rst.getString("username"));
            }
            rst.close();
            stmt.close();
            connection.close();
            return arrayList;
        }
        catch (NetworkException e)
        {
            Log.d("getMembersRed","Network Exception "+e);
            throw e;
        }
        catch (Exception e)
        {
            Log.d("getMembersRed","other Exception "+e);
            throw new NetworkException(NetworkException.UNKNOWN);
        }
    }


    /**
     * 得到游戏的规则
     * @param roomNumber 房间号码
     * @return 游戏规则
     * @throws NetworkException
     */
    public RoomRule getRoomRule(int roomNumber) throws NetworkException
    {
        Connection connection = getConnection();
        if(connection==null)
        {
            Log.d("getRoomRule", "TIME OUT");
            throw new NetworkException(NetworkException.TIME_OUT);
        }

        try
        {
            Statement stmt = connection.createStatement();

            String roomQuery = "select * from room where roomid=" + roomNumber + ";";
            ResultSet roomRst = stmt.executeQuery(roomQuery);
            if (roomRst.next())
            {
                RoomRule roomRule = new RoomRule(false,false, MODE_TEAM);
                roomRule.mode=roomRst.getInt("mode");
                roomRule.useItem=roomRst.getBoolean("useitem");
                roomRule.autoReady=roomRst.getBoolean("autoready");
                roomRule.signals=new ArrayList<>();
                roomRst.close();

                String signalQuery = "select * from signal where roomid="+roomNumber+";";
                ResultSet rst = stmt.executeQuery(signalQuery);
                while(rst.next())
                {
                    Signal signal = new Signal(rst.getDouble("latitude"),rst.getDouble("longitude"),rst.getInt("frequency"));
                    roomRule.signals.add(signal);
                }

                rst.close();
                stmt.close();
                connection.close();

                return roomRule;
            }
            else
            {
                roomRst.close();
                stmt.close();
                connection.close();
                throw new NetworkException(NetworkException.WRONG_NUM);
            }
        }
        catch (NetworkException e)
        {
            Log.d("getRoomRule","Network Exception "+e);
            throw e;
        }
        catch (Exception e)
        {
            Log.d("getRoomRule","other Exception "+e);
            throw new NetworkException(NetworkException.UNKNOWN);
        }
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
        Connection connection = getConnection();
        if(connection==null)
        {
            Log.d("gameReady", "TIME OUT");
            throw new NetworkException(NetworkException.TIME_OUT);
        }

        try
        {
            Statement stmt = connection.createStatement();

            String userQuery = "select * from user where roomid=" + roomNumber + " and username='"+playerName+"';";
            ResultSet userRst = stmt.executeQuery(userQuery);
            if (userRst.next())
            {
                int isready = userRst.getInt("isready");
                String userUpdate = "update user set isready="+(1-isready)+" where roomid="+roomNumber+" and username='"+playerName+"';";
                if (stmt.executeUpdate(userUpdate)==1)
                {
                    userRst.close();
                    stmt.close();
                    connection.close();
                    if(isready == 1) {
                        return false;
                    } else {
                        return true;
                    }
                }
                else
                {
                    userRst.close();
                    stmt.close();
                    connection.close();
                    throw new NetworkException(NetworkException.UNKNOWN);
                }
            }
            else
            {
                userRst.close();
                stmt.close();
                connection.close();
                throw new NetworkException(NetworkException.UNKNOWN);
            }
        }
        catch (NetworkException e)
        {
            Log.d("gameReady","Network Exception "+e);
            throw e;
        }
        catch (Exception e)
        {
            Log.d("gameReady","other Exception "+e);
            throw new NetworkException(NetworkException.UNKNOWN);
        }
    }


    /**
     * 房主发出开始游戏信号
     * @param roomNumber 待查房间号
     * @return 游戏是否开始，开始则返回true，如果未全准备好，返回false，有异常抛出
     * @throws NetworkException
     */
    public boolean gameStart(int roomNumber) throws NetworkException
    {
        Connection connection = getConnection();
        if(connection==null)
        {
            Log.d("gameStart", "TIME OUT");
            throw new NetworkException(NetworkException.TIME_OUT);
        }

        try
        {
            Statement stmt = connection.createStatement();

            String roomQuery = "select * from room where roomid=" + roomNumber + ";";
            ResultSet roomRst = stmt.executeQuery(roomQuery);
            if (roomRst.next())
            {
                if (roomRst.getBoolean("autoready"))
                {
                    roomRst.close();
                    stmt.close();
                    connection.close();
                    return true;
                }
                else
                {
                    roomRst.close();

                    String userQuery = "select * from user where roomid=" + roomNumber + ";";
                    ResultSet rst = stmt.executeQuery(userQuery);
                    while (rst.next())
                    {
                        if(!rst.getBoolean("isready"))
                        {
                            rst.close();
                            stmt.close();
                            connection.close();
                            return false;
                        }
                    }

                    rst.close();
                    stmt.close();
                    connection.close();

                    return true;
                }
            }
            else
            {
                roomRst.close();
                stmt.close();
                connection.close();
                throw new NetworkException(NetworkException.WRONG_NUM);
            }
        }
        catch (NetworkException e)
        {
            Log.d("gameStart","Network Exception "+e);
            throw e;
        }
        catch (Exception e)
        {
            Log.d("gameStart","other Exception "+e);
            throw new NetworkException(NetworkException.UNKNOWN);
        }
    }


    /**
     * 当前游戏的状态.
     * @param roomNumber 待查房间号
     * @return 游戏状态，详见接口变量
     * @throws NetworkException
     */
    public int getGameState(int roomNumber) throws NetworkException
    {
        Connection connection = getConnection();
        if(connection==null)
        {
            Log.d("getGameState", "TIME OUT");
            throw new NetworkException(NetworkException.TIME_OUT);
        }

        try
        {
            Statement stmt = connection.createStatement();

            String roomQuery = "select * from room where roomid=" + roomNumber + ";";
            ResultSet roomRst = stmt.executeQuery(roomQuery);
            if (roomRst.next())
            {
                int gameState = roomRst.getInt("gamestate");

                roomRst.close();
                stmt.close();
                connection.close();

                return gameState;
            }
            else
            {
                roomRst.close();
                stmt.close();
                connection.close();
                throw new NetworkException(NetworkException.WRONG_NUM);
            }
        }
        catch (NetworkException e)
        {
            Log.d("getGameState","Network Exception "+e);
            throw e;
        }
        catch (Exception e)
        {
            Log.d("getGameState","other Exception "+e);
            throw new NetworkException(NetworkException.UNKNOWN);
        }
    }


    /**
     * 设置当前游戏的状态.
     * @param roomNumber 房间号，改变之后的状态
     * @return 是否设置成功
     * @throws NetworkException
     */
    public boolean setGameState(int roomNumber, int gameState) throws NetworkException
    {
        Connection connection = getConnection();
        if(connection==null)
        {
            Log.d("setGameState", "TIME OUT");
            throw new NetworkException(NetworkException.TIME_OUT);
        }

        try
        {
            Statement stmt = connection.createStatement();

            String roomUpdate = "update room set gamestate="+gameState+" where roomid="+roomNumber+";";
            if (stmt.executeUpdate(roomUpdate)==1)
            {
                stmt.close();
                connection.close();

                return true;
            }
            else
            {
                stmt.close();
                connection.close();

                throw new NetworkException(NetworkException.UNKNOWN);
            }
        }
        catch (NetworkException e)
        {
            Log.d("setGameState","Network Exception "+e);
            throw e;
        }
        catch (Exception e)
        {
            Log.d("setGameState","other Exception "+e);
            throw new NetworkException(NetworkException.UNKNOWN);
        }
    }


    /**
     * 查询房主.
     * @param roomNumber 房间号码
     * @return 房主昵称
     * @throws NetworkException
     */
    public String getHostName(int roomNumber) throws NetworkException
    {
        Connection connection = getConnection();
        if(connection==null)
        {
            Log.d("getHostName", "TIME OUT");
            throw new NetworkException(NetworkException.TIME_OUT);
        }

        try
        {
            Statement stmt = connection.createStatement();

            String roomQuery = "select * from room where roomid=" + roomNumber + ";";
            ResultSet roomRst = stmt.executeQuery(roomQuery);
            if (roomRst.next())
            {
                String hostName = roomRst.getString("hostname");

                roomRst.close();
                stmt.close();
                connection.close();

                return hostName;
            }
            else
            {
                roomRst.close();
                stmt.close();
                connection.close();
                throw new NetworkException(NetworkException.WRONG_NUM);
            }
        }
        catch (NetworkException e)
        {
            Log.d("getHostName","Network Exception "+e);
            throw e;
        }
        catch (Exception e)
        {
            Log.d("getHostName","other Exception "+e);
            throw new NetworkException(NetworkException.UNKNOWN);
        }
    }


    /**
     * 获得最高分列表。其中对于混战模式，获得前三人的分数和名字（分数降序）,例如：
     * <br>"Name 2","Name 1","Name 0"
     * <br>对于团队模式，获得两个团队总分，固定红前蓝后,例如
     * <br>"5","4"(代表红队5分，蓝队4分)
     * @param roomNumber 房间号
     * @return 排行榜
     * @throws NetworkException
     */
    public ArrayList<String> getHighScores(int roomNumber) throws NetworkException
    {
        Connection connection = getConnection();
        if(connection==null)
        {
            Log.d("getHighScores", "TIME OUT");
            throw new NetworkException(NetworkException.TIME_OUT);
        }

        try
        {
            int roomMode = getRoomRule(roomNumber).mode;
            ArrayList<String> score = new ArrayList<>();

            Statement stmt = connection.createStatement();

            String userQuery = "select * from user where roomid=" + roomNumber + ";";
            ResultSet userRst = stmt.executeQuery(userQuery);

            if(roomMode == MODE_TEAM)   //团队模式 得到红蓝两队分数
            {
                int redScore = 0;
                int blueScore = 0;
                while (userRst.next())
                {
                    if(userRst.getBoolean("isblue"))
                        blueScore+=userRst.getInt("score");
                    else
                        redScore+=userRst.getInt("score");
                }

                score.add(""+redScore);
                score.add(""+blueScore);
            }
            else                        //混战模式 得到前三人的分数和名字
            {
                class User implements Comparable
                {
                    int score;
                    String userName;

                    User(int score, String userName)
                    {
                        this.score=score;
                        this.userName=userName;
                    }

                    public int compareTo(Object obj)
                    {
                        User B = (User) obj;
                        return B.score-this.score;
                    }
                }

                List<User> list = new ArrayList<User>();
                while(userRst.next())
                {
                    list.add(new User(userRst.getInt("score"),userRst.getString("username")+" "+userRst.getString("score")));
                }
                Collections.sort(list);

                int rank = 0;
                for (User i : list)
                {
                    //System.out.println("score="+i.score+" name="+i.userName);
                    score.add(i.userName);
                    rank++;
                    if(rank==3)
                        break;
                }
                for(int i=rank; i<3; i++)
                    score.add("");

            }

            userRst.close();
            stmt.close();
            connection.close();

            return score;
        }
        catch (NetworkException e)
        {
            Log.d("getHighScores","Network Exception "+e);
            throw e;
        }
        catch (Exception e)
        {
            Log.d("getHighScores","other Exception "+e);
            throw new NetworkException(NetworkException.UNKNOWN);
        }
    }


    /**
     * 某个用户使用了道具.对相应角色在服务器上添加相应的消息.
     * @param roomNumber
     * @param playerName
     * @param item 道具编号，详见Item类.
     * @throws NetworkException
     */
    public void useItem(int roomNumber, String playerName, Item item) throws NetworkException
    {
        Log.d("useItem","begin");
        Connection connection = getConnection();
        if(connection==null)
        {
            Log.d("useItem", "TIME OUT");
            throw new NetworkException(NetworkException.TIME_OUT);
        }

        try
        {
            int roomMode = getRoomRule(roomNumber).mode;
            HashMap<String, Boolean> team = new HashMap<String, Boolean>();
            boolean useItemUserTeam = false;
            ArrayList<String> effectedUser = new ArrayList<String>();
            Statement stmt = connection.createStatement();

            String userQuery = "select * from user where roomid=" + roomNumber + ";";
            ResultSet userRst = stmt.executeQuery(userQuery);

            while(userRst.next())
            {
                team.put(userRst.getString("username"), userRst.getBoolean("isblue"));
            }

            useItemUserTeam = team.get(playerName);

            switch (item.getItemType())
            {
                case ITEM_SHORTEN_COLD :
                    if(roomMode==MODE_TEAM)
                    {
                        for(String i:team.keySet())
                            if(team.get(i)==useItemUserTeam)
                                effectedUser.add(i);
                    }
                    else
                    {
                        effectedUser.add(playerName);
                    }
                    break;

                case ITEM_COMPASS_LOSS:
                    if(roomMode==MODE_TEAM)
                    {
                        for(String i:team.keySet())
                            if(team.get(i)!=useItemUserTeam)
                                effectedUser.add(i);
                    }
                    else
                    {
                        for(String i:team.keySet())
                            if(!i.equals(playerName))
                                effectedUser.add(i);
                    }
                    break;

                case ITEM_FREQ_LOSS:
                    if(roomMode==MODE_TEAM)
                    {
                        for(String i:team.keySet())
                            if(team.get(i)!=useItemUserTeam)
                                effectedUser.add(i);
                    }
                    else
                    {
                        for(String i:team.keySet())
                            if(!i.equals(playerName))
                                effectedUser.add(i);
                    }
                    break;

                case ITEM_ENLARGE_FREQ:
                    if(roomMode==MODE_TEAM)
                    {
                        for(String i:team.keySet())
                            if(team.get(i)==useItemUserTeam)
                                effectedUser.add(i);
                    }
                    else
                    {
                        effectedUser.add(playerName);
                    }
                    break;

                case ITEM_DIRECT_REVERT:

                    ArrayList<String> userList = new ArrayList<>();

                    if(roomMode==MODE_TEAM)
                    {
                        for(String i:team.keySet())
                            if(team.get(i)!=useItemUserTeam)
                                userList.add(i);
                    }
                    else
                    {
                        for(String i:team.keySet())
                            if(!i.equals(playerName))
                                userList.add(i);
                    }
                    effectedUser.add(userList.get((int)(Math.random()*userList.size())));

                    break;

                default:
                    throw new NetworkException(NetworkException.UNKNOWN);
            }

            for(String i:effectedUser)
            {
                String itemUpdate = "insert into item values("+roomNumber+",'"+ i +"',"+item.getItemType()+","+item.getRemainTime()+");";
                Log.d("useItem", "useItem: "+itemUpdate);
                if(stmt.executeUpdate(itemUpdate)!=1)
                {
                    stmt.close();
                    connection.close();
                    throw new NetworkException(NetworkException.UNKNOWN);
                }
            }
            stmt.close();
            connection.close();
        }
        catch (NetworkException e)
        {
            Log.d("useItem","Network Exception "+e);
            throw e;
        }
        catch (Exception e)
        {
            Log.d("useItem","other Exception "+e);
            throw new NetworkException(NetworkException.UNKNOWN);
        }
    }


    /**
     * 获得服务器上用户当前收到的自身/对手的状态影响.一次状态改变只收一次消息.
     * @param roomNumber
     * @param playerName
     * @return
     * @throws NetworkException
     */
    public ArrayList<Item> getItemsEffect(int roomNumber, String playerName) throws NetworkException {
        Connection connection = getConnection();
        if(connection==null)
        {
            Log.d("getItemsEffect", "TIME OUT");
            throw new NetworkException(NetworkException.TIME_OUT);
        }

        try
        {
            ArrayList<Item> item = new ArrayList<>();
            Statement stmt = connection.createStatement();

            String itemQuery = "select * from item where roomid=" + roomNumber + " and username=" + playerName +";";
            ResultSet userRst = stmt.executeQuery(itemQuery);

            while(userRst.next())
            {
                Item oneItem = new Item(userRst.getInt("itemtype"));
                oneItem.setRemainTime(userRst.getFloat("remaintime"));
                item.add(oneItem);
            }

            String itemUpdate = "delete from item where roomid="+roomNumber+" and username='"+playerName+"';";
            stmt.executeUpdate(itemUpdate);

            stmt.close();
            connection.close();

            return item;
        }
        catch (Exception e)
        {
            Log.d("getItemsEffect","other Exception "+e);
            throw new NetworkException(NetworkException.UNKNOWN);
        }
    }

    /**
     * 用户找到的信号源，服务器返回一个道具(50%几率).
     * 如果是组队模式，检查该信号源是否未被对方发现
     * @param roomNumber
     * @param playerName
     * @param signal 信号源的编号     *从0开始编号
     * @return
     * @throws NetworkException
     */
    public Item findSignal(int roomNumber, String playerName, int signal) throws NetworkException
    {
        Connection connection = getConnection();
        if(connection==null)
        {
            Log.d("findSignal", "TIME OUT");
            throw new NetworkException(NetworkException.TIME_OUT);
        }

        try
        {
            int roomMode = getRoomRule(roomNumber).mode;
            int team = 0;

            Statement stmt = connection.createStatement();
            String userQuery = "select * from user where roomid=" + roomNumber + " and username='" + playerName + "';";
            //Log.d("findSignal", userQuery);
            ResultSet userRst = stmt.executeQuery(userQuery);
            int score = 0;
            if(userRst.next())
            {
                score = userRst.getInt("score");
                team = userRst.getInt("isblue") + 1;
            }
            else
                throw new NetworkException(NetworkException.UNKNOWN);

            userRst.close();
            score++;
            String userUpdate = "update user set score="+score+" where roomid="+roomNumber+" and username='"+playerName+"';";
            if(stmt.executeUpdate(userUpdate) != 1)
                throw new NetworkException(NetworkException.UNKNOWN);

            if(roomMode==MODE_TEAM)
            {
                String signalUpdate = "update signal set belong="+team+" where roomid="+roomNumber+" and signalid='"+signal+"';";
                if(stmt.executeUpdate(signalUpdate)!=1)
                    throw new NetworkException(NetworkException.UNKNOWN);
            }
            stmt.close();
            connection.close();

            if(Math.random()<0.5)
            {
                Item item = new Item((int)(Math.random()*5));
                //Log.d("findSignal",""+item.getItemType());
                if(item.getItemType()==0)
                    item.setRemainTime(180);                // * 用秒做时间单位
                else
                    item.setRemainTime(60);

                return item;
            }
            else
                return null;
        }
        catch (NetworkException e)
        {
            Log.d("findSignal","Network Exception "+e);
            throw e;
        }
        catch (Exception e)
        {
            Log.d("findSignal","other Exception "+e);
            throw new NetworkException(NetworkException.UNKNOWN);
        }
    }


    /**
     * 用户返回服务器上的最新游戏信息，即各个信号源的归属问题.按顺序，0未发现，1红队，2蓝队.
     * 如返回0,1,2,0,1,2 代表第0、3号信号源未被两队发现，1、4号信号源被红队发现，2、5号信号源被蓝队发现.
     * @param roomNumber
     * @return
     * @throws NetworkException
     */
    public ArrayList<Integer> getSignalBelong(int roomNumber) throws NetworkException {
        Connection connection = getConnection();
        if(connection==null)
        {
            Log.d("getSignalBelong", "TIME OUT");
            throw new NetworkException(NetworkException.TIME_OUT);
        }

        try
        {

            ArrayList<Integer> belong = new ArrayList<>();

            Statement stmt = connection.createStatement();

            String userQuery = "select * from signal where roomid=" + roomNumber + ";";
            ResultSet signalRst = stmt.executeQuery(userQuery);


            while (signalRst.next())
            {
                belong.add(new Integer(signalRst.getInt("belong")));
            }

            return belong;
        }
        catch (Exception e)
        {
            Log.d("getSignalBelong","other Exception "+e);
            throw new NetworkException(NetworkException.UNKNOWN);
        }
    }

}
