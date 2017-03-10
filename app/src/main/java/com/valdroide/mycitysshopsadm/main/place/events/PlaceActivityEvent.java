package com.valdroide.mycitysshopsadm.main.place.events;

import com.valdroide.mycitysshopsadm.entities.user.MyPlace;
import com.valdroide.mycitysshopsadm.entities.place.City;
import com.valdroide.mycitysshopsadm.entities.place.Country;
import com.valdroide.mycitysshopsadm.entities.place.State;

import java.util.List;

/**
 * Created by LEO on 30/1/2017.
 */
public class PlaceActivityEvent {
    private int type;
    public static final int GETCOUNTRIES = 0;
    public static final int GETSTATES = 1;
    public static final int GETCITIES = 2;
    public static final int SAVE = 3;
    public static final int ERROR = 4;
    private List<Country> countries;
    private List<State> states;
    private List<City> cities;
    private MyPlace place;
    private String error;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public List<Country> getCountries() {
        return countries;
    }

    public void setCountries(List<Country> countries) {
        this.countries = countries;
    }

    public List<State> getStates() {
        return states;
    }

    public void setStates(List<State> states) {
        this.states = states;
    }

    public List<City> getCities() {
        return cities;
    }

    public void setCities(List<City> cities) {
        this.cities = cities;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public MyPlace getPlace() {
        return place;
    }

    public void setPlace(MyPlace place) {
        this.place = place;
    }
}
