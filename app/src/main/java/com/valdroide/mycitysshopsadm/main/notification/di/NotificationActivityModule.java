package com.valdroide.mycitysshopsadm.main.notification.di;

import android.app.Activity;

import com.valdroide.mycitysshopsadm.api.APIService;
import com.valdroide.mycitysshopsadm.api.ShopClient;
import com.valdroide.mycitysshopsadm.lib.base.EventBus;
import com.valdroide.mycitysshopsadm.main.notification.NotificationActivityInteractor;
import com.valdroide.mycitysshopsadm.main.notification.NotificationActivityInteractorImpl;
import com.valdroide.mycitysshopsadm.main.notification.NotificationActivityPresenter;
import com.valdroide.mycitysshopsadm.main.notification.NotificationActivityPresenterImpl;
import com.valdroide.mycitysshopsadm.main.notification.NotificationActivityRepository;
import com.valdroide.mycitysshopsadm.main.notification.NotificationActivityRepositoryImpl;
import com.valdroide.mycitysshopsadm.main.notification.ui.NotificationActivityView;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;


@Module
public class NotificationActivityModule {
    NotificationActivityView view;
    Activity activity;

    public NotificationActivityModule(NotificationActivityView view, Activity activity) {
        this.view = view;
        this.activity = activity;
    }

    @Provides
    @Singleton
    NotificationActivityView provideNotificationActivityView() {
        return this.view;
    }

    @Provides
    @Singleton
    NotificationActivityPresenter provideNotificationActivityPresenter(NotificationActivityView view, EventBus eventBus, NotificationActivityInteractor interactor) {
        return new NotificationActivityPresenterImpl(view, eventBus, interactor);
    }

    @Provides
    @Singleton
    NotificationActivityInteractor provideNotificationActivityInteractor(NotificationActivityRepository repository) {
        return new NotificationActivityInteractorImpl(repository);
    }

    @Provides
    @Singleton
    NotificationActivityRepository provideNotificationActivityRepository(EventBus eventBus, APIService service) {
        return new NotificationActivityRepositoryImpl(eventBus, service);
    }

    @Provides
    @Singleton
    APIService provideAPIService () {
        ShopClient client = new ShopClient();
        return client.getAPIService();
    }
}
