package com.hunter.game;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.hunter.master.foxhunter.R;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
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
        switch (mode) {
            case MODE_BATTLE:
                ((TextView)mContentView).setText("混战模式 创建房间>房间设置");
                break;
            case MODE_TEAM:
                ((TextView)mContentView).setText("团队模式 创建房间>房间设置");
                break;
            default:
        }

    }

}
