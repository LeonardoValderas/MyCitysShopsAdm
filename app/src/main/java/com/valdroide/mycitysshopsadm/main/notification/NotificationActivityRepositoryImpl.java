package com.valdroide.mycitysshopsadm.main.notification;

import android.content.Context;

import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.valdroide.mycitysshopsadm.api.APIService;
import com.valdroide.mycitysshopsadm.entities.response.ResultPlace;
import com.valdroide.mycitysshopsadm.entities.shop.Account;
import com.valdroide.mycitysshopsadm.entities.shop.Account_Table;
import com.valdroide.mycitysshopsadm.entities.response.ResponseWS;
import com.valdroide.mycitysshopsadm.entities.shop.DateShop;
import com.valdroide.mycitysshopsadm.lib.base.EventBus;
import com.valdroide.mycitysshopsadm.main.notification.events.NotificationActivityEvent;
import com.valdroide.mycitysshopsadm.utils.Utils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class NotificationActivityRepositoryImpl implements NotificationActivityRepository {
    private EventBus eventBus;
    private APIService service;
    private ResponseWS responseWS;
    private DateShop dateShop;

    public NotificationActivityRepositoryImpl(EventBus eventBus, APIService service) {
        this.eventBus = eventBus;
        this.service = service;
    }

    @Override
    public void sendNotification(Context context, String notification) {
        if (Utils.isNetworkAvailable(context)) {
            String shop_name = getTitleShop();
            final String date_init = Utils.getFechaOficial();
            if (!shop_name.isEmpty()) {
                try {
                    Call<ResultPlace> notificationService = service.sendNotification(Utils.getIdShop(context), shop_name,
                            notification, date_init);
                    notificationService.enqueue(new Callback<ResultPlace>() {
                        @Override
                        public void onResponse(Call<ResultPlace> call, Response<ResultPlace> response) {
                            if (response.isSuccessful()) {
                                responseWS = response.body().getResponseWS();
                                if (responseWS != null) {
                                    if (responseWS.getSuccess().equals("0")) {
                                        getDateShop(date_init);
                                        dateShop.update();
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
        } else {
            post(NotificationActivityEvent.ERROR, Utils.ERROR_DATA_BASE);
        }
    }

    public String getTitleShop() {
        try {
            String name = SQLite.select(Account_Table.SHOP_NAME).from(Account.class).getQuery();
            if (name != null)
                return name;
            else
                return "";
        } catch (Exception e) {
            return "";
        }
    }

    public void getDateShop(String date_edit) {
        dateShop = SQLite.select().from(DateShop.class).querySingle();
        dateShop.setNOTIFICATION_DATE(date_edit);
        dateShop.setDATE_USER_DATE(date_edit);
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
