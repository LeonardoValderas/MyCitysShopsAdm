package com.valdroide.mycitysshopsadm.main.map;

import android.content.Context;

import com.valdroide.mycitysshopsadm.main.map.events.MapActivityEvent;

public interface MapActivityPresenter {
    void onCreate();
    void onDestroy();
    void getLatLong(Context context, int id_city);
    void onEventMainThread(MapActivityEvent event);
}
