package com.valdroide.mycitysshopsadm.main.draw.fragments.draw.ui;


public interface DrawFragmentView {
    void getPhoto();
    void setError(String msg);
    void onClickDateButton();
    void onClickTimeButton();
    void onClickDateUseButton();
    void setDate();
    void setTime();
    void createSuccess();
}
