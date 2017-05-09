package com.valdroide.mycitysshopsadm.main.navigation;

import android.content.Context;

import com.valdroide.mycitysshopsadm.main.navigation.events.NavigationActivityEvent;

public interface NavigationActivityPresenter {
    void onCreate();
    void onDestroy();
    void logOut(Context context);
    void getURLShop(Context context);
    void onEventMainThread(NavigationActivityEvent event);
}
