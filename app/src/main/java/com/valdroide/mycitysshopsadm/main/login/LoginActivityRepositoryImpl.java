package com.valdroide.mycitysshopsadm.main.login;

import android.content.Context;

import com.raizlabs.android.dbflow.sql.language.Delete;
import com.valdroide.mycitysshopsadm.R;
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
        Utils.writelogFile(context, "Metodo validateLogin y Se valida conexion a internet(Login, Repository)");
        if (Utils.isNetworkAvailable(context)) {
            try {
                Utils.writelogFile(context, "Call validateLogin(Login, Repository)");
                Call<ResultShop> loginService = service.validateLogin(user,
                        pass, id_city);
                loginService.enqueue(new Callback<ResultShop>() {
                    @Override
                    public void onResponse(Call<ResultShop> call, Response<ResultShop> response) {
                        if (response.isSuccessful()) {
                            Utils.writelogFile(context, "Response Successful y get ResponseWS(Login, Repository)");
                            responseWS = response.body().getResponseWS();
                            if (responseWS != null) {
                                Utils.writelogFile(context, "ResponseWS != null y valida getSuccess(Login, Repository)");
                                if (responseWS.getSuccess().equals("0")) {
                                    Utils.writelogFile(context, " getSuccess = 0 y getAccount(Login, Repository)");
                                    Shop shop = response.body().getShop();
                                    if (shop != null) {
                                        Utils.writelogFile(context, "shop != null y save shop (Login, Repository)");
                                        shop.save();
                                        Utils.setIdShop(context, shop.getID_SHOP_KEY());
                                        post(LoginActivityEvent.LOGIN);
                                    } else {
                                        Utils.writelogFile(context, " Base de datos error " + context.getString(R.string.error_data_base) + "(Login, Repository)");
                                        post(LoginActivityEvent.ERROR, context.getString(R.string.error_data_base));
                                    }
                                } else {
                                    Utils.writelogFile(context, " getSuccess = error " + responseWS.getMessage() + "(Login, Repository)");
                                    post(LoginActivityEvent.ERROR, responseWS.getMessage());
                                }
                            } else {
                                Utils.writelogFile(context, " Base de datos error " + context.getString(R.string.error_data_base) + "(Login, Repository)");
                                post(LoginActivityEvent.ERROR, context.getString(R.string.error_data_base));
                            }
                        } else {
                            Utils.writelogFile(context, " Base de datos error " + context.getString(R.string.error_data_base) + "(Login, Repository)");
                            post(LoginActivityEvent.ERROR, context.getString(R.string.error_data_base));
                        }
                    }

                    @Override
                    public void onFailure(Call<ResultShop> call, Throwable t) {
                        Utils.writelogFile(context, " Call error " + t.getMessage() + "(Login, Repository)");
                        post(LoginActivityEvent.ERROR, t.getMessage());
                    }
                });
            } catch (Exception e) {
                Utils.writelogFile(context, " catch error " + e.getMessage() + "(Login, Repository)");
                post(LoginActivityEvent.ERROR, e.getMessage());
            }
        } else {
            Utils.writelogFile(context, " Internet error " + context.getString(R.string.error_internet) + "(Login, Repository)");
            post(LoginActivityEvent.ERROR, context.getString(R.string.error_internet));
        }
    }

    @Override
    public void changePlace(Context context) {
        Utils.writelogFile(context, "Metodo changePlace (Login, Repository)");
        try {
            Utils.writelogFile(context, "delete MyPlace(Login, Repository)");
            Delete.table(MyPlace.class);
            Utils.writelogFile(context, "reset id city shared y post CHANGEPLACE(Login, Repository)");
            Utils.resetIdCity(context);
            post(LoginActivityEvent.CHANGEPLACE);
        } catch (Exception e) {
            Utils.writelogFile(context, " catch error " + e.getMessage() + "(Login, Repository)");
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
