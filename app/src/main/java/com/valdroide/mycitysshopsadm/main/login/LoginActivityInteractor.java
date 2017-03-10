package com.valdroide.mycitysshopsadm.main.login;

import android.content.Context;

import com.valdroide.mycitysshopsadm.entities.user.User;

public interface LoginActivityInteractor {
    void validateLogin(Context context, User user);
    void changePlace(Context context);
}
