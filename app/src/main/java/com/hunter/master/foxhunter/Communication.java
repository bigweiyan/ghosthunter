package com.hunter.master.foxhunter;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

public class Communication extends AppCompatActivity {

    private Button button;
    private EditText editText;
    private TextView textView;
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
                String input = editText.getText().toString();
                Log.d("lzj",input);
                textView.setText(input);

                new Thread(new Runnable() {
                    public void run() {
                        getHttp(); //发送文本内容到Web服务器
                    }
                }).start(); // 开启线程

                /*try {

                    Log.d("lzj", "0");
                    //URL url = new URL("http://192.168.191.1:8080/lab2/");
                }
                catch(Exception e)
                {
                    Log.d("lzj","try error");
                }
                */

            }
        });
    }

    public void getHttp()
    {
        String result = "123";
        try{
            URL url = new URL ("http://www.baidu.com");
            Log.d("lzj","1");

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            Log.d("lzj","2");

            InputStreamReader in = new InputStreamReader(
                    connection.getInputStream()); // 获得读取的内容
            Log.d("lzj","3");
            BufferedReader buffer = new BufferedReader(in); // 获取输入流对象
            Log.d("lzj","4");
            String inputLine = null;
            //通过循环逐行读取输入流中的内容
            while ((inputLine = buffer.readLine()) != null) {
                result += inputLine + "\n";

            }
            Log.d("lzj","5");
            Log.d("lzj",result);
            textView.setText(result);
            Log.d("lzj","6");
        }
        catch(Exception e)
        {
            Log.d("lzj","NETWORK ERROR");
        }
    }
}
