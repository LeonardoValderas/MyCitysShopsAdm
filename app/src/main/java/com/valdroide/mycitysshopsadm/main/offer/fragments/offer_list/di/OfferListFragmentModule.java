package com.valdroide.mycitysshopsadm.main.offer.fragments.offer_list.di;

import android.support.v4.app.Fragment;

import com.valdroide.mycitysshopsadm.api.APIService;
import com.valdroide.mycitysshopsadm.api.ShopClient;
import com.valdroide.mycitysshopsadm.entities.shop.Offer;
import com.valdroide.mycitysshopsadm.lib.base.EventBus;
import com.valdroide.mycitysshopsadm.main.offer.fragments.offer.OfferFragmentInteractor;
import com.valdroide.mycitysshopsadm.main.offer.fragments.offer.OfferFragmentInteractorImpl;
import com.valdroide.mycitysshopsadm.main.offer.fragments.offer.OfferFragmentPresenter;
import com.valdroide.mycitysshopsadm.main.offer.fragments.offer.OfferFragmentPresenterImpl;
import com.valdroide.mycitysshopsadm.main.offer.fragments.offer.OfferFragmentRepository;
import com.valdroide.mycitysshopsadm.main.offer.fragments.offer.OfferFragmentRepositoryImpl;
import com.valdroide.mycitysshopsadm.main.offer.fragments.offer.ui.OfferFragmentView;
import com.valdroide.mycitysshopsadm.main.offer.fragments.offer_list.OfferListFragmentInteractor;
import com.valdroide.mycitysshopsadm.main.offer.fragments.offer_list.OfferListFragmentInteractorImpl;
import com.valdroide.mycitysshopsadm.main.offer.fragments.offer_list.OfferListFragmentPresenter;
import com.valdroide.mycitysshopsadm.main.offer.fragments.offer_list.OfferListFragmentPresenterImpl;
import com.valdroide.mycitysshopsadm.main.offer.fragments.offer_list.OfferListFragmentRepository;
import com.valdroide.mycitysshopsadm.main.offer.fragments.offer_list.OfferListFragmentRepositoryImpl;
import com.valdroide.mycitysshopsadm.main.offer.fragments.offer_list.ui.OfferListFragmentView;
import com.valdroide.mycitysshopsadm.main.offer.fragments.offer_list.ui.adapters.OfferListFragmentRecyclerAdapter;
import com.valdroide.mycitysshopsadm.main.offer.fragments.offer_list.ui.adapters.OnItemClickListener;


import java.util.ArrayList;
import java.util.List;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;


@Module
public class OfferListFragmentModule {
    OfferListFragmentView view;
    Fragment fragment;
    OnItemClickListener onItemClickListener;

    public OfferListFragmentModule(OfferListFragmentView view, Fragment fragment, OnItemClickListener onItemClickListener) {
        this.view = view;
        this.fragment = fragment;
        this.onItemClickListener = onItemClickListener;
    }

    @Provides
    @Singleton
    OfferListFragmentView provideOfferListFragmentView() {
        return this.view;
    }

    @Provides
    @Singleton
    OfferListFragmentPresenter provideOfferListFragmentPresenter(OfferListFragmentView view, EventBus eventBus, OfferListFragmentInteractor interactor) {
        return new OfferListFragmentPresenterImpl(view, eventBus, interactor);
    }

    @Provides
    @Singleton
    OfferListFragmentInteractor provideOfferListFragmentInteractor(OfferListFragmentRepository repository) {
        return new OfferListFragmentInteractorImpl(repository);
    }

    @Provides
    @Singleton
    OfferListFragmentRepository provideOfferListFragmentRepository(EventBus eventBus, APIService service) {
        return new OfferListFragmentRepositoryImpl(eventBus, service);
    }

    @Provides
    @Singleton
    APIService provideAPIService() {
        ShopClient client = new ShopClient();
        return client.getAPIService();
    }

    @Provides
    @Singleton
    OfferListFragmentRecyclerAdapter provideOfferListFragmentRecyclerAdapter(List<Offer> offersList, OnItemClickListener onItemClickListener, Fragment fragment) {
        return new OfferListFragmentRecyclerAdapter(offersList, onItemClickListener, fragment.getActivity());
    }

    @Provides
    @Singleton
    OnItemClickListener provideOnItemClickListener() {
        return this.onItemClickListener;
    }

    @Provides
    @Singleton
    List<Offer> providesOffersList() {
        return new ArrayList<>();
    }
}
