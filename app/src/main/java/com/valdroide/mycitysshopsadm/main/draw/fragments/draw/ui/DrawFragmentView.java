package com.valdroide.mycitysshopsadm.main.draw.fragments.draw.ui;


public interface DrawFragmentView {
    void getPhoto();
    void setError(String msg);
    void onClickDateButton();
    void onClickTimeButton();
    void onClickDateUseButton();
    void setCity(String city);
    void setDate();
    void setTime();
    void createSuccess();
    void showProgressDialog();
    void hidePorgressDialog();
    void refreshAdapter();
}
