package com.valdroide.mycitysshopsadm.main.offer;

import android.content.Context;

import com.valdroide.mycitysshopsadm.entities.Account;
import com.valdroide.mycitysshopsadm.entities.Offer;
import com.valdroide.mycitysshopsadm.main.account.events.AccountActivityEvent;
import com.valdroide.mycitysshopsadm.main.account.ui.AccountActivityView;
import com.valdroide.mycitysshopsadm.main.offer.events.OfferActivityEvent;
import com.valdroide.mycitysshopsadm.main.offer.ui.OfferActivityView;

/**
 * Created by LEO on 20/2/2017.
 */

public interface OfferActivityPresenter {
    void onCreate();
    void onDestroy();
    OfferActivityView getView();
    void getOffer();
    void saveOffer(Context context, Offer offer);
    void updateOffer(Context context, Offer offer);
    void deleteOffer(Context context, Offer offer);
    void onEventMainThread(OfferActivityEvent event);
}
