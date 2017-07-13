package com.valdroide.mycitysshopsadm.main.account;

import android.content.Context;
import com.valdroide.mycitysshopsadm.entities.shop.Account;

public class AccountActivityInteractorImpl implements AccountActivityInteractor {
    AccountActivityRepository repository;

    public AccountActivityInteractorImpl(AccountActivityRepository repository) {
        this.repository = repository;
    }

    @Override
    public void getAccount(Context context) {
        repository.getAccount(context);
    }

    @Override
    public void getCity(Context context) {
        repository.getCity(context);
    }

    @Override
    public void updateAccount(Context context, Account account) {
        repository.updateAccount(context, account);
    }

    @Override
    public void validateDateShop(Context context) {
        repository.validateDateShop(context);
    }
}
