package com.valdroide.mycitysshopsadm.main.notification;


import android.content.Context;

public class NotificationActivityInteractorImpl implements NotificationActivityInteractor {
    NotificationActivityRepository repository;

    public NotificationActivityInteractorImpl(NotificationActivityRepository repository) {
        this.repository = repository;
    }

    @Override
    public void sendNotification(Context context, String notification) {
        repository.sendNotification(context, notification);
    }

    @Override
    public void validateNotificationExpire(Context context, String now) {
        repository.validateNotificationExpire(context, now);
    }

    @Override
    public void sendNotificationAdm(Context context, String notification) {
        repository.sendNotificationAdm(context, notification);
    }

    @Override
    public void validateDateShop(Context context) {
        repository.validateDateShop(context);
    }
}
