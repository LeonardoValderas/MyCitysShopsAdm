package com.valdroide.mycitysshopsadm.main.account;

import android.content.Context;

import com.valdroide.mycitysshopsadm.entities.shop.Account;

public interface AccountActivityInteractor {
    void getAccount(Context context);
    void getCity(Context context);
    void updateAccount(Context context, Account account);
    void validateDateShop(Context context);
}
