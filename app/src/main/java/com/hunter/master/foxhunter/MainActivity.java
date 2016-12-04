package com.hunter.master.foxhunter;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.hunter.game.models.RoomRule;

import java.lang.String;

public class MainActivity extends ListActivity {
    String tests[] = {"master.foxhunter.Communication","game.FoxHunter","game.HuntGame","master.foxhunter.GameLogic"};
    public void onCreate(Bundle savedInstanceState){
        Log.d("lzj","main start");
        super.onCreate(savedInstanceState);
        setListAdapter(new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1,tests));
    }

    @Override
    protected void onListItemClick(ListView list, View view, int position, long id){
        super.onListItemClick(list, view, position, id);
        String testName = tests[position];
        try{
            @SuppressWarnings("rawtypes")

            Class clazz = Class.forName("com.hunter."+testName);
            Intent intent = new Intent(this,clazz);
            if (testName.equals("game.HuntGame")) {
                intent.putExtra("name","testName");
                intent.putExtra("mode", RoomRule.MODE_BATTLE);
                intent.putExtra("roomNumber",42);
            }
            startActivity(intent);
        }catch(ClassNotFoundException e){
            e.printStackTrace();
        }
    }
}