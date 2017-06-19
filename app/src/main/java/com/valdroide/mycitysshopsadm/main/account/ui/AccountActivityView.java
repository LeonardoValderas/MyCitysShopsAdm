package com.valdroide.mycitysshopsadm.main.account.ui;

import com.valdroide.mycitysshopsadm.entities.shop.Account;

public interface AccountActivityView {
    void getPhoto();
    void error(String mgs);
    void saveSuccess();
    void setAccount(Account account);
    void setCity(String city);
    void showProgressDialog();
    void hidePorgressDialog();
}
