package com.valdroide.mycitysshopsadm.main.offer.fragments.offer_list;

import android.content.Context;

import com.valdroide.mycitysshopsadm.entities.shop.Offer;
import com.valdroide.mycitysshopsadm.main.offer.fragments.offer.OfferFragmentInteractor;
import com.valdroide.mycitysshopsadm.main.offer.fragments.offer.OfferFragmentRepository;

public class OfferListFragmentInteractorImpl implements OfferListFragmentInteractor {
    OfferListFragmentRepository repository;

    public OfferListFragmentInteractorImpl(OfferListFragmentRepository repository) {
        this.repository = repository;
    }

    @Override
    public void getOffers(Context context) {
        repository.getOffers(context);
    }

    @Override
    public void switchOffer(Context context, Offer offer, int position) {
        repository.switchOffer(context, offer, position);
    }

    @Override
    public void getCity(Context context) {
        repository.getCity(context);
    }

    @Override
    public void validateDateShop(Context context) {
        repository.validateDateShop(context);
    }
}
