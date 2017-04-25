package com.valdroide.mycitysshopsadm.main.notification;

import android.content.Context;

public interface NotificationActivityInteractor {
    void sendNotification(Context context, String notification);
    void validateNotificationExpire(Context context, String now);
}
