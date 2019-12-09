package com.example.getevent;

public class Result {
    private String date;
    private String url;
    private Data data;

    public void setDate(String setDate) {
        date = setDate;
    }
    public void setUrl(String setUrl) {
        url = setUrl;
    }
    public void setData(Data setData) {
        data = setData;
    }
    public String getDate() {
        return date;
    }
    public String getUrl() {
        return url;
    }
    public Data getData() {
        return data;
    }
}
