package com.valdroide.mycitysshopsadm.entities.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.valdroide.mycitysshopsadm.entities.shop.Shop;

public class ResultShop {
    @SerializedName("responseWS")
    @Expose
    ResponseWS responseWS;
    @SerializedName("shop")
    @Expose
    Shop shop;

    public ResponseWS getResponseWS() {
        return responseWS;
    }

    public void setResponseWS(ResponseWS responseWS) {
        this.responseWS = responseWS;
    }

    public Shop getShop() {
        return shop;
    }

    public void setShop(Shop shop) {
        this.shop = shop;
    }
}
