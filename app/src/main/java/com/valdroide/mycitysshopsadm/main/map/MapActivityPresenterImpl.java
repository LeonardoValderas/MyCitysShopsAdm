package com.valdroide.mycitysshopsadm.main.map;


import android.content.Context;

import com.valdroide.mycitysshopsadm.lib.base.EventBus;
import com.valdroide.mycitysshopsadm.main.map.events.MapActivityEvent;
import com.valdroide.mycitysshopsadm.main.map.ui.MapActivityView;

import org.greenrobot.eventbus.Subscribe;

public class MapActivityPresenterImpl implements MapActivityPresenter {
    EventBus eventBus;
    MapActivityView view;
    MapActivityInteractor interactor;

    public MapActivityPresenterImpl(EventBus eventBus, MapActivityView view, MapActivityInteractor interactor) {
        this.eventBus = eventBus;
        this.view = view;
        this.interactor = interactor;
    }

    @Override
    public void onCreate() {
        eventBus.register(this);
    }

    @Override
    public void onDestroy() {
        eventBus.unregister(this);
        view = null;
    }

    @Override
    public void getLatLong(Context context, int id_city) {
        interactor.getLatLong(context, id_city);
    }

    @Override
    public MapActivityView getView() {
        return this.view;
    }

    @Subscribe
    @Override
    public void onEventMainThread(MapActivityEvent event) {
        if (this.view != null) {
            switch (event.getType()) {
                case MapActivityEvent.SUCCESS:
                    view.setLatLong(event.getLatitud(), event.getLongitud());
                    break;
                case MapActivityEvent.ERROR:
                    view.error(event.getError());
                    break;
            }
        }
    }
}
