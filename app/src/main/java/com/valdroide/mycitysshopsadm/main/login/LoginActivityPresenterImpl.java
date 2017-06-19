package com.valdroide.mycitysshopsadm.main.login;

import android.content.Context;

import com.valdroide.mycitysshopsadm.lib.base.EventBus;
import com.valdroide.mycitysshopsadm.main.login.events.LoginActivityEvent;
import com.valdroide.mycitysshopsadm.main.login.ui.LoginActivityView;


import org.greenrobot.eventbus.Subscribe;

public class LoginActivityPresenterImpl implements LoginActivityPresenter {

    private LoginActivityView view;
    private EventBus eventBus;
    private LoginActivityInteractor interactor;

    public LoginActivityPresenterImpl(LoginActivityView view, EventBus eventBus, LoginActivityInteractor interactor) {
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
        view = null;
    }

    @Override
    public void changePlace(Context context) {
        interactor.changePlace(context);
    }

    @Override
    public void validateLogin(Context context, String user, String pass, int id_city) {
        interactor.validateLogin(context, user, pass, id_city);
    }

    @Override
    public void validateLoginAdm(Context context, String user, String pass, int id_city) {
        interactor.validateLoginAdm(context, user, pass, id_city);
    }

    @Override
    public LoginActivityView getView() {
        return this.view;
    }

    @Override
    @Subscribe
    public void onEventMainThread(LoginActivityEvent event) {
        if (this.view != null) {
            switch (event.getType()) {
                case LoginActivityEvent.LOGIN:
                    view.hidePorgressDialog();
                    view.loginSuccess();
                    break;
                case LoginActivityEvent.ERROR:
                    view.hidePorgressDialog();
                    view.setError(event.getError());
                    break;
                case LoginActivityEvent.CHANGEPLACE:
                    view.hidePorgressDialog();
                    view.goToPlace();
                    break;
                case LoginActivityEvent.ADM:
                    view.hidePorgressDialog();
                    view.goToNotification();
                    break;
            }
        }
    }
}
