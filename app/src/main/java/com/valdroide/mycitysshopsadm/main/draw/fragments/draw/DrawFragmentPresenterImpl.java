package com.valdroide.mycitysshopsadm.main.draw.fragments.draw;

import android.content.Context;

import com.valdroide.mycitysshopsadm.entities.shop.Draw;
import com.valdroide.mycitysshopsadm.lib.base.EventBus;
import com.valdroide.mycitysshopsadm.main.draw.fragments.draw.events.DrawFragmentEvent;
import com.valdroide.mycitysshopsadm.main.draw.fragments.draw.ui.DrawFragmentView;

import org.greenrobot.eventbus.Subscribe;

public class DrawFragmentPresenterImpl implements DrawFragmentPresenter {
    private DrawFragmentView view;
    private EventBus eventBus;
    private DrawFragmentInteractor interactor;

    public DrawFragmentPresenterImpl(DrawFragmentView view, EventBus eventBus, DrawFragmentInteractor interactor) {
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
    public DrawFragmentView getView() {
        return this.view;
    }

    @Override
    public void getCity(Context context) {
        interactor.getCity(context);
    }

    @Override
    public void createDraw(Context context, Draw draw) {
        interactor.createDraw(context, draw);
    }

    @Override
    public void validateBroadcast(Context context) {
        interactor.validateBroadcast(context);
    }

    @Override
    public void validateDateShop(Context context) {
        interactor.validateDateShop(context);
    }

    @Override
    @Subscribe
    public void onEventMainThread(DrawFragmentEvent event) {
        if (this.view != null) {
            switch (event.getType()) {
                case DrawFragmentEvent.CREATEDRAW:
                    view.hidePorgressDialog();
                    view.createSuccess();
                    break;
                case DrawFragmentEvent.ERROR:
                    view.hidePorgressDialog();
                    view.setError(event.getError());
                    break;
                case DrawFragmentEvent.CITY:
                    view.setCity(event.getCity());
                    break;
                case DrawFragmentEvent.UPDATESUCCESS:
                    view.refreshAdapter();
                    view.hidePorgressDialog();
                    break;
                case DrawFragmentEvent.UPDATEWITHOUTCHANGE:
                    view.hidePorgressDialog();
                    break;
            }
        }
    }
}
