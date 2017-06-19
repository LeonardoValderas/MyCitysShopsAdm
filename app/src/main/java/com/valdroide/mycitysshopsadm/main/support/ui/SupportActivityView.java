package com.valdroide.mycitysshopsadm.main.support.ui;

public interface SupportActivityView {
    void sendEmail();
    void sendEmailSuccess();
    void setError(String error);
    void showProgressDialog();
    void hidePorgressDialog();
}
