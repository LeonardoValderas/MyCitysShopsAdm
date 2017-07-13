package com.valdroide.mycitysshopsadm.main.draw.broadcast.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.valdroide.mycitysshopsadm.MyCitysShopsAdmApp;
import com.valdroide.mycitysshopsadm.R;
import com.valdroide.mycitysshopsadm.entities.shop.Draw;
import com.valdroide.mycitysshopsadm.main.draw.activities.ui.DrawActivity;
import com.valdroide.mycitysshopsadm.main.draw.broadcast.BroadcastDrawPresenter;
import com.valdroide.mycitysshopsadm.utils.Utils;

import javax.inject.Inject;

public class BroadcastDraw extends BroadcastReceiver implements BroadcastDrawView {
    @Inject
    BroadcastDrawPresenter presenter;
    Context context;
    private boolean isRegister = false;
    @Override
    public void onReceive(Context context, Intent intent) {
        Utils.writelogFile(context, "Se inicia onReceive(BroadcastDraw)");
        this.context = context;
        setupInjection();
        register();
        presenter.getDraw(context);
    }

    private void setupInjection() {
        MyCitysShopsAdmApp app = (MyCitysShopsAdmApp) context.getApplicationContext();
        app.getBroadcastDrawComponent(this, this).inject(this);
    }

    public void register() {
        if (!isRegister) {
            presenter.registerEventBus();
            isRegister = true;
        }
    }

    public void unregister() {
        if (isRegister) {
            presenter.unregisterEventBus();
            isRegister = false;
        }
    }

    @Override
    public void setDraw(Draw draw) {
        Utils.writelogFile(context, "setDraw(Broadcast, Repository)");
        if (draw != null) {
            if (draw.getIS_ACTIVE() == 1) {
                Utils.writelogFile(context, "draw.getIS_ACTIVE() == 1(Broadcast, Repository)");
                register();
                presenter.getWinner(context, draw);
            } else
                Utils.writelogFile(context, "draw.getIS_ACTIVE() == 0(Broadcast, Repository)");
        }
    }

    @Override
    public void setError(String error) {
        Utils.writelogFile(context, "setErrorDraw (Broadcast, Repository)");
        try {
            Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Utils.writelogFile(context, "catch error " + e.getMessage() + "(Broadcast, Repository)");
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void setWinnerSuccess() {
        Toast.makeText(context, R.string.winner_draw_broadcast, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void refresh() {
        try {
            if (DrawActivity.getInstace() != null)
                DrawActivity.getInstace().refresh(true);
        } finally {

        }
    }

    @Override
    public void unRegister() {
        unregister();
    }
}
