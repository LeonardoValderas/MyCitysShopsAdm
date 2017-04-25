package com.valdroide.mycitysshopsadm.main.offer.ui;

import com.valdroide.mycitysshopsadm.entities.shop.Offer;
import java.util.List;

public interface OfferActivityView {
    void setOffers(List<Offer> offers, int max);
    void saveOffer(Offer offer);
    void updateOffer(Offer offer);
    void switchOffer(Offer offer);
    void error(String msg);
}
