package com.valdroide.mycitysshopsadm.main.draw.broadcast.di;

import android.content.BroadcastReceiver;

import com.valdroide.mycitysshopsadm.api.APIService;
import com.valdroide.mycitysshopsadm.api.ShopClient;
import com.valdroide.mycitysshopsadm.lib.base.EventBus;
import com.valdroide.mycitysshopsadm.main.draw.broadcast.BroadcastDrawInteractor;
import com.valdroide.mycitysshopsadm.main.draw.broadcast.BroadcastDrawInteractorImpl;
import com.valdroide.mycitysshopsadm.main.draw.broadcast.BroadcastDrawPresenter;
import com.valdroide.mycitysshopsadm.main.draw.broadcast.BroadcastDrawPresenterImpl;
import com.valdroide.mycitysshopsadm.main.draw.broadcast.BroadcastDrawRepository;
import com.valdroide.mycitysshopsadm.main.draw.broadcast.BroadcastDrawRepositoryImpl;
import com.valdroide.mycitysshopsadm.main.draw.broadcast.ui.BroadcastDraw;
import com.valdroide.mycitysshopsadm.main.draw.broadcast.ui.BroadcastDrawView;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class BroadcastDrawModule {

    BroadcastReceiver broadcastDraw;
    BroadcastDrawView  broadcastDrawView;

    public BroadcastDrawModule(BroadcastReceiver broadcastDraw, BroadcastDrawView broadcastDrawView) {
        this.broadcastDraw = broadcastDraw;
        this.broadcastDrawView = broadcastDrawView;
    }

    @Provides
    @Singleton
    BroadcastDrawView provideBroadcastDrawView() {
        return this.broadcastDrawView;
    }

    @Provides
    @Singleton
    BroadcastDrawPresenter provideBroadcastDrawPresenter(EventBus eventBus, BroadcastDrawView view, BroadcastDrawInteractor interactor) {
        return new BroadcastDrawPresenterImpl(eventBus, view, interactor);
    }

    @Provides
    @Singleton
    BroadcastDrawInteractor provideBroadcastDrawInteractor(BroadcastDrawRepository repository) {
        return new BroadcastDrawInteractorImpl(repository);
    }

    @Provides
    @Singleton
    BroadcastDrawRepository provideBroadcastDrawRepository(EventBus eventBus, APIService service) {
        return new BroadcastDrawRepositoryImpl(eventBus, service);
    }

    @Provides
    @Singleton
    APIService provideAPIService () {
        ShopClient client = new ShopClient();
        return client.getAPIService();
    }
 }
