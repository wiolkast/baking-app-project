package com.example.baking_app_project.utils;

import android.support.annotation.Nullable;
import android.support.test.espresso.IdlingResource;

import java.util.concurrent.atomic.AtomicBoolean;

public class SimpleIdlingResource implements IdlingResource {
    @Nullable private volatile ResourceCallback resourceCallback;
    private AtomicBoolean isIddleNow = new AtomicBoolean(true);

    @Override
    public String getName() {
        return this.getClass().getName();
    }

    @Override
    public boolean isIdleNow() {
        return isIddleNow.get();
    }

    @Override
    public void registerIdleTransitionCallback(ResourceCallback callback) {
        this.resourceCallback = callback;
    }

    public void setIdleState(boolean isIdleNow){
        this.isIddleNow.set(isIdleNow);
        if(isIdleNow && resourceCallback != null){
            resourceCallback.onTransitionToIdle();
        }
    }
}
