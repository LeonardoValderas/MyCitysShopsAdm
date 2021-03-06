package com.valdroide.mycitysshopsadm.entities.shop;

import com.google.gson.annotations.SerializedName;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;
import com.valdroide.mycitysshopsadm.db.ShopDatabase;


@Table(database = ShopDatabase.class)
public class Shop extends BaseModel {

    @Column
    @PrimaryKey
    @SerializedName("id")
    public int ID_SHOP_KEY;
    @Column
    @SerializedName("shop")
    public String SHOP;
    @Column
    @SerializedName("id_account")
    public int ID_ACCOUNT_FOREIGN;
    @Column
    @SerializedName("user")
    public String USER;
    @Column
    @SerializedName("pass")
    public String PASS;
    @Column
    @SerializedName("id_city")
    public int ID_CITY_FOREIGN;
    @Column
    @SerializedName("id_subcategory")
    public int ID_SUBCATEGORY_FOREIGN;
    @Column
    @SerializedName("follow")
    public int FOLLOW;
    @Column
    @SerializedName("is_active")
    public int ISACTIVE;
    @Column
    @SerializedName("count_offer")
    public int COUNT_OFFER;

    public int getID_SHOP_KEY() {
        return ID_SHOP_KEY;
    }

    public void setID_SHOP_KEY(int ID_SHOP_KEY) {
        this.ID_SHOP_KEY = ID_SHOP_KEY;
    }

    public int getISACTIVE() {
        return ISACTIVE;
    }

    public void setISACTIVE(int ISACTIVE) {
        this.ISACTIVE = ISACTIVE;
    }

    public String getSHOP() {
        return SHOP;
    }

    public void setSHOP(String SHOP) {
        this.SHOP = SHOP;
    }

    public int getID_ACCOUNT_FOREIGN() {
        return ID_ACCOUNT_FOREIGN;
    }

    public void setID_ACCOUNT_FOREIGN(int ID_ACCOUNT_FOREIGN) {
        this.ID_ACCOUNT_FOREIGN = ID_ACCOUNT_FOREIGN;
    }

    public String getUSER() {
        return USER;
    }

    public void setUSER(String USER) {
        this.USER = USER;
    }

    public String getPASS() {
        return PASS;
    }

    public void setPASS(String PASS) {
        this.PASS = PASS;
    }

    public int getID_CITY_FOREIGN() {
        return ID_CITY_FOREIGN;
    }

    public void setID_CITY_FOREIGN(int ID_CITY_FOREIGN) {
        this.ID_CITY_FOREIGN = ID_CITY_FOREIGN;
    }

    public int getID_SUBCATEGORY_FOREIGN() {
        return ID_SUBCATEGORY_FOREIGN;
    }

    public void setID_SUBCATEGORY_FOREIGN(int ID_SUBCATEGORY_FOREIGN) {
        this.ID_SUBCATEGORY_FOREIGN = ID_SUBCATEGORY_FOREIGN;
    }

    public int getCOUNT_OFFER() {
        return COUNT_OFFER;
    }

    public void setCOUNT_OFFER(int COUNT_OFFER) {
        this.COUNT_OFFER = COUNT_OFFER;
    }

    public int getFOLLOW() {
        return FOLLOW;
    }

    public void setFOLLOW(int FOLLOW) {
        this.FOLLOW = FOLLOW;
    }
}
