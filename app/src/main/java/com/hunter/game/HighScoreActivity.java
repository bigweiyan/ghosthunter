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
import com.hunter.network.NetworkException;
import com.hunter.network.NetworkImplement;
import com.hunter.network.NetworkSupport;

import java.util.ArrayList;

/**
 * 显示最高分页面
 * Created by weiyan on 2016/12/10.
 */

public class HighScoreActivity extends Activity {


    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        int roomNumber = getIntent().getIntExtra("roomNumber",0);
        NetworkSupport ns = new NetworkImplement();

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.high_score_activity);

        Button button = (Button) findViewById(R.id.gameOver);
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
        TextView resultList = (TextView)findViewById(R.id.resultList1);
        TextView result = (TextView)findViewById(R.id.result);
        StringBuilder sb = new StringBuilder();
        ArrayList<String> highscores = new ArrayList<>();

        try {
            highscores = ns.getHighScores(roomNumber);
            mode = ns.getRoomRule(roomNumber).mode;

        }catch (NetworkException e){
            Tools.showDialog(this,"连接失败","请检查网络连接");
        }
        if(mode == RoomRule.MODE_BATTLE) {
            for (int i = 0; i < highscores.size(); i++) {
                sb.append(highscores.get(i)).append('\n');
            }
        }else{
            sb.append("红队得分 ").append(highscores.get(0)).append("\n蓝队得分 ").append(highscores.get(1));
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
