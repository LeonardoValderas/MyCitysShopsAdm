package com.valdroide.mycitysshopsadm.main.login.ui;

/**
 * Created by LEO on 28/2/2017.
 */

public interface LoginActivityView {
    void loginSuccess();
    void setError(String msg);
    void goToPlace();
}