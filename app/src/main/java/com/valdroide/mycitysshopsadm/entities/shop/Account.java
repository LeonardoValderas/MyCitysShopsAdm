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
    @SerializedName("working_hours")
    public String WORKING_HOURS;

    @Column
    @SerializedName("phone")
    public String PHONE;

    @Column
    @SerializedName("email")
    public String EMAIL;

    @Column
    @SerializedName("web")
    public String WEB;

    @Column
    @SerializedName("whatsapp")
    public String WHATSAAP;

    @Column
    @SerializedName("facebook")
    public String FACEBOOK;

    @Column
    @SerializedName("instagram")
    public String INSTAGRAM;

    @Column
    @SerializedName("twitter")
    public String TWITTER;

    @Column
    @SerializedName("snapchat")
    public String SNAPCHAT;

    @Column
    @SerializedName("latitud")
    public String LATITUD;

    @Column
    @SerializedName("longitud")
    public String LONGITUD;

    @Column
    @SerializedName("adrress")
    public String ADDRESS;

    @Column
    @SerializedName("follow")
    public int FOLLOW;

    @Column
    @SerializedName("date_unique")
    public String DATE_UNIQUE;

    public String Encode;

    public String NAME_BEFORE;

    public Account() {
    }

    public Account(int ID_ACCOUNT_KEY, String SHOP_NAME, String URL_LOGO, String NAME_LOGO, String NAME_BEFORE,
                    String encode, String DESCRIPTION, String WORKING_HOURS, String PHONE, String EMAIL, String WEB,
                    String WHATSAAP, String FACEBOOK, String INSTAGRAM, String TWITTER, String SNAPCHAT, String LATITUD,
                    String LONGITUD, String ADDRESS, int FOLLOW, String DATE_UNIQUE) {

        this.ID_ACCOUNT_KEY = ID_ACCOUNT_KEY;
        this.SHOP_NAME = SHOP_NAME;
        this.URL_LOGO = URL_LOGO;
        this.NAME_LOGO = NAME_LOGO;
        this.DESCRIPTION = DESCRIPTION;
        this.WORKING_HOURS = WORKING_HOURS;
        this.PHONE = PHONE;
        this.EMAIL = EMAIL;
        this.WEB = WEB;
        this.WHATSAAP = WHATSAAP;
        this.FACEBOOK = FACEBOOK;
        this.INSTAGRAM = INSTAGRAM;
        this.TWITTER = TWITTER;
        this.SNAPCHAT = SNAPCHAT;
        this.LATITUD = LATITUD;
        this.LONGITUD = LONGITUD;
        this.ADDRESS = ADDRESS;
        this.Encode = encode;
        this.NAME_BEFORE = NAME_BEFORE;
        this.FOLLOW = FOLLOW;
        this.DATE_UNIQUE = DATE_UNIQUE;
    }
    public Account(int ID_ACCOUNT_KEY, String SHOP_NAME, String URL_LOGO, String NAME_LOGO, String NAME_BEFORE,
                   String encode, String DESCRIPTION, String WORKING_HOURS, String PHONE, String EMAIL, String WEB,
                   String WHATSAAP, String FACEBOOK, String INSTAGRAM, String TWITTER, String SNAPCHAT, String LATITUD,
                   String LONGITUD, String ADDRESS, String DATE_UNIQUE) {

        this.ID_ACCOUNT_KEY = ID_ACCOUNT_KEY;
        this.SHOP_NAME = SHOP_NAME;
        this.URL_LOGO = URL_LOGO;
        this.NAME_LOGO = NAME_LOGO;
        this.DESCRIPTION = DESCRIPTION;
        this.WORKING_HOURS = WORKING_HOURS;
        this.PHONE = PHONE;
        this.EMAIL = EMAIL;
        this.WEB = WEB;
        this.WHATSAAP = WHATSAAP;
        this.FACEBOOK = FACEBOOK;
        this.INSTAGRAM = INSTAGRAM;
        this.TWITTER = TWITTER;
        this.SNAPCHAT = SNAPCHAT;
        this.LATITUD = LATITUD;
        this.LONGITUD = LONGITUD;
        this.ADDRESS = ADDRESS;
        this.Encode = encode;
        this.NAME_BEFORE = NAME_BEFORE;
        this.DATE_UNIQUE = DATE_UNIQUE;
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

    public int getFOLLOW() {
        return FOLLOW;
    }

    public void setFOLLOW(int FOLLOW) {
        this.FOLLOW = FOLLOW;
    }

    public String getWORKING_HOURS() {
        return WORKING_HOURS;
    }

    public void setWORKING_HOURS(String WORKING_HOURS) {
        this.WORKING_HOURS = WORKING_HOURS;
    }

    public String getWEB() {
        return WEB;
    }

    public void setWEB(String WEB) {
        this.WEB = WEB;
    }

    public String getWHATSAAP() {
        return WHATSAAP;
    }

    public void setWHATSAAP(String WHATSAAP) {
        this.WHATSAAP = WHATSAAP;
    }

    public String getINSTAGRAM() {
        return INSTAGRAM;
    }

    public void setINSTAGRAM(String INSTAGRAM) {
        this.INSTAGRAM = INSTAGRAM;
    }

    public String getTWITTER() {
        return TWITTER;
    }

    public void setTWITTER(String TWITTER) {
        this.TWITTER = TWITTER;
    }

    public String getSNAPCHAT() {
        return SNAPCHAT;
    }

    public void setSNAPCHAT(String SNAPCHAT) {
        this.SNAPCHAT = SNAPCHAT;
    }

    public String getDATE_UNIQUE() {
        return DATE_UNIQUE;
    }

    public void setDATE_UNIQUE(String DATE_UNIQUE) {
        this.DATE_UNIQUE = DATE_UNIQUE;
    }

    public String getFACEBOOK() {
        return FACEBOOK;
    }

    public void setFACEBOOK(String FACEBOOK) {
        this.FACEBOOK = FACEBOOK;
    }
}
