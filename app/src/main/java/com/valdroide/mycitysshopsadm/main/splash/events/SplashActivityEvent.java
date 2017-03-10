package com.valdroide.mycitysshopsadm.main.splash.events;

public class SplashActivityEvent {
    private int type;
    public static final int GOTOLOG = 0;
    public static final int GOTOPLACE = 1;
    public static final int GOTONAV = 2;
    public static final int ERROR = 3;
    private String error;

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
}
