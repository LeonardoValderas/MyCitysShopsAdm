package com.valdroide.mycitysshopsadm.main.draw.fragments.draw_list;


import android.content.Context;

import com.valdroide.mycitysshopsadm.entities.shop.Draw;

public class DrawListFragmentInteractorImpl implements DrawListFragmentInteractor {
    DrawListFragmentRepository repository;

    public DrawListFragmentInteractorImpl(DrawListFragmentRepository repository) {
        this.repository = repository;
    }

    @Override
    public void getDraws(Context context) {
        repository.getDraws(context);
    }

    @Override
    public void cancelDraw(Context context, Draw draw) {
        repository.cancelDraw(context, draw);
    }

    @Override
    public void forceDraw(Context context, Draw draw) {
        repository.forceDraw(context, draw);
    }

    @Override
    public void validateBroadcast(Context context) {
        repository.validateBroadcast(context);
    }
}
