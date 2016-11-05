package com.hunter.master.foxhunter;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.wxyz.framework.Input;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
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
                        //getHttp();
                        postHTTP();
                    }
                }).start();

                Log.d("lzj","Thread end");
            }
        });
    }

    public void postHTTP()
    {
        StringBuffer buffer = new StringBuffer();
        buffer.append("12321");
        try
        {
            URL url = new URL("http://10.0.2.2:8080/lab2/");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setDoOutput(true);

            Log.d("lzj", connection.getResponseCode() + "");

            connection.setRequestMethod("POST");

            Log.d("lzj","post");

            try
            {

                byte[] bytes = buffer.toString().getBytes();

                System.out.println(bytes);

                Log.d("lzj","0");

                connection.getOutputStream().write(bytes);

                Log.d("lzj","1");

                InputStream inputStream = connection.getInputStream();


                Log.d("lzj","2");


                Log.d("lzj","ret="+inputStream.toString());


            }
            catch (Exception e)
            {
                Log.d("lzj","error");
            }

        }
        catch (Exception e)
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
