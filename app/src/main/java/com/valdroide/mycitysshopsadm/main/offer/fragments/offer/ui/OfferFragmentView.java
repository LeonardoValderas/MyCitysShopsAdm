package com.valdroide.mycitysshopsadm.main.offer.fragments.offer.ui;

import com.valdroide.mycitysshopsadm.entities.shop.Offer;
import java.util.List;

public interface OfferFragmentView {
    void setQuantity(int diff);
    void saveOffer();
    void updateOffer();
    void error(String msg);
    void showProgressDialog();
    void hidePorgressDialog();
    void setCity(String city);
    void setOfferForId(Offer offer);
    void validateSuccess();
}
