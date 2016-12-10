package com.hunter.game;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.hunter.game.models.RoomRule;
import com.hunter.game.models.Tools;
import com.hunter.master.foxhunter.R;
import com.hunter.network.NetworkExample;
import com.hunter.network.NetworkException;
import com.hunter.network.NetworkSupport;

import java.util.ArrayList;

/**
 * 显示最高分页面
 * Created by weiyan on 2016/12/10.
 */

public class HighScoreActivity extends Activity {
    private NetworkSupport ns;
    private Button button;
    private int roomNumber;
    private TextView result;
    private TextView resultList;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        roomNumber = getIntent().getIntExtra("roomNumber",0);
        ns = new NetworkExample();

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.high_score_activity);

        button = (Button) findViewById(R.id.gameOver);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(HighScoreActivity.this, FoxHunter.class);
                startActivity(intent);
                finish();
            }
        });

        int mode = 0;
        resultList = (TextView)findViewById(R.id.resultList1);
        result = (TextView)findViewById(R.id.result);
        StringBuilder sb = new StringBuilder();
        ArrayList<String> highscores = new ArrayList<>();
        try {
            highscores = ns.getHighScores(roomNumber);
            mode = ns.getRoomRule(roomNumber).mode;
        }catch (NetworkException e){
            Tools.showDialog(this,"连接失败","请检查网络连接");
        }
        for (int i = 0; i < highscores.size(); i++) {
            sb.append(highscores.get(i)).append('\n');
        }
        resultList.setText(sb.toString());

        sb = new StringBuilder();
        sb.append("获胜者：");
        if(mode == RoomRule.MODE_BATTLE) {
            if (highscores.size() > 0) {
                sb.append(highscores.get(0).substring(0, highscores.get(0).length() - 2));
            }

        }else {
            if (highscores.size() > 1) {
                int red = Integer.parseInt(highscores.get(0));
                int blue = Integer.parseInt(highscores.get(1));
                if (red > blue) {
                    sb.append("红队");
                }else {
                    sb.append("蓝队");
                }
            }
        }
        result.setText(sb.toString());
    }
}
