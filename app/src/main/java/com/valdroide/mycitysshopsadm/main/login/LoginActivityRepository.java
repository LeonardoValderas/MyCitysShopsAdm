package com.valdroide.mycitysshopsadm.main.login;

import android.content.Context;

public interface LoginActivityRepository {
    void validateLogin(Context context,  String user, String pass, int id_city);
    void changePlace(Context context);
}
