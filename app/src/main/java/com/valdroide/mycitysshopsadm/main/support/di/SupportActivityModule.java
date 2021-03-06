package com.valdroide.mycitysshopsadm.main.support.di;

import android.app.Activity;

import com.valdroide.mycitysshopsadm.lib.base.EventBus;
import com.valdroide.mycitysshopsadm.main.support.SupportActivityInteractor;
import com.valdroide.mycitysshopsadm.main.support.SupportActivityInteractorImpl;
import com.valdroide.mycitysshopsadm.main.support.SupportActivityPresenter;
import com.valdroide.mycitysshopsadm.main.support.SupportActivityPresenterImpl;
import com.valdroide.mycitysshopsadm.main.support.SupportActivityRepository;
import com.valdroide.mycitysshopsadm.main.support.SupportActivityRepositoryImpl;
import com.valdroide.mycitysshopsadm.main.support.ui.SupportActivityView;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;


@Module
public class SupportActivityModule {
    SupportActivityView view;
    Activity activity;

    public SupportActivityModule(SupportActivityView view, Activity activity) {
        this.view = view;
        this.activity = activity;
    }

    @Provides
    @Singleton
    SupportActivityView provideSupportActivityView() {
        return this.view;
    }

    @Provides
    @Singleton
    SupportActivityPresenter provideSupportActivityPresenter(SupportActivityView view, EventBus eventBus, SupportActivityInteractor interactor) {
        return new SupportActivityPresenterImpl(view, eventBus, interactor);
    }

    @Provides
    @Singleton
    SupportActivityInteractor provideSupportActivityInteractor(SupportActivityRepository repository) {
        return new SupportActivityInteractorImpl(repository);
    }

    @Provides
    @Singleton
    SupportActivityRepository provideSupportActivityRepository(EventBus eventBus) {
        return new SupportActivityRepositoryImpl(eventBus);
    }
}
