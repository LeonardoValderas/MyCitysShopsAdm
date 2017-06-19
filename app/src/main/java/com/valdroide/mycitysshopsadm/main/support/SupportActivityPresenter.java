package com.valdroide.mycitysshopsadm.main.support;

import android.content.Context;

import com.valdroide.mycitysshopsadm.main.support.events.SupportActivityEvent;
import com.valdroide.mycitysshopsadm.main.support.ui.SupportActivityView;

public interface SupportActivityPresenter {
    void onCreate();
    void onDestroy();
    void sendEmail(Context context, String comment);
    SupportActivityView getView();
    void onEventMainThread(SupportActivityEvent event);
}
