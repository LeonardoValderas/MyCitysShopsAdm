package com.valdroide.mycitysshopsadm.main.login.events;

public class LoginActivityEvent {
    private int type;
    public static final int GETALLDATA = 0;
    public static final int ERROR = 1;
    public static final int GOTOTAB = 2;
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
