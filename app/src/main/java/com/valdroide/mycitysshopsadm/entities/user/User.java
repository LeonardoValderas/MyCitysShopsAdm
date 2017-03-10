package com.valdroide.mycitysshopsadm.entities.user;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;
import com.valdroide.mycitysshopsadm.db.ShopDatabase;


@Table(database = ShopDatabase.class)
public class User extends BaseModel {

    @Column
    @PrimaryKey
    public int ID_USER_KEY;

    @Column
    public String USER;

    @Column
    public String PASS;

    @Column
    public int ID_CITY_FOREIGN;

    public int getID_USER_KEY() {
        return ID_USER_KEY;
    }

    public void setID_USER_KEY(int ID_USER_KEY) {
        this.ID_USER_KEY = ID_USER_KEY;
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
}
