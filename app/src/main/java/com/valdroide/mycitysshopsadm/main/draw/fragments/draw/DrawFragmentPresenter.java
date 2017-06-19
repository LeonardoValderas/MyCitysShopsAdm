package com.valdroide.mycitysshopsadm.main.draw.fragments.draw;

import android.content.Context;

import com.valdroide.mycitysshopsadm.entities.shop.Draw;
import com.valdroide.mycitysshopsadm.main.draw.fragments.draw.events.DrawFragmentEvent;
import com.valdroide.mycitysshopsadm.main.draw.fragments.draw.ui.DrawFragmentView;

public interface DrawFragmentPresenter {
    void onCreate();
    void onDestroy();
    DrawFragmentView getView();
    void getCity(Context context);
    void createDraw(Context context, Draw draw);
    void validateBroadcast(Context context);
    void onEventMainThread(DrawFragmentEvent event);
}
