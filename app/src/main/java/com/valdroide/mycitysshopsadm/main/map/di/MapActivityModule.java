package com.valdroide.mycitysshopsadm.main.map.di;

import android.app.Activity;

import com.valdroide.mycitysshopsadm.lib.base.EventBus;
import com.valdroide.mycitysshopsadm.main.map.MapActivityInteractor;
import com.valdroide.mycitysshopsadm.main.map.MapActivityInteractorImpl;
import com.valdroide.mycitysshopsadm.main.map.MapActivityPresenter;
import com.valdroide.mycitysshopsadm.main.map.MapActivityPresenterImpl;
import com.valdroide.mycitysshopsadm.main.map.MapActivityRepository;
import com.valdroide.mycitysshopsadm.main.map.MapActivityRepositoryImpl;
import com.valdroide.mycitysshopsadm.main.map.ui.MapActivity;
import com.valdroide.mycitysshopsadm.main.map.ui.MapActivityView;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class MapActivityModule {
    Activity activity;
    MapActivityView view;

    public MapActivityModule(Activity activity, MapActivityView view) {
        this.activity = activity;
        this.view = view;
    }

    @Provides
    @Singleton
    MapActivityView provideMapActivityView() {
        return this.view;
    }

    @Provides
    @Singleton
    MapActivityPresenter provideMapActivityPresenter(EventBus eventBus, MapActivityView view, MapActivityInteractor interactor) {
        return new MapActivityPresenterImpl(eventBus, view, interactor);
    }

    @Provides
    @Singleton
    MapActivityInteractor provideMapActivityInteractor(MapActivityRepository repository) {
        return new MapActivityInteractorImpl(repository);
    }

    @Provides
    @Singleton
    MapActivityRepository provideMapActivityRepository(EventBus eventBus) {
        return new MapActivityRepositoryImpl(eventBus);
    }
}
