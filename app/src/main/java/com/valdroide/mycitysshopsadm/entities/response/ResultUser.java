package com.valdroide.mycitysshopsadm.entities.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.valdroide.mycitysshopsadm.entities.shop.Account;
import com.valdroide.mycitysshopsadm.entities.shop.DateShop;
import com.valdroide.mycitysshopsadm.entities.shop.Notification;
import com.valdroide.mycitysshopsadm.entities.shop.Offer;

import java.util.List;

public class ResultUser {
    @SerializedName("responseWS")
    @Expose
    ResponseWS responseWS;
    @SerializedName("date_user")
    @Expose
    DateShop dateUser;
    @SerializedName("account")
    @Expose
    Account account;
    @SerializedName("offer")
    @Expose
    List<Offer> offers;
    @SerializedName("notification")
    @Expose
    Notification notification;

    public ResponseWS getResponseWS() {
        return responseWS;
    }

    public void setResponseWS(ResponseWS responseWS) {
        this.responseWS = responseWS;
    }

    public DateShop getDateUser() {
        return dateUser;
    }

    public void setDateUser(DateShop dateUser) {
        this.dateUser = dateUser;
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
}
