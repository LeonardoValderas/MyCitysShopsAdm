package com.valdroide.mycitysshopsadm.main.splash.di;

import android.app.Activity;

import com.valdroide.mycitysshopsadm.api.APIService;
import com.valdroide.mycitysshopsadm.api.ShopClient;
import com.valdroide.mycitysshopsadm.lib.base.EventBus;
import com.valdroide.mycitysshopsadm.main.splash.SplashActivityInteractor;
import com.valdroide.mycitysshopsadm.main.splash.SplashActivityInteractorImpl;
import com.valdroide.mycitysshopsadm.main.splash.SplashActivityPresenter;
import com.valdroide.mycitysshopsadm.main.splash.SplashActivityPresenterImpl;
import com.valdroide.mycitysshopsadm.main.splash.SplashActivityRepository;
import com.valdroide.mycitysshopsadm.main.splash.SplashActivityRepositoryImpl;
import com.valdroide.mycitysshopsadm.main.splash.ui.SplashActivityView;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class SplashActivityModule {
    SplashActivityView view;
    Activity activity;

    public SplashActivityModule(SplashActivityView view,  Activity activity) {
        this.view = view;
        this.activity = activity;
    }

    @Provides
    @Singleton
    SplashActivityView providesSplashActivityView() {
        return this.view;
    }

    @Provides
    @Singleton
    SplashActivityPresenter providesSplashActivityPresenter(SplashActivityView view, EventBus eventBus, SplashActivityInteractor listInteractor) {
        return new SplashActivityPresenterImpl(view, eventBus, listInteractor);
    }

    @Provides
    @Singleton
    SplashActivityInteractor providesSplashActivityInteractor(SplashActivityRepository repository) {
        return new SplashActivityInteractorImpl(repository);
    }

    @Provides
    @Singleton
    SplashActivityRepository providesSplashActivityRepository(EventBus eventBus, APIService service) {
        return new SplashActivityRepositoryImpl(eventBus, service);
    }

    @Provides
    @Singleton
    APIService provideAPIService() {
        ShopClient client = new ShopClient();
        return client.getAPIService();
    }
}
