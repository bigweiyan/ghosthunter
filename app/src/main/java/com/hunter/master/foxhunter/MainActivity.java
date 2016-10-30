package com.hunter.master.foxhunter;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import java.lang.String;

public class MainActivity extends ListActivity {
    String tests[] = {"Communication","GameLogic","UserInterface"};
    public void onCreate(Bundle savedInstanceState){
        Log.d("lzj","mainstart");
        super.onCreate(savedInstanceState);
        setListAdapter(new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1,tests));
    }

    @Override
    protected void onListItemClick(ListView list, View view, int position, long id){
        super.onListItemClick(list, view, position, id);
        String testName = tests[position];
        try{
            Log.d("lzj",testName);
            @SuppressWarnings("rawtypes")
            Class clazz = Class.forName("com.hunter.master.foxhunter."+testName);
            Intent intent = new Intent(this,clazz);
            startActivity(intent);
        }catch(ClassNotFoundException e){
            Log.d("lzj","ERROR");
            e.printStackTrace();
        }
    }
}