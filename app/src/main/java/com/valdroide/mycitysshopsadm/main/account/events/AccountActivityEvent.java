package com.valdroide.mycitysshopsadm.main.account.events;

import com.valdroide.mycitysshopsadm.entities.shop.Account;

public class AccountActivityEvent {
    private int type;
    public static final int GETACCOUNT = 0;
    public static final int SAVEACCOUNT = 1;
    public static final int ERROR = 2;
    public static final int UPDATEACCOUNT = 3;
    public static final int CITY = 4;
    public static final int UPDATESUCCESS = 5;
    public static final int UPDATEWHITOUTCHANGE = 6;
    private String error;
    private Account account;
    private String city;
    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
