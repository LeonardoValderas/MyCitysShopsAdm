package com.valdroide.mycitysshopsadm.entities.place;

import com.google.gson.annotations.SerializedName;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;
import com.valdroide.mycitysshopsadm.db.ShopDatabase;


@Table(database = ShopDatabase.class)
public class DatePlace extends BaseModel {

    // @PrimaryKey(autoincrement = true)
    @Column
    @PrimaryKey
    @SerializedName("id")
    public int ID_PLACE_KEY;

    @Column
    @SerializedName("place")
    public String PLACE;

    @Column
    @SerializedName("date_place")
    public String DATE;

    public DatePlace() {
    }

    public int getID_PLACE_KEY() {
        return ID_PLACE_KEY;
    }

    public void setID_PLACE_KEY(int ID_PLACE_KEY) {
        this.ID_PLACE_KEY = ID_PLACE_KEY;
    }

    public String getPLACE() {
        return PLACE;
    }

    public void setPLACE(String PLACE) {
        this.PLACE = PLACE;
    }

    public String getDATE() {
        return DATE;
    }

    public void setDATE(String DATE) {
        this.DATE = DATE;
    }
}
