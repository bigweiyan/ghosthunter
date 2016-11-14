package com.hunter.game;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.hunter.game.models.Tools;
import com.hunter.master.foxhunter.R;
import com.hunter.network.NetworkExample;
import com.hunter.network.NetworkException;
import com.hunter.network.NetworkSupport;
import com.hunter.sensor.SensorExample;
import com.hunter.sensor.SensorSupport;

/**
 * 加入房间界面.
 */
public class EnterRoom extends AppCompatActivity {
    public static final int MODE_TEAM = 0;
    public static final int MODE_BATTLE = 1;
    private int mode;
    private TextView mContentView;
    NetworkSupport ns;
    SensorSupport ss;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        mode = intent.getIntExtra("mode",MODE_BATTLE);
        
        ns = new NetworkExample();
        ss = new SensorExample();

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_enter_room);
        mContentView = (TextView) findViewById(R.id.enterRoomLabel);
        switch (mode) {
            case MODE_BATTLE:
                mContentView.setText("加入混战模式>选择房间");
                break;
            case MODE_TEAM:
                mContentView.setText("加入团队模式>选择房间");
                break;
            default:
        }

        Button back = (Button)findViewById(R.id.enterRoomBack);
        Button join = (Button)findViewById(R.id.enterRoomForward);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!ns.checkLink() || ss.checkSensor() == SensorSupport.NO_START) {
                    Tools.showDialog(EnterRoom.this,"连接异常","请检查您的网络连接和位置");
                    return;
                }
                
                EditText playerET = (EditText)findViewById(R.id.playerNameInput);
                String playerName = playerET.getText().toString();
                EditText roomNumberET = (EditText)findViewById(R.id.roomNumberInput);
                String roomNumber = roomNumberET.getText().toString();
                boolean isBlue = ((RadioButton)findViewById(R.id.setBlueTeam)).isChecked();
                try {
                    joinGame(playerName, Integer.parseInt(roomNumber), isBlue);
                }catch (NumberFormatException e){
                    Tools.showDialog(EnterRoom.this, "输入错误",e.getMessage());
                }
            }
        });
        RadioGroup teamChoice = (RadioGroup)findViewById(R.id.enterRoomRadioGroup);
        if (mode == MODE_BATTLE) {
            teamChoice.setVisibility(View.INVISIBLE);
        }
    }

    private void joinGame(String playerName, int roomNumber, boolean isBlue) {
        if(!Tools.checkAlpha(playerName)) {
            Tools.showDialog(this, "输入错误","昵称请填写英文或数字");
            return;
        }

        if ("".equals(playerName) || "".equals(roomNumber)){
            Tools.showDialog(this, "输入错误","请填写您的昵称和要加入的房间号");
            return;
        }else {
            try {
                ns.checkIn(roomNumber,playerName,isBlue);
            }catch (NetworkException e) {
                Tools.showDialog(this, "网络异常",e.getMessage());
                return;
            }
            Intent intent = new Intent();
            intent.setClass(this, WaitRoom.class);
            intent.putExtra("roomNumber",roomNumber);
            intent.putExtra("playerName",playerName);
            intent.putExtra("isBlue",isBlue);
            intent.putExtra("isHost",false);
            this.startActivity(intent);
            finish();
        }
    }


}
