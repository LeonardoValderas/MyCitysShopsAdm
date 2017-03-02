package com.valdroide.mycitysshopsadm.main.account;

import android.content.Context;

import com.valdroide.mycitysshopsadm.entities.Account;

/**
 * Created by LEO on 20/2/2017.
 */

public interface AccountActivityRepository {
    void getAccount();
    void saveAccount(Context context, Account account);
    void updateAccount(Context context, Account account);
}
