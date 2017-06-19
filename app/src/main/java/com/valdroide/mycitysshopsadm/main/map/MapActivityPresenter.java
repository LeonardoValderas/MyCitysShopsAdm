package com.valdroide.mycitysshopsadm.main.map;

import android.content.Context;

import com.valdroide.mycitysshopsadm.main.map.events.MapActivityEvent;
import com.valdroide.mycitysshopsadm.main.map.ui.MapActivityView;

public interface MapActivityPresenter {
    void onCreate();
    void onDestroy();
    void getLatLong(Context context, int id_city);
    MapActivityView getView();
    void onEventMainThread(MapActivityEvent event);
}
