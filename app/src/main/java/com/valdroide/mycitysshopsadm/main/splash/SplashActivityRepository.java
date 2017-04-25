package com.valdroide.mycitysshopsadm.main.splash;

import android.content.Context;

public interface SplashActivityRepository {
    void validateDatePlace(Context context);
    void validateDateShop(Context context);
    void sendEmail(Context context, String comment);
}
