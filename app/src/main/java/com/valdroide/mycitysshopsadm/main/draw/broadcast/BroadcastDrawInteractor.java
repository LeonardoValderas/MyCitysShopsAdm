package com.valdroide.mycitysshopsadm.main.draw.broadcast;


import android.content.Context;

import com.valdroide.mycitysshopsadm.entities.shop.Draw;

public interface BroadcastDrawInteractor {
    void getDraw(Context context);
    void getWinner(Context context, Draw draw);

}
