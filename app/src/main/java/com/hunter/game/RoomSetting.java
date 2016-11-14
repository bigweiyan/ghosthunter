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

import com.gps.Sensor_If;
import com.hunter.game.models.Signal;
import com.hunter.game.models.Tools;
import com.hunter.master.foxhunter.R;
import com.hunter.network.NetworkExample;
import com.hunter.network.NetworkException;
import com.hunter.network.NetworkSupport;
import com.hunter.sensor.SensorExample;
import com.hunter.sensor.SensorException;
import com.hunter.sensor.SensorSupport;

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
    private ArrayList<String> signalText;
    private ArrayList<Signal> signals;
    private SensorSupport ss;
    private NetworkSupport ns;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        mode = intent.getIntExtra("mode",MODE_BATTLE);

        ss = new Sensor_If(getApplicationContext());
        ns = new NetworkExample();
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_room_setting);
        mContentView = findViewById(R.id.createRoomLabel);
        signalListText = (TextView)findViewById(R.id.signalListText);
        signalListText.setMovementMethod(ScrollingMovementMethod.getInstance());
        signals = new ArrayList<>();
        signalText = new ArrayList<>();

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
                try {
                    double lat = ss.getLatitude();
                    double lon = ss.getLongitude();
                    int dir = ss.getDirection();
                    signals.add(new Signal(lat,lon,dir));
                    signalText.add("信号源：纬度"+lat+" 经度:"+lon);
                }catch (SensorException e) {
                    Tools.showDialog(RoomSetting.this,"传感器异常",e.getMessage());
                }

                showSignals();
            }
        });
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signalText.clear();
                showSignals();
            }
        });
        removeLast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (signalText.size() == 0) return;
                signalText.remove(signalText.size()-1);
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
        for (int i = 0; i < signalText.size(); i++){
            sb.append(signalText.get(i)).append('\n');
        }
        signalListText.setText(sb.toString());
    }

    private void createGame() {
        EditText hostnameET = (EditText)findViewById(R.id.hostNameInput);
        String hostname = hostnameET.getText().toString();
        if(!Tools.checkAlpha(hostname)) {
            Tools.showDialog(this,"输入错误","昵称请输入英文或数字");
            return;
        }
        signalText.add(hostname);

        CheckBox useItemCB = (CheckBox)findViewById(R.id.useItemCheck);
        boolean useItem = useItemCB.isChecked();
        signalText.add(useItem?"使用道具":"禁用道具");

        CheckBox autoReadyCB = (CheckBox)findViewById(R.id.autoReadyCheck);
        boolean autoReady = autoReadyCB.isChecked();
        signalText.add(autoReady?"自动准备":"手动准备");

        switch (mode) {
            case MODE_BATTLE:
                signalText.add("混战模式");
                break;
            case MODE_TEAM:
                signalText.add("团队模式");
                break;
        }
        
        showSignals();
        int roomNumber = 0;
        try {
            roomNumber = ns.createRoom(mode,hostname,useItem,autoReady,signals);
        } catch (NetworkException e) {
            Tools.showDialog(this,"网络异常",e.getMessage());
            return;
        }
        Intent intent = new Intent();
        intent.setClass(this, WaitRoom.class);
        intent.putExtra("roomNumber",roomNumber);
        intent.putExtra("playerName",hostname);
        intent.putExtra("isBlue",false);
        intent.putExtra("isHost",true);
        this.startActivity(intent);
        finish();
        // TODO: 2016/11/12 连接通讯协议，转入WaitRoom
    }
}
