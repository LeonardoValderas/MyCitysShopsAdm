package com.valdroide.mycitysshopsadm.main.navigation;

import android.content.Context;

import com.valdroide.mycitysshopsadm.lib.base.EventBus;
import com.valdroide.mycitysshopsadm.main.navigation.events.NavigationActivityEvent;
import com.valdroide.mycitysshopsadm.main.navigation.ui.NavigationActivityView;

import org.greenrobot.eventbus.Subscribe;

public class NavigationActivityPresenterImpl implements NavigationActivityPresenter {

    private NavigationActivityView view;
    private EventBus eventBus;
    private NavigationActivityInteractor interactor;

    public NavigationActivityPresenterImpl(NavigationActivityView view, EventBus eventBus, NavigationActivityInteractor interactor) {
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
    public void logOut(Context context) {
        interactor.logOut(context);
    }

    @Override
    public void getURLShop(Context context) {
        interactor.getURLShop(context);
    }

    @Override
    @Subscribe
    public void onEventMainThread(NavigationActivityEvent event) {
        if (this.view != null) {
            switch (event.getType()) {
                case NavigationActivityEvent.LOGOUT:
                    view.logOut();
                    break;
                case NavigationActivityEvent.ERROR:
                    view.setError(event.getError());
                    break;
                case NavigationActivityEvent.URL:
                    view.setURLShop(event.getUrl());
                    break;
            }
        }
    }
}
