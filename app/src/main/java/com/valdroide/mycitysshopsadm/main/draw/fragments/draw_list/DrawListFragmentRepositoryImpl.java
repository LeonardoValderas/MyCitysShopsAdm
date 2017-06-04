package com.valdroide.mycitysshopsadm.main.draw.fragments.draw_list;

import android.content.Context;

import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.valdroide.mycitysshopsadm.R;
import com.valdroide.mycitysshopsadm.api.APIService;
import com.valdroide.mycitysshopsadm.entities.response.ResponseWS;
import com.valdroide.mycitysshopsadm.entities.shop.Account;
import com.valdroide.mycitysshopsadm.entities.shop.Account_Table;
import com.valdroide.mycitysshopsadm.entities.shop.DateShop;
import com.valdroide.mycitysshopsadm.entities.shop.Draw;
import com.valdroide.mycitysshopsadm.lib.base.EventBus;
import com.valdroide.mycitysshopsadm.main.draw.fragments.draw.events.DrawFragmentEvent;
import com.valdroide.mycitysshopsadm.main.draw.fragments.draw_list.events.DrawListFragmentEvent;
import com.valdroide.mycitysshopsadm.main.offer.events.OfferActivityEvent;
import com.valdroide.mycitysshopsadm.utils.Utils;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DrawListFragmentRepositoryImpl implements DrawListFragmentRepository {
    private EventBus eventBus;
    private APIService service;
    private DateShop dateShop;

    public DrawListFragmentRepositoryImpl(EventBus eventBus, APIService service) {
        this.eventBus = eventBus;
        this.service = service;
    }

    @Override
    public void getDraws(Context context) {
        Utils.writelogFile(context, "Metodo getDraws, draws(DrawListFragment, Repository)");
        try {
            List<Draw> draws = SQLite.select().from(Draw.class).queryList();
            if (draws != null) {
                Utils.writelogFile(context, "draws != null, post(DrawListFragment, Repository)");
                post(DrawListFragmentEvent.DRAWS, draws);
            } else {
                Utils.writelogFile(context, "draws == null, post(DrawListFragment, Repository)");
                post(DrawListFragmentEvent.ERROR, context.getString(R.string.error_data_base));
            }
        } catch (Exception e) {
            Utils.writelogFile(context, "catch error, post(DrawListFragment, Repository)");
            post(DrawListFragmentEvent.ERROR, e.getMessage());
        }
    }

    @Override
    public void cancelDraw(final Context context, final Draw draw) {
        Utils.writelogFile(context, "Metodo cancelDraw y se valida draw(DrawListFragment, Repository)");
        if (draw != null) {
            Utils.writelogFile(context, "draw != null y Se valida conexion a internet(DrawListFragment, Repository)");
            String name = getShopName(context);
            if (!name.isEmpty()) {
                if (Utils.isNetworkAvailable(context)) {
                    try {
                        Utils.writelogFile(context, "Call createDraw(DrawListFragment, Repository)");
                        draw.setDATE_UNIQUE(Utils.getFechaOficialSeparate());
                        Call<ResponseWS> drawService = service.cancelDraw(Utils.getIdShop(context), Utils.getIdCity(context),
                                draw.getID_DRAW_KEY(), draw.getIS_CANCEL(), draw.getIS_TAKE(),
                                draw.getIS_LIMIT(), draw.getFOR_FOLLOWING(), name, draw.getDATE_UNIQUE());
                        drawService.enqueue(new Callback<ResponseWS>() {
                            @Override
                            public void onResponse(Call<ResponseWS> call, Response<ResponseWS> response) {
                                if (response != null) {
                                    Utils.writelogFile(context, "response != null y validate Response is Successful(DrawListFragment, Repository)");
                                    if (response.isSuccessful()) {
                                        Utils.writelogFile(context, "Response Successful y get getSuccess()(DrawListFragment, Repository)");
                                        if (response.body().getSuccess().equals("0")) {
                                            Utils.writelogFile(context, "getSuccess = 0 y updateDateShop(DrawListFragment, Repository)");
                                            updateDateShop(context, draw.getDATE_UNIQUE());
                                            Utils.writelogFile(context, "updateDateShop ok y draw.delete()(DrawListFragment, Repository)");
                                            draw.delete();
                                            post(DrawListFragmentEvent.CANCELSUCCESS);
                                        } else {
                                            Utils.writelogFile(context, "getSuccess = error " + response.body().getMessage() + "(DrawListFragment, Repository)");
                                            post(DrawListFragmentEvent.ERROR, response.body().getMessage());
                                        }
                                    } else {
                                        Utils.writelogFile(context, "!response.isSuccessful()" + context.getString(R.string.error_data_base) + "(DrawListFragment, Repository)");
                                        post(DrawListFragmentEvent.ERROR, context.getString(R.string.error_data_base));
                                    }
                                } else {
                                    Utils.writelogFile(context, "response == null" + context.getString(R.string.error_data_base) + "(DrawListFragment, Repository)");
                                    post(DrawListFragmentEvent.ERROR, context.getString(R.string.error_data_base));
                                }
                            }

                            @Override
                            public void onFailure(Call<ResponseWS> call, Throwable t) {
                                Utils.writelogFile(context, " Call error " + t.getMessage() + "(DrawListFragment, Repository)");
                                post(DrawListFragmentEvent.ERROR, t.getMessage());
                            }
                        });
                    } catch (Exception e) {
                        Utils.writelogFile(context, "catch error " + e.getMessage() + "(DrawListFragment, Repository)");
                        post(DrawListFragmentEvent.ERROR, e.getMessage());
                    }
                } else {
                    Utils.writelogFile(context, "Internet error " + context.getString(R.string.error_internet) + "(DrawListFragment, Repository)");
                    post(DrawListFragmentEvent.ERROR, context.getString(R.string.error_internet));
                }
            } else {
                Utils.writelogFile(context, "Name isEmpty" + context.getString(R.string.error_internet) + "(DrawListFragment, Repository)");
                post(DrawListFragmentEvent.ERROR, context.getString(R.string.error_draw_null));
            }
        } else {
            Utils.writelogFile(context, "Draw null " + context.getString(R.string.error_internet) + "(DrawListFragment, Repository)");
            post(DrawListFragmentEvent.ERROR, context.getString(R.string.error_draw_null));
        }
    }

    private String getShopName(Context context) {
        try {
            Utils.writelogFile(context, "Metodo getShopName (DrawListFragment, Repository)");
            return SQLite.select(Account_Table.SHOP_NAME).from(Account.class).querySingle().getSHOP_NAME();
        } catch (Exception e) {
            Utils.writelogFile(context, "catch error: " + e.getMessage() + "(DrawListFragment, Repository)");
            return "";
        }
    }
    private void updateDateShop(Context context, String date_edit) {
        Utils.writelogFile(context, "Metodo updateDateShop (DrawListFragment, Repository)");
        try {
            dateShop = SQLite.select().from(DateShop.class).querySingle();
            if (dateShop != null) {
                dateShop.setDRAW_DATE(date_edit);
                dateShop.setDATE_SHOP_DATE(date_edit);
                dateShop.update();
            }
        } catch (Exception e) {
            Utils.writelogFile(context, "catch error " + e.getMessage() + "(DrawListFragment, Repository)");
            post(OfferActivityEvent.ERROR, e.getMessage());
        }
    }

    private void post(int type) {
        post(type, null, null);
    }

    private void post(int type, List<Draw> draws) {
        post(type, draws, null);
    }

    private void post(int type, String error) {
        post(type, null, error);
    }

    private void post(int type, List<Draw> draws, String error) {
        DrawListFragmentEvent event = new DrawListFragmentEvent();
        event.setType(type);
        event.setDrawList(draws);
        event.setError(error);
        eventBus.post(event);
    }
}
