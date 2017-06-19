package com.valdroide.mycitysshopsadm.main.login.ui;

public interface LoginActivityView {
    void loginSuccess();
    void goToPlace();
    void goToNotification();
    void setError(String msg);
    void showProgressDialog();
    void hidePorgressDialog();
}
