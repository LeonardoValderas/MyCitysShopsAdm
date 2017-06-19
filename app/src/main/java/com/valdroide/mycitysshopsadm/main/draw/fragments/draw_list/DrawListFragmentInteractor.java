package com.valdroide.mycitysshopsadm.main.draw.fragments.draw_list;

import android.content.Context;

import com.valdroide.mycitysshopsadm.entities.shop.Draw;

public interface DrawListFragmentInteractor {
    void getDraws(Context context);
    void cancelDraw(Context context, Draw draw);
    void forceDraw(Context context, Draw draw);
    void validateBroadcast(Context context);
}
