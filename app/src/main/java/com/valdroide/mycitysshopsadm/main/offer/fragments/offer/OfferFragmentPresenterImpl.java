package com.valdroide.mycitysshopsadm.main.offer.fragments.offer;

import android.content.Context;

import com.valdroide.mycitysshopsadm.entities.shop.Offer;
import com.valdroide.mycitysshopsadm.lib.base.EventBus;
import com.valdroide.mycitysshopsadm.main.offer.fragments.offer.events.OfferFragmentEvent;
import com.valdroide.mycitysshopsadm.main.offer.fragments.offer.ui.OfferFragmentView;

import org.greenrobot.eventbus.Subscribe;

public class OfferFragmentPresenterImpl implements OfferFragmentPresenter {
    private OfferFragmentView view;
    private EventBus eventBus;
    private OfferFragmentInteractor interactor;

    public OfferFragmentPresenterImpl(OfferFragmentView view, EventBus eventBus, OfferFragmentInteractor interactor) {
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
    public OfferFragmentView getView() {
        return this.view;
    }

    @Override
    public void getOfferForId(Context context, int id_offer) {
        interactor.getOfferForId(context, id_offer);
    }

    @Override
    public void getQuantity(Context context) {
        interactor.getQuantity(context);
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
    public void getCity(Context context) {
        interactor.getCity(context);
    }

    @Override
    public void validateDateShop(Context context) {
        interactor.validateDateShop(context);
    }

    @Override
    @Subscribe
    public void onEventMainThread(OfferFragmentEvent event) {
        if (this.view != null) {
            switch (event.getType()) {
                case OfferFragmentEvent.GETOFFERFORID:
                    view.setOfferForId(event.getOffer());
                    view.hidePorgressDialog();
                    break;
                case OfferFragmentEvent.SAVEOFFER:
                    view.hidePorgressDialog();
                    view.saveOffer();
                    break;
                case OfferFragmentEvent.UPDATEOFFERSUCCESS:
                    view.hidePorgressDialog();
                    view.updateOffer();
                    break;
                case OfferFragmentEvent.GETQUANTITY:
                    view.hidePorgressDialog();
                    view.setQuantity(event.getMax_offer());
                    break;
                case OfferFragmentEvent.ERROR:
                    view.hidePorgressDialog();
                    view.error(event.getError());
                    break;
                case OfferFragmentEvent.CITY:
                    view.setCity(event.getCity());
                    break;
                case OfferFragmentEvent.UPDATEDATESUCCESS:
                    view.validateSuccess();
                    break;
            }
        }
    }
}
