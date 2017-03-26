package com.valdroide.mycitysshopsadm.main.offer.events;

import com.valdroide.mycitysshopsadm.entities.shop.Offer;

import java.util.List;

public class OfferActivityEvent {
    private int type;
    public static final int GETOFFER = 0;
    public static final int SAVEOFFER = 1;
    public static final int UPDATEOFFER = 2;
    public static final int DELETEOFFER = 3;
    public static final int SWITCHOFFER = 4;
    public static final int ERROR = 5;
    private String error;
    private Offer offer;
    private List<Offer> offers;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public Offer getOffer() {
        return offer;
    }

    public void setOffer(Offer offer) {
        this.offer = offer;
    }

    public List<Offer> getOffers() {
        return offers;
    }

    public void setOffers(List<Offer> offers) {
        this.offers = offers;
    }
}
