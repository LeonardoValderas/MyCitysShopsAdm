package com.valdroide.mycitysshopsadm.main.draw.fragments.draw_list.ui;


import com.valdroide.mycitysshopsadm.entities.shop.Draw;

import java.util.List;

public interface DrawListFragmentView {
    void setDraws(List<Draw> draws);
    void setError(String error);
    void cancelSuccess();
    void forceSuccess(Draw draw);
    void showProgressDialog();
    void hidePorgressDialog();
    void withoutChange();
    void refreshDateShops();
}
