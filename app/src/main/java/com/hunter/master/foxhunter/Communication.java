package com.hunter.master.foxhunter;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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


public class Communication extends AppCompatActivity {

    private Button button;
    private EditText editText;
    private TextView textView;
    String input = "";
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
        Log.d("lzj","oncreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_communication);
        button = (Button) findViewById(R.id.button);
        editText = (EditText)findViewById(R.id.editText);
        textView = (TextView)findViewById(R.id.textView);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                input = editText.getText().toString();
                Log.d("lzj",input);
                //textView.setText(input);


                new Thread(new Runnable() {
                    public void run() {
                        //getHttp();
                        testPHP();
                    }
                }).start();

                Log.d("lzj","Thread end");
            }
        });
    }

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


    public void getHttp()
    {
        InputStream inputStream = null;
        InputStreamReader inputStreamReader = null;
        BufferedReader reader = null;
        StringBuffer resultBuffer = new StringBuffer();
        String tempLine = null;
        try
        {
            URL url = new URL("http://10.0.2.2:8080/lab2/");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setDoInput(true);
            connection.setDoOutput(true);
            Log.d("lzj",connection.getResponseCode()+"");

            try
            {
                inputStream = connection.getInputStream();
                inputStreamReader = new InputStreamReader(inputStream);
                reader = new BufferedReader(inputStreamReader);

                while ((tempLine = reader.readLine()) != null)
                {
                    resultBuffer.append(tempLine);
                }
                Log.d("lzj", resultBuffer.toString());

            }
            catch (Exception e)
            {
                Log.d("lzj", "Connection ERROR");

            }
            connection.disconnect();

        }
        catch(Exception e)
        {
            Log.d("lzj", "error");
        }
    }
}
