package com.valdroide.mycitysshopsadm.main.notification;

import android.content.Context;

public interface NotificationActivityRepository {
    void sendNotification(Context context, String notification);
}
