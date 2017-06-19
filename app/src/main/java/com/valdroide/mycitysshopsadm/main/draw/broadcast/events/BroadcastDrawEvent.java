package com.valdroide.mycitysshopsadm.main.draw.broadcast.events;


import com.valdroide.mycitysshopsadm.entities.shop.Draw;

public class BroadcastDrawEvent {
    private int type;
    private String error;
    private Draw draw;
    public static final int DRAW = 0;
    public static final int ERROR = 1;
    public static final int WINNIER = 2;
    public static final int WITHOUTPARTICIPATE = 3;

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

    public Draw getDraw() {
        return draw;
    }

    public void setDraw(Draw draw) {
        this.draw = draw;
    }

}
