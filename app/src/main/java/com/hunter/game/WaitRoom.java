package com.hunter.game;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.hunter.game.models.RoomRule;
import com.hunter.master.foxhunter.R;
import com.hunter.network.NetworkExample;
import com.hunter.network.NetworkException;

import java.util.ArrayList;

/**
 * 等待开始界面.
 */
public class WaitRoom extends AppCompatActivity {

    private RoomRule rule;
    private int roomNumber;
    private String playerName;
    private ArrayList<String> playerNameRed;
    private ArrayList<String> playerNameBlue;
    private TextView playerListRed;
    private TextView playerListBlue;
    private TextView mode;
    private TextView signalNumber;
    private TextView useItem;
    private TextView roomNumberTV;
    private TextView endCondition;
    private Button readyButton;
    private NetworkExample ne;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        roomNumber = intent.getIntExtra("roomNumber",0);
        playerName = intent.getStringExtra("playerName");

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_wait_room);

        ne = new NetworkExample(); //临时

        playerListRed = (TextView)findViewById(R.id.playerList1);
        playerListBlue = (TextView)findViewById(R.id.playerList2);
        mode = (TextView)findViewById(R.id.waitMode);
        signalNumber = (TextView)findViewById(R.id.waitSignals);
        useItem = (TextView)findViewById(R.id.waitItem);
        roomNumberTV = (TextView)findViewById(R.id.waitRoomNumber);
        endCondition = (TextView)findViewById(R.id.waitGameEnd);
        readyButton = (Button)findViewById(R.id.waitGameReady);
        Button back = (Button)findViewById(R.id.waitQuitRoom);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        playerNameBlue = new ArrayList<>();
        playerNameRed = new ArrayList<>();

        // TODO: 2016/11/12 添加rule的初始化和playNameRed、Blue的初始化
        try {
            rule = ne.getRoomRule(roomNumber);
            playerNameRed = ne.getMembersRed(roomNumber);
            playerNameBlue = ne.getMembersBlue(roomNumber);
        }catch (NetworkException e){
            e.printStackTrace();
            finish();
        }
        setPlayerList();
        setRulesTV();
        // TODO: 2016/11/12 添加准备动作
        readyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean ready = false;
                try {
                    ready = ne.gameReady(roomNumber, playerName);
                }catch (NetworkException e){
                    e.printStackTrace();
                }
                if (ready) {
                    readyButton.setText("取消准备");
                }else {
                    readyButton.setText("准备");
                }
            }
        });

    }

    private void setPlayerList() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < playerNameRed.size(); i++) {
            sb.append(playerNameRed.get(i)).append('\n');
        }
        playerListRed.setText(sb.toString());
        playerListRed.setTextColor(getResources().getColor(R.color.color_red));

        sb = new StringBuilder();
        for (int i = 0; i < playerNameBlue.size(); i++) {
            sb.append(playerNameBlue.get(i)).append('\n');
        }
        playerListBlue.setText(sb.toString());
        playerListBlue.setTextColor(getResources().getColor(R.color.color_blue));
    }

    private void setRulesTV() {
        switch (rule.mode) {
            case RoomRule.MODE_BATTLE:
                mode.setText(R.string.battleMode);
                endCondition.setText(R.string.gameOver1);
                break;
            case RoomRule.MODE_TEAM:
                mode.setText(R.string.teamMode);
                endCondition.setText(R.string.gameOver2);
                break;
        }
        int signalNum = rule.signals.size();
        String text = "信号源个数："+signalNum;
        signalNumber.setText(text);
        if (rule.useItem) {
            useItem.setText("道具：开启");
        }else{
            useItem.setText("道具：关闭");
        }
        text = "房间编号："+roomNumber;
        roomNumberTV.setText(text);

    }
}
