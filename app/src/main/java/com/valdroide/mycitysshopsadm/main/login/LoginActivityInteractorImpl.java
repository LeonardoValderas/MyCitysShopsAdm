package com.valdroide.mycitysshopsadm.main.login;

import android.content.Context;

import com.valdroide.mycitysshopsadm.entities.user.User;

public class LoginActivityInteractorImpl implements LoginActivityInteractor {

    private LoginActivityRepository repository;

    public LoginActivityInteractorImpl(LoginActivityRepository repository) {
        this.repository = repository;
    }

    @Override
    public void validateLogin(Context context,  User user) {
        repository.validateLogin(context, user);
    }

    @Override
    public void changePlace(Context context) {
        repository.changePlace(context);
    }
}
