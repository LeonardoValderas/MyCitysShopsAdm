package com.valdroide.mycitysshopsadm.main.navigation;

import com.valdroide.mycitysshopsadm.main.navigation.events.NavigationActivityEvent;

public interface NavigationActivityPresenter {
    void onCreate();
    void onDestroy();
    void logOut();
    void onEventMainThread(NavigationActivityEvent event);
}
