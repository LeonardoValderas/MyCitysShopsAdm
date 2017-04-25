package com.valdroide.mycitysshopsadm.main.offer;

import android.content.Context;
import com.valdroide.mycitysshopsadm.entities.shop.Offer;
import com.valdroide.mycitysshopsadm.main.offer.events.OfferActivityEvent;
import com.valdroide.mycitysshopsadm.main.offer.ui.OfferActivityView;

public interface OfferActivityPresenter {
    void onCreate();
    void onDestroy();
    OfferActivityView getView();
    void getOffer(Context context);
    void saveOffer(Context context, Offer offer);
    void updateOffer(Context context, Offer offer);
    void switchOffer(Context context, Offer offer);
    void onEventMainThread(OfferActivityEvent event);
}
