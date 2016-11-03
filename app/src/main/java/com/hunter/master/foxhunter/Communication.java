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

                try
                {

                    Log.d("lzj","0");
                    //URL url = new URL("http://192.168.191.1:8080/lab2/");
                    URL url = new URL ("http://www.baidu.com");
                    URLConnection urlconnection = url.openConnection();
                    HttpURLConnection connection = (HttpURLConnection)urlconnection;

                    Log.d("lzj","1");
                    connection.setRequestMethod("GET");
                    connection.setReadTimeout(30000);
                    connection.setConnectTimeout(30000);
                    Log.d("lzj","1.5");
                    if(connection==null)
                        Log.d("lzj","null");
                    connection.connect();

                    Log.d("lzj","2");

                    InputStream get = connection.getInputStream();
                    Log.d("lzj","3");
                    BufferedReader reader = new BufferedReader(new InputStreamReader(get));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while((line = reader.readLine()) != null){
                        response.append(line);
                        Log.d("lzj",line);
                    }

                    textView.setText(response);
                }
                catch(Exception e)
                {
                    Log.d("lzj","NETWORK ERROR");
                }
            }
        });
    }
}
