package com.valdroide.mycitysshopsadm.main.login.di;

import android.app.Activity;

import com.valdroide.mycitysshopsadm.api.APIService;
import com.valdroide.mycitysshopsadm.api.ShopClient;
import com.valdroide.mycitysshopsadm.lib.base.EventBus;
import com.valdroide.mycitysshopsadm.main.login.LoginActivityInteractor;
import com.valdroide.mycitysshopsadm.main.login.LoginActivityInteractorImpl;
import com.valdroide.mycitysshopsadm.main.login.LoginActivityPresenter;
import com.valdroide.mycitysshopsadm.main.login.LoginActivityPresenterImpl;
import com.valdroide.mycitysshopsadm.main.login.LoginActivityRepository;
import com.valdroide.mycitysshopsadm.main.login.LoginActivityRepositoryImpl;
import com.valdroide.mycitysshopsadm.main.login.ui.LoginActivityView;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;


@Module
public class LoginActivityModule {
    LoginActivityView view;
    Activity activity;

    public LoginActivityModule(LoginActivityView view, Activity activity) {
        this.view = view;
        this.activity = activity;
    }

    @Provides
    @Singleton
    LoginActivityView provideLoginActivityView() {
        return this.view;
    }

    @Provides
    @Singleton
    LoginActivityPresenter provideLoginActivityPresenter(LoginActivityView view, EventBus eventBus, LoginActivityInteractor interactor) {
        return new LoginActivityPresenterImpl(view, eventBus, interactor);
    }

    @Provides
    @Singleton
    LoginActivityInteractor provideLoginActivityInteractor(LoginActivityRepository repository) {
        return new LoginActivityInteractorImpl(repository);
    }

    @Provides
    @Singleton
    LoginActivityRepository provideLoginActivityRepository(EventBus eventBus, APIService service) {
        return new LoginActivityRepositoryImpl(eventBus, service);
    }

    @Provides
    @Singleton
    APIService provideAPIService() {
        ShopClient client = new ShopClient();
        return client.getAPIService();
    }
}
