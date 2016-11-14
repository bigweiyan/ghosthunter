package com.hunter.master.foxhunter;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.gps.Sensor_If;

public class GameLogic extends AppCompatActivity {
    Sensor_If loc;
    TextView loctext,dirtext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_logic);
        loc=new Sensor_If(getApplicationContext());
        loctext= (TextView) findViewById(R.id.loctext);
        dirtext=(TextView)findViewById(R.id.dirText);
        loc.setShowlocID(loctext);
        loc.setShowDirectionID(dirtext);
        loc.startLocation();
    }
}
