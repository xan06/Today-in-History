package com.example.getevent;

import java.util.List;

public class Data {
    private List<Event> Events;
    private List<Event> Births;
    private List<Event> Deaths;

    public void setEvents(List<Event> setEvents) {
        Events = setEvents;
    }

    public void setBirths(List<Event> setBirths) {
        Births = setBirths;
    }

    public void setDeaths(List<Event> setDeaths) {
        Deaths = setDeaths;
    }

    public List<Event> getEvents() {
        return Events;
    }

    public List<Event> getBirths() {
        return Births;
    }

    public List<Event> getDeaths() {
        return Deaths;
    }
}
