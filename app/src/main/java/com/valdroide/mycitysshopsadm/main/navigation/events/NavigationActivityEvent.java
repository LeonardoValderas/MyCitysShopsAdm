package com.valdroide.mycitysshopsadm.main.navigation.events;

public class NavigationActivityEvent {
    private int type;
    private String error;
    public static final int LOGOUT = 0;
    public static final int ERROR = 1;

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
