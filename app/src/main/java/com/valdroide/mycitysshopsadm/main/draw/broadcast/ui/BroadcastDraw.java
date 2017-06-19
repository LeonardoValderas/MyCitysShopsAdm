package com.valdroide.mycitysshopsadm.main.draw.broadcast.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;
import com.valdroide.mycitysshopsadm.MyCitysShopsAdmApp;
import com.valdroide.mycitysshopsadm.R;
import com.valdroide.mycitysshopsadm.entities.shop.Draw;
import com.valdroide.mycitysshopsadm.main.draw.activity.ui.DrawActivity;
import com.valdroide.mycitysshopsadm.main.draw.broadcast.BroadcastDrawPresenter;
import com.valdroide.mycitysshopsadm.utils.Utils;

import javax.inject.Inject;



public class BroadcastDraw extends BroadcastReceiver implements BroadcastDrawView {
//    private APIService service;
//    private ResponseWS responseWS;
//    private DateShop dateShop;
    @Inject
    BroadcastDrawPresenter presenter;
    Context context;

    @Override
    public void onReceive(Context context, Intent intent) {
        Utils.writelogFile(context, "Se inicia onReceive(BroadcastDraw)");
        this.context = context;
        setupInjection();
        presenter.registerEventBus();
        // getDraw(context);
        presenter.getDraw(context);
    }

    private void setupInjection() {
        MyCitysShopsAdmApp app = (MyCitysShopsAdmApp) context.getApplicationContext();
        app.getBroadcastDrawComponent(this, this).inject(this);
    }

    @Override
    public void setDraw(Draw draw) {
        Utils.writelogFile(context, "setDraw(Broadcast, Repository)");
        if (draw != null) {
            if (draw.getIS_ACTIVE() == 1) {
                Utils.writelogFile(context, "draw.getIS_ACTIVE() == 1(Broadcast, Repository)");
                presenter.getWinner(context, draw);
            } else
                Utils.writelogFile(context, "draw.getIS_ACTIVE() == 0(Broadcast, Repository)");
        }
    }

