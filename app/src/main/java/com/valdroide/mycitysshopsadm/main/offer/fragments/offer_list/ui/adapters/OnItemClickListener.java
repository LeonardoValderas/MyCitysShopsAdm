package com.valdroide.mycitysshopsadm.main.offer.fragments.offer_list.ui.adapters;

import android.view.View;

import com.valdroide.mycitysshopsadm.entities.shop.Offer;

public interface OnItemClickListener {
    void onClick(View view, Offer offer);
    void onClickSwitch(int position, Offer offer);
}
