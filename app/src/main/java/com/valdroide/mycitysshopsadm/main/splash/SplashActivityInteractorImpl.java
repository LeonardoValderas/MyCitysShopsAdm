package com.valdroide.mycitysshopsadm.main.splash;

import android.content.Context;

public class SplashActivityInteractorImpl implements SplashActivityInteractor {

    private SplashActivityRepository repository;

    public SplashActivityInteractorImpl(SplashActivityRepository repository) {
        this.repository = repository;
    }

    @Override
    public void validateDatePlace(Context context) {
        repository.validateDatePlace(context);
    }

//    @Override
//    public void getDateTable() {
//        repository.getDateTable();
//    }
//
//    @Override
//    public void getAllData(Context context) {
//        repository.getAllData(context);
//    }
}
