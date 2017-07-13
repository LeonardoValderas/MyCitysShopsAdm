package com.valdroide.mycitysshopsadm.main.notification;

import android.content.Context;

import com.raizlabs.android.dbflow.sql.language.Delete;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.valdroide.mycitysshopsadm.R;
import com.valdroide.mycitysshopsadm.api.APIService;
import com.valdroide.mycitysshopsadm.entities.response.ResultPlace;
import com.valdroide.mycitysshopsadm.entities.response.ResultUser;
import com.valdroide.mycitysshopsadm.entities.shop.Account;
import com.valdroide.mycitysshopsadm.entities.response.ResponseWS;
import com.valdroide.mycitysshopsadm.entities.shop.DateShop;
import com.valdroide.mycitysshopsadm.entities.shop.Draw;
import com.valdroide.mycitysshopsadm.entities.shop.Login;
import com.valdroide.mycitysshopsadm.entities.shop.Login_Table;
import com.valdroide.mycitysshopsadm.entities.shop.Notification;
import com.valdroide.mycitysshopsadm.entities.shop.Notification_Table;
import com.valdroide.mycitysshopsadm.entities.shop.Offer;
import com.valdroide.mycitysshopsadm.entities.shop.Shop;
import com.valdroide.mycitysshopsadm.entities.shop.Support;
import com.valdroide.mycitysshopsadm.lib.base.EventBus;
import com.valdroide.mycitysshopsadm.main.notification.events.NotificationActivityEvent;
import com.valdroide.mycitysshopsadm.utils.Utils;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class NotificationActivityRepositoryImpl implements NotificationActivityRepository {
    private EventBus eventBus;
    private APIService service;
    private ResponseWS responseWS;
    private DateShop dateShop;
    private Notification notification;
    private DateShop dateUserWS;
    private Shop shop;
    private Account account;
    private Notification notifications;
    private List<Offer> offerss;
    private List<Draw> draws;
    private Support support;

    public NotificationActivityRepositoryImpl(EventBus eventBus, APIService service) {
        this.eventBus = eventBus;
        this.service = service;
    }

    @Override
    public void sendNotification(final Context context, final String message) {
        Utils.writelogFile(context, "Metodo sendNotification y Se valida conexion a internet(Notification, Repository)");
        if (Utils.isNetworkAvailable(context)) {
            Account account = getAccount(context);
            String user = getUser(context);
            if (account != null && user != null && !user.isEmpty()) {
                Utils.writelogFile(context, "account != null y getFechaOficialSeparate y getLastDayNotification(Notification, Repository)");
                final String date_unique = Utils.getFechaOficialSeparate();
                final String date_end = Utils.getLastDayNotification();
                if (!account.getURL_LOGO().isEmpty() && !account.getSHOP_NAME().isEmpty()) {
                    Utils.writelogFile(context, "logo y name != empty(Notification, Repository)");
                    try {
                        Utils.writelogFile(context, "Call sendNotification(Notification, Repository)");
                        Call<ResultPlace> notificationService = service.sendNotification(Utils.getIdShop(context), Utils.getIdCity(context),
                                account.getSHOP_NAME(), message, account.getURL_LOGO(), date_end, date_unique, user);
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
                                    Utils.writelogFile(context, "Base de datos error(Notification, Repository)");
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
                    Utils.writelogFile(context, "Internet error(Notification, Repository)");
                    post(NotificationActivityEvent.ERROR, context.getString(R.string.error_internet));
                }
            } else {
                Utils.writelogFile(context, "Base de datos error(Notification, Repository)");
                post(NotificationActivityEvent.ERROR, context.getString(R.string.error_data_base));
            }
        } else {
            Utils.writelogFile(context, "Base de datos error(Notification, Repository)");
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
                            Utils.writelogFile(context, "Base de datos error(Notification, Repository)");
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
            Utils.writelogFile(context, "Internet error(Notification, Repository)");
            post(NotificationActivityEvent.ERROR, context.getString(R.string.error_internet));
        }
    }

    private String getUser(Context context) {
        Utils.writelogFile(context, "getUser(Account, Repository)");
        return SQLite.select(Login_Table.USER).from(Login.class).querySingle().getUSER();
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

    @Override
    public void validateDateShop(final Context context) {
        Utils.writelogFile(context, "Metodo validateDateShop y Se valida conexion a internet(Notification, Repository)");
        DateShop dateShop = getDateShop();
        if (dateShop != null) {
            if (Utils.isNetworkAvailable(context)) {
                try {
                    Utils.writelogFile(context, "Call validateDateShop(Notification, Repository)");
                    Call<ResultUser> validateDateShop = service.validateDateShop(dateShop.getID_SHOP_FOREIGN(), Utils.getIdCity(context),
                            dateShop.getACCOUNT_DATE(), dateShop.getOFFER_DATE(), dateShop.getNOTIFICATION_DATE(), dateShop.getDRAW_DATE(),
                            dateShop.getDATE_SHOP_DATE(), dateShop.getSUPPORT_DATE());
                    validateDateShop.enqueue(new Callback<ResultUser>() {
                        @Override
                        public void onResponse(Call<ResultUser> call, Response<ResultUser> response) {
                            if (response.isSuccessful()) {
                                Utils.writelogFile(context, "Response Successful y get ResponseWS(Notification, Repository)");
                                responseWS = response.body().getResponseWS();
                                if (responseWS != null) {
                                    Utils.writelogFile(context, "ResponseWS != null y valida getSuccess(Notification, Repository)");
                                    if (responseWS.getSuccess().equals("0")) {
                                        Utils.writelogFile(context, " getSuccess = 0 y getDateShop(Notification, Repository)");
                                        dateUserWS = response.body().getDateShop();
                                        if (dateUserWS != null) {
                                            Utils.writelogFile(context, "dateUserWS != null y delete DateShop(Notification, Repository)");
                                            Delete.table(DateShop.class);
                                            Utils.writelogFile(context, "delete DateShop ok y save dateUserWS(Notification, Repository)");
                                            dateUserWS.save();
                                            Utils.writelogFile(context, "save dateUserWS y getAccount(Notification, Repository)");
                                        }
                                        account = response.body().getAccount();
                                        if (account != null) {
                                            Utils.writelogFile(context, "accounts != null y delete Offer(Notification, Repository)");
                                            Delete.table(Account.class);
                                            Utils.writelogFile(context, "delete Account ok y save accounts(Notification, Repository)");
                                            account.save();
                                            Utils.writelogFile(context, "save Account ok(Notification, Repository)");
                                        }
                                        shop = response.body().getShop();
                                        if (shop != null) {
                                            Utils.writelogFile(context, "shop != null y delete Shop(Notification, Repository)");
                                            Delete.table(Shop.class);
                                            Utils.writelogFile(context, "delete Shop ok y save shop(Notification, Repository)");
                                            shop.save();
                                            Utils.writelogFile(context, "save shop ok(DrawFragment, Notification)");
                                        }

                                        offerss = response.body().getOffers();
                                        if (offerss != null) {
                                            Utils.writelogFile(context, "offers != null(Notification, Repository)");
                                            if (offerss.size() > 0) {
                                                Utils.writelogFile(context, "offerss.size() > 0 y delete Offer(Notification, Repository)");
                                                Delete.table(Offer.class);
                                                Utils.writelogFile(context, "delete Offer ok y save offers(Notification, Repository)");
                                                for (Offer offer : offerss) {
                                                    Utils.writelogFile(context, "save offer: " + offer.getID_OFFER_KEY() + " (Notification, Repository)");
                                                    offer.save();
                                                }
                                                Utils.writelogFile(context, "fin FOR offer y getNotification (Notification, Repository)");
                                            } else {
                                                Utils.writelogFile(context, "offers == null y delete Offer(Notification, Repository)");
                                                Delete.table(Offer.class);
                                            }
                                        }
                                        draws = response.body().getDraws();
                                        if (draws != null) {
                                            Utils.writelogFile(context, "draws != null(Notification, Repository)");
                                            if (draws.size() > 0) {
                                                Utils.writelogFile(context, "draws.size() > 0 y delete Draw(Notification, Repository)");
                                                Delete.table(Draw.class);
                                                Utils.writelogFile(context, "delete draws ok y save Draw(Notification, Repository)");
                                                for (Draw draw : draws) {
                                                    Utils.writelogFile(context, "save draw: " + draw.getID_DRAW_KEY() + " (Notification, Repository)");
                                                    draw.save();
                                                }
                                                Utils.writelogFile(context, "fin FOR draw y getNotification(Notification, Repository)");
                                            } else {
                                                Utils.writelogFile(context, "draws == null y delete Draw(Notification, Repository)");
                                                Delete.table(Draw.class);
                                            }
                                        }

                                        notifications = response.body().getNotification();
                                        if (notifications != null) {
                                            Utils.writelogFile(context, "notification != null y delete Notification(Notification, Repository)");
                                            Delete.table(Notification.class);
                                            Utils.writelogFile(context, "delete Notification ok y save notification(Notification, Repository)");
                                            notifications.save();
                                            Utils.writelogFile(context, "save notification ok y getSupport(Notification, Repository)");
                                        }

                                        support = response.body().getSupport();
                                        if (support != null) {
                                            Utils.writelogFile(context, "support != null y delete Support(Notification, Repository)");
                                            Delete.table(Support.class);
                                            Utils.writelogFile(context, "delete Support ok y save support(Notification, Repository)");
                                            support.save();
                                            Utils.writelogFile(context, "save Support ok y GOTONAV(Notification, Repository)");
                                        }
                                        post(NotificationActivityEvent.UPDATESUCCESS);
                                    } else if (responseWS.getSuccess().equals("4")) {
                                        Utils.writelogFile(context, "getSuccess = 4 y post GOTONAV(Notification, Repository)");
                                        post(NotificationActivityEvent.UPDATESUCCESS);
                                    } else {
                                        Utils.writelogFile(context, "getSuccess = error " + responseWS.getMessage() + "(Notification, Repository)");
                                        post(NotificationActivityEvent.ERROR, responseWS.getMessage());
                                    }
                                }
                            } else {
                                Utils.writelogFile(context, "!isSuccess(Notification, Repository)");
                                post(NotificationActivityEvent.ERROR, context.getString(R.string.error_data_base));
                            }
                        }

                        @Override
                        public void onFailure(Call<ResultUser> call, Throwable t) {
                            Utils.writelogFile(context, "Call error " + t.getMessage() + "(Notification, Repository)");
                            post(NotificationActivityEvent.ERROR, t.getMessage());
                        }
                    });
                } catch (Exception e) {
                    Utils.writelogFile(context, "catch error " + e.getMessage() + "(Notification, Repository)");
                    post(NotificationActivityEvent.ERROR, e.getMessage());
                }
            } else {
                Utils.writelogFile(context, "Internet error(Notification, Repository)");
                post(NotificationActivityEvent.ERROR, context.getString(R.string.error_internet));
            }
        } else {
            Utils.writelogFile(context, "dateshop == null(Notification, Repository)");
            post(NotificationActivityEvent.ERROR, context.getString(R.string.error_data_base));
        }
    }

    private DateShop getDateShop() {
        return SQLite.select().from(DateShop.class).querySingle();
    }


    private void getDateShop(Context context, String date_edit) {
        Utils.writelogFile(context, "getDateShop(Notification, Repository)");
        try {
            dateShop = getDateShop();
            if (dateShop != null) {
                dateShop.setNOTIFICATION_DATE(date_edit);
                dateShop.setDATE_SHOP_DATE(date_edit);
                dateShop.update();
            } else {
                Utils.writelogFile(context, "Base de datos error(Notification, Repository)");
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
                Utils.writelogFile(context, "Base de datos error(Notification, Repository)");
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
