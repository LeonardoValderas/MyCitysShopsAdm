package com.valdroide.mycitysshopsadm.main.notification;

import android.content.Context;

import com.valdroide.mycitysshopsadm.entities.Offer;

public interface NotificationActivityRepository {
    void sendNotification(Context context, String notification);
}
