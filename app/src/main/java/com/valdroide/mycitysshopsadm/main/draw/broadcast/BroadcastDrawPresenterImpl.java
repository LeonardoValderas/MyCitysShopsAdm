package com.valdroide.mycitysshopsadm.main.draw.broadcast;


import android.content.Context;
import android.util.Log;

import com.valdroide.mycitysshopsadm.entities.shop.Draw;
import com.valdroide.mycitysshopsadm.lib.base.EventBus;
import com.valdroide.mycitysshopsadm.main.draw.broadcast.events.BroadcastDrawEvent;
import com.valdroide.mycitysshopsadm.main.draw.broadcast.ui.BroadcastDrawView;

import org.greenrobot.eventbus.Subscribe;

public class BroadcastDrawPresenterImpl implements BroadcastDrawPresenter {
    private EventBus eventBus;
    private BroadcastDrawView view;
    private BroadcastDrawInteractor interactor;

    public BroadcastDrawPresenterImpl(EventBus eventBus, BroadcastDrawView view, BroadcastDrawInteractor interactor) {
        this.eventBus = eventBus;
        this.view = view;
        this.interactor = interactor;
    }

    @Override
    public void registerEventBus() {
        eventBus.register(this);
    }

    @Override
    public void unregisterEventBus() {
        eventBus.unregister(this);
    }

    @Override
    public BroadcastDrawView getView() {
        return this.view;
    }

    @Override
    public void getDraw(Context context) {
        interactor.getDraw(context);
    }

    @Override
    public void getWinner(Context context, Draw draw) {
        interactor.getWinner(context, draw);
    }

    @Subscribe
    @Override
    public void onEventMainThread(BroadcastDrawEvent event) {
        if (view != null) {
            switch (event.getType()) {
                case BroadcastDrawEvent.DRAW:
                    view.setDraw(event.getDraw());
               //     view.unRegister();
                    break;
                case BroadcastDrawEvent.ERROR:
                    Log.i("DRAW", "ERROR presenter");
                    view.refresh();
                    view.setError(event.getError());
                    view.unRegister();
                    break;
                case BroadcastDrawEvent.WINNIER:
                    Log.i("DRAW", "WINNIER presenter");
                    view.setWinnerSuccess();
                    view.refresh();
                    view.unRegister();
                    break;
                case BroadcastDrawEvent.WITHOUTPARTICIPATE:
                    Log.i("DRAW", "WITHOUTPARTICIPATE presenter");
                    view.refresh();
                    view.unRegister();
                    break;
            }
        }
    }
}
