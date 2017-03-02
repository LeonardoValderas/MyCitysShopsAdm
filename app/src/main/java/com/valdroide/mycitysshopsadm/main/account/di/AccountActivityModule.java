package com.valdroide.mycitysshopsadm.main.account.di;

import android.app.Activity;

import com.valdroide.mycitysshopsadm.api.APIService;
import com.valdroide.mycitysshopsadm.api.ShopClient;
import com.valdroide.mycitysshopsadm.lib.base.EventBus;
import com.valdroide.mycitysshopsadm.main.account.AccountActivityInteractor;
import com.valdroide.mycitysshopsadm.main.account.AccountActivityInteractorImpl;
import com.valdroide.mycitysshopsadm.main.account.AccountActivityPresenter;
import com.valdroide.mycitysshopsadm.main.account.AccountActivityPresenterImpl;
import com.valdroide.mycitysshopsadm.main.account.AccountActivityRepository;
import com.valdroide.mycitysshopsadm.main.account.AccountActivityRepositoryImpl;
import com.valdroide.mycitysshopsadm.main.account.ui.AccountActivityView;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;


@Module
public class AccountActivityModule {
    AccountActivityView view;
    Activity activity;

    public AccountActivityModule(AccountActivityView view, Activity activity) {
        this.view = view;
        this.activity = activity;
    }

    @Provides
    @Singleton
    AccountActivityView provideAccountActivityView() {
        return this.view;
    }

    @Provides
    @Singleton
    AccountActivityPresenter provideAccountActivityPresenter(AccountActivityView view, EventBus eventBus, AccountActivityInteractor interactor) {
        return new AccountActivityPresenterImpl(view, eventBus, interactor);
    }

    @Provides
    @Singleton
    AccountActivityInteractor provideAccountActivityInteractor(AccountActivityRepository repository) {
        return new AccountActivityInteractorImpl(repository);
    }

    @Provides
    @Singleton
    AccountActivityRepository provideAccountActivityRepository(EventBus eventBus, APIService service) {
        return new AccountActivityRepositoryImpl(eventBus, service);
    }

    @Provides
    @Singleton
    APIService provideAPIService () {
        ShopClient client = new ShopClient();
        return client.getAPIService();
    }
}
