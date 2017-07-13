package com.valdroide.mycitysshopsadm.main.account;

import android.content.Context;

import com.valdroide.mycitysshopsadm.entities.shop.Account;
import com.valdroide.mycitysshopsadm.main.account.events.AccountActivityEvent;
import com.valdroide.mycitysshopsadm.main.account.ui.AccountActivityView;

public interface AccountActivityPresenter {
    void onCreate();
    void onDestroy();
    AccountActivityView getView();
    void getCity(Context context);
    void getAccount(Context context);
    void updateAccount(Context context, Account account);
    void validateDateShop(Context context);
    void onEventMainThread(AccountActivityEvent event);

}
