package com.valdroide.mycitysshopsadm.main.draw.fragments.draw_list;

import android.content.Context;

import com.valdroide.mycitysshopsadm.entities.shop.Draw;
import com.valdroide.mycitysshopsadm.main.draw.fragments.draw_list.events.DrawListFragmentEvent;
import com.valdroide.mycitysshopsadm.main.draw.fragments.draw_list.ui.DrawListFragmentView;

public interface DrawListFragmentPresenter {
    void onCreate();
    void onDestroy();
    void getDraws(Context context);
    DrawListFragmentView getView();
    void cancelDraw(Context context, Draw draw);
    void forceDraw(Context context, Draw draw);
    void validateBroadcast(Context context);
    void validateDateShop(Context context);
    void onEventMainThread(DrawListFragmentEvent event);
}
