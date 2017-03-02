package com.valdroide.mycitysshopsadm.main.account.ui;

import com.valdroide.mycitysshopsadm.entities.Account;

/**
 * Created by LEO on 20/2/2017.
 */

public interface AccountActivityView {
    void getPhoto();
    void error(String mgs);
    void saveSuccess();
    void setAccount(Account account);
}
