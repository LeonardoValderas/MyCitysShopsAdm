package com.valdroide.mycitysshopsadm.main.login;

import android.content.Context;

import com.valdroide.mycitysshopsadm.main.login.events.LoginActivityEvent;

public interface LoginActivityPresenter {
    void onCreate();
    void onDestroy();
    void changePlace(Context context);
    void validateLogin(Context context, String user, String pass, int id_city);
    void onEventMainThread(LoginActivityEvent event);
}
