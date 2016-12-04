package com.hunter.network;

import com.hunter.game.models.Item;
import com.hunter.game.models.RoomRule;
import com.hunter.game.models.Signal;

import java.util.ArrayList;

/**
 * 测试用例：模拟服务器
 * Created by weiyan on 2016/11/12.
 */

public class NetworkExample implements NetworkSupport{

    private boolean ready;
    private int requests;

    public NetworkExample() {
        ready = false;
        requests = 0;
    }

    @Override
    public boolean checkLink() {
        return true;
    }

    @Override
    public int createRoom(int mode, String hostName, boolean useItem, boolean autoReady, ArrayList<Signal> signals) throws NetworkException {
        if (mode == RoomRule.MODE_TEAM) {
            return 1234;
        }else {
            return 5678;
        }
    }

    @Override
    public boolean checkIn(int roomNumber, String playerName, boolean isBlue) throws NetworkException {
        return true;
    }

    @Override
    public boolean checkOut(int roomNumber, String playerName) throws NetworkException {
        return true;
    }

    @Override
    public ArrayList<String> getMembersBlue(int roomNumber) throws NetworkException {
        requests++;
        ArrayList<String> list = new ArrayList<>();
        if (roomNumber == 1234) {
            list.add("Mike");
            if (requests > 2)
                list.add("Jack");
            if (requests > 3)
                list.add("John");
        }
        return list;
    }

    @Override
    public ArrayList<String> getMembersRed(int roomNumber) throws NetworkException {
        ArrayList<String> list = new ArrayList<>();
        if (requests > 1)
            list.add("Sam");
        if (requests > 4)
            list.add("kevin");
        if (requests > 5)
            list.add("Ted");
        return list;
    }

    @Override
    public RoomRule getRoomRule(int roomNumber) throws NetworkException {
        RoomRule rule = new RoomRule(false,false,RoomRule.MODE_TEAM);
        if (roomNumber == 1234) {
            rule.useItem = false;
            rule.mode = RoomRule.MODE_TEAM;
            rule.autoReady = false;
            ArrayList<Signal> signal = new ArrayList<>();
            signal.add(new Signal(0, 0, 1));
            signal.add(new Signal(1, 1, 2));
            signal.add(new Signal(2, 2, 3));
            signal.add(new Signal(3, 3, 4));
            rule.signals = signal;
        } else if (roomNumber == 42) {
            rule.useItem = true;
            rule.mode = RoomRule.MODE_BATTLE;
            rule.autoReady = false;
            ArrayList<Signal> signal = new ArrayList<>();
            signal.add(new Signal(0, 0, 1));
            signal.add(new Signal(1, 1, 2));
            signal.add(new Signal(2, 2, 3));
            signal.add(new Signal(3, 3, 4));
            signal.add(new Signal(4, 4, 5));
            rule.signals = signal;
        }else {
            throw new NetworkException(NetworkException.WRONG_NUM);
        }
        return rule;
    }

    @Override
    public boolean gameReady(int roomNumber, String playerName) throws NetworkException {
        if (ready) {
            ready = !ready;
            return false;
        }else{
            ready = !ready;
            return true;
        }
    }

    @Override
    public int getGameState(int roomNumber) throws NetworkException {
        if (requests < 10){
            return NetworkSupport.NOT_READY_YET;
        } else{
            return NetworkSupport.START;
        }
    }

    @Override
    public boolean gameStart(int roomNumber) throws NetworkException
    {
        return true;
    }

    @Override
    public boolean setGameState(int roomNumber, int gameState) throws  NetworkException{
        return true;
    }
    @Override
    public String getHostName(int roomNumber) throws NetworkException {
        return "Host";
    }

    @Override
    public ArrayList<String> getHighScores(int roomNumber) throws NetworkException {
        ArrayList<String> result = new ArrayList<>();
        result.add("Mike 3");
        result.add("Tom 2");
        result.add("Sam 1");
        return result;
    }

    @Override
    public ArrayList<Item> getItemsEffect(int roomNumber, String playerName) throws NetworkException {
        return null;
    }

    @Override
    public Item findSignal(int roomNumber, String playerName, int signal) throws NetworkException {
        return null;
    }
}