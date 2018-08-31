package com.arctouch.codechallenge.application;

import android.app.Application;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class App extends Application {

    private static Gson gson;
    @Override
    public void onCreate() {
        super.onCreate();
        gson = new GsonBuilder().create();
    }

    public static Gson getGson() {
        return gson;
    }
}
