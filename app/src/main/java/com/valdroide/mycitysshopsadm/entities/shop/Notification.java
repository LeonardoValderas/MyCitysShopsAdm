package com.valdroide.mycitysshopsadm.entities.shop;

import com.google.gson.annotations.SerializedName;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;
import com.valdroide.mycitysshopsadm.db.ShopDatabase;


@Table(database = ShopDatabase.class)
public class Notification extends BaseModel {

    // @PrimaryKey(autoincrement = true)
    @Column
    @PrimaryKey
    @SerializedName("id")
    public int ID_NOTIFICATION_KEY;

    @Column
    @SerializedName("id_shop")
  //  @ForeignKey(references = {@ForeignKeyReference(columnName = "id_shop_foreign", columnType = Integer.class, foreignKeyColumnName = "ID_SHOP_KEY")}, tableClass = Shop.class, saveForeignKeyModel = false, onDelete = ForeignKeyAction.CASCADE, onUpdate = ForeignKeyAction.CASCADE)
    public int ID_SHOP_FOREIGN;

    @Column
    @SerializedName("date_end")
    public String DATE_END;

    public Notification() {
    }

    public int getID_NOTIFICATION_KEY() {
        return ID_NOTIFICATION_KEY;
    }

    public void setID_NOTIFICATION_KEY(int ID_NOTIFICATION_KEY) {
        this.ID_NOTIFICATION_KEY = ID_NOTIFICATION_KEY;
    }

    public int getID_SHOP_FOREIGN() {
        return ID_SHOP_FOREIGN;
    }

    public void setID_SHOP_FOREIGN(int ID_SHOP_FOREIGN) {
        this.ID_SHOP_FOREIGN = ID_SHOP_FOREIGN;
    }

    public String getDATE_END() {
        return DATE_END;
    }

    public void setDATE_END(String DATE_END) {
        this.DATE_END = DATE_END;
    }
}
