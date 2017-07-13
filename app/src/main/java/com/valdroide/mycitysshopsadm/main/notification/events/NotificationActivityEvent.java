package com.valdroide.mycitysshopsadm.main.notification.events;

public class NotificationActivityEvent {
    private int type;
    public static final int SEND = 0;
    public static final int ERROR = 1;
    public static final int ISAVAILABLE = 2;
    public static final int SENDADM = 3;
    public static final int UPDATESUCCESS = 4;
    private String error;
    private boolean is_available;
    private String date;
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

    public boolean is_available() {
        return is_available;
    }

    public void setIs_available(boolean is_available) {
        this.is_available = is_available;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
