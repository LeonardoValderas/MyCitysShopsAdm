package com.valdroide.mycitysshopsadm.main.place;


public class PlaceActivityInteractorImpl implements PlaceActivityInteractor {

    private PlaceActivityRepository repository;

    public PlaceActivityInteractorImpl(PlaceActivityRepository repository) {
        this.repository = repository;
    }

    @Override
    public void getCountries() {
        repository.getCountries();
    }

    @Override
    public void getStateForCountry(int id_country) {
        repository.getStateForCountry(id_country);
    }

    @Override
    public void getCitiesForState(int id_state) {
        repository.getCitiesForState(id_state);
    }
}
