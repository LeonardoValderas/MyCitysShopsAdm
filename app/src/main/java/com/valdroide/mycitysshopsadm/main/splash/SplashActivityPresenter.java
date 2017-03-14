package com.valdroide.mycitysshopsadm.main.splash;

import android.content.Context;

import com.valdroide.mycitysshopsadm.main.splash.events.SplashActivityEvent;

public interface SplashActivityPresenter {
    void onCreate();
    void onDestroy();
    void validateDatePlace(Context context);
    void validateDateShop(Context context);
    void onEventMainThread(SplashActivityEvent event);
}
