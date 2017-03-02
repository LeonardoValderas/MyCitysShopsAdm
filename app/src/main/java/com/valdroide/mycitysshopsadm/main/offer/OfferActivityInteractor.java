package com.valdroide.mycitysshopsadm.main.offer;

import android.content.Context;

import com.valdroide.mycitysshopsadm.entities.Account;
import com.valdroide.mycitysshopsadm.entities.Offer;

/**
 * Created by LEO on 20/2/2017.
 */

public interface OfferActivityInteractor {
    void getOffer();
    void saveOffer(Context context, Offer offer);
    void updateOffer(Context context, Offer offer);
    void deleteOffer(Context context, Offer offer);
}
