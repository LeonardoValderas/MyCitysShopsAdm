package com.valdroide.mycitysshopsadm.main.account;

import android.content.Context;

import com.valdroide.mycitysshopsadm.entities.user.Account;

/**
 * Created by LEO on 20/2/2017.
 */

public interface AccountActivityInteractor {
    void getAccount();
    //void saveAccount(Context context, Account account);
    void updateAccount(Context context, Account account);
}