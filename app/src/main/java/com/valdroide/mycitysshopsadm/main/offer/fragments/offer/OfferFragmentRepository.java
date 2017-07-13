package com.valdroide.mycitysshopsadm.main.offer.fragments.offer;

import android.content.Context;

import com.valdroide.mycitysshopsadm.entities.shop.Offer;

public interface OfferFragmentRepository {
    void getOfferForId(Context context, int id_offer);
    void getQuantity(Context context);
    void saveOffer(Context context, Offer offer);
    void updateOffer(Context context, Offer offer);
   // void switchOffer(Context context, Offer offer);
    void getCity(Context context);
    void validateDateShop(Context context);
}
