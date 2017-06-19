package com.valdroide.mycitysshopsadm.main.notification;

import android.content.Context;

import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.valdroide.mycitysshopsadm.R;
import com.valdroide.mycitysshopsadm.api.APIService;
import com.valdroide.mycitysshopsadm.entities.response.ResultPlace;
import com.valdroide.mycitysshopsadm.entities.shop.Account;
import com.valdroide.mycitysshopsadm.entities.response.ResponseWS;
import com.valdroide.mycitysshopsadm.entities.shop.DateShop;
import com.valdroide.mycitysshopsadm.entities.shop.Notification;
import com.valdroide.mycitysshopsadm.entities.shop.Notification_Table;
import com.valdroide.mycitysshopsadm.entities.shop.Shop;
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
    private Notification notification;

    public NotificationActivityRepositoryImpl(EventBus eventBus, APIService service) {
        this.eventBus = eventBus;
        this.service = service;
    }

    @Override
    public void sendNotification(final Context context, final String message) {
        Utils.writelogFile(context, "Metodo sendNotification y Se valida conexion a internet(Notification, Repository)");
        if (Utils.isNetworkAvailable(context)) {
            Account account = getAccount(context);
            if (account != null) {
                Utils.writelogFile(context, "account != null y getFechaOficialSeparate y getLastDayNotification(Notification, Repository)");
                final String date_unique = Utils.getFechaOficialSeparate();
                final String date_end = Utils.getLastDayNotification();
                if (!account.getURL_LOGO().isEmpty() && !account.getSHOP_NAME().isEmpty()) {
                    Utils.writelogFile(context, "logo y name != empty(Notification, Repository)");
                    try {
                        Utils.writelogFile(context, "Call sendNotification(Notification, Repository)");
                        Call<ResultPlace> notificationService = service.sendNotification(Utils.getIdShop(context), Utils.getIdCity(context),
                                account.getSHOP_NAME(), message, account.getURL_LOGO(), date_end, date_unique);
                        notificationService.enqueue(new Callback<ResultPlace>() {
                            @Override
                            public void onResponse(Call<ResultPlace> call, Response<ResultPlace> response) {
                                if (response.isSuccessful()) {
                                    Utils.writelogFile(context, "Response Successful y get ResponseWS(Notification, Repository)");
                                    responseWS = response.body().getResponseWS();
                                    if (responseWS != null) {
                                        Utils.writelogFile(context, "ResponseWS != null y valida getSuccess(Notification, Repository)");
                                        if (responseWS.getSuccess().equals("0")) {
                                            Utils.writelogFile(context, "getSuccess = 0 y getDateShop(Notification, Repository)");
                                            getNotification(context, date_end);
                                            getDateShop(context, date_unique);
                                            post(NotificationActivityEvent.SEND, false, date_end);
                                        } else {
                                            Utils.writelogFile(context, "getSuccess != 0 " + responseWS.getMessage() + "(Notification, Repository)");
                                            post(NotificationActivityEvent.ERROR, responseWS.getMessage());
                                        }
                                    } else {
                                        Utils.writelogFile(context, "responseWS == null (Notification, Repository)");
                                        post(NotificationActivityEvent.ERROR, context.getString(R.string.error_data_base));
                                    }
                                } else {
                                    Utils.writelogFile(context, " Base de datos error " + context.getString(R.string.error_data_base) + "(Notification, Repository)");
                                    post(NotificationActivityEvent.ERROR, context.getString(R.string.error_data_base));
                                }
                            }

                            @Override
                            public void onFailure(Call<ResultPlace> call, Throwable t) {
                                Utils.writelogFile(context, " Call error " + t.getMessage() + "(Notification, Repository)");
                                post(NotificationActivityEvent.ERROR, t.getMessage());
                            }
                        });
                    } catch (Exception e) {
                        Utils.writelogFile(context, "getDateShop error: " + e.getMessage() + "(Notification, Repository)");
                        post(NotificationActivityEvent.ERROR, e.getMessage());
                    }
                } else {
                    Utils.writelogFile(context, " Internet error " + context.getString(R.string.error_internet) + "(Notification, Repository)");
                    post(NotificationActivityEvent.ERROR, context.getString(R.string.error_internet));
                }
            } else {
                Utils.writelogFile(context, " Base de datos error " + context.getString(R.string.error_data_base) + "(Notification, Repository)");
                post(NotificationActivityEvent.ERROR, context.getString(R.string.error_data_base));
            }
        } else {
            Utils.writelogFile(context, " Base de datos error " + context.getString(R.string.error_data_base) + "(Notification, Repository)");
            post(NotificationActivityEvent.ERROR, context.getString(R.string.error_data_base));
        }
    }

    @Override
    public void validateNotificationExpire(Context context, String now) {
        Utils.writelogFile(context, "validateNotificationExpire(Notification, Repository)");
        try {
            String date = SQLite.select(Notification_Table.DATE_END).from(Notification.class).querySingle().getDATE_END();
            boolean is_available = Utils.validateExpirateCurrentTime(date, context.getString(R.string.format_notification));
            post(NotificationActivityEvent.ISAVAILABLE, is_available, date);
        } catch (Exception e) {
            Utils.writelogFile(context, "validateNotificationExpire error: " + e.getMessage() + "(Notification, Repository)");
            post(NotificationActivityEvent.ERROR, e.getMessage());
        }
    }

    @Override
    public void sendNotificationAdm(final Context context, String notification) {
        Utils.writelogFile(context, "Metodo sendNotificationAdm y Se valida conexion a internet(Notification, Repository)");
        if (Utils.isNetworkAvailable(context)) {
            try {
                Utils.writelogFile(context, "Call sendNotification(Notification, Repository)");
                Call<ResponseWS> notificationService = service.sendNotificationAdm(Utils.getIdCity(context), notification);
                notificationService.enqueue(new Callback<ResponseWS>() {
                    @Override
                    public void onResponse(Call<ResponseWS> call, Response<ResponseWS> response) {
                        if (response.isSuccessful()) {
                            Utils.writelogFile(context, "Response Successful y get ResponseWS(Notification, Repository)");
                            if (response.body().getSuccess().equals("0")) {
                                Utils.writelogFile(context, "getSuccess = 0 y post(Notification, Repository)");
                                post(NotificationActivityEvent.SENDADM);
                            } else {
                                Utils.writelogFile(context, "getSuccess != 0 " + response.body().getMessage() + "(Notification, Repository)");
                                post(NotificationActivityEvent.ERROR, response.body().getMessage());
                            }
                        } else {
                            Utils.writelogFile(context, " Base de datos error " + context.getString(R.string.error_data_base) + "(Notification, Repository)");
                            post(NotificationActivityEvent.ERROR, context.getString(R.string.error_data_base));
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseWS> call, Throwable t) {
                        Utils.writelogFile(context, " Call error " + t.getMessage() + "(Notification, Repository)");
                        post(NotificationActivityEvent.ERROR, t.getMessage());
                    }
                });
            } catch (Exception e) {
                Utils.writelogFile(context, "getDateShop error: " + e.getMessage() + "(Notification, Repository)");
                post(NotificationActivityEvent.ERROR, e.getMessage());
            }
        } else {
            Utils.writelogFile(context, " Internet error " + context.getString(R.string.error_internet) + "(Notification, Repository)");
            post(NotificationActivityEvent.ERROR, context.getString(R.string.error_internet));
        }
    }

    private Account getAccount(Context context) {
        Utils.writelogFile(context, "getAccount(Notification, Repository)");
        try {
            return SQLite.select().from(Account.class).querySingle();
        } catch (Exception e) {
            Utils.writelogFile(context, "getAccount error: " + e.getMessage() + "(Notification, Repository)");
            return null;
        }
    }

    private void getDateShop(Context context, String date_edit) {
        Utils.writelogFile(context, "getDateShop(Notification, Repository)");
        try {
            dateShop = SQLite.select().from(DateShop.class).querySingle();
            if (dateShop != null) {
                dateShop.setNOTIFICATION_DATE(date_edit);
                dateShop.setDATE_SHOP_DATE(date_edit);
                dateShop.update();
            } else {
                Utils.writelogFile(context, " Base de datos error " + context.getString(R.string.error_data_base) + "(Notification, Repository)");
                post(NotificationActivityEvent.ERROR, context.getString(R.string.error_data_base));
            }
        } catch (Exception e) {
            Utils.writelogFile(context, "getDateShop error: " + e.getMessage() + "(Notification, Repository)");
            post(NotificationActivityEvent.ERROR, e.getMessage());
        }
    }

    private void getNotification(Context context, String date_end) {
        Utils.writelogFile(context, "getNotification(Notification, Repository)");
        try {
            notification = SQLite.select().from(Notification.class).querySingle();
            if (notification != null) {
                notification.setDATE_END(date_end);
                notification.update();
            } else {
                Utils.writelogFile(context, " Base de datos error " + context.getString(R.string.error_data_base) + "(Notification, Repository)");
                post(NotificationActivityEvent.ERROR, context.getString(R.string.error_data_base));
            }
        } catch (Exception e) {
            Utils.writelogFile(context, "getNotification error: " + e.getMessage() + "(Notification, Repository)");
            post(NotificationActivityEvent.ERROR, e.getMessage());
        }
    }

    private void post(int type) {
        post(type, false, null, null);
    }

    private void post(int type, String error) {
        post(type, false, null, error);
    }

    private void post(int type, boolean is_available, String date) {
        post(type, is_available, date, null);
    }

    private void post(int type, boolean is_available, String date, String error) {
        NotificationActivityEvent event = new NotificationActivityEvent();
        event.setType(type);
        event.setIs_available(is_available);
        event.setDate(date);
        event.setError(error);
        eventBus.post(event);
    }
}
