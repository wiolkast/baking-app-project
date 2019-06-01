package com.example.baking_app_project.utils;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class AppExecutors {
    private static AppExecutors instance;
    private final Executor networkIO;

    private AppExecutors(Executor networkIO) {
        this.networkIO = networkIO;
    }

    public static AppExecutors getInstance() {
        if (instance == null) {
            synchronized (AppExecutors.class) {
                instance = new AppExecutors(Executors.newSingleThreadExecutor());
            }
        }
        return instance;
    }

    public Executor networkIO() {
        return networkIO;
    }
}