package com.valdroide.mycitysshopsadm.entities.shop;

import com.google.gson.annotations.SerializedName;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;
import com.valdroide.mycitysshopsadm.db.ShopDatabase;

@Table(database = ShopDatabase.class)
public class Draw extends BaseModel {

    @Column
    @PrimaryKey
    @SerializedName("id")
    private int ID_DRAW_KEY;

    @Column
    @SerializedName("id_shop_foreign")
    private int ID_SHOP_FOREIGN;
    @Column
    @SerializedName("id_city_foreign")
    private int ID_CITY_FOREIGN;

    @Column
    @SerializedName("description")
    private String DESCRIPTION;

    @Column
    @SerializedName("for_following")
    private int FOR_FOLLOWING;

    @Column
    @SerializedName("condition")
    private String CONDITION;

    @Column
    @SerializedName("start_date")
    private String START_DATE;

    @Column
    @SerializedName("end_date")
    private String END_DATE;

    @Column
    @SerializedName("limit_date")
    private String LIMIT_DATE;

    @Column
    @SerializedName("url_logo")
    private String URL_LOGO;

    @Column
    @SerializedName("name_logo")
    private String NAME_LOGO;

    @Column
    @SerializedName("date_unique")
    private String DATE_UNIQUE;
//    @Column
//    @SerializedName("is_active")
    private int IS_ACTIVE;
//    @Column
//    @SerializedName("is_cancel")
    private int IS_CANCEL;
//    @Column
//    @SerializedName("is_limit")
    private int IS_LIMIT;
//    @Column
//    @SerializedName("is_take")
    private int IS_TAKE;

    @Column
    @SerializedName("name")
    private String NAME;

    @Column
    @SerializedName("dni")
    private String DNI;

    private String Encode;

    public Draw() {
    }

    public Draw(int ID_DRAW_KEY, int ID_SHOP_FOREIGN, int ID_CITY_FOREIGN, String DESCRIPTION,
                int FOR_FOLLOWING, String CONDITION, String START_DATE, String END_DATE, String LIMIT_DATE,
                String URL_LOGO, String NAME_LOGO, String DATE_UNIQUE, int IS_ACTIVE, int IS_CANCEL, int IS_LIMIT,
                int IS_TAKE, String encode, String NAME, String DNI) {

        this.ID_DRAW_KEY = ID_DRAW_KEY;
        this.ID_SHOP_FOREIGN = ID_SHOP_FOREIGN;
        this.ID_CITY_FOREIGN = ID_CITY_FOREIGN;
        this.DESCRIPTION = DESCRIPTION;
        this.FOR_FOLLOWING = FOR_FOLLOWING;
        this.CONDITION = CONDITION;
        this.START_DATE = START_DATE;
        this.END_DATE = END_DATE;
        this.LIMIT_DATE = LIMIT_DATE;
        this.URL_LOGO = URL_LOGO;
        this.NAME_LOGO = NAME_LOGO;
        this.DATE_UNIQUE = DATE_UNIQUE;
        this.IS_ACTIVE = IS_ACTIVE;
        this.IS_CANCEL = IS_CANCEL;
        this.IS_LIMIT = IS_LIMIT;
        this.IS_TAKE = IS_TAKE;
        Encode = encode;
        this.NAME = NAME;
        this.DNI = DNI;
    }

    public int getID_DRAW_KEY() {
        return ID_DRAW_KEY;
    }

    public void setID_DRAW_KEY(int ID_DRAW_KEY) {
        this.ID_DRAW_KEY = ID_DRAW_KEY;
    }

    public String getDESCRIPTION() {
        return DESCRIPTION;
    }

    public void setDESCRIPTION(String DESCRIPTION) {
        this.DESCRIPTION = DESCRIPTION;
    }

    public int getFOR_FOLLOWING() {
        return FOR_FOLLOWING;
    }

    public void setFOR_FOLLOWING(int FOR_FOLLOWING) {
        this.FOR_FOLLOWING = FOR_FOLLOWING;
    }

    public String getCONDITION() {
        return CONDITION;
    }

    public void setCONDITION(String CONDITION) {
        this.CONDITION = CONDITION;
    }

    public String getSTART_DATE() {
        return START_DATE;
    }

    public void setSTART_DATE(String START_DATE) {
        this.START_DATE = START_DATE;
    }

    public String getEND_DATE() {
        return END_DATE;
    }

    public void setEND_DATE(String END_DATE) {
        this.END_DATE = END_DATE;
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

    public int getID_SHOP_FOREIGN() {
        return ID_SHOP_FOREIGN;
    }

    public void setID_SHOP_FOREIGN(int ID_SHOP_FOREIGN) {
        this.ID_SHOP_FOREIGN = ID_SHOP_FOREIGN;
    }

    public int getID_CITY_FOREIGN() {
        return ID_CITY_FOREIGN;
    }

    public void setID_CITY_FOREIGN(int ID_CITY_FOREIGN) {
        this.ID_CITY_FOREIGN = ID_CITY_FOREIGN;
    }

    public String getLIMIT_DATE() {
        return LIMIT_DATE;
    }

    public void setLIMIT_DATE(String LIMIT_DATE) {
        this.LIMIT_DATE = LIMIT_DATE;
    }

    public int getIS_CANCEL() {
        return IS_CANCEL;
    }

    public void setIS_CANCEL(int IS_CANCEL) {
        this.IS_CANCEL = IS_CANCEL;
    }

    public int getIS_LIMIT() {
        return IS_LIMIT;
    }

    public void setIS_LIMIT(int IS_LIMIT) {
        this.IS_LIMIT = IS_LIMIT;
    }

    public int getIS_TAKE() {
        return IS_TAKE;
    }

    public void setIS_TAKE(int IS_TAKE) {
        this.IS_TAKE = IS_TAKE;
    }

    public int getIS_ACTIVE() {
        return IS_ACTIVE;
    }

    public void setIS_ACTIVE(int IS_ACTIVE) {
        this.IS_ACTIVE = IS_ACTIVE;
    }

    public String getNAME() {
        return NAME;
    }

    public void setNAME(String NAME) {
        this.NAME = NAME;
    }

    public String getDNI() {
        return DNI;
    }

    public void setDNI(String DNI) {
        this.DNI = DNI;
    }

    public String getWinner() {
        if (!getDNI().isEmpty() && !getNAME().isEmpty())
            return "GANADOR: " + getNAME() + " DNI: " + getDNI();
        else
            return "";
    }
}