package com.valdroide.mycitysshopsadm.main.draw.broadcast;

import android.content.Context;

import com.valdroide.mycitysshopsadm.entities.shop.Draw;
import com.valdroide.mycitysshopsadm.main.draw.broadcast.events.BroadcastDrawEvent;
import com.valdroide.mycitysshopsadm.main.draw.broadcast.ui.BroadcastDrawView;

public interface BroadcastDrawPresenter{
    void registerEventBus();
    void unregisterEventBus();
    BroadcastDrawView getView();
    void getDraw(Context context);
    void getWinner(Context context, Draw draw);
    void onEventMainThread(BroadcastDrawEvent event);
}
