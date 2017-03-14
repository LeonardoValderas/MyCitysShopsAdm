package com.valdroide.mycitysshopsadm.main.place;


import android.content.Context;

import com.valdroide.mycitysshopsadm.entities.shop.MyPlace;
import com.valdroide.mycitysshopsadm.main.place.events.PlaceActivityEvent;

/**
 * Created by LEO on 30/1/2017.
 */
public interface PlaceActivityPresenter {
    void onCreate();
    void onDestroy();
    void getCountries();
    void getStateForCountry(int id_country);
    void getCitiesForState(int id_state);
    void savePlace(Context context, MyPlace place);
    void onEventMainThread(PlaceActivityEvent event);
}
