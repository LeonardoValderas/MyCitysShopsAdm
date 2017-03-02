package com.valdroide.mycitysshopsadm.entities.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.valdroide.mycitysshopsadm.entities.place.City;
import com.valdroide.mycitysshopsadm.entities.place.Country;
import com.valdroide.mycitysshopsadm.entities.place.DatePlace;
import com.valdroide.mycitysshopsadm.entities.place.State;

import java.util.List;

/**
 * Created by LEO on 6/2/2017.
 */

public class Result {
    @SerializedName("responseWS")
    @Expose
    ResponseWS responseWS;
    @SerializedName("date_place")
    @Expose
    List<DatePlace> datePlaces;
    @SerializedName("country")
    @Expose
    List<Country> countries;
    @SerializedName("state")
    @Expose
    List<State> states;
    @SerializedName("city")
    @Expose
    List<City> cities;

     public ResponseWS getResponseWS() {
        return responseWS;
    }

    public void setResponseWS(ResponseWS responseWS) {
        this.responseWS = responseWS;
    }

    public List<DatePlace> getDatePlaces() {
        return datePlaces;
    }

    public void setDatePlaces(List<DatePlace> datePlaces) {
        this.datePlaces = datePlaces;
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
}
