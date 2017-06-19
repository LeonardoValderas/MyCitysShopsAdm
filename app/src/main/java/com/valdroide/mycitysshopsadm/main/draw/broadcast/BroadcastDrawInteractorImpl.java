package com.valdroide.mycitysshopsadm.main.draw.broadcast;

import android.content.Context;

import com.valdroide.mycitysshopsadm.entities.shop.Draw;

public class BroadcastDrawInteractorImpl implements BroadcastDrawInteractor {
    BroadcastDrawRepository repository;

    public BroadcastDrawInteractorImpl(BroadcastDrawRepository repository) {
        this.repository = repository;
    }

    @Override
    public void getDraw(Context context) {
        repository.getDraw(context);
    }

    @Override
    public void getWinner(Context context, Draw draw) {
        repository.getWinner(context, draw);
    }
}
