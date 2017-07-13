package com.valdroide.mycitysshopsadm.main.offer.fragments.offer;

import android.content.Context;
import com.valdroide.mycitysshopsadm.entities.shop.Offer;

public class OfferFragmentInteractorImpl implements OfferFragmentInteractor {
    OfferFragmentRepository repository;

    public OfferFragmentInteractorImpl(OfferFragmentRepository repository) {
        this.repository = repository;
    }

//    @Override
//    public void getOffer(Context context) {
//        repository.getOffer(context);
//    }

    @Override
    public void getQuantity(Context context) {
        repository.getQuantity(context);
    }

    @Override
    public void getOfferForId(Context context, int id_offer) {
        repository.getOfferForId(context, id_offer);
    }

    @Override
    public void saveOffer(Context context, Offer offer) {
        repository.saveOffer(context, offer);
    }

    @Override
    public void updateOffer(Context context, Offer offer) {
        repository.updateOffer(context, offer);
    }

//    @Override
//    public void switchOffer(Context context, Offer offer) {
//        repository.switchOffer(context, offer);
//    }

    @Override
    public void getCity(Context context) {
        repository.getCity(context);
    }

    @Override
    public void validateDateShop(Context context) {
        repository.validateDateShop(context);
    }
}
