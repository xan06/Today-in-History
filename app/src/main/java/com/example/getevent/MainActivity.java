package com.example.getevent;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
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
                //Http request is executed in a child thread, and make result to oKHttp3.Callback
                HttpUtil.sendOkHttpRequest(API_BASE + input,new okhttp3.Callback(){
                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        //得到服务器返回的具体内容
                        //Get result
                        String responseData=response.body().string();
                        parseJSONWithGSON(responseData);
                    }
                    @Override
                    public void onFailure(Call call,IOException e){
                        //在这里进行异常情况处理
                        //Error message
                        AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
                        alertDialog.setTitle("Alert");
                        alertDialog.setMessage(e.getMessage());
                        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                        alertDialog.show();
                    }
                });
            }
        }).start();
    }

    private void parseJSONWithGSON(String jsonData) {
        //使用轻量级的Gson解析得到的json
        //Create Gson
        Gson gson = new Gson();
        Result result = gson.fromJson(jsonData, Result.class);
        //显示UI界面，调用的showResponse方法
        //Setup UI
        showResponse(result);
    }
    private void showResponse(final Result response) {
        //在子线程中更新UI
        //Update UI in child thread
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // 在这里进行UI操作，将结果显示到界面上
                //Set up UI
                TextView event1 = findViewById(R.id.event1);
                com.barnettwong.autofitcolortextview_library.AutoFitColorTextView event2 = findViewById(R.id.event2);
                event1.setText(response.getData().getBirths().get(0).toString());
                event2.setText(response.getData().getEvents().get(0).toString());
            }
        });
    }
}
