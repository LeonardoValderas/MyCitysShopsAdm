package com.valdroide.mycitysshopsadm.main.offer;

import android.content.Context;
import com.valdroide.mycitysshopsadm.entities.shop.Offer;

public class OfferActivityInteractorImpl implements OfferActivityInteractor {
    OfferActivityRepository repository;

    public OfferActivityInteractorImpl(OfferActivityRepository repository) {
        this.repository = repository;
    }

    @Override
    public void getOffer(Context context) {
        repository.getOffer(context);
    }

    @Override
    public void saveOffer(Context context, Offer offer) {
        repository.saveOffer(context, offer);
    }

    @Override
    public void updateOffer(Context context, Offer offer) {
        repository.updateOffer(context, offer);
    }

    @Override
    public void switchOffer(Context context, Offer offer) {
        repository.switchOffer(context, offer);
    }
}
