package com.valdroide.mycitysshopsadm.main.offer.fragments.offer.di;

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

import java.util.ArrayList;
import java.util.List;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;


@Module
public class OfferFragmentModule {
    OfferFragmentView view;
    Fragment fragment;
   // OnItemClickListener onItemClickListener;

    public OfferFragmentModule(OfferFragmentView view, Fragment fragment) {
        this.view = view;
        this.fragment = fragment;
     //   this.onItemClickListener = onItemClickListener;
    }

    @Provides
    @Singleton
    OfferFragmentView provideOfferActivityView() {
        return this.view;
    }

    @Provides
    @Singleton
    OfferFragmentPresenter provideOfferActivityPresenter(OfferFragmentView view, EventBus eventBus, OfferFragmentInteractor interactor) {
        return new OfferFragmentPresenterImpl(view, eventBus, interactor);
    }

    @Provides
    @Singleton
    OfferFragmentInteractor provideOfferActivityInteractor(OfferFragmentRepository repository) {
        return new OfferFragmentInteractorImpl(repository);
    }

    @Provides
    @Singleton
    OfferFragmentRepository provideOfferActivityRepository(EventBus eventBus, APIService service) {
        return new OfferFragmentRepositoryImpl(eventBus, service);
    }

    @Provides
    @Singleton
    APIService provideAPIService() {
        ShopClient client = new ShopClient();
        return client.getAPIService();
    }

//    @Provides
//    @Singleton
//    OfferActivityRecyclerAdapter provideOfferActivityRecyclerAdapter(List<Offer> offersList, OnItemClickListener onItemClickListener, Fragment fragment) {
//        return new OfferActivityRecyclerAdapter(offersList, onItemClickListener, fragment.getActivity());
//    }

//    @Provides
//    @Singleton
//    OnItemClickListener provideOnItemClickListener() {
//        return this.onItemClickListener;
//    }

//    @Provides
//    @Singleton
//    List<Offer> providesOffersList() {
//        return new ArrayList<Offer>();
//    }
}
