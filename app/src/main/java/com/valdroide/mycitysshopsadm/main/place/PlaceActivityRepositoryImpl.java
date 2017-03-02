package com.valdroide.mycitysshopsadm.main.place;


import com.raizlabs.android.dbflow.sql.language.Condition;
import com.raizlabs.android.dbflow.sql.language.ConditionGroup;
import com.raizlabs.android.dbflow.sql.language.NameAlias;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.valdroide.mycitysshopsadm.api.APIService;
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
    private APIService service;

    public PlaceActivityRepositoryImpl(EventBus eventBus, APIService service) {
        this.eventBus = eventBus;
        this.service = service;
    }

    @Override
    public void getCountries() {
        countries = SQLite.select().from(Country.class).where().orderBy(new NameAlias("COUNTRY"), true).queryList();
        if (countries != null)
            post(PlaceActivityEvent.GETCOUNTRIES, countries);
        else
            post(PlaceActivityEvent.ERROR, Utils.ERROR_DATA_BASE);
    }

    @Override
    public void getStateForCountry(int id_country) {
        ConditionGroup conditionGroup = ConditionGroup.clause();
        conditionGroup.and(Condition.column(new NameAlias("ID_COUNTRY_FOREIGN")).is(id_country));
        states = SQLite.select().from(State.class).where(conditionGroup).orderBy(new NameAlias("STATE"), true).queryList();

        if (states != null)
            post(PlaceActivityEvent.GETSTATES, states, true);
        else
            post(PlaceActivityEvent.ERROR, Utils.ERROR_DATA_BASE);
    }

    @Override
    public void getCitiesForState(int id_state) {
        ConditionGroup conditionGroup = ConditionGroup.clause();
        conditionGroup.and(Condition.column(new NameAlias("ID_STATE_FOREIGN")).is(id_state));
        cities = SQLite.select().from(City.class).where(conditionGroup).orderBy(new NameAlias("CITY"), true).queryList();

        if (cities != null)
            post(PlaceActivityEvent.GETCITIES, 0, cities);
        else
            post(PlaceActivityEvent.ERROR, Utils.ERROR_DATA_BASE);
    }

    public void post(int type, List<Country> countries) {
        post(type, countries, null, null, null);
    }

    public void post(int type, List<State> states, boolean isSub) {
        post(type, null, states, null, null);
    }

    public void post(int type, int nothing, List<City> cities) {
        post(type, null, null, cities, null);
    }

    public void post(int type, String error) {
        post(type, null, null, null, error);
    }

    public void post(int type, List<Country> countries, List<State> states, List<City> cities, String error) {
        PlaceActivityEvent event = new PlaceActivityEvent();
        event.setType(type);
        event.setCountries(countries);
        event.setStates(states);
        event.setCities(cities);
        event.setError(error);
        eventBus.post(event);
    }
}
