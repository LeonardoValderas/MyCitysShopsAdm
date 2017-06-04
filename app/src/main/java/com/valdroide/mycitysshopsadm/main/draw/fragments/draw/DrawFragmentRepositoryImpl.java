package com.valdroide.mycitysshopsadm.main.draw.fragments.draw;

import android.content.Context;

import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.valdroide.mycitysshopsadm.R;
import com.valdroide.mycitysshopsadm.api.APIService;
import com.valdroide.mycitysshopsadm.entities.response.ResponseWS;
import com.valdroide.mycitysshopsadm.entities.shop.Account;
import com.valdroide.mycitysshopsadm.entities.shop.Account_Table;
import com.valdroide.mycitysshopsadm.entities.shop.Draw;
import com.valdroide.mycitysshopsadm.lib.base.EventBus;
import com.valdroide.mycitysshopsadm.main.draw.fragments.draw.events.DrawFragmentEvent;
import com.valdroide.mycitysshopsadm.utils.Utils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class DrawFragmentRepositoryImpl implements DrawFragmentRepository {
    private EventBus eventBus;
    private APIService service;
    private Account account = new Account();

    public DrawFragmentRepositoryImpl(EventBus eventBus, APIService service) {
        this.eventBus = eventBus;
        this.service = service;
    }

    @Override
    public void createDraw(final Context context, final Draw draw) {
        Utils.writelogFile(context, "Metodo createDraw y se valida draw(DrawFragment, Repository)");
        if (draw != null) {
            Utils.writelogFile(context, "draw != null y Se valida conexion a internet(DrawFragment, Repository)");
            String name = getShopName(context);
            if (!name.isEmpty()) {
                if (Utils.isNetworkAvailable(context)) {
                    try {
                        Utils.writelogFile(context, "Call createDraw(DrawFragment, Repository)");
                        Call<ResponseWS> drawService = service.createDraw(Utils.getIdShop(context), Utils.getIdCity(context),
                                draw.getDESCRIPTION(), draw.getFOR_FOLLOWING(), draw.getCONDITION(),
                                draw.getSTART_DATE(), draw.getEND_DATE(), draw.getLIMIT_DATE(), draw.getURL_LOGO(),
                                draw.getNAME_LOGO(), draw.getDATE_UNIQUE(), draw.getEncode(), name);
                        drawService.enqueue(new Callback<ResponseWS>() {
                            @Override
                            public void onResponse(Call<ResponseWS> call, Response<ResponseWS> response) {
                                if (response != null) {
                                    Utils.writelogFile(context, "response != null y validate Response is Successful(DrawFragment, Repository)");
                                    if (response.isSuccessful()) {
                                        Utils.writelogFile(context, "Response Successful y get getSuccess()(DrawFragment, Repository)");
                                        if (response.body().getSuccess().equals("0")) {
                                            Utils.writelogFile(context, "getSuccess = 0 y draw save()(DrawFragment, Repository)");
                                            int id = response.body().getId();
                                            if (id != 0) {
                                                Utils.writelogFile(context, "id != 0 y draw save()(DrawFragment, Repository)");
                                                draw.setID_DRAW_KEY(id);
                                                draw.save();
                                                post(DrawFragmentEvent.CREATEDRAW);
                                            } else {
                                                Utils.writelogFile(context, "id = 0 y post error(DrawFragment, Repository)");
                                                post(DrawFragmentEvent.ERROR, context.getString(R.string.error_data_base) + " ID incorrecto.");
                                            }
                                        } else {
                                            Utils.writelogFile(context, "getSuccess = error " + response.body().getMessage() + "(DrawFragment, Repository)");
                                            post(DrawFragmentEvent.ERROR, response.body().getMessage());
                                        }
                                    } else {
                                        Utils.writelogFile(context, "!response.isSuccessful()" + context.getString(R.string.error_data_base) + "(DrawFragment, Repository)");
                                        post(DrawFragmentEvent.ERROR, context.getString(R.string.error_data_base));
                                    }
                                } else {
                                    Utils.writelogFile(context, "response == null" + context.getString(R.string.error_data_base) + "(DrawFragment, Repository)");
                                    post(DrawFragmentEvent.ERROR, context.getString(R.string.error_data_base));
                                }
                            }

                            @Override
                            public void onFailure(Call<ResponseWS> call, Throwable t) {
                                Utils.writelogFile(context, " Call error " + t.getMessage() + "(DrawFragment, Repository)");
                                post(DrawFragmentEvent.ERROR, t.getMessage());
                            }
                        });
                    } catch (Exception e) {
                        Utils.writelogFile(context, "catch error " + e.getMessage() + "(DrawFragment, Repository)");
                        post(DrawFragmentEvent.ERROR, e.getMessage());
                    }
                } else {
                    Utils.writelogFile(context, "Internet error " + context.getString(R.string.error_internet) + "(DrawFragment, Repository)");
                    post(DrawFragmentEvent.ERROR, context.getString(R.string.error_internet));
                }
            } else {
                Utils.writelogFile(context, "Name isEmpty" + context.getString(R.string.error_internet) + "(DrawFragment, Repository)");
                post(DrawFragmentEvent.ERROR, context.getString(R.string.error_draw_null));
            }
        } else {
            Utils.writelogFile(context, "Draw null " + context.getString(R.string.error_internet) + "(DrawFragment, Repository)");
            post(DrawFragmentEvent.ERROR, context.getString(R.string.error_draw_null));
        }
    }

    private String getShopName(Context context) {
        try {
            Utils.writelogFile(context, "Metodo getShopName (DrawFragment, Repository)");
            return SQLite.select(Account_Table.SHOP_NAME).from(Account.class).querySingle().getSHOP_NAME();
        } catch (Exception e) {
            Utils.writelogFile(context, "catch error: " + e.getMessage() + "(DrawFragment, Repository)");
            return "";
        }
    }

    public void post(int type) {
        post(type, null);
    }

    public void post(int type, String error) {
        DrawFragmentEvent event = new DrawFragmentEvent();
        event.setType(type);
        event.setError(error);
        eventBus.post(event);
    }


}
