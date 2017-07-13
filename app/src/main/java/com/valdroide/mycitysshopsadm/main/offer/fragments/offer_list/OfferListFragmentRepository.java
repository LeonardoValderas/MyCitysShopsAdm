package com.valdroide.mycitysshopsadm.main.offer.fragments.offer_list;

import android.content.Context;

import com.valdroide.mycitysshopsadm.entities.shop.Offer;

public interface OfferListFragmentRepository {
    void getOffers(Context context);
    void switchOffer(Context context, Offer offer, int position);
    void getCity(Context context);
    void validateDateShop(Context context);
}
