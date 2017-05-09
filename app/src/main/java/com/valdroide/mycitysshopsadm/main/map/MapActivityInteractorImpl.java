package com.valdroide.mycitysshopsadm.main.map;


import android.content.Context;

public class MapActivityInteractorImpl implements MapActivityInteractor {
    MapActivityRepository repository;

    public MapActivityInteractorImpl(MapActivityRepository repository) {
        this.repository = repository;
    }

    @Override
    public void getLatLong(Context context, int id_city) {
        repository.getLatLong(context, id_city);
    }
}
