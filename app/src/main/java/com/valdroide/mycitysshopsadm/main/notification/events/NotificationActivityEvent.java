package com.valdroide.mycitysshopsadm.main.notification.events;

public class NotificationActivityEvent {
    private int type;
    public static final int SEND = 0;
    public static final int ERROR = 1;
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
