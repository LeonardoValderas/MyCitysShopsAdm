package com.valdroide.mycitysshopsadm.main.offer;

import android.content.Context;

import com.valdroide.mycitysshopsadm.entities.Offer;
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
    public void getOffer() {
        interactor.getOffer();
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
    public void deleteOffer(Context context, Offer offer) {
        interactor.deleteOffer(context, offer);
    }

    @Override
    @Subscribe
    public void onEventMainThread(OfferActivityEvent event) {
        if (this.view != null) {
            switch (event.getType()) {
                case OfferActivityEvent.GETOFFER:
                    view.setOffers(event.getOffers());
                    break;
                case OfferActivityEvent.SAVEOFFER:
                    view.saveOffer(event.getOffer());
                    break;
                case OfferActivityEvent.UPDATEOFFER:
                    view.updateOffer(event.getOffer());
                    break;
                case OfferActivityEvent.DELETEOFFER:
                    view.deleteOffer(event.getOffer());
                    break;
                case OfferActivityEvent.ERROR:
                    view.error(event.getError());
            }
        }
    }
}
