package com.valdroide.mycitysshopsadm.main.offer.fragments.offer_list.ui;

import com.valdroide.mycitysshopsadm.entities.shop.Offer;

import java.util.List;

public interface OfferListFragmentView {
    void setOffers(List<Offer> offers);
    void withoutChange();
    void switchOffer(Offer offer, int position);
    void error(String msg);
    void showProgressDialog();
    void hidePorgressDialog();
    void setCity(String city);
    void refreshAdapter();
}
