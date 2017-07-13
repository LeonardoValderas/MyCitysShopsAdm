package com.valdroide.mycitysshopsadm.main.offer.fragments.offer_list.events;

import com.valdroide.mycitysshopsadm.entities.shop.Offer;

import java.util.List;

public class OfferListFragmentEvent {
    private int type;
    public static final int GETOFFER = 0;
    public static final int SWITCHOFFER = 1;
    public static final int ERROR = 2;
    public static final int CITY = 3;
    public static final int UPDATESUCCESS = 4;
    public static final int WITHOUTCHANGE = 5;
    private String error;
    private String city;
    private Offer offer;
    private List<Offer> offers;
    private int position;
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

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
