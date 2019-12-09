package com.example.getevent;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import java.io.IOException;

import okhttp3.Call;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    /** the API base. */
    private static final String API_BASE = "http://history.muffinlabs.com/date/";

    /** the user input of the date. */
    private String input = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final EditText date = findViewById(R.id.InputDate);
        Button submit = findViewById(R.id.submitButton);
        submit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Code here executes on main thread after user presses button
                input = date.getText().toString();
                sendRequestWithOkHttp();
            }
        });
    }

    /*
    OkHttpClient client = new OkHttpClient();
    String run(String url) throws IOException {
        Request request = new Request.Builder().url(url).build();
        Response response = client.newCall(request).execute();
        if (response.isSuccessful()) {
            return response.body().string();
        } else {
            throw new IOException("Unexpected code " + response);
        }
    }
    */

    private void sendRequestWithOkHttp() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                //在子线程中执行Http请求，并将最终的请求结果回调到okhttp3.Callback中
                HttpUtil.sendOkHttpRequest(API_BASE + input,new okhttp3.Callback(){
                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        //得到服务器返回的具体内容
                        String responseData=response.body().string();
                        parseJSONWithGSON(responseData);
                    }
                    @Override
                    public void onFailure(Call call,IOException e){
                        //在这里进行异常情况处理
                        TextView erroMsg = findViewById(R.id.errorMessage);
                        erroMsg.setText(e.toString());
                    }
                });
            }
        }).start();
    }

    private void parseJSONWithGSON(String jsonData) {
        //使用轻量级的Gson解析得到的json
        Gson gson = new Gson();
        Result result = gson.fromJson(jsonData, Result.class);
        //显示UI界面，调用的showResponse方法
        showResponse(result.getData().getEvents().get(0).toString());
    }
    private void showResponse(final String response) {
        //在子线程中更新UI
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // 在这里进行UI操作，将结果显示到界面上
                TextView event1 = findViewById(R.id.event1);
                event1.setText(response);
            }
        });
    }
}
