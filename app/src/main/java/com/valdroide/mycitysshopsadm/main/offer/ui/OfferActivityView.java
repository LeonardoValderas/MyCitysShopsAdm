package com.valdroide.mycitysshopsadm.main.offer.ui;

import com.valdroide.mycitysshopsadm.entities.Offer;
import java.util.List;

public interface OfferActivityView {
    void setOffers(List<Offer> offers);
    void saveOffer(Offer offer);
    void updateOffer(Offer offer);
    void deleteOffer(Offer offer);
    void error(String msg);
}
