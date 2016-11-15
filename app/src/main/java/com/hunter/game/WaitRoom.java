package com.hunter.game;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.hunter.game.models.GameState;
import com.hunter.game.models.RoomRule;
import com.hunter.game.models.Tools;
import com.hunter.master.foxhunter.R;
import com.hunter.network.NetworkException;
import com.hunter.network.NetworkImplement;
import com.hunter.network.NetworkSupport;

import java.util.ArrayList;

/**
 * 等待开始界面.
 */
public class WaitRoom extends AppCompatActivity {

    private RoomRule rule;
    private int roomNumber;
    private String playerName;
    private String hostName;
    private boolean isHost;
    private int gameState;
    private ArrayList<String> playerNameRed;
    private ArrayList<String> playerNameBlue;

    private NetworkSupport ne;


    private TextView playerListRed;
    private TextView playerListBlue;
    private TextView mode;
    private TextView signalNumber;
    private TextView useItem;
    private TextView roomNumberTV;
    private TextView endCondition;
    private Button readyButton;

    private Handler mHandler = new Handler();
    private Runnable timerTask = new Runnable() {
        @Override
        public void run() {
            try {
                playerNameRed = ne.getMembersRed(roomNumber);
                playerNameBlue = ne.getMembersBlue(roomNumber);
                gameState = ne.getGameState(roomNumber);
                for(int i = 0; i < playerNameRed.size(); i++) {
                    if (playerNameRed.get(i).equals(playerName)) {
                        playerNameRed.set(i,playerName+"(您)");
                    }
                }
                for(int i = 0; i < playerNameBlue.size(); i++) {
                    if (playerNameBlue.get(i).equals(playerName)) {
                        playerNameBlue.set(i,playerName+"(您)");
                    }
                }
                setPlayerList();
            } catch (NetworkException e) {
                Tools.showDialog(WaitRoom.this,"网络异常",e.getMessage());
            }
            if (gameState == GameState.START && !isHost) {
                Intent intent = new Intent();
                intent.setFlags(Intent.FLAG_ACTIVITY_LAUNCHED_FROM_HISTORY);
                intent.putExtra("name",playerName);
                intent.putExtra("mode",rule.mode);
                intent.putExtra("roomNumber",roomNumber);

                intent.setClass(WaitRoom.this,HuntGame.class);
                WaitRoom.this.startActivity(intent);
                WaitRoom.this.finish();
            }
            mHandler.postDelayed(timerTask,1000);
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        StrictMode.ThreadPolicy policy=new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        roomNumber = intent.getIntExtra("roomNumber",0);
        playerName = intent.getStringExtra("playerName");
        isHost = intent.getBooleanExtra("isHost",false);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_wait_room);

        ne = new NetworkImplement(); //临时

        playerListRed = (TextView)findViewById(R.id.playerList1);
        playerListBlue = (TextView)findViewById(R.id.playerList2);
        mode = (TextView)findViewById(R.id.waitMode);
        signalNumber = (TextView)findViewById(R.id.waitSignals);
        useItem = (TextView)findViewById(R.id.waitItem);
        roomNumberTV = (TextView)findViewById(R.id.waitRoomNumber);
        endCondition = (TextView)findViewById(R.id.waitGameEnd);
        readyButton = (Button)findViewById(R.id.waitGameReady);
        Button back = (Button)findViewById(R.id.waitQuitRoom);
        if (isHost) {
            readyButton.setText("开始游戏");
        }
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    ne.checkOut(roomNumber, playerName);
                    finish();
                }catch (NetworkException e) {
                    Tools.showDialog(WaitRoom.this,"网络异常",e.getMessage());
                }
            }
        });
        playerNameBlue = new ArrayList<>();
        playerNameRed = new ArrayList<>();
        try {
            rule = ne.getRoomRule(roomNumber);
            hostName = ne.getHostName(roomNumber);

            if (hostName.equals(playerName)) {
                hostName = hostName + "(您)";
            }
            setRulesTV();
        }catch (NetworkException e){
            Tools.showDialog(this,"网络异常",e.getMessage());
        }


        readyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean ready = false;
                try {
                    if (isHost) {
                        ne.gameStart(roomNumber);
                    }else {
                        ready = ne.gameReady(roomNumber, playerName);
                    }
                }catch (NetworkException e){
                    Tools.showDialog(WaitRoom.this,"网络异常",e.getMessage());
                    return;
                }

                if (isHost) {
                    readyButton.setText("开始游戏");
                }else if (ready) {
                    readyButton.setText(R.string.cancelReady);
                }else {
                    readyButton.setText(R.string.gameReady);
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
        if(rule != null && rule.mode == RoomRule.MODE_TEAM) {
            playerListRed.setTextColor(getResources().getColor(R.color.color_red));
        }

        sb = new StringBuilder();
        for (int i = 0; i < playerNameBlue.size(); i++) {
            sb.append(playerNameBlue.get(i)).append('\n');
        }
        playerListBlue.setText(sb.toString());
        playerListBlue.setTextColor(getResources().getColor(R.color.color_blue));
    }

    private void setRulesTV() {
        if (rule == null) return;

        switch (rule.mode) {
            case RoomRule.MODE_BATTLE:
                mode.setText("混战模式 房主："+hostName);
                endCondition.setText(R.string.gameOver1);
                break;
            case RoomRule.MODE_TEAM:
                mode.setText("团队模式 房主："+hostName);
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

    @Override
    public void onResume() {
        super.onResume();
        mHandler.post(timerTask);
    }

    @Override
    public void onPause() {
        super.onPause();
        mHandler.removeCallbacks(timerTask);
    }
}
