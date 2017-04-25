package com.valdroide.mycitysshopsadm.main.place;


import android.content.Context;

import com.raizlabs.android.dbflow.sql.language.Condition;
import com.raizlabs.android.dbflow.sql.language.ConditionGroup;
import com.raizlabs.android.dbflow.sql.language.NameAlias;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.valdroide.mycitysshopsadm.R;
import com.valdroide.mycitysshopsadm.entities.shop.MyPlace;
import com.valdroide.mycitysshopsadm.entities.place.City;
import com.valdroide.mycitysshopsadm.entities.place.Country;
import com.valdroide.mycitysshopsadm.entities.place.State;
import com.valdroide.mycitysshopsadm.lib.base.EventBus;
import com.valdroide.mycitysshopsadm.main.place.events.PlaceActivityEvent;
import com.valdroide.mycitysshopsadm.utils.Utils;

import java.util.List;

public class PlaceActivityRepositoryImpl implements PlaceActivityRepository {
    private EventBus eventBus;
    private List<Country> countries;
    private List<State> states;
    private List<City> cities;

    public PlaceActivityRepositoryImpl(EventBus eventBus) {
        this.eventBus = eventBus;
    }

    @Override
    public void getCountries(Context context) {
        try {
            Utils.writelogFile(context, "Metodo getCountries y Se trae countries(Place, Repository)");
            countries = SQLite.select().from(Country.class).where().orderBy(new NameAlias("COUNTRY"), true).queryList();
            if (countries != null) {
                Utils.writelogFile(context, "countries != null y post GETCOUNTRIES (Place, Repository)");
                post(PlaceActivityEvent.GETCOUNTRIES, countries);
            } else {
                Utils.writelogFile(context, " Base de datos error " + context.getString(R.string.error_data_base) + "(Place, Repository)");
                post(PlaceActivityEvent.ERROR, context.getString(R.string.error_data_base));
            }
        } catch (Exception e) {
            Utils.writelogFile(context, " catch error " + e.getMessage() + "(Splash, Repository)");
            post(PlaceActivityEvent.ERROR, e.getMessage());
        }
    }

    @Override
    public void getStateForCountry(Context context, int id_country) {
        Utils.writelogFile(context, "Metodo getStateForCountry y Se arma  conditionGroup, id_country = " + id_country + "(Place, Repository)");
        ConditionGroup conditionGroup = ConditionGroup.clause();
        conditionGroup.and(Condition.column(new NameAlias("ID_COUNTRY_FOREIGN")).is(id_country));
        try {
            Utils.writelogFile(context, "traer states(Place, Repository)");
            states = SQLite.select().from(State.class).where(conditionGroup).orderBy(new NameAlias("STATE"), true).queryList();
            if (states != null) {
                Utils.writelogFile(context, "states != null y post GETSTATES(Place, Repository)");
                post(PlaceActivityEvent.GETSTATES, states, true);
            } else {
                Utils.writelogFile(context, " Base de datos error " + context.getString(R.string.error_data_base) + "(Place, Repository)");
                post(PlaceActivityEvent.ERROR, context.getString(R.string.error_data_base));
            }
        } catch (Exception e) {
            Utils.writelogFile(context, " catch error " + e.getMessage() + "(Splash, Repository)");
            post(PlaceActivityEvent.ERROR, e.getMessage());
        }
    }

    @Override
    public void getCitiesForState(Context context, int id_state) {
        Utils.writelogFile(context, "Metodo getCitiesForState y Se arma conditionGroup, id_state = " + id_state + "(Place, Repository)");
        ConditionGroup conditionGroup = ConditionGroup.clause();
        conditionGroup.and(Condition.column(new NameAlias("ID_STATE_FOREIGN")).is(id_state));
        try {
            Utils.writelogFile(context, "traer cities(Place, Repository)");
            cities = SQLite.select().from(City.class).where(conditionGroup).orderBy(new NameAlias("CITY"), true).queryList();
            if (cities != null) {
                Utils.writelogFile(context, "cities != null y post GETCITIES(Place, Repository)");
                post(PlaceActivityEvent.GETCITIES, 0, cities);
            } else {
                Utils.writelogFile(context, " Base de datos error " + context.getString(R.string.error_data_base) + "(Place, Repository)");
                post(PlaceActivityEvent.ERROR, context.getString(R.string.error_data_base));
            }
        } catch (Exception e) {
            Utils.writelogFile(context, " catch error " + e.getMessage() + "(Splash, Repository)");
            post(PlaceActivityEvent.ERROR, e.getMessage());
        }
    }

    @Override
    public void savePlace(Context context, final MyPlace place) {
        Utils.writelogFile(context, "Metodo savePlace y save place(Place, Repository)");
        try {
            place.save();
            Utils.writelogFile(context, "save place ok y setIdCity shared(Place, Repository)");
            Utils.setIdCity(context, place.getID_CITY_FOREIGN());
            Utils.writelogFile(context, "setIdCity shared ok y post SAVE(Place, Repository)");
            post(PlaceActivityEvent.SAVE, place);
        } catch (Exception e) {
            Utils.writelogFile(context, " catch error " + e.getMessage() + "(Splash, Repository)");
            post(PlaceActivityEvent.ERROR, e.getMessage());
        }
    }

    public void post(int type, MyPlace place) {
        post(type, null, null, null, null, place);
    }

    public void post(int type, List<Country> countries) {
        post(type, countries, null, null, null, null);
    }

    public void post(int type, List<State> states, boolean isSub) {
        post(type, null, states, null, null, null);
    }

    public void post(int type, int nothing, List<City> cities) {
        post(type, null, null, cities, null, null);
    }

    public void post(int type, String error) {
        post(type, null, null, null, error, null);
    }

    public void post(int type, List<Country> countries, List<State> states, List<City> cities, String error, MyPlace place) {
        PlaceActivityEvent event = new PlaceActivityEvent();
        event.setType(type);
        event.setCountries(countries);
        event.setStates(states);
        event.setCities(cities);
        event.setError(error);
        event.setPlace(place);
        eventBus.post(event);
    }
}
