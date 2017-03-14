package com.valdroide.mycitysshopsadm.entities.shop;

import com.google.gson.annotations.SerializedName;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;
import com.valdroide.mycitysshopsadm.db.ShopDatabase;

@Table(database = ShopDatabase.class)
public class Account extends BaseModel {

    @Column
    @PrimaryKey
    @SerializedName("id")
    public int ID_ACCOUNT_KEY;

    @Column
    @SerializedName("shop_name")
    public String SHOP_NAME;

    @Column
    @SerializedName("url_logo")
    public String URL_LOGO;

    @Column
    @SerializedName("name_logo")
    public String NAME_LOGO;

    @Column
    @SerializedName("description")
    public String DESCRIPTION;

    @Column
    @SerializedName("phone")
    public String PHONE;

    @Column
    @SerializedName("email")
    public String EMAIL;

    @Column
    @SerializedName("latitud")
    public String LATITUD;

    @Column
    @SerializedName("longitud")
    public String LONGITUD;

    @Column
    @SerializedName("adrress")
    public String ADDRESS;

    public String Encode;
    public String NAME_BEFORE;

    public Account() {
    }

    public Account(int ID_ACCOUNT_KEY, String SHOP_NAME, String URL_LOGO, String NAME_LOGO,
                   String NAME_BEFORE, String encode, String DESCRIPTION, String PHONE, String EMAIL, String LATITUD, String LONGITUD,
                   String ADDRESS) {

        this.ID_ACCOUNT_KEY = ID_ACCOUNT_KEY;
        this.SHOP_NAME = SHOP_NAME;
        this.URL_LOGO = URL_LOGO;
        this.NAME_LOGO = NAME_LOGO;
        this.DESCRIPTION = DESCRIPTION;
        this.PHONE = PHONE;
        this.EMAIL = EMAIL;
        this.LATITUD = LATITUD;
        this.LONGITUD = LONGITUD;
        this.ADDRESS = ADDRESS;
        Encode = encode;
        this.NAME_BEFORE = NAME_BEFORE;
    }

    public int getID_ACCOUNT_KEY() {
        return ID_ACCOUNT_KEY;
    }

    public void setID_ACCOUNT_KEY(int ID_ACCOUNT_KEY) {
        this.ID_ACCOUNT_KEY = ID_ACCOUNT_KEY;
    }

    public String getSHOP_NAME() {
        return SHOP_NAME;
    }

    public void setSHOP_NAME(String SHOP_NAME) {
        this.SHOP_NAME = SHOP_NAME;
    }

    public String getURL_LOGO() {
        return URL_LOGO;
    }

    public void setURL_LOGO(String URL_LOGO) {
        this.URL_LOGO = URL_LOGO;
    }

    public String getNAME_LOGO() {
        return NAME_LOGO;
    }

    public void setNAME_LOGO(String NAME_LOGO) {
        this.NAME_LOGO = NAME_LOGO;
    }

    public String getDESCRIPTION() {
        return DESCRIPTION;
    }

    public void setDESCRIPTION(String DESCRIPTION) {
        this.DESCRIPTION = DESCRIPTION;
    }

    public String getPHONE() {
        return PHONE;
    }

    public void setPHONE(String PHONE) {
        this.PHONE = PHONE;
    }

    public String getEMAIL() {
        return EMAIL;
    }

    public void setEMAIL(String EMAIL) {
        this.EMAIL = EMAIL;
    }

    public String getLATITUD() {
        return LATITUD;
    }

    public void setLATITUD(String LATITUD) {
        this.LATITUD = LATITUD;
    }

    public String getLONGITUD() {
        return LONGITUD;
    }

    public void setLONGITUD(String LONGITUD) {
        this.LONGITUD = LONGITUD;
    }

    public String getADDRESS() {
        return ADDRESS;
    }

    public void setADDRESS(String ADDRESS) {
        this.ADDRESS = ADDRESS;
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
}
