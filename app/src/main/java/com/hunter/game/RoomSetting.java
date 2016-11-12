package com.hunter.game;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.hunter.master.foxhunter.R;

import java.util.ArrayList;

/**
 * 房间设置界面.
 */
public class RoomSetting extends AppCompatActivity {

    /**
     * 团队模式.
     */
    public static final int MODE_TEAM = 0;
    /**
     * 混战模式.
     */
    public static final int MODE_BATTLE = 1;
    /**
     * 游戏当前运行的模式.
     */
    private int mode;

    private View mContentView;

    private TextView signalListText;
    private ArrayList<String> signals;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        mode = intent.getIntExtra("mode",MODE_BATTLE);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_room_setting);
        mContentView = findViewById(R.id.createRoomLabel);
        signalListText = (TextView)findViewById(R.id.signalListText);
        signalListText.setMovementMethod(ScrollingMovementMethod.getInstance());
        signals = new ArrayList<>();

        switch (mode) {
            case MODE_BATTLE:
                ((TextView)mContentView).setText("混战模式 创建房间>房间设置");
                break;
            case MODE_TEAM:
                ((TextView)mContentView).setText("团队模式 创建房间>房间设置");
                break;
            default:
                ((TextView)mContentView).setText("创建房间>房间设置");
        }

        Button addSignal = (Button)(findViewById(R.id.addSignal));
        Button clear = (Button)(findViewById(R.id.clearSignal));
        Button removeLast = (Button)(findViewById(R.id.popSignal));
        Button back = (Button)(findViewById(R.id.settingBack));
        Button moveOn = (Button)(findViewById(R.id.settingForward));
        addSignal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: 2016/11/12 连接GPS协议 
                signals.add("line");
                showSignals();
            }
        });
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signals.clear();
                showSignals();
            }
        });
        removeLast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (signals.size() == 0) return;
                signals.remove(signals.size()-1);
                showSignals();
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        moveOn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createGame();
            }
        });

    }

    private void showSignals() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < signals.size(); i++){
            sb.append(signals.get(i)+"\n");
        }
        signalListText.setText(sb.toString());
    }

    private void createGame() {
        EditText hostnameET = (EditText)findViewById(R.id.hostNameInput);
        String hostname = hostnameET.getText().toString();
        signals.add(hostname);

        CheckBox useItemCB = (CheckBox)findViewById(R.id.useItemCheck);
        boolean useItem = useItemCB.isChecked();
        signals.add(useItem?"使用道具":"禁用道具");

        CheckBox autoReadyCB = (CheckBox)findViewById(R.id.autoReadyCheck);
        boolean autoReady = autoReadyCB.isChecked();
        signals.add(autoReady?"自动准备":"手动准备");

        switch (mode) {
            case MODE_BATTLE:
                signals.add("混战模式");
                break;
            case MODE_TEAM:
                signals.add("团队模式");
                break;
        }
        
        showSignals();
        // TODO: 2016/11/12 连接通讯协议，转入WaitRoom 
    }
}
