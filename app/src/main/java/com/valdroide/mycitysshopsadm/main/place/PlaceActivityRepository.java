package com.valdroide.mycitysshopsadm.main.place;

import android.content.Context;

import com.valdroide.mycitysshopsadm.entities.shop.MyPlace;

public interface PlaceActivityRepository {
    void getCountries(Context context);
    void getStateForCountry(Context context, int id_country);
    void getCitiesForState(Context context, int id_state);
    void savePlace(Context context, MyPlace place);
}
