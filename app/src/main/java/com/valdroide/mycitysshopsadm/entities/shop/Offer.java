package com.valdroide.mycitysshopsadm.entities.shop;

import com.google.gson.annotations.SerializedName;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;
import com.valdroide.mycitysshopsadm.db.ShopDatabase;

@Table(database = ShopDatabase.class)
public class Offer extends BaseModel {

    @Column
    @PrimaryKey
    @SerializedName("id")
    public int ID_OFFER_KEY;

    @Column
    @SerializedName("id_shop_foreign")
    public int ID_SHOP_FOREIGN;
    @Column
    @SerializedName("id_city_foreign")
    public int ID_CITY_FOREIGN;
    @Column
    @SerializedName("title")
    public String TITLE;

    @Column
    @SerializedName("offer")
    public String OFFER;

    @Column
    @SerializedName("url_image")
    public String URL_IMAGE;
    @Column
    @SerializedName("name_image")
    public String NAME_IMAGE;

    @Column
    @SerializedName("date_unique")
    public String DATE_UNIQUE;

    @Column
    @SerializedName("is_active")
    public int IS_ACTIVE;

    public String Encode;

    public String NAME_BEFORE;

    public Offer() {
    }

    public Offer(int ID_OFFER_KEY, int ID_SHOP_FOREIGN, int ID_CITY_FOREIGN, String TITLE, String OFFER, String URL_IMAGE,
                 String NAME_IMAGE, int IS_ACTIVE, String DATE_UNIQUE, String Encode, String NAME_BEFORE) {
        this.ID_OFFER_KEY = ID_OFFER_KEY;
        this.ID_SHOP_FOREIGN = ID_SHOP_FOREIGN;
        this.ID_CITY_FOREIGN = ID_CITY_FOREIGN;
        this.TITLE = TITLE;
        this.OFFER = OFFER;
        this.URL_IMAGE = URL_IMAGE;
        this.NAME_IMAGE = NAME_IMAGE;
        this.IS_ACTIVE = IS_ACTIVE;
        this.DATE_UNIQUE = DATE_UNIQUE;
        this.Encode = Encode;
        this.NAME_BEFORE = NAME_BEFORE;
    }

    public int getID_OFFER_KEY() {
        return ID_OFFER_KEY;
    }

    public void setID_OFFER_KEY(int ID_OFFER_KEY) {
        this.ID_OFFER_KEY = ID_OFFER_KEY;
    }

    public String getTITLE() {
        return TITLE;
    }

    public void setTITLE(String TITLE) {
        this.TITLE = TITLE;
    }

    public String getOFFER() {
        return OFFER;
    }

    public void setOFFER(String OFFER) {
        this.OFFER = OFFER;
    }

    public int getID_SHOP_FOREIGN() {
        return ID_SHOP_FOREIGN;
    }

    public void setID_SHOP_FOREIGN(int ID_SHOP_FOREIGN) {
        this.ID_SHOP_FOREIGN = ID_SHOP_FOREIGN;
    }

    public String getURL_IMAGE() {
        return URL_IMAGE;
    }

    public void setURL_IMAGE(String URL_IMAGE) {
        this.URL_IMAGE = URL_IMAGE;
    }

    public String getNAME_IMAGE() {
        return NAME_IMAGE;
    }

    public void setNAME_IMAGE(String NAME_IMAGE) {
        this.NAME_IMAGE = NAME_IMAGE;
    }

    public String getDATE_UNIQUE() {
        return DATE_UNIQUE;
    }

    public void setDATE_UNIQUE(String DATE_UNIQUE) {
        this.DATE_UNIQUE = DATE_UNIQUE;
    }

    public String getEncode() {
        return Encode;
    }

    public void setEncode(String encode) {
        Encode = encode;
    }

    public String getNAME_BEFORE() {
        return NAME_BEFORE;
    }

    public void setNAME_BEFORE(String NAME_BEFORE) {
        this.NAME_BEFORE = NAME_BEFORE;
    }

    public int getIS_ACTIVE() {
        return IS_ACTIVE;
    }

    public void setIS_ACTIVE(int IS_ACTIVE) {
        this.IS_ACTIVE = IS_ACTIVE;
    }

    public int getID_CITY_FOREIGN() {
        return ID_CITY_FOREIGN;
    }

    public void setID_CITY_FOREIGN(int ID_CITY_FOREIGN) {
        this.ID_CITY_FOREIGN = ID_CITY_FOREIGN;
    }
}
