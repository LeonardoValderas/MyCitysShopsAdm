package com.valdroide.mycitysshopsadm.main.map;


import android.content.Context;

import com.raizlabs.android.dbflow.sql.language.Condition;
import com.raizlabs.android.dbflow.sql.language.ConditionGroup;
import com.raizlabs.android.dbflow.sql.language.NameAlias;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.valdroide.mycitysshopsadm.R;
import com.valdroide.mycitysshopsadm.entities.place.City;
import com.valdroide.mycitysshopsadm.lib.base.EventBus;
import com.valdroide.mycitysshopsadm.main.map.events.MapActivityEvent;
import com.valdroide.mycitysshopsadm.utils.Utils;

public class MapActivityRepositoryImpl implements MapActivityRepository {
    EventBus eventBus;

    public MapActivityRepositoryImpl(EventBus eventBus) {
        this.eventBus = eventBus;
    }

    @Override
    public void getLatLong(Context context, int id_city) {
        Utils.writelogFile(context, "Metodo getLatLong, id_city; " + id_city + " (Map, Repository)");
        ConditionGroup conditions = ConditionGroup.clause();
        conditions.and(Condition.column(new NameAlias("ID_CITY_KEY")).is(id_city));
        try {
            City city = SQLite.select().from(City.class).where(conditions).querySingle();
            if (city != null) {
                Utils.writelogFile(context, "city != null(Map, Repository)");
                String lat = city.getLATITUD();
                String lon = city.getLONGITUD();
                if (lat != null && lon != null) {
                    if (!lat.isEmpty() && !lon.isEmpty()) {
                        if (!lat.equalsIgnoreCase("null") && !lon.equalsIgnoreCase("null")) {
                            post(MapActivityEvent.SUCCESS, lat, lon);
                        } else {
                            Utils.writelogFile(context, "lat == null || lon == null, post(Map, Repository)");
                            post(MapActivityEvent.ERROR, context.getString(R.string.error_data_base));
                        }
                    } else {
                        Utils.writelogFile(context, "lat.isEmpty() || lon.isEmpty(), post(Map, Repository)");
                        post(MapActivityEvent.ERROR, context.getString(R.string.error_data_base));
                    }
                } else {
                    Utils.writelogFile(context, "lat == null || lon == null, post(Map, Repository)");
                    post(MapActivityEvent.ERROR, context.getString(R.string.error_data_base));
                }
            } else {
                Utils.writelogFile(context, "city == null, post(Map, Repository)");
                post(MapActivityEvent.ERROR, context.getString(R.string.error_data_base));
            }
        } catch (Exception e) {
            Utils.writelogFile(context, "catch: " + e.getMessage() + ", post(Map, Repository)");
            post(MapActivityEvent.ERROR, e.getMessage());
        }
    }

    private void post(int type, String error) {
        post(type, null, null, error);
    }

    private void post(int type, String lat, String lon) {
        post(type, lat, lon, null);
    }

    private void post(int type, String lat, String lon, String error) {
        MapActivityEvent event = new MapActivityEvent();
        event.setType(type);
        event.setLatitud(lat);
        event.setLongitud(lon);
        event.setError(error);
        eventBus.post(event);
    }
}
