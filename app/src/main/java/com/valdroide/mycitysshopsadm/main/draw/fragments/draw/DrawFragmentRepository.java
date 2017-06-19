package com.valdroide.mycitysshopsadm.main.draw.fragments.draw;

import android.content.Context;

import com.valdroide.mycitysshopsadm.entities.shop.Draw;

public interface DrawFragmentRepository {
    void createDraw(Context context, Draw draw);
    void validateBroadcast(Context context);
    void getCity(Context context);
}
