package com.valdroide.mycitysshopsadm.main.account;

import android.content.Context;
import com.valdroide.mycitysshopsadm.entities.shop.Account;

public interface AccountActivityRepository {
    void getAccount(Context context);
    void updateAccount(Context context, Account account);
}
