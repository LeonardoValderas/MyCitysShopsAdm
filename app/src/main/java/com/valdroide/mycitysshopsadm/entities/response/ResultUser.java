package com.valdroide.mycitysshopsadm.entities.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.valdroide.mycitysshopsadm.entities.place.City;
import com.valdroide.mycitysshopsadm.entities.place.Country;
import com.valdroide.mycitysshopsadm.entities.place.DatePlace;
import com.valdroide.mycitysshopsadm.entities.place.State;
import com.valdroide.mycitysshopsadm.entities.user.Account;
import com.valdroide.mycitysshopsadm.entities.user.DateUser;
import com.valdroide.mycitysshopsadm.entities.user.Offer;

import java.util.List;

/**
 * Created by LEO on 6/2/2017.
 */

public class ResultUser {
    @SerializedName("responseWS")
    @Expose
    ResponseWS responseWS;
    @SerializedName("date_user")
    @Expose
    DateUser dateUser;
    @SerializedName("account")
    @Expose
    Account account;
    @SerializedName("offer")
    @Expose
    List<Offer> offers;

    public ResponseWS getResponseWS() {
        return responseWS;
    }

    public void setResponseWS(ResponseWS responseWS) {
        this.responseWS = responseWS;
    }

    public DateUser getDateUser() {
        return dateUser;
    }

    public void setDateUser(DateUser dateUser) {
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
}
