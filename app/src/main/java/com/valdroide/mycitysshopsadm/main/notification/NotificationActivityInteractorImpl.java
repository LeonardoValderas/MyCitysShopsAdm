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
}
