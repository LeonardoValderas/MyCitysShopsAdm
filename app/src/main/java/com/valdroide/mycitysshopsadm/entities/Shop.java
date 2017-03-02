package com.valdroide.mycitysshopsadm.entities;

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
    public int ID_SHOP_KEY;

    @Column
    public String SHOP;

    @Column
    public int ID_ACCOUNT_FOREIGN;

    @Column
    public int ID_USER_FOREIGN;

    @Column
    public int ISACTIVE;

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

    public int getID_USER_FOREIGN() {
        return ID_USER_FOREIGN;
    }

    public void setID_USER_FOREIGN(int ID_USER_FOREIGN) {
        this.ID_USER_FOREIGN = ID_USER_FOREIGN;
    }
}
