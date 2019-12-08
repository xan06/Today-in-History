package com.example.getevent;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    /** the API base. */
    private static final String API_BASE = "http://history.muffinlabs.com/date/";

    /** the content of the event. */
    private String content = "";

    /** the year of the event. */
    private int year = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    /**
     * connect the server and get the response.
     */
    private void connect() {
        sendRequestWithOkhttp(API_BASE, new okhttp3.Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                parseJsonWithJsonObject(response);

            }
        });
    }

    /**
     * Convert the response to JSON.
     * @param response the response from server.
     * @throws IOException exception
     */
    private void parseJsonWithJsonObject(Response response) throws IOException {
        String responseData = response.body().string();
        try {
            JSONArray jsonArray = new JSONArray(responseData);
            JSONObject data = jsonArray.getJSONObject(2);
            JSONArray events = data.getJSONArray("Events");
            JSONObject event = events.getJSONObject(0);
            content = event.getString("text");
            year = event.getInt("year");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Send Request
     * @param address the URL
     * @param callback not important
     */
    public static void sendRequestWithOkhttp(String address,okhttp3.Callback callback) {
        //Create a OkHttpClient
        OkHttpClient client = new OkHttpClient();
        //Create a Request and put URL
        Request request = new Request.Builder().url(address).build();
        //Create Asynchronous request
        client.newCall(request).enqueue(callback);
    }
}
