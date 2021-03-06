package com.valdroide.mycitysshopsadm.entities.place;

import com.google.gson.annotations.SerializedName;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;
import com.valdroide.mycitysshopsadm.db.ShopDatabase;


@Table(database = ShopDatabase.class)
public class City extends BaseModel {

    // @PrimaryKey(autoincrement = true)
    @Column
    @PrimaryKey
    @SerializedName("id")
    public int ID_CITY_KEY;

    @Column
    @SerializedName("id_state")
    public int ID_STATE_FOREIGN;

    @Column
    @SerializedName("is_active")
    public int IS_ACTIVE;

    @Column
    @SerializedName("city")
    public String CITY;

    @Column
    @SerializedName("latitud")
    public String LATITUD;

    @Column
    @SerializedName("longitud")
    public String LONGITUD;

    public City() {
    }

    public int getID_CITY_KEY() {
        return ID_CITY_KEY;
    }

    public void setID_CITY_KEY(int ID_CITY_KEY) {
        this.ID_CITY_KEY = ID_CITY_KEY;
    }

    public int getID_STATE_FOREIGN() {
        return ID_STATE_FOREIGN;
    }

    public void setID_STATE_FOREIGN(int ID_STATE_FOREIGN) {
        this.ID_STATE_FOREIGN = ID_STATE_FOREIGN;
    }

    public int getIS_ACTIVE() {
        return IS_ACTIVE;
    }

    public void setIS_ACTIVE(int IS_ACTIVE) {
        this.IS_ACTIVE = IS_ACTIVE;
    }

    public String getCITY() {
        return CITY;
    }

    public String toString() {
        return CITY;
    }

    public void setCITY(String CITY) {
        this.CITY = CITY;
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
}
