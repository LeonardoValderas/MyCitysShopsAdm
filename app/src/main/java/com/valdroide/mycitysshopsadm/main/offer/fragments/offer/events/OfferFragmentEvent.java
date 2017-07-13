package com.valdroide.mycitysshopsadm.main.offer.fragments.offer.events;

import com.valdroide.mycitysshopsadm.entities.shop.Offer;

import java.util.List;

public class OfferFragmentEvent {
    private int type;

    public static final int GETOFFERFORID = 0;
    public static final int SAVEOFFER = 1;
    public static final int UPDATEOFFERSUCCESS = 2;
    public static final int ERROR = 3;
    public static final int CITY = 4;
    public static final int UPDATEDATESUCCESS = 5;
    public static final int GETQUANTITY = 6;
    private String error;
    private String city;
    private Offer offer;
    private int max_offer;

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

//    public List<Offer> getOffers() {
//        return offers;
//    }
//
//    public void setOffers(List<Offer> offers) {
//        this.offers = offers;
//    }

    public int getMax_offer() {
        return max_offer;
    }

    public void setMax_offer(int max_offer) {
        this.max_offer = max_offer;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
