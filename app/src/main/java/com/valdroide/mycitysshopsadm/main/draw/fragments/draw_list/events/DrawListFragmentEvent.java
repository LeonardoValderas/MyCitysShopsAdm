package com.valdroide.mycitysshopsadm.main.draw.fragments.draw_list.events;

import com.valdroide.mycitysshopsadm.entities.shop.Draw;

import java.util.List;

public class DrawListFragmentEvent {
    private int type;
    private String error;
    private List<Draw> drawList;
    private Draw draw;
    public static final int DRAWS = 0;
    public static final int ERROR = 1;
    public static final int CANCELSUCCESS = 2;
    public static final int FORCEDRAW = 3;
    public static final int UPDATESUCCESS = 4;
    public static final int WITHOUTCHANGE = 5;

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

    public List<Draw> getDrawList() {
        return drawList;
    }

    public void setDrawList(List<Draw> drawList) {
        this.drawList = drawList;
    }

    public Draw getDraw() {
        return draw;
    }

    public void setDraw(Draw draw) {
        this.draw = draw;
    }
}
