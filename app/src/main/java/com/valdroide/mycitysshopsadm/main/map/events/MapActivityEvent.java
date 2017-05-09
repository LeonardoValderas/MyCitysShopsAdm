package com.valdroide.mycitysshopsadm.main.map.events;

public class MapActivityEvent {
    public static final int SUCCESS = 0;
    public static final int ERROR = 1;
    private int type;
    private String latitud;
    private String longitud;
    private String error;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getLatitud() {
        return latitud;
    }

    public void setLatitud(String latitud) {
        this.latitud = latitud;
    }

    public String getLongitud() {
        return longitud;
    }

    public void setLongitud(String longitud) {
        this.longitud = longitud;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
