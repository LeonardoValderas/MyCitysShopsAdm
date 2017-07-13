package com.valdroide.mycitysshopsadm.main.offer.fragments.offer;

import android.content.Context;
import com.valdroide.mycitysshopsadm.entities.shop.Offer;
import com.valdroide.mycitysshopsadm.main.offer.fragments.offer.events.OfferFragmentEvent;
import com.valdroide.mycitysshopsadm.main.offer.fragments.offer.ui.OfferFragmentView;

public interface OfferFragmentPresenter {
    void onCreate();
    void onDestroy();
    OfferFragmentView getView();
    void getOfferForId(Context context, int id_offer);
    void getQuantity(Context context);
    void saveOffer(Context context, Offer offer);
    void updateOffer(Context context, Offer offer);
  ///  void switchOffer(Context context, Offer offer);
    void getCity(Context context);
    void validateDateShop(Context context);
    void onEventMainThread(OfferFragmentEvent event);
}
