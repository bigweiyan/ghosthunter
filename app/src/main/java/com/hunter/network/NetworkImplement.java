package com.hunter.network;

import android.os.Message;
import android.util.Log;

import com.hunter.game.models.Item;
import com.hunter.game.models.RoomRule;
import com.hunter.game.models.Signal;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.logging.Handler;


/**
 * 网络类的实现
 * Created by abgnwl on 2016-11-13.
 */

public class NetworkImplement implements NetworkSupport
{

    private String result;

    private class myThread extends Thread
    {
        private String string;

        myThread(String string)
        {
            this.string=string;
        }

        public void run()
        {
            HttpURLConnection connection = null;
            try {
                URL url = new URL("http://115.236.59.67:8080/CloudERP/"+string);
                Log.d("th","http://115.236.59.67:8080/CloudERP/"+string);

                connection = (HttpURLConnection) url.openConnection();
                // 设置请求方法，默认是GET
                connection.setRequestMethod("GET");
                // 设置字符集
                connection.setRequestProperty("Charset", "UTF-8");
                // 设置文件类型
                connection.setRequestProperty("Content-Type", "text/xml; charset=UTF-8");
                // 设置请求参数，可通过Servlet的getHeader()获取
                connection.setRequestProperty("Cookie", "AppName=" + URLEncoder.encode("你好", "UTF-8"));
                // 设置自定义参数
                connection.setRequestProperty("MyProperty", "this is me!");

                if(connection.getResponseCode() == 200){
                    InputStream is = connection.getInputStream();
                    ByteArrayOutputStream baos   =   new   ByteArrayOutputStream();
                    int i=-1;
                    while((i=is.read())!=-1){
                        baos.write(i);
                    }
                    result = baos.toString();
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } finally {
                if(connection != null){
                    connection.disconnect();
                }
            }
        }

    }

    private String oneSignalToString(Signal signal)
    {
        if(signal==null)
            return null;
        String ret = "";
        ret=ret+signal.latitude+","+signal.longitude+","+signal.frequency+";";
        return ret;
    }

    private String signalToString(ArrayList<Signal> signal)
    {
        if(signal==null)
            return null;

        String ret = "";

        for(Signal i:signal)
        {
            ret=ret+oneSignalToString(i);
        }
        return ret;
    }

    private ArrayList<Signal> stringToSignalList(String string)
    {

        try
        {
            ArrayList<Signal> signal = new ArrayList<>();
            String[] list = string.split(";");
            for (String i : list)
            {
                String[] temp = i.split(",");
                Signal onesignal = new Signal(Double.parseDouble(temp[0]), Double.parseDouble(temp[1]), Integer.parseInt(temp[2]));
                signal.add(onesignal);
            }
            return signal;
        }
        catch (Exception e)
        {
            return null;
        }

    }


