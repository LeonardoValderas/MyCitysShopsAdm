package com.valdroide.mycitysshopsadm.main.notification;

import android.content.Context;

import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.valdroide.mycitysshopsadm.api.APIService;
import com.valdroide.mycitysshopsadm.entities.response.ResultPlace;
import com.valdroide.mycitysshopsadm.entities.user.Shop;
import com.valdroide.mycitysshopsadm.entities.response.ResponseWS;
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
                Call<ResultPlace> notificationService = service.sendNotification(shop_name,
                        notification);
                notificationService.enqueue(new Callback<ResultPlace>() {
                    @Override
                    public void onResponse(Call<ResultPlace> call, Response<ResultPlace> response) {
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
                    public void onFailure(Call<ResultPlace> call, Throwable t) {
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
        try {
            shop = SQLite.select().from(Shop.class).querySingle();
            if (shop != null)
                return shop.getSHOP();
            else
                return "";
        } catch (Exception e) {
            return "";
        }
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
