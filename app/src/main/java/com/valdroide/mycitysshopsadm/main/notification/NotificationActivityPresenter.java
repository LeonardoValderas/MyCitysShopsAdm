package com.valdroide.mycitysshopsadm.main.notification;

import android.content.Context;
import com.valdroide.mycitysshopsadm.main.notification.events.NotificationActivityEvent;
import com.valdroide.mycitysshopsadm.main.notification.ui.NotificationActivityView;

public interface NotificationActivityPresenter {
    void onCreate();
    void onDestroy();
    void sendNotification(Context context, String notification);
    void sendNotificationAdm(Context context, String notification);
    void validateNotificationExpire(Context context, String now);
    NotificationActivityView getView();
    void onEventMainThread(NotificationActivityEvent event);
}
