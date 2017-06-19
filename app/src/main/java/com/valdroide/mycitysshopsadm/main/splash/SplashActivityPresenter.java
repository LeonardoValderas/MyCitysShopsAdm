package com.valdroide.mycitysshopsadm.main.splash;

import android.content.Context;

import com.valdroide.mycitysshopsadm.main.splash.events.SplashActivityEvent;
import com.valdroide.mycitysshopsadm.main.splash.ui.SplashActivityView;

public interface SplashActivityPresenter {
    void onCreate();
    void onDestroy();
    void validateDatePlace(Context context);
    void validateDateShop(Context context);
    void sendEmail(Context context, String comment);
    SplashActivityView getView();
    void onEventMainThread(SplashActivityEvent event);
}
