package com.valdroide.mycitysshopsadm.main.draw.fragments.draw_list;

import android.content.Context;

import com.valdroide.mycitysshopsadm.entities.shop.Draw;
import com.valdroide.mycitysshopsadm.lib.base.EventBus;
import com.valdroide.mycitysshopsadm.main.draw.fragments.draw_list.events.DrawListFragmentEvent;
import com.valdroide.mycitysshopsadm.main.draw.fragments.draw_list.ui.DrawListFragmentView;

import org.greenrobot.eventbus.Subscribe;

public class DrawListFragmentPresenterImpl implements DrawListFragmentPresenter {
    EventBus eventBus;
    DrawListFragmentView view;
    DrawListFragmentInteractor interactor;

    public DrawListFragmentPresenterImpl(EventBus eventBus, DrawListFragmentView view, DrawListFragmentInteractor interactor) {
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
    }

    @Override
    public void getDraws(Context context) {
        interactor.getDraws(context);
    }

    @Override
    public DrawListFragmentView getView() {
        return this.view;
    }

    @Override
    public void cancelDraw(Context context, Draw draw) {
        interactor.cancelDraw(context, draw);
    }

    @Override
    @Subscribe
    public void onEventMainThread(DrawListFragmentEvent event) {
        if (view != null) {
            switch (event.getType()) {
                case DrawListFragmentEvent.DRAWS:
                    view.setDraws(event.getDrawList());
                    break;
                case DrawListFragmentEvent.ERROR:
                    view.setError(event.getError());
                    break;
                case DrawListFragmentEvent.CANCELSUCCESS:
                    view.cancelSuccess();
                    break;
            }
        }
    }
}
