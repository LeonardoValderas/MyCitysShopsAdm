package com.valdroide.mycitysshopsadm.main.offer.di;

import android.app.Activity;
import android.content.Context;

import com.valdroide.mycitysshopsadm.api.APIService;
import com.valdroide.mycitysshopsadm.api.ShopClient;
import com.valdroide.mycitysshopsadm.entities.shop.Offer;
import com.valdroide.mycitysshopsadm.lib.base.EventBus;
import com.valdroide.mycitysshopsadm.main.offer.OfferActivityInteractor;
import com.valdroide.mycitysshopsadm.main.offer.OfferActivityInteractorImpl;
import com.valdroide.mycitysshopsadm.main.offer.OfferActivityPresenter;
import com.valdroide.mycitysshopsadm.main.offer.OfferActivityPresenterImpl;
import com.valdroide.mycitysshopsadm.main.offer.OfferActivityRepository;
import com.valdroide.mycitysshopsadm.main.offer.OfferActivityRepositoryImpl;
import com.valdroide.mycitysshopsadm.main.offer.ui.OfferActivityView;
import com.valdroide.mycitysshopsadm.main.offer.ui.adapters.OfferActivityRecyclerAdapter;
import com.valdroide.mycitysshopsadm.main.offer.ui.adapters.OnItemClickListener;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;


@Module
public class OfferActivityModule {
    OfferActivityView view;
    Activity activity;
    OnItemClickListener onItemClickListener;

    public OfferActivityModule(OfferActivityView view, Activity activity, OnItemClickListener onItemClickListener) {
        this.view = view;
        this.activity = activity;
        this.onItemClickListener = onItemClickListener;
    }

    @Provides
    @Singleton
    OfferActivityView provideOfferActivityView() {
        return this.view;
    }

    @Provides
    @Singleton
    OfferActivityPresenter provideOfferActivityPresenter(OfferActivityView view, EventBus eventBus, OfferActivityInteractor interactor) {
        return new OfferActivityPresenterImpl(view, eventBus, interactor);
    }

    @Provides
    @Singleton
    OfferActivityInteractor provideOfferActivityInteractor(OfferActivityRepository repository) {
        return new OfferActivityInteractorImpl(repository);
    }

    @Provides
    @Singleton
    OfferActivityRepository provideOfferActivityRepository(EventBus eventBus, APIService service) {
        return new OfferActivityRepositoryImpl(eventBus, service);
    }

    @Provides
    @Singleton
    APIService provideAPIService() {
        ShopClient client = new ShopClient();
        return client.getAPIService();
    }

    @Provides
    @Singleton
    OfferActivityRecyclerAdapter provideOfferActivityRecyclerAdapter(List<Offer> offersList, OnItemClickListener onItemClickListener, Activity activity) {
        return new OfferActivityRecyclerAdapter(offersList, onItemClickListener, activity);
    }

    @Provides
    @Singleton
    OnItemClickListener provideOnItemClickListener() {
        return this.onItemClickListener;
    }

    @Provides
    @Singleton
    List<Offer> providesOffersList() {
        return new ArrayList<Offer>();
    }
}
