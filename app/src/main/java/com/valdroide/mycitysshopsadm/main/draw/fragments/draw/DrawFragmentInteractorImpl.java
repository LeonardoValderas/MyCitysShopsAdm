package com.valdroide.mycitysshopsadm.main.draw.fragments.draw;

import android.content.Context;

import com.valdroide.mycitysshopsadm.entities.shop.Draw;

public class DrawFragmentInteractorImpl implements DrawFragmentInteractor {

    DrawFragmentRepository repository;

    public DrawFragmentInteractorImpl(DrawFragmentRepository repository) {
        this.repository = repository;
    }

    @Override
    public void createDraw(Context context, Draw draw) {
        repository.createDraw(context, draw);
    }
}
