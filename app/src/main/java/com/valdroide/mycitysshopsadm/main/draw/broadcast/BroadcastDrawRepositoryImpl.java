package com.valdroide.mycitysshopsadm.main.draw.broadcast;

import android.content.Context;
import android.widget.Toast;

import com.raizlabs.android.dbflow.sql.language.Condition;
import com.raizlabs.android.dbflow.sql.language.ConditionGroup;
import com.raizlabs.android.dbflow.sql.language.NameAlias;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.valdroide.mycitysshopsadm.R;
import com.valdroide.mycitysshopsadm.api.APIService;
import com.valdroide.mycitysshopsadm.entities.response.ResponseWS;
import com.valdroide.mycitysshopsadm.entities.response.ResultDraw;
import com.valdroide.mycitysshopsadm.entities.shop.DateShop;
import com.valdroide.mycitysshopsadm.entities.shop.Draw;
import com.valdroide.mycitysshopsadm.lib.base.EventBus;
import com.valdroide.mycitysshopsadm.main.draw.activity.ui.DrawActivity;
import com.valdroide.mycitysshopsadm.main.draw.broadcast.events.BroadcastDrawEvent;
import com.valdroide.mycitysshopsadm.utils.Utils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BroadcastDrawRepositoryImpl implements BroadcastDrawRepository {
    private EventBus eventBus;
    private APIService service;
    private ResponseWS responseWS;
    private DateShop dateShop;

    public BroadcastDrawRepositoryImpl(EventBus eventBus, APIService service) {
        this.eventBus = eventBus;
        this.service = service;
    }

    @Override
    public void getDraw(Context context) {
        Utils.writelogFile(context, "Metodo getDraw(Broadcast, Repository)");
        ConditionGroup conditions = ConditionGroup.clause();
        conditions.and(Condition.column(new NameAlias("ID_DRAW_KEY")).is(Utils.getIdDrawEnd(context)));
        Draw draw = SQLite.select().from(Draw.class).where(conditions).querySingle();
        if (draw != null) {
            Utils.writelogFile(context, "draw != null y post draw(Broadcast, Repository)");
            post(BroadcastDrawEvent.DRAW, draw);
        } else {
            Utils.writelogFile(context, "draw == null y post error(Broadcast, Repository)");
            post(BroadcastDrawEvent.ERROR, context.getString(R.string.error_data_base));
        }
    }

    @Override
    public void getWinner(final Context context, final Draw draw) {
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
                                                post(BroadcastDrawEvent.WINNIER);
                                            } else {
                                                Utils.writelogFile(context, "drawAux data isEmpty or null(Broadcast, Repository)");
                                                setError(draw, context.getString(R.string.error_draw));
                                            }
                                        } else {
                                            Utils.writelogFile(context, "drawAux == null (Broadcast, Repository)");
                                            setError(draw, context.getString(R.string.error_draw));
                                        }
                                    } else if (responseWS.getSuccess().equals("4")) {
                                        draw.setIS_ZERO(1);
                                        draw.setDATE_UNIQUE(date);
                                        draw.setIS_ACTIVE(0);
                                        draw.update();
                                        post(BroadcastDrawEvent.WITHOUTPARTICIPATE);
                                    } else {
                                        Utils.writelogFile(context, "getSuccess = error " + responseWS.getMessage() + "(Broadcast, Repository)");
                                        setError(draw, responseWS.getMessage());
                                    }
                                } else {
                                    Utils.writelogFile(context, "responseWS == null (Broadcast, Repository)");
                                    setError(draw, context.getString(R.string.error_draw));
                                }
                            } else {
                                Utils.writelogFile(context, "!response.isSuccessful()(Broadcast, Repository)");
                                setError(draw, context.getString(R.string.error_draw));
                            }
                        }

                        @Override
                        public void onFailure(Call<ResultDraw> call, Throwable t) {
                            Utils.writelogFile(context, "Call error " + t.getMessage() + "(Broadcast, Repository)");
                            setError(draw, t.getMessage());
                        }
                    });
                } catch (Exception e) {
                    Utils.writelogFile(context, "catch error " + e.getMessage() + "(Broadcast, Repository)");
                    setError(draw, e.getMessage());
                }
            } else {
                Utils.writelogFile(context, "Internet error (Broadcast, Repository)");
                setError(draw, context.getString(R.string.error_internet));
            }
        } else {
            Utils.writelogFile(context, "Name isEmpty(Broadcast, Repository)");
            setError(draw, context.getString(R.string.error_draw));
        }
    }

    private void setError(Draw draw, String error){
        draw.setERROR_REPORTING(1);
        draw.update();
        post(BroadcastDrawEvent.ERROR, error);
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

    private void post(int type, Draw draw) {
        post(type, draw, null);
    }

    private void post(int type) {
        post(type, null, null);
    }

    private void post(int type, String error) {
        post(type, null, error);
    }

    private void post(int type, Draw draw, String error) {
        BroadcastDrawEvent event = new BroadcastDrawEvent();
        event.setType(type);
        event.setDraw(draw);
        event.setError(error);
        eventBus.post(event);
    }
}
