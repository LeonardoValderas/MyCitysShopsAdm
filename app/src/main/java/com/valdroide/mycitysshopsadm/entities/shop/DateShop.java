package com.valdroide.mycitysshopsadm.entities.shop;

import com.google.gson.annotations.SerializedName;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;
import com.valdroide.mycitysshopsadm.db.ShopDatabase;

@Table(database = ShopDatabase.class)
public class DateShop extends BaseModel {

    // @PrimaryKey(autoincrement = true)
    @Column
    @PrimaryKey
    @SerializedName("id")
    public int ID_DATE_SHOP_KEY;

    @Column
    @SerializedName("id_shop")
    public int ID_SHOP_FOREIGN;

    @Column
    @SerializedName("account_date")
    public String ACCOUNT_DATE;

    @Column
    @SerializedName("offer_date")
    public String OFFER_DATE;
    @Column
    @SerializedName("notification_date")
    public String NOTIFICATION_DATE;

    @Column
    @SerializedName("date_shop_date")
    public String DATE_SHOP_DATE;

    public DateShop() {
    }

    public int getID_DATE_SHOP_KEY() {
        return ID_DATE_SHOP_KEY;
    }

    public void setID_DATE_SHOP_KEY(int ID_DATE_SHOP_KEY) {
        this.ID_DATE_SHOP_KEY = ID_DATE_SHOP_KEY;
    }

    public int getID_SHOP_FOREIGN() {
        return ID_SHOP_FOREIGN;
    }

    public void setID_SHOP_FOREIGN(int ID_SHOP_FOREIGN) {
        this.ID_SHOP_FOREIGN = ID_SHOP_FOREIGN;
    }

    public String getACCOUNT_DATE() {
        return ACCOUNT_DATE;
    }

    public void setACCOUNT_DATE(String ACCOUNT_DATE) {
        this.ACCOUNT_DATE = ACCOUNT_DATE;
    }

    public String getOFFER_DATE() {
        return OFFER_DATE;
    }

    public void setOFFER_DATE(String OFFER_DATE) {
        this.OFFER_DATE = OFFER_DATE;
    }

    public String getDATE_SHOP_DATE() {
        return DATE_SHOP_DATE;
    }

    public void setDATE_SHOP_DATE(String DATE_SHOP_DATE) {
        this.DATE_SHOP_DATE = DATE_SHOP_DATE;
    }

    public String getNOTIFICATION_DATE() {
        return NOTIFICATION_DATE;
    }

    public void setNOTIFICATION_DATE(String NOTIFICATION_DATE) {
        this.NOTIFICATION_DATE = NOTIFICATION_DATE;
    }
}
