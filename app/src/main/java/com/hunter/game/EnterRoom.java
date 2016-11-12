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

import com.hunter.master.foxhunter.R;

/**
 * 加入房间界面.
 */
public class EnterRoom extends AppCompatActivity {
    public static final int MODE_TEAM = 0;
    public static final int MODE_BATTLE = 1;
    private int mode;
    private TextView mContentView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        mode = intent.getIntExtra("mode",MODE_BATTLE);

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
                EditText playerET = (EditText)findViewById(R.id.playerNameInput);
                String playerName = playerET.getText().toString();
                EditText roomNumberET = (EditText)findViewById(R.id.roomNumberInput);
                String roomNumber = roomNumberET.getText().toString();
                boolean isBlue = ((RadioButton)findViewById(R.id.setBlueTeam)).isChecked();
                joinGame(playerName,roomNumber,isBlue);
            }
        });
        RadioGroup teamChoice = (RadioGroup)findViewById(R.id.enterRoomRadioGroup);
        if (mode == MODE_BATTLE) {
            teamChoice.setVisibility(View.INVISIBLE);
        }
    }

    private void joinGame(String playerName, String roomNumber, boolean isBlue) {
        if ("".equals(playerName) || "".equals(roomNumber)){
            mContentView.setText("Error");
        }else {
            Intent intent = new Intent();
            intent.setClass(this, WaitRoom.class);
            intent.putExtra("roomNumber",Integer.parseInt(roomNumber));
            intent.putExtra("playerName",playerName);
            intent.putExtra("isBlue",isBlue);
            this.startActivity(intent);
        }
    }
}
