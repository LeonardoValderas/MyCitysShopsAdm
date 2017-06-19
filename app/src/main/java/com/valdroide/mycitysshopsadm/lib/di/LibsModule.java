package com.valdroide.mycitysshopsadm.lib.di;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.support.v4.app.Fragment;

import com.valdroide.mycitysshopsadm.lib.base.EventBus;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;


@Module
public class LibsModule {
    private Activity activity;
    private Fragment fragment;
    private BroadcastReceiver broadcastReceiver;

    public LibsModule() {
    }

    public LibsModule(Activity activity) {
        this.activity = activity;
    }

    public LibsModule(Fragment fragment) {
        this.fragment = fragment;
    }

    public LibsModule(BroadcastReceiver broadcastReceiver) {
        this.broadcastReceiver = broadcastReceiver;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public void setFragment(Fragment fragment) {
        this.fragment = fragment;
    }

    public void setBroadcastReceiver(BroadcastReceiver broadcastReceiver) {
        this.broadcastReceiver = broadcastReceiver;
    }
    @Provides
    @Singleton
    EventBus providesEventBus() {
        return new GreenRobotEventBus();
    }

    @Provides
    @Singleton
    Activity providesActivity() {
        return this.activity;
    }

    @Provides
    @Singleton
    Fragment providesFragment() {
        return this.fragment;
    }

    @Provides
    @Singleton
    BroadcastReceiver providesBroadcastReceiver() {
        return this.broadcastReceiver;
    }
}
