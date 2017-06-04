package com.valdroide.mycitysshopsadm.main.draw.fragments.draw_list;


import android.content.Context;

import com.valdroide.mycitysshopsadm.entities.shop.Draw;

public interface DrawListFragmentRepository {
    void getDraws(Context context);
    void cancelDraw(Context context, Draw draw);
}
