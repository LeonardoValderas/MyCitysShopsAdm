package com.valdroide.mycitysshopsadm.main.offer.ui.adapters;

import android.view.View;

import com.valdroide.mycitysshopsadm.entities.shop.Offer;

public interface OnItemClickListener {
    void onClick(View view, int position, Offer offer);
    void onLongClick(View view, int position, Offer offer);
}
