package com.valdroide.mycitysshopsadm.main.navigation.di;

import android.app.Activity;

import com.valdroide.mycitysshopsadm.lib.base.EventBus;
import com.valdroide.mycitysshopsadm.main.navigation.NavigationActivityInteractor;
import com.valdroide.mycitysshopsadm.main.navigation.NavigationActivityInteractorImpl;
import com.valdroide.mycitysshopsadm.main.navigation.NavigationActivityPresenter;
import com.valdroide.mycitysshopsadm.main.navigation.NavigationActivityPresenterImpl;
import com.valdroide.mycitysshopsadm.main.navigation.NavigationActivityRepository;
import com.valdroide.mycitysshopsadm.main.navigation.NavigationActivityRepositoryImpl;
import com.valdroide.mycitysshopsadm.main.navigation.ui.NavigationActivityView;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;


@Module
public class NavigationActivityModule {
    NavigationActivityView view;
    Activity activity;

    public NavigationActivityModule(NavigationActivityView view, Activity activity) {
        this.view = view;
        this.activity = activity;
    }

    @Provides
    @Singleton
    NavigationActivityView provideNavigationActivityView() {
        return this.view;
    }

    @Provides
    @Singleton
    NavigationActivityPresenter provideNavigationActivityPresenter(NavigationActivityView view, EventBus eventBus, NavigationActivityInteractor interactor) {
        return new NavigationActivityPresenterImpl(view, eventBus, interactor);
    }

    @Provides
    @Singleton
    NavigationActivityInteractor provideNavigationActivityInteractor(NavigationActivityRepository repository) {
        return new NavigationActivityInteractorImpl(repository);
    }

    @Provides
    @Singleton
    NavigationActivityRepository provideNavigationActivityRepository(EventBus eventBus) {
        return new NavigationActivityRepositoryImpl(eventBus);
    }
}
