package com.valdroide.mycitysshopsadm.main.account;

import android.content.Context;
import com.valdroide.mycitysshopsadm.entities.user.Account;

public class AccountActivityInteractorImpl implements AccountActivityInteractor {
    AccountActivityRepository repository;

    public AccountActivityInteractorImpl(AccountActivityRepository repository) {
        this.repository = repository;
    }

    @Override
    public void getAccount() {
        repository.getAccount();
    }

//    @Override
//    public void saveAccount(Context context, Account account) {
//        repository.saveAccount(context, account);
//    }

    @Override
    public void updateAccount(Context context, Account account) {
        repository.updateAccount(context, account);
    }
}
