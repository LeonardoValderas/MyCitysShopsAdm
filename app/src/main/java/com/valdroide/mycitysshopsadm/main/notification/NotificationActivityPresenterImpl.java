package com.valdroide.mycitysshopsadm.main.notification;

import android.content.Context;

import com.valdroide.mycitysshopsadm.lib.base.EventBus;
import com.valdroide.mycitysshopsadm.main.notification.events.NotificationActivityEvent;
import com.valdroide.mycitysshopsadm.main.notification.ui.NotificationActivityView;

import org.greenrobot.eventbus.Subscribe;

public class NotificationActivityPresenterImpl implements NotificationActivityPresenter {
    private NotificationActivityView view;
    private EventBus eventBus;
    private NotificationActivityInteractor interactor;

    public NotificationActivityPresenterImpl(NotificationActivityView view, EventBus eventBus, NotificationActivityInteractor interactor) {
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
    public void sendNotification(Context context, String notification) {
        interactor.sendNotification(context, notification);
    }

    @Override
    public void validateNotificationExpire(Context context, String now) {
        interactor.validateNotificationExpire(context, now);
    }

    @Override
    @Subscribe
    public void onEventMainThread(NotificationActivityEvent event) {
        if (this.view != null) {
            switch (event.getType()) {
                case NotificationActivityEvent.SEND:
                    view.setSuccess(event.getDate());
                    break;
                case NotificationActivityEvent.ISAVAILABLE:
                    view.isAvailable(event.is_available(), event.getDate());
                    break;
                case NotificationActivityEvent.ERROR:
                    view.setError(event.getError());
                    break;
            }
        }
    }
}
