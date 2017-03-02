package com.valdroide.mycitysshopsadm.main.place;

public interface PlaceActivityRepository {
    void getCountries();
    void getStateForCountry(int id_country);
    void getCitiesForState(int id_state);
}
