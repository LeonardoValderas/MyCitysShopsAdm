package com.valdroide.mycitysshopsadm.entities;

import com.google.gson.annotations.SerializedName;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.ForeignKey;
import com.raizlabs.android.dbflow.annotation.ForeignKeyAction;
import com.raizlabs.android.dbflow.annotation.ForeignKeyReference;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;
import com.valdroide.mycitysshopsadm.db.ShopDatabase;


@Table(database = ShopDatabase.class)
public class Offer extends BaseModel {

    // @PrimaryKey(autoincrement = true)
    @Column
    @PrimaryKey
    public int ID_OFFER_KEY;

    @Column
    @SerializedName("id_shop_foreign")
  //  @ForeignKey(references = {@ForeignKeyReference(columnName = "id_shop_foreign", columnType = Integer.class, foreignKeyColumnName = "ID_SHOP_KEY")}, tableClass = Shop.class, saveForeignKeyModel = false, onDelete = ForeignKeyAction.CASCADE, onUpdate = ForeignKeyAction.CASCADE)
    public int ID_SHOP_FOREIGN;

    @Column
    @SerializedName("title")
    public String TITLE;

    @Column
    @SerializedName("offer")
    public String OFFER;

    @Column
    @SerializedName("date_init")
    public String DATE_INIT;
    @Column
    @SerializedName("date_edit")
    public String DATE_EDIT;

    @Column
    @SerializedName("date_end")
    public String DATE_END;

    public Offer() {
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

    public String getDATE_INIT() {
        return DATE_INIT;
    }

    public void setDATE_INIT(String DATE_INIT) {
        this.DATE_INIT = DATE_INIT;
    }

    public String getDATE_EDIT() {
        return DATE_EDIT;
    }

    public void setDATE_EDIT(String DATE_EDIT) {
        this.DATE_EDIT = DATE_EDIT;
    }

    public String getDATE_END() {
        return DATE_END;
    }

    public void setDATE_END(String DATE_END) {
        this.DATE_END = DATE_END;
    }

    public int getID_SHOP_FOREIGN() {
        return ID_SHOP_FOREIGN;
    }

    public void setID_SHOP_FOREIGN(int ID_SHOP_FOREIGN) {
        this.ID_SHOP_FOREIGN = ID_SHOP_FOREIGN;
    }

}
