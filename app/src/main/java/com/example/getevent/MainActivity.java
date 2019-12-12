package com.example.getevent;


import androidx.appcompat.app.AppCompatActivity;


import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import java.io.IOException;

import okhttp3.Call;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    /** the API base. */
    private static final String API_BASE = "http://history.muffinlabs.com/date/";

    /** the user input of the date. */
    private String input = "";

    private LinearLayout eventLayout;
    private ObservableScrollView scroll;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        eventLayout = (LinearLayout) findViewById(R.id.eventLayout);
        scroll = (ObservableScrollView) findViewById(R.id.scrollView);
        scroll.bringToFront();

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

    /**
     * send Request with OkHttp (Get)
     */
    private void sendRequestWithOkHttp() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                //Http request is executed in a child thread, and make result to oKHttp3.Callback
                HttpUtil.sendOkHttpRequest(API_BASE + input,new okhttp3.Callback(){
                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        //Get result
                        String responseData=response.body().string();
                        parseJSONWithGSON(responseData);
                    }
                    @Override
                    public void onFailure(Call call,IOException e){
                        //Error message
                        displayExceptionMessage(e.getMessage());
                    }
                });
            }
        }).start();
    }

    /**
     * error msg
     * @param msg message
     */
    public void displayExceptionMessage(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }


    private void parseJSONWithGSON(String jsonData) {
        //Create Gson
        Gson gson = new Gson();
        Result result = gson.fromJson(jsonData, Result.class);
        //Setup UI
        showResponse(result);
    }
    private void showResponse(final Result response) {
        //Update UI in child thread
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //Set up UI
                TextView event1 = findViewById(R.id.event1);
                TextView event2 = findViewById(R.id.event2);
                TextView event3 = findViewById(R.id.event3);
                TextView event4 = findViewById(R.id.event4);
                TextView event5 = findViewById(R.id.even5);
                event1.setText("Birthday: \n\n" + response.getData().getBirths().get(0).toString());
                event2.setText("Events: \n\n"+response.getData().getEvents().get(0).toString());
                event3.setText(response.getData().getEvents().get(1).toString());
                event4.setText(response.getData().getEvents().get(2).toString());
                event5.setText("Death: \n\n"+response.getData().getDeaths().get(0).toString());
            }
        });
    }
}
