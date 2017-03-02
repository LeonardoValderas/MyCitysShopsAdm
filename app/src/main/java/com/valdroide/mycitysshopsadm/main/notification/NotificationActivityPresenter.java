package com.valdroide.mycitysshopsadm.main.notification;

import android.content.Context;
import com.valdroide.mycitysshopsadm.main.notification.events.NotificationActivityEvent;

public interface NotificationActivityPresenter {
    void onCreate();
    void onDestroy();
    void sendNotification(Context context, String notification);
    void onEventMainThread(NotificationActivityEvent event);
}
