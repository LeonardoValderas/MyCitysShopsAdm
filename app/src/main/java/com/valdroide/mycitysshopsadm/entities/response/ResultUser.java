package com.valdroide.mycitysshopsadm.entities.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.valdroide.mycitysshopsadm.entities.shop.Account;
import com.valdroide.mycitysshopsadm.entities.shop.DateShop;
import com.valdroide.mycitysshopsadm.entities.shop.Draw;
import com.valdroide.mycitysshopsadm.entities.shop.Notification;
import com.valdroide.mycitysshopsadm.entities.shop.Offer;
import com.valdroide.mycitysshopsadm.entities.shop.Shop;
import com.valdroide.mycitysshopsadm.entities.shop.Support;

import java.util.List;

public class ResultUser {
    @SerializedName("responseWS")
    @Expose
    ResponseWS responseWS;
    @SerializedName("date_shop")
    @Expose
    DateShop dateShop;
    @SerializedName("account")
    @Expose
    Account account;
    @SerializedName("shop")
    @Expose
    Shop shop;
    @SerializedName("offer")
    @Expose
    List<Offer> offers;
    @SerializedName("notification")
    @Expose
    Notification notification;
    @SerializedName("support")
    @Expose
    Support support;
    @SerializedName("draw")
    @Expose
    List<Draw> draws;
    public ResponseWS getResponseWS() {
        return responseWS;
    }

    public void setResponseWS(ResponseWS responseWS) {
        this.responseWS = responseWS;
    }

    public DateShop getDateShop() {
        return dateShop;
    }

    public void setDateShop(DateShop dateShop) {
        this.dateShop = dateShop;
    }

    public Account getAccount() {
        return account;
    }
    public void setAccount(Account account) {
        this.account = account;
    }
    public List<Offer> getOffers() {
        return offers;
    }
    public void setOffer(List<Offer> offers) {
        this.offers = offers;
    }

    public Notification getNotification() {
        return notification;
    }

    public void setNotification(Notification notification) {
        this.notification = notification;
    }

    public Support getSupport() {
        return support;
    }

    public void setSupport(Support support) {
        this.support = support;
    }

    public List<Draw> getDraws() {
        return draws;
    }

    public void setDraws(List<Draw> draws) {
        this.draws = draws;
    }

    public Shop getShop() {
        return shop;
    }

    public void setShop(Shop shop) {
        this.shop = shop;
    }
}
