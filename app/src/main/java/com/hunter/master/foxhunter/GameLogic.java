package com.hunter.master.foxhunter;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.gps.Sensor_If;

public class GameLogic extends AppCompatActivity {
    Sensor_If loc;
    TextView text;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_logic);
        text= (TextView) findViewById(R.id.loctext);
        loc=new Sensor_If(getApplicationContext());
        loc.setShowID(text);
        loc.startLocation();
    }
}
