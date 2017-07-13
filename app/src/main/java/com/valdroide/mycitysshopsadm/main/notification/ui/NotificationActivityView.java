package com.valdroide.mycitysshopsadm.main.notification.ui;

public interface NotificationActivityView {
    void setSuccess(String date);
    void setSuccessAdm();
    void setError(String error);
    void isAvailable(boolean is_available, String date);
    void showProgressDialog();
    void hidePorgressDialog();
    void validateDate();
}