    @Override
    public void setError(String error) {
        Utils.writelogFile(context, "setErrorDraw (Broadcast, Repository)");
        try {
            Toast.makeText(context, error, Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Utils.writelogFile(context, "catch error " + e.getMessage() + "(Broadcast, Repository)");
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void setWinnerSuccess() {
        Toast.makeText(context, R.string.winner_draw_broadcast, Toast.LENGTH_LONG).show();
    }

    @Override
    public void refresh() {
        try {
            if (DrawActivity.getInstace() != null)
                DrawActivity.getInstace().refresh(true);
        } finally {

        }
    }

    /*
    private void getDraw(Context context) {
        Utils.writelogFile(context, "Metodo getDraw(Broadcast, Repository)");
        ConditionGroup conditions = ConditionGroup.clause();
        conditions.and(Condition.column(new NameAlias("ID_DRAW_KEY")).is(Utils.getIdDrawEnd(context)));
        Draw draw = SQLite.select().from(Draw.class).where(conditions).querySingle();
        if (draw != null) {
            Utils.writelogFile(context, "draw != null y post draw(Broadcast, Repository)");
            if (draw.getIS_ACTIVE() == 1) {
                initService();
                getWinner(context, draw);
            }
        } else {
            Utils.writelogFile(context, "draw == null y post error(Broadcast, Repository)");
            Toast.makeText(context, R.string.error_data_base, Toast.LENGTH_LONG).show();
        }
    }

    private void getWinner(final Context context, final Draw draw) {
        Utils.writelogFile(context, "Metodo getWinner y validate internet (Broadcast, Repository)");
        String name = Utils.getNameShop(context);
        if (!name.isEmpty()) {
            if (Utils.isNetworkAvailable(context)) {
                try {
                    Utils.writelogFile(context, "setDATE_UNIQUE(Broadcast, Repository)");
                    final String date = Utils.getFechaOficialSeparate();
                    Utils.writelogFile(context, "Call getWinnerDraw(Broadcast, Repository)");
                    Call<ResultDraw> broadcastService = service.getWinnerDraw(draw.getID_SHOP_FOREIGN(), Utils.getIdCity(context),
                            draw.getID_DRAW_KEY(), name, date);
                    broadcastService.enqueue(new Callback<ResultDraw>() {
                        @Override
                        public void onResponse(Call<ResultDraw> call, Response<ResultDraw> response) {
                            if (response.isSuccessful()) {
                                Utils.writelogFile(context, "Response Successful y get ResponseWS(Broadcast, Repository)");
                                responseWS = response.body().getResponseWS();
                                if (responseWS != null) {
                                    Utils.writelogFile(context, "ResponseWS != null y valida getSuccess(Broadcast, Repository)");
                                    if (responseWS.getSuccess().equals("0")) {
                                        Utils.writelogFile(context, "getSuccess = 0 y getId(Broadcast, Repository)");
                                        Draw drawAux = response.body().getDraw();
                                        if (drawAux != null) {
                                            if (drawAux.getNAME() != null && !drawAux.getNAME().isEmpty() &&
                                                    drawAux.getDNI() != null && !drawAux.getDNI().isEmpty()) {
                                                draw.setNAME(drawAux.getNAME());
                                                draw.setDNI(drawAux.getDNI());
                                                draw.setIS_ACTIVE(0);
                                                draw.setDATE_UNIQUE(date);
                                                draw.update();
                                                Utils.setIdDrawEnd(context, 0);
                                                getDateShop(context, date);
                                                refresh(context);
                                                Toast.makeText(context, R.string.winner_draw_broadcast, Toast.LENGTH_LONG).show();
                                            } else {
                                                Utils.writelogFile(context, "drawAux data isEmpty or null" + context.getString(R.string.error_data_base) + "(Broadcast, Repository)");
                                                Toast.makeText(context, R.string.error_data_base, Toast.LENGTH_LONG).show();
                                                setErrorDraw(context, draw);
                                            }
                                        } else {
                                            Utils.writelogFile(context, "drawAux == null " + context.getString(R.string.error_data_base) + "(Broadcast, Repository)");
                                            Toast.makeText(context, R.string.error_data_base, Toast.LENGTH_LONG).show();
                                            setErrorDraw(context, draw);
                                        }
                                    } else if (responseWS.getSuccess().equals("4")) {
                                        draw.setIS_ZERO(1);
                                        draw.setDATE_UNIQUE(date);
                                        draw.setIS_ACTIVE(0);
                                        draw.update();
                                        refresh(context);
                                    } else {
                                        Utils.writelogFile(context, "getSuccess = error " + responseWS.getMessage() + "(Broadcast, Repository)");
                                        Toast.makeText(context, responseWS.getMessage(), Toast.LENGTH_LONG).show();
                                        setErrorDraw(context, draw);
                                    }
                                } else {
                                    Utils.writelogFile(context, "responseWS == null (Broadcast, Repository)");
                                    Toast.makeText(context, R.string.error_data_base, Toast.LENGTH_LONG).show();
                                    setErrorDraw(context, draw);
                                }
                            } else {
                                Utils.writelogFile(context, "!response.isSuccessful()(Broadcast, Repository)");
                                Toast.makeText(context, R.string.error_data_base, Toast.LENGTH_LONG).show();
                                setErrorDraw(context, draw);
                            }
                        }

                        @Override
                        public void onFailure(Call<ResultDraw> call, Throwable t) {
                            Utils.writelogFile(context, "Call error " + t.getMessage() + "(Broadcast, Repository)");
                            Toast.makeText(context, t.getMessage(), Toast.LENGTH_LONG).show();
                            setErrorDraw(context, draw);
                        }
                    });
                } catch (Exception e) {
                    Utils.writelogFile(context, "catch error " + e.getMessage() + "(Broadcast, Repository)");
                    Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
                    setErrorDraw(context, draw);
                }
            } else {
                Utils.writelogFile(context, "Internet error (Broadcast, Repository)");
                Toast.makeText(context, R.string.error_data_base, Toast.LENGTH_LONG).show();
                setErrorDraw(context, draw);
            }
        } else {
            Utils.writelogFile(context, "Name isEmpty" + context.getString(R.string.error_internet) + "(Broadcast, Repository)");
            Toast.makeText(context, R.string.error_data_base, Toast.LENGTH_LONG).show();
            setErrorDraw(context, draw);
        }
    }

    private void initService() {
        ShopClient client = new ShopClient();
        service = client.getAPIService();
    }

    private void setErrorDraw(Context context, Draw draw) {
        Utils.writelogFile(context, "setErrorDraw (Broadcast, Repository)");
        try {
            draw.setERROR_REPORTING(1);
            draw.update();
            refresh(context);
        } catch (Exception e) {
            Utils.writelogFile(context, "catch error " + e.getMessage() + "(Broadcast, Repository)");
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void getDateShop(Context context, String date_edit) {
        Utils.writelogFile(context, "Metodo getDateShop (Broadcast, Repository)");
        try {
            dateShop = SQLite.select().from(DateShop.class).querySingle();
            if (dateShop != null) {
                dateShop.setDRAW_DATE(date_edit);
                dateShop.setDATE_SHOP_DATE(date_edit);
                dateShop.update();
            }
        } catch (Exception e) {
            Utils.writelogFile(context, "catch error " + e.getMessage() + "(Broadcast, Repository)");
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void refresh(Context context) {
        Utils.writelogFile(context, "Metodo refresh(Broadcast, Repository)");
        try {
            if(DrawActivity.getInstace()!=null)
                DrawActivity.getInstace().refresh(true);
            //refreshAdapterDraw.refreshAdapter();
        } finally {

        }
    }
*/
//    private String getShopName(Context context) {
//        try {
//            Utils.writelogFile(context, "Metodo getShopName (DrawFragment, Repository)");
//            return SQLite.select(Account_Table.SHOP_NAME).from(Account.class).querySingle().getSHOP_NAME();
//        } catch (Exception e) {
//            Utils.writelogFile(context, "catch error: " + e.getMessage() + "(DrawFragment, Repository)");
//            return "";
//        }
//    }


}
