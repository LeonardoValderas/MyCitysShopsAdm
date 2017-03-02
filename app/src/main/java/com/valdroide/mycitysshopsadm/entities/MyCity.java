package com.valdroide.mycitysshopsadm.entities;

import com.google.gson.annotations.SerializedName;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;
import com.valdroide.mycitysshopsadm.db.ShopDatabase;


@Table(database = ShopDatabase.class)
public class MyCity extends BaseModel {

    // @PrimaryKey(autoincrement = true)
    @Column
    @PrimaryKey
    private int ID_CITY_KEY;

    @Column
    @SerializedName("id_city_foreign")
    private int ID_CITY_FOREIGN;

    @Column
    @SerializedName("date_table")
    private String DATE_TABLE;

    public MyCity() {
    }

    public int getID_CITY_KEY() {
        return ID_CITY_KEY;
    }

    public void setID_CITY_KEY(int ID_CITY_KEY) {
        this.ID_CITY_KEY = ID_CITY_KEY;
    }

    public int getID_CITY_FOREIGN() {
        return ID_CITY_FOREIGN;
    }

    public void setID_CITY_FOREIGN(int ID_CITY_FOREIGN) {
        this.ID_CITY_FOREIGN = ID_CITY_FOREIGN;
    }

    public String getDATE_TABLE() {
        return DATE_TABLE;
    }

    public void setDATE_TABLE(String DATE_TABLE) {
        this.DATE_TABLE = DATE_TABLE;
    }
}
