package com.hunter.master.foxhunter;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.menu.ExpandedMenuView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.ArrayList;

import com.hunter.game.models.Signal;
import com.hunter.network.NetworkImplement;

public class Communication extends AppCompatActivity {

    private Button button1,button2;
    private EditText editText1, editText2,editText3;
    private TextView textView;
    String id = "", name = "";

    Handler handler = new Handler()
    {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    //完成主界面更新,拿到数据
                    String data = (String)msg.obj;
                    textView.setText(data);
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("Communication","oncreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_communication);
        button1 = (Button) findViewById(R.id.button);
        button2 = (Button) findViewById(R.id.button2);
        editText1 = (EditText)findViewById(R.id.editText);
        editText2 = (EditText)findViewById(R.id.editText2);
        editText3 = (EditText)findViewById(R.id.editText3);

        textView = (TextView)findViewById(R.id.textView);

        button1.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                final NetworkImplement network = new NetworkImplement();

                new Thread(new Runnable() {
                    public void run()
                    {
                        try
                        {
                            boolean isBlue;
                            if(editText3.getText().toString().equals("0"))
                                isBlue=false;
                            else
                                isBlue=true;
                            Log.d("Communication",network.checkIn(Integer.parseInt(editText1.getText().toString()),editText2.getText().toString(),isBlue)+"");
                        }
                        catch (Exception e)
                        {
                            Log.d("Communication","exception!!!"+e);
                        }
                    }
                }).start();

                Log.d("Communication","Thread end");
            }
        });


        button2.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                final NetworkImplement network = new NetworkImplement();

                new Thread(new Runnable() {
                    public void run()
                    {
                        try
                        {
                            Log.d("Communication",network.checkOut(Integer.parseInt(editText1.getText().toString()),editText2.getText().toString())+"");
                        }
                        catch (Exception e)
                        {
                            Log.d("Communication","exception!!!"+e);
                        }
                    }
                }).start();

                Log.d("Communication","Thread end");
            }
        });
    }

    /*  Communication whit apache-php sever
    public void testPHP()
    {
        try
        {
            URL url = new URL("http://10.0.2.2/android.php"); //virtual android
            //URL url = new URL("http://192.168.253.1/android.php"); //xperia Z2
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoInput(true);
            connection.setDoOutput(true);
            Log.d("lzj","r1");

            connection.connect();
            String param = "name="+input;
            DataOutputStream data = new DataOutputStream(connection.getOutputStream());
            data.writeBytes(param);
            data.flush();
            data.close();
            Log.d("lzj","r2");

            int result = connection.getResponseCode();

            Log.d("lzj","result "+result);

            if(connection.HTTP_OK == result)
            {
                StringBuffer sb = new StringBuffer();
                String Line = "";
                BufferedReader response = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                while((Line = response.readLine()) != null)
                {
                    sb.append(Line).append("\n");
                }
                response.close();
                System.out.println(sb.toString());
                //editText.setText(sb.toString());  //need to do it using Handler
                Message msg = new Message();
                msg.what=1;
                msg.obj=sb.toString();
                handler.sendMessage(msg);
            }
        }
        catch(Exception e)
        {
            Log.d("lzj","ERROR");
        }
    }
    */
}
