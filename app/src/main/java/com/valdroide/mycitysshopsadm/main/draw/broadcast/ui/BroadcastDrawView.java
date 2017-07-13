package com.valdroide.mycitysshopsadm.main.draw.broadcast.ui;


import com.valdroide.mycitysshopsadm.entities.shop.Draw;

public interface BroadcastDrawView {
    void setDraw(Draw draw);
    void setError(String error);
    void setWinnerSuccess();
    void refresh();
    void unRegister();
}