    private String doubleToString(double f)
    {
        return ""+(int)(f*1000000);
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
        try
        {
            String string = "?method=2";
            string += "&mode=" + mode;
            string += "&hostName=" + hostName;
            string += "&useItem=" + useItem;
            string += "&autoReady=" + autoReady;
            for (int i = 0; i < signals.size(); i++)
            {
                string += "&lat" + i + "=" + doubleToString(signals.get(i).latitude);
                string += "&lon" + i + "=" + doubleToString(signals.get(i).longitude);
                string += "&freq" + i + "=" + signals.get(i).frequency;
            }

            result = null;

            Thread thread = new myThread(string);

            thread.start();
            while (true)
                if (result != null)
                    break;

            if (result.startsWith("error:"))
                throw new NetworkException(result.substring(6));
            else
                return Integer.parseInt(result);
        }
        catch (NetworkException e)
        {
            throw e;
        }
        catch (Exception e)
        {
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
        String string = "?method=1";
        string+="&roomNumber="+roomNumber;
        string+="&playerName="+playerName;
        string+="&isBlue="+isBlue;


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
        String string = "?method=2";
        string+="&roomNumber="+roomNumber;
        string+="&playerName="+playerName;
    }


    /**
     * 得到蓝方队员的列表（团队模式），即列表1.
     * @param roomNumber 房间号码
     * @return 蓝方队员列表
     * @throws NetworkException
     */
    public ArrayList<String> getMembersBlue(int roomNumber) throws  NetworkException
    {
        String string = "?method=3";
        string+="&roomNumber="+roomNumber;
    }


    /**
     * 得到红方队员的列表（团队模式）/所有玩家的列表（混战模式），即列表2.
     * @param roomNumber 房间号码
     * @return 红方队员列表
     * @throws NetworkException
     */
    public ArrayList<String> getMembersRed(int roomNumber) throws  NetworkException
    {
        String string = "?method=4";
        string+="&roomNumber="+roomNumber;
    }


    /**
     * 得到游戏的规则
     * @param roomNumber 房间号码
     * @return 游戏规则
     * @throws NetworkException
     */
    public RoomRule getRoomRule(int roomNumber) throws NetworkException
    {
        String string = "?method=5";
        string+="&roomNumber="+roomNumber;
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
        String string = "?method=6";
        string+="&roomNumber="+roomNumber;
        string+="&playerName="+playerName;
    }


    /**
     * 房主发出开始游戏信号
     * @param roomNumber 待查房间号
     * @return 游戏是否开始，开始则返回true，如果未全准备好，返回false，有异常抛出
     * @throws NetworkException
     */
    public boolean gameStart(int roomNumber) throws NetworkException
    {
        String string = "?method=7";
        string+="&roomNumber="+roomNumber;
    }


    /**
     * 当前游戏的状态.
     * @param roomNumber 待查房间号
     * @return 游戏状态，详见接口变量
     * @throws NetworkException
     */
    public int getGameState(int roomNumber) throws NetworkException
    {
        String string = "?method=8";
        string+="&roomNumber="+roomNumber;
    }


    /**
     * 设置当前游戏的状态.
     * @param roomNumber 房间号，改变之后的状态
     * @return 是否设置成功
     * @throws NetworkException
     */
    public boolean setGameState(int roomNumber, int gameState) throws NetworkException
    {
        String string = "?method=9";
        string+="&roomNumber="+roomNumber;
        string+="&gameState="+gameState;
    }


    /**
     * 查询房主.
     * @param roomNumber 房间号码
     * @return 房主昵称
     * @throws NetworkException
     */
    public String getHostName(int roomNumber) throws NetworkException
    {
        String string = "?method=10";
        string+="&roomNumber="+roomNumber;
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
        String string = "?method=11";
        string+="&roomNumber="+roomNumber;
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
        String string = "?method=12";
        string+="&roomNumber="+roomNumber;
        string+="&playerName="+playerName;
        string+="&item="+item;
    }


    /**
     * 获得服务器上用户当前收到的自身/对手的状态影响.一次状态改变只收一次消息.
     * @param roomNumber
     * @param playerName
     * @return
     * @throws NetworkException
     */
    public ArrayList<Item> getItemsEffect(int roomNumber, String playerName) throws NetworkException {
        String string = "?method=13";
        string+="&roomNumber="+roomNumber;
        string+="&playerName="+playerName;
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
        String string = "?method=14";
        string+="&roomNumber="+roomNumber;
        string+="&playerName="+playerName;
        string+="&signal="+signal;
    }


    /**
     * 用户返回服务器上的最新游戏信息，即各个信号源的归属问题.按顺序，0未发现，1红队，2蓝队.
     * 如返回0,1,2,0,1,2 代表第0、3号信号源未被两队发现，1、4号信号源被红队发现，2、5号信号源被蓝队发现.
     * @param roomNumber
     * @return
     * @throws NetworkException
     */
    public ArrayList<Integer> getSignalBelong(int roomNumber) throws NetworkException {
        String string = "?method=15";
        string+="&roomNumber="+roomNumber;

}
