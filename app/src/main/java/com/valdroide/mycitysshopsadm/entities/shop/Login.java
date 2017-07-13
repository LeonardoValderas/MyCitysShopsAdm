package com.valdroide.mycitysshopsadm.entities.shop;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;
import com.valdroide.mycitysshopsadm.db.ShopDatabase;

@Table(database = ShopDatabase.class)
public class Login extends BaseModel {

    // @PrimaryKey(autoincrement = true)
    @Column
    @PrimaryKey
    private int ID_LOGIN_KEY;
    @Column
    private String USER;
    @Column
    private String PASS;

    public Login() {
    }

    public int getID_LOGIN_KEY() {
        return ID_LOGIN_KEY;
    }

    public void setID_LOGIN_KEY(int ID_LOGIN_KEY) {
        this.ID_LOGIN_KEY = ID_LOGIN_KEY;
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
}
