package com.valdroide.mycitysshopsadm.main.login;

import android.content.Context;

public class LoginActivityInteractorImpl implements LoginActivityInteractor {

    private LoginActivityRepository repository;

    public LoginActivityInteractorImpl(LoginActivityRepository repository) {
        this.repository = repository;
    }

    @Override
    public void validateLogin(Context context, String user, String pass, int id_city) {
        repository.validateLogin(context, user, pass, id_city);
    }

    @Override
    public void changePlace(Context context) {
        repository.changePlace(context);
    }
}
