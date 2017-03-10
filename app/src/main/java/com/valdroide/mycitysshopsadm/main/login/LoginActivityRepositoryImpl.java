package com.valdroide.mycitysshopsadm.main.login;

import android.content.Context;

import com.raizlabs.android.dbflow.sql.language.Delete;
import com.valdroide.mycitysshopsadm.api.APIService;
import com.valdroide.mycitysshopsadm.entities.response.ResponseWS;
import com.valdroide.mycitysshopsadm.entities.response.ResultPlace;
import com.valdroide.mycitysshopsadm.entities.user.MyPlace;
import com.valdroide.mycitysshopsadm.entities.user.User;
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
    public void validateLogin(final Context context, final User user) {
        if (Utils.isNetworkAvailable(context)) {
            try {
                Call<ResultPlace> loginService = service.validateLogin(user.getUSER(),
                        user.getPASS(), user.getID_CITY_FOREIGN());
                loginService.enqueue(new Callback<ResultPlace>() {
                    @Override
                    public void onResponse(Call<ResultPlace> call, Response<ResultPlace> response) {
                        if (response.isSuccessful()) {
                            responseWS = response.body().getResponseWS();
                            if (responseWS != null) {
                                if (responseWS.getSuccess().equals("0")) {
                                    int id = responseWS.getId();
                                    if (id != 0) {
                                        user.setID_USER_KEY(id);
                                        user.save();
                                        Utils.setIdUser(context, id);
                                        post(LoginActivityEvent.LOGIN);
                                    } else {
                                        post(LoginActivityEvent.ERROR, Utils.ERROR_DATA_BASE);
                                    }
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
                    public void onFailure(Call<ResultPlace> call, Throwable t) {
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
