package com.hunter.master.foxhunter;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class UserInterface extends AppCompatActivity {
    private TextView textView;
    private Button button;
    private String result;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_interface);
        textView = (TextView)findViewById(R.id.testText);
        button = (Button) findViewById(R.id.testButton);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(getThread).start();
            }
        });
    }

    private Thread getThread = new Thread(){
        public void run() {
            HttpURLConnection connection = null;
            try {
                URL url = new URL("http://115.236.59.67:8080/CloudERP/");
                connection = (HttpURLConnection) url.openConnection();
                // 设置请求方法，默认是GET
                connection.setRequestMethod("GET");
                // 设置字符集
                connection.setRequestProperty("Charset", "UTF-8");
                // 设置文件类型
                connection.setRequestProperty("Content-Type", "text/xml; charset=UTF-8");
                // 设置请求参数，可通过Servlet的getHeader()获取
                connection.setRequestProperty("Cookie", "AppName=" + URLEncoder.encode("你好", "UTF-8"));
                // 设置自定义参数
                connection.setRequestProperty("MyProperty", "this is me!");

                if(connection.getResponseCode() == 200){
                    InputStream is = connection.getInputStream();
                    ByteArrayOutputStream baos   =   new   ByteArrayOutputStream();
                    int i=-1;
                    while((i=is.read())!=-1){
                        baos.write(i);
                    }
                    result = baos.toString();

                    Message msg = Message.obtain();
                    msg.what = 0;
                    getHandler.sendMessage(msg);
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } finally {
                if(connection != null){
                    connection.disconnect();
                }
            }
        };
    };

    private Handler getHandler = new Handler(){
        public void handleMessage(android.os.Message msg) {
            if(msg.what == 0 && result!=null){
                textView.setText(result);
            }
        };
    };


}
