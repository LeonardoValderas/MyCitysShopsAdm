package com.valdroide.mycitysshopsadm.main.splash;

import android.content.Context;

import com.valdroide.mycitysshopsadm.lib.base.EventBus;
import com.valdroide.mycitysshopsadm.main.splash.events.SplashActivityEvent;
import com.valdroide.mycitysshopsadm.main.splash.ui.SplashActivityView;

import org.greenrobot.eventbus.Subscribe;

public class SplashActivityPresenterImpl implements SplashActivityPresenter {


    private SplashActivityView view;
    private EventBus eventBus;
    private SplashActivityInteractor interactor;

    public SplashActivityPresenterImpl(SplashActivityView view, EventBus eventBus, SplashActivityInteractor interactor) {
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
    public void validateDatePlace(Context context) {
        interactor.validateDatePlace(context);
    }

    @Override
    public void validateDateShop(Context context) {
        interactor.validateDateShop(context);
    }

    @Override
    @Subscribe
    public void onEventMainThread(SplashActivityEvent event) {
        if (this.view != null) {
            switch (event.getType()) {
                case SplashActivityEvent.GOTOLOG:
                    view.goToLog();
                    break;
                case SplashActivityEvent.GOTONAV:
                    view.goToNav();
                    break;
                case SplashActivityEvent.GOTOPLACE:
                    view.goToPlace();
                    break;
                case SplashActivityEvent.ERROR:
                    view.setError(event.getError());
                    break;
            }
        }
    }
}
