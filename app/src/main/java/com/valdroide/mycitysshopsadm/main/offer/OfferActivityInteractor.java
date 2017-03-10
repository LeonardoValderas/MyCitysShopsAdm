package com.valdroide.mycitysshopsadm.main.offer;

import android.content.Context;

import com.valdroide.mycitysshopsadm.entities.user.Offer;

/**
 * Created by LEO on 20/2/2017.
 */

public interface OfferActivityInteractor {
    void getOffer(Context context);
    void saveOffer(Context context, Offer offer);
    void updateOffer(Context context, Offer offer);
    void deleteOffer(Context context, Offer offer, boolean isDelete);
}
