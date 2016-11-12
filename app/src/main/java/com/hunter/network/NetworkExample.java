package com.hunter.network;

import com.hunter.game.models.RoomRule;
import com.hunter.game.models.Signal;

import java.util.ArrayList;

/**
 * 测试用例：模拟服务器
 * Created by weiyan on 2016/11/12.
 */

public class NetworkExample implements NetworkSupport{

    private boolean ready = false;

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
    public ArrayList<String> getMembersBlue(int roomNumber) throws NetworkException {
        ArrayList<String> list = new ArrayList<>();
        if (roomNumber == 1234) {
            list.add("Mike");
            list.add("Jack");
            list.add("John");
        }
        return list;
    }

    @Override
    public ArrayList<String> getMembersRed(int roomNumber) throws NetworkException {
        ArrayList<String> list = new ArrayList<>();
        list.add("Sam");
        list.add("kevin");
        list.add("Ted");
        return list;
    }

    @Override
    public RoomRule getRoomRule(int roomNumber) throws NetworkException {
        RoomRule rule = new RoomRule();
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
        } else if (roomNumber == 5678) {
            rule.useItem = false;
            rule.mode = RoomRule.MODE_BATTLE;
            rule.autoReady = false;
            ArrayList<Signal> signal = new ArrayList<>();
            signal.add(new Signal(0, 0, 1));
            signal.add(new Signal(1, 1, 2));
            signal.add(new Signal(2, 2, 3));
            signal.add(new Signal(3, 3, 4));
            rule.signals = signal;
        }else {
            throw new NetworkException(NetworkException.WRONG_NUM);
        }
        return rule;
    }

    @Override
    public boolean gameReady(int roomNumber, String playerName) throws NetworkException {
        if (ready) return false;
        else return true;
    }
}
