package com.valdroide.mycitysshopsadm.main.offer;

import android.content.Context;

import com.valdroide.mycitysshopsadm.entities.shop.Offer;
import com.valdroide.mycitysshopsadm.lib.base.EventBus;
import com.valdroide.mycitysshopsadm.main.offer.events.OfferActivityEvent;
import com.valdroide.mycitysshopsadm.main.offer.ui.OfferActivityView;

import org.greenrobot.eventbus.Subscribe;

public class OfferActivityPresenterImpl implements OfferActivityPresenter {
    private OfferActivityView view;
    private EventBus eventBus;
    private OfferActivityInteractor interactor;

    public OfferActivityPresenterImpl(OfferActivityView view, EventBus eventBus, OfferActivityInteractor interactor) {
        this.view = view;
        this.eventBus = eventBus;
        this.interactor = interactor;
    }

    @Override
    public void onCreate() {
        eventBus.register(this);
        if (view != null)
            view.showProgressDialog();
    }

    @Override
    public void onDestroy() {
        eventBus.unregister(this);
        view = null;
    }

    @Override
    public OfferActivityView getView() {
        return this.view;
    }

    @Override
    public void getOffer(Context context) {
        interactor.getOffer(context);
    }

    @Override
    public void saveOffer(Context context, Offer offer) {
        interactor.saveOffer(context, offer);
    }

    @Override
    public void updateOffer(Context context, Offer offer) {
        interactor.updateOffer(context, offer);
    }

    @Override
    public void switchOffer(Context context, Offer offer) {
        interactor.switchOffer(context, offer);
    }

    @Override
    public void getCity(Context context) {
        interactor.getCity(context);
    }

    @Override
    @Subscribe
    public void onEventMainThread(OfferActivityEvent event) {
        if (this.view != null) {
            switch (event.getType()) {
                case OfferActivityEvent.GETOFFER:
                    view.setOffers(event.getOffers(), event.getMax_offer());
                    view.hidePorgressDialog();
                    break;
                case OfferActivityEvent.SAVEOFFER:
                    view.hidePorgressDialog();
                    view.saveOffer(event.getOffer());
                    break;
                case OfferActivityEvent.UPDATEOFFER:
                    view.hidePorgressDialog();
                    view.updateOffer(event.getOffer());
                    break;
                case OfferActivityEvent.SWITCHOFFER:
                    view.hidePorgressDialog();
                    view.switchOffer(event.getOffer());
                    break;
                case OfferActivityEvent.ERROR:
                    view.hidePorgressDialog();
                    view.error(event.getError());
            }
        }
    }
}
