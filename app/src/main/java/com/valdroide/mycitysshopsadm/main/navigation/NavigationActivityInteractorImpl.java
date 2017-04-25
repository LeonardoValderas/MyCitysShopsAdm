package com.valdroide.mycitysshopsadm.main.navigation;

import android.content.Context;

public class NavigationActivityInteractorImpl implements NavigationActivityInteractor {
    private NavigationActivityRepository repository;

    public NavigationActivityInteractorImpl(NavigationActivityRepository repository) {
        this.repository = repository;
    }

    @Override
    public void logOut(Context context) {
      repository.logOut(context);
    }
}
