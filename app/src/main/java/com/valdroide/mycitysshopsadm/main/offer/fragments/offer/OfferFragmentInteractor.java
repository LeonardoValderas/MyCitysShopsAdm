package com.valdroide.mycitysshopsadm.main.offer.fragments.offer;

import android.content.Context;

import com.valdroide.mycitysshopsadm.entities.shop.Offer;

public interface OfferFragmentInteractor {
    void getQuantity(Context context);
    void getOfferForId(Context context, int id_offer);
    void saveOffer(Context context, Offer offer);
    void updateOffer(Context context, Offer offer);
  //  void switchOffer(Context context, Offer offer);
    void getCity(Context context);
    void validateDateShop(Context context);
}
