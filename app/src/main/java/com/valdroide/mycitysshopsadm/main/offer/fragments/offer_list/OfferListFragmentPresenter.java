package com.valdroide.mycitysshopsadm.main.offer.fragments.offer_list;

import android.content.Context;

import com.valdroide.mycitysshopsadm.entities.shop.Offer;
import com.valdroide.mycitysshopsadm.main.offer.fragments.offer.events.OfferFragmentEvent;
import com.valdroide.mycitysshopsadm.main.offer.fragments.offer.ui.OfferFragmentView;
import com.valdroide.mycitysshopsadm.main.offer.fragments.offer_list.events.OfferListFragmentEvent;
import com.valdroide.mycitysshopsadm.main.offer.fragments.offer_list.ui.OfferListFragmentView;

public interface OfferListFragmentPresenter {
    void onCreate();
    void onDestroy();
    OfferListFragmentView getView();
    void getOffers(Context context);
    void switchOffer(Context context, Offer offer, int position);
    void getCity(Context context);
    void validateDateShop(Context context);
    void onEventMainThread(OfferListFragmentEvent event);
}
