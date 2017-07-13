package com.valdroide.mycitysshopsadm.main.offer.fragments.offer_list;

import android.content.Context;

import com.valdroide.mycitysshopsadm.entities.shop.Offer;
import com.valdroide.mycitysshopsadm.lib.base.EventBus;
import com.valdroide.mycitysshopsadm.main.offer.fragments.offer_list.events.OfferListFragmentEvent;
import com.valdroide.mycitysshopsadm.main.offer.fragments.offer_list.ui.OfferListFragmentView;

import org.greenrobot.eventbus.Subscribe;

public class OfferListFragmentPresenterImpl implements OfferListFragmentPresenter {
    private OfferListFragmentView view;
    private EventBus eventBus;
    private OfferListFragmentInteractor interactor;

    public OfferListFragmentPresenterImpl(OfferListFragmentView view, EventBus eventBus, OfferListFragmentInteractor interactor) {
        this.view = view;
        this.eventBus = eventBus;
        this.interactor = interactor;
    }

    @Override
    public void onCreate() {
        eventBus.register(this);
//        if (view != null)
//            view.showProgressDialog();
    }

    @Override
    public void onDestroy() {
        eventBus.unregister(this);
        view = null;
    }

    @Override
    public OfferListFragmentView getView() {
        return this.view;
    }

    @Override
    public void getOffers(Context context) {
        interactor.getOffers(context);
    }

    @Override
    public void switchOffer(Context context, Offer offer, int position) {
        interactor.switchOffer(context, offer, position);
    }

    @Override
    public void getCity(Context context) {
        interactor.getCity(context);
    }

    @Override
    public void validateDateShop(Context context) {
        interactor.validateDateShop(context);
    }

    @Override
    @Subscribe
    public void onEventMainThread(OfferListFragmentEvent event) {
        if (this.view != null) {
            switch (event.getType()) {
                case OfferListFragmentEvent.GETOFFER:
                    view.setOffers(event.getOffers());
                    view.hidePorgressDialog();
                    break;
                case OfferListFragmentEvent.SWITCHOFFER:
                    view.hidePorgressDialog();
                    view.switchOffer(event.getOffer(), event.getPosition());
                    break;
                case OfferListFragmentEvent.ERROR:
                    view.hidePorgressDialog();
                    view.error(event.getError());
                    break;
                case OfferListFragmentEvent.CITY:
                    view.setCity(event.getCity());
                    break;
                case OfferListFragmentEvent.UPDATESUCCESS:
                    view.refreshAdapter();
                    break;
                case OfferListFragmentEvent.WITHOUTCHANGE:
                    view.withoutChange();
                    break;
            }
        }
    }
}
