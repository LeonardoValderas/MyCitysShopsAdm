package com.valdroide.mycitysshopsadm.main.navigation;

import android.content.Context;

import com.valdroide.mycitysshopsadm.main.navigation.events.NavigationActivityEvent;
import com.valdroide.mycitysshopsadm.main.navigation.ui.NavigationActivityView;

public interface NavigationActivityPresenter {
    void onCreate();
    void onDestroy();
    void logOut(Context context);
    void getURLShop(Context context);
    NavigationActivityView getView();
    void onEventMainThread(NavigationActivityEvent event);
}
