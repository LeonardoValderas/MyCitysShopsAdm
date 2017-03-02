package com.valdroide.mycitysshopsadm.main.notification;

import android.content.Context;

import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.valdroide.mycitysshopsadm.api.APIService;
import com.valdroide.mycitysshopsadm.entities.Shop;
import com.valdroide.mycitysshopsadm.entities.response.ResponseWS;
import com.valdroide.mycitysshopsadm.entities.response.Result;
import com.valdroide.mycitysshopsadm.lib.base.EventBus;
import com.valdroide.mycitysshopsadm.main.notification.events.NotificationActivityEvent;
import com.valdroide.mycitysshopsadm.utils.Utils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class NotificationActivityRepositoryImpl implements NotificationActivityRepository {
    private EventBus eventBus;
    private APIService service;
    ResponseWS responseWS;
    private Shop shop;

    public NotificationActivityRepositoryImpl(EventBus eventBus, APIService service) {
        this.eventBus = eventBus;
        this.service = service;
    }

    @Override
    public void sendNotification(Context context, String notification) {
        if (Utils.isNetworkAvailable(context)) {
            String shop_name = getTitleShop();
            try {
                Call<Result> notificationService = service.sendNotification(shop_name,
                        notification);
                notificationService.enqueue(new Callback<Result>() {
                    @Override
                    public void onResponse(Call<Result> call, Response<Result> response) {
                        if (response.isSuccessful()) {
                            responseWS = response.body().getResponseWS();
                            if (responseWS != null) {
                                if (responseWS.getSuccess().equals("0")) {
                                    post(NotificationActivityEvent.SEND);
                                } else {
                                    post(NotificationActivityEvent.ERROR, Utils.ERROR_DATA_BASE);
                                }
                            } else {
                                post(NotificationActivityEvent.ERROR, responseWS.getMessage());
                            }
                        } else {
                            post(NotificationActivityEvent.ERROR, Utils.ERROR_DATA_BASE);
                        }
                    }

                    @Override
                    public void onFailure(Call<Result> call, Throwable t) {
                        post(NotificationActivityEvent.ERROR, t.getMessage());
                    }
                });
            } catch (Exception e) {
                post(NotificationActivityEvent.ERROR, e.getMessage());
            }
        } else {
            post(NotificationActivityEvent.ERROR, Utils.ERROR_INTERNET);
        }
    }

    public String getTitleShop() {
        shop = SQLite.select().from(Shop.class).querySingle();
        if (shop != null)
            return shop.getSHOP();
        else
            return "";
    }

    public void post(int type) {
        post(type, null);
    }

    public void post(int type, String error) {
        NotificationActivityEvent event = new NotificationActivityEvent();
        event.setType(type);
        event.setError(error);
        eventBus.post(event);
    }
}
