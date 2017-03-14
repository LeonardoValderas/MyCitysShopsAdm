package com.valdroide.mycitysshopsadm.main.account;

import android.content.Context;

import com.valdroide.mycitysshopsadm.entities.shop.Account;
import com.valdroide.mycitysshopsadm.main.account.events.AccountActivityEvent;
import com.valdroide.mycitysshopsadm.main.account.ui.AccountActivityView;

/**
 * Created by LEO on 20/2/2017.
 */

public interface AccountActivityPresenter {
    void onCreate();
    void onDestroy();
    AccountActivityView getView();
    void getAccount();
  //  void saveAccount(Context context, Account account);
    void updateAccount(Context context, Account account);
    void onEventMainThread(AccountActivityEvent event);
}
