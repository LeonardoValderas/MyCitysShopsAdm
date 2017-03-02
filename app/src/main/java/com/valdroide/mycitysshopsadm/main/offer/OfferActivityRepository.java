package com.valdroide.mycitysshopsadm.main.offer;

import android.content.Context;

import com.valdroide.mycitysshopsadm.entities.Offer;

public interface OfferActivityRepository {
    void getOffer();
    void saveOffer(Context context, Offer offer);
    void updateOffer(Context context, Offer offer);
    void deleteOffer(Context context, Offer offer);
}
