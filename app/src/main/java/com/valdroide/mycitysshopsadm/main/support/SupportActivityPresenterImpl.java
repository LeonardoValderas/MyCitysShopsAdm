package com.valdroide.mycitysshopsadm.main.support;

import android.content.Context;

import com.valdroide.mycitysshopsadm.lib.base.EventBus;
import com.valdroide.mycitysshopsadm.main.support.events.SupportActivityEvent;
import com.valdroide.mycitysshopsadm.main.support.ui.SupportActivityView;

import org.greenrobot.eventbus.Subscribe;

public class SupportActivityPresenterImpl implements SupportActivityPresenter {

    private SupportActivityView view;
    private EventBus eventBus;
    private SupportActivityInteractor interactor;

    public SupportActivityPresenterImpl(SupportActivityView view, EventBus eventBus, SupportActivityInteractor interactor) {
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
    public void sendEmail(Context context, String comment) {
        interactor.sendEmail(context, comment);
    }

    @Override
    public SupportActivityView getView() {
        return this.view;
    }

    @Override
    @Subscribe
    public void onEventMainThread(SupportActivityEvent event) {
        if (this.view != null) {
            switch (event.getType()) {
                case SupportActivityEvent.SENDEMAILSUCCESS:
                    view.hidePorgressDialog();
                    view.sendEmailSuccess();
                    break;
                case SupportActivityEvent.ERROR:
                    view.hidePorgressDialog();
                    view.setError(event.getError());
                    break;
            }
        }
    }
}
