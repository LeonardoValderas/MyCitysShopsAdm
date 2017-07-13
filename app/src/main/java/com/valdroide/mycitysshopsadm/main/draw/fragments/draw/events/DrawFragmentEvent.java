package com.valdroide.mycitysshopsadm.main.draw.fragments.draw.events;

public class DrawFragmentEvent {
    private int type;
    public static final int CREATEDRAW = 0;
    public static final int ERROR = 1;
    public static final int CITY = 2;
    public static final int UPDATESUCCESS = 3;
    public static final int UPDATEWITHOUTCHANGE = 4;
    private String error;
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

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
