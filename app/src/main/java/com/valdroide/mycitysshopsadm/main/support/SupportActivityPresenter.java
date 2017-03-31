package com.valdroide.mycitysshopsadm.main.support;

import android.content.Context;

import com.valdroide.mycitysshopsadm.main.support.events.SupportActivityEvent;

public interface SupportActivityPresenter {
    void onCreate();
    void onDestroy();
    void sendEmail(Context context, String comment);
    void onEventMainThread(SupportActivityEvent event);
}
