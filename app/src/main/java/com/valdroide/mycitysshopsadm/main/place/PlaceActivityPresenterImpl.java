package com.valdroide.mycitysshopsadm.main.place;

import com.valdroide.mycitysshopsadm.lib.base.EventBus;
import com.valdroide.mycitysshopsadm.main.place.events.PlaceActivityEvent;
import com.valdroide.mycitysshopsadm.main.place.ui.PlaceActivityView;

import org.greenrobot.eventbus.Subscribe;

public class PlaceActivityPresenterImpl implements PlaceActivityPresenter {


    private PlaceActivityView view;
    private EventBus eventBus;
    private PlaceActivityInteractor interactor;

    public PlaceActivityPresenterImpl(PlaceActivityView view, EventBus eventBus, PlaceActivityInteractor interactor) {
        this.view = view;
        this.eventBus = eventBus;
        this.interactor = interactor;
    }

    @Override
    public void onCreate() {
        eventBus.register(this);
    }

    @Override
    public void onDestroy() {
        eventBus.unregister(this);
    }

    @Override
    public void getCountries() {
        interactor.getCountries();
    }

    @Override
    public void getStateForCountry(int id_country) {
        interactor.getStateForCountry(id_country);
    }

    @Override
    public void getCitiesForState(int id_state) {
        interactor.getCitiesForState(id_state);
    }

    @Override
    @Subscribe
    public void onEventMainThread(PlaceActivityEvent event) {
        if (this.view != null) {
            switch (event.getType()) {
                case PlaceActivityEvent.GETCOUNTRIES:
                    view.setCountry(event.getCountries());
                    break;
                case PlaceActivityEvent.GETSTATES:
                    view.setState(event.getStates());
                    break;
                case PlaceActivityEvent.GETCITIES:
                    view.setCity(event.getCities());
                    break;
                case PlaceActivityEvent.ERROR:
                    view.setError(event.getError());
                    break;
            }
        }
    }
}
