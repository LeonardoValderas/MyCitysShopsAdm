package com.valdroide.mycitysshopsadm.main.login;

import android.content.Context;

import com.raizlabs.android.dbflow.sql.language.Delete;
import com.valdroide.mycitysshopsadm.api.APIService;
import com.valdroide.mycitysshopsadm.entities.response.ResponseWS;
import com.valdroide.mycitysshopsadm.entities.response.ResultShop;
import com.valdroide.mycitysshopsadm.entities.shop.MyPlace;
import com.valdroide.mycitysshopsadm.entities.shop.Shop;
import com.valdroide.mycitysshopsadm.lib.base.EventBus;
import com.valdroide.mycitysshopsadm.main.login.events.LoginActivityEvent;
import com.valdroide.mycitysshopsadm.utils.Utils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivityRepositoryImpl implements LoginActivityRepository {
    private EventBus eventBus;
    private APIService service;
    private ResponseWS responseWS;

    public LoginActivityRepositoryImpl(EventBus eventBus, APIService service) {
        this.eventBus = eventBus;
        this.service = service;
    }

    @Override
    public void validateLogin(final Context context, String user, String pass, int id_city) {
        if (Utils.isNetworkAvailable(context)) {
            try {
                Call<ResultShop> loginService = service.validateLogin(user,
                        pass, id_city);
                loginService.enqueue(new Callback<ResultShop>() {
                    @Override
                    public void onResponse(Call<ResultShop> call, Response<ResultShop> response) {
                        if (response.isSuccessful()) {
                            responseWS = response.body().getResponseWS();
                            if (responseWS != null) {
                                if (responseWS.getSuccess().equals("0")) {
                                    Shop shop = response.body().getShop();
                                    shop.save();
                                    Utils.setIdShop(context, shop.getID_SHOP_KEY());
                                    post(LoginActivityEvent.LOGIN);
                                } else {
                                    post(LoginActivityEvent.ERROR, responseWS.getMessage());
                                }
                            } else {
                                post(LoginActivityEvent.ERROR, Utils.ERROR_DATA_BASE);
                            }
                        } else {
                            post(LoginActivityEvent.ERROR, Utils.ERROR_DATA_BASE);
                        }
                    }

                    @Override
                    public void onFailure(Call<ResultShop> call, Throwable t) {
                        post(LoginActivityEvent.ERROR, t.getMessage());
                    }
                });
            } catch (Exception e) {
                post(LoginActivityEvent.ERROR, e.getMessage());
            }
        } else {
            post(LoginActivityEvent.ERROR, Utils.ERROR_INTERNET);
        }
    }

    @Override
    public void changePlace(Context context) {
        try {
            Delete.table(MyPlace.class);
            Utils.resetIdCity(context);
            post(LoginActivityEvent.CHANGEPLACE);
        } catch (Exception e) {
            post(LoginActivityEvent.ERROR, e.getMessage());
        }
    }

    public void post(int type) {
        post(type, null);
    }

    public void post(int type, String error) {
        LoginActivityEvent event = new LoginActivityEvent();
        event.setType(type);
        event.setError(error);
        eventBus.post(event);
    }
}
