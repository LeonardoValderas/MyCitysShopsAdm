package com.valdroide.mycitysshopsadm.main.place;

import android.content.Context;

import com.valdroide.mycitysshopsadm.entities.shop.MyPlace;
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
        if (view != null)
            view.showProgressDialog();
    }

    @Override
    public void onDestroy() {
        eventBus.unregister(this);
        view = null;
    }

    @Override
    public void getCountries(Context context) {
        interactor.getCountries(context);
    }

    @Override
    public void getStateForCountry(Context context, int id_country) {
        interactor.getStateForCountry(context, id_country);
    }

    @Override
    public void getCitiesForState(Context context, int id_state) {
        interactor.getCitiesForState(context, id_state);
    }

    @Override
    public void savePlace(Context context, MyPlace place) {
        interactor.savePlace(context, place);
    }

    @Override
    public PlaceActivityView getView() {
        return this.view;
    }

    @Override
    @Subscribe
    public void onEventMainThread(PlaceActivityEvent event) {
        if (this.view != null) {
            switch (event.getType()) {
                case PlaceActivityEvent.GETCOUNTRIES:
                    view.setCountry(event.getCountries());
                    view.hidePorgressDialog();
                    break;
                case PlaceActivityEvent.GETSTATES:
                    view.setState(event.getStates());
                    view.hidePorgressDialog();
                    break;
                case PlaceActivityEvent.GETCITIES:
                    view.setCity(event.getCities());
                    view.hidePorgressDialog();
                    break;
                case PlaceActivityEvent.SAVE:
                    view.hidePorgressDialog();
                    view.saveSuccess(event.getPlace());
                    break;
                case PlaceActivityEvent.ERROR:
                    view.hidePorgressDialog();
                    view.setError(event.getError());
                    break;
            }
        }
    }
}
