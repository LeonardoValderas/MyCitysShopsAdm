package com.valdroide.mycitysshopsadm.main.draw.fragments.draw;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.raizlabs.android.dbflow.sql.language.Condition;
import com.raizlabs.android.dbflow.sql.language.ConditionGroup;
import com.raizlabs.android.dbflow.sql.language.Delete;
import com.raizlabs.android.dbflow.sql.language.NameAlias;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.valdroide.mycitysshopsadm.R;
import com.valdroide.mycitysshopsadm.api.APIService;
import com.valdroide.mycitysshopsadm.entities.place.City;
import com.valdroide.mycitysshopsadm.entities.place.City_Table;
import com.valdroide.mycitysshopsadm.entities.response.ResponseWS;
import com.valdroide.mycitysshopsadm.entities.response.ResultUser;
import com.valdroide.mycitysshopsadm.entities.shop.Account;
import com.valdroide.mycitysshopsadm.entities.shop.DateShop;
import com.valdroide.mycitysshopsadm.entities.shop.Draw;
import com.valdroide.mycitysshopsadm.entities.shop.Draw_Table;
import com.valdroide.mycitysshopsadm.entities.shop.Login;
import com.valdroide.mycitysshopsadm.entities.shop.Login_Table;
import com.valdroide.mycitysshopsadm.entities.shop.Notification;
import com.valdroide.mycitysshopsadm.entities.shop.Offer;
import com.valdroide.mycitysshopsadm.entities.shop.Shop;
import com.valdroide.mycitysshopsadm.entities.shop.Support;
import com.valdroide.mycitysshopsadm.lib.base.EventBus;
import com.valdroide.mycitysshopsadm.main.draw.broadcast.ui.BroadcastDraw;
import com.valdroide.mycitysshopsadm.main.draw.fragments.draw.events.DrawFragmentEvent;
import com.valdroide.mycitysshopsadm.utils.Utils;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class DrawFragmentRepositoryImpl implements DrawFragmentRepository {
    private EventBus eventBus;
    private APIService service;
    private Intent intentAlarm;
    private static final int REQUEST_CODE = 0;
    private PendingIntent pendingIntent;
    private AlarmManager alarmManager;
    private DateShop dateUserWS;
    private Account accounts;
    private Shop shop;
    private Notification notification;
    private List<Offer> offers;
    private List<Draw> draws;
    private Support support;
    private ResponseWS responseWS;

    public DrawFragmentRepositoryImpl(EventBus eventBus, APIService service) {
        this.eventBus = eventBus;
        this.service = service;
    }

    @Override
    public void createDraw(final Context context, final Draw draw) {
        Utils.writelogFile(context, "Metodo createDraw y se valida draw(DrawFragment, Repository)");
        if (draw != null) {
            if (dateEndExist(context, draw.getEND_DATE())) {
                Utils.writelogFile(context, "draw != null y Se valida conexion a internet(DrawFragment, Repository)");
                String name = Utils.getNameShop(context);
                String user = getUser(context);
                if (!name.isEmpty() && user != null && !user.isEmpty()) {
                    if (Utils.isNetworkAvailable(context)) {
                        try {
                            Utils.writelogFile(context, "Call createDraw(DrawFragment, Repository)");
                            Call<ResponseWS> drawService = service.createDraw(Utils.getIdShop(context), Utils.getIdCity(context),
                                    draw.getDESCRIPTION(), draw.getFOR_FOLLOWING(), draw.getCONDITION(),
                                    draw.getSTART_DATE(), draw.getEND_DATE(), draw.getLIMIT_DATE(), draw.getURL_LOGO(),
                                    draw.getNAME_LOGO(), draw.getDATE_UNIQUE(), draw.getEncode(), name, user);
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
                                                    draw.setIS_ACTIVE(1);
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
                                            Utils.writelogFile(context, "!response.isSuccessful()(DrawFragment, Repository)");
                                            post(DrawFragmentEvent.ERROR, context.getString(R.string.error_data_base));
                                        }
                                    } else {
                                        Utils.writelogFile(context, "response == null(DrawFragment, Repository)");
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
                        Utils.writelogFile(context, "Internet error(DrawFragment, Repository)");
                        post(DrawFragmentEvent.ERROR, context.getString(R.string.error_internet));
                    }
                } else {
                    Utils.writelogFile(context, "Name isEmpty(DrawFragment, Repository)");
                    post(DrawFragmentEvent.ERROR, context.getString(R.string.error_draw_null));
                }
            } else {
                Utils.writelogFile(context, "DateEnd exist(DrawFragment, Repository)");
                post(DrawFragmentEvent.ERROR, context.getString(R.string.error_draw_date_end_exist));
            }
        } else {
            Utils.writelogFile(context, "Draw null(DrawFragment, Repository)");
            post(DrawFragmentEvent.ERROR, context.getString(R.string.error_draw_null));
        }
    }

    private String getUser(Context context) {
        Utils.writelogFile(context, "getUser(DrawFragment, Repository)");
        return SQLite.select(Login_Table.USER).from(Login.class).querySingle().getUSER();
    }

    private boolean dateEndExist(Context context, String dateEnd) {
        Utils.writelogFile(context, "dateEndExist(DrawFragment, Repository)");
        return SQLite.select().from(Draw.class).where(Draw_Table.END_DATE.eq(dateEnd)).querySingle() == null;
    }

    @Override
    public void validateBroadcast(Context context) {
        Utils.writelogFile(context, "validateBroadcast(DrawFragment, Repository)");
        Log.i("DRAW","validateBroadcast, fragment");
        List<Draw> draws = getDrawsForEndDate(context);
        Long time = null;
        if (draws != null)
            if (draws.size() > 0) {
                Log.i("DRAW","draws.size() > 0");
                Log.i("DRAW",draws.get(0).getID_DRAW_KEY()+"");
                Log.i("DRAW",Utils.getIdDrawEnd(context)+"");
                if (draws.get(0).getID_DRAW_KEY() != Utils.getIdDrawEnd(context)) {
                    Utils.setIdDrawEnd(context, draws.get(0).getID_DRAW_KEY());
                    time = Utils.dateEndTimeFormat(draws.get(0).getEND_DATE());
                    if (broadcastIsWorking(context)) {
                        Log.i("DRAW","broadcastIsWorking true");
                     //   cancelAlarmManager(context);
                        startBroadcast(context, time);
                    } else {
                        Log.i("DRAW","broadcastIsWorking false");
                        startBroadcast(context, time);
                    }
                } else {
                    Log.i("DRAW","draws.get(0).getID_DRAW_KEY() == Utils.getIdDrawEnd(context)");
                    if (!broadcastIsWorking(context)) {
                        Log.i("DRAW","broadcastIsWorking false");
                        time = Utils.dateEndTimeFormat(draws.get(0).getEND_DATE());
                        startBroadcast(context, time);
                    }
                }
            } else {
                Log.i("DRAW","draws.size() = 0");
               // cancelAlarmManager(context);
            }
    }

    private void cancelAlarmManager(Context context){
        intentAlarm = new Intent(context, BroadcastDraw.class);
        pendingIntent = PendingIntent.getBroadcast(context, REQUEST_CODE, intentAlarm, PendingIntent.FLAG_UPDATE_CURRENT);
        pendingIntent.cancel();
        alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);
    }

    @Override
    public void getCity(Context context) {
        Utils.writelogFile(context, "getCity(DrawFragment, Repository)");
        try {
            String city = SQLite.select(City_Table.CITY).from(City.class).where(City_Table.ID_CITY_KEY.eq(Utils.getIdCity(context))).querySingle().getCITY();
            post(DrawFragmentEvent.CITY, true, city);
        } catch (Exception e) {
            Utils.writelogFile(context, "catch error " + e.getMessage() + "(DrawFragment, Repository)");
            post(DrawFragmentEvent.ERROR, e.getMessage());
        }
    }

    @Override
    public void validateDateShop(final Context context) {
        Utils.writelogFile(context, "Metodo validateDateShop y Se valida conexion a internet(DrawFragment, Repository)");
        DateShop dateShop = getDateShop();
        if (dateShop != null) {
            if (Utils.isNetworkAvailable(context)) {
                try {
                    Utils.writelogFile(context, "Call validateDateShop(DrawFragment, Repository)");
                    Call<ResultUser> validateDateShop = service.validateDateShop(dateShop.getID_SHOP_FOREIGN(), Utils.getIdCity(context),
                            dateShop.getACCOUNT_DATE(), dateShop.getOFFER_DATE(), dateShop.getNOTIFICATION_DATE(), dateShop.getDRAW_DATE(),
                            dateShop.getDATE_SHOP_DATE(), dateShop.getSUPPORT_DATE());
                    validateDateShop.enqueue(new Callback<ResultUser>() {
                        @Override
                        public void onResponse(Call<ResultUser> call, Response<ResultUser> response) {
                            if (response.isSuccessful()) {
                                Utils.writelogFile(context, "Response Successful y get ResponseWS(DrawFragment, Repository)");
                                responseWS = response.body().getResponseWS();
                                if (responseWS != null) {
                                    Utils.writelogFile(context, "ResponseWS != null y valida getSuccess(DrawFragment, Repository)");
                                    if (responseWS.getSuccess().equals("0")) {
                                        Utils.writelogFile(context, " getSuccess = 0 y getDateShop(DrawFragment, Repository)");
                                        dateUserWS = response.body().getDateShop();
                                        if (dateUserWS != null) {
                                            Utils.writelogFile(context, "dateUserWS != null y delete DateShop(DrawFragment, Repository)");
                                            Delete.table(DateShop.class);
                                            Utils.writelogFile(context, "delete DateShop ok y save dateUserWS(DrawFragment, Repository)");
                                            dateUserWS.save();
                                            Utils.writelogFile(context, "save dateUserWS y getAccount(DrawFragment, Repository)");
                                        }
                                        accounts = response.body().getAccount();
                                        if (accounts != null) {
                                            Utils.writelogFile(context, "accounts != null y delete DrawFragment(DrawFragment, Repository)");
                                            Delete.table(Account.class);
                                            Utils.writelogFile(context, "delete DrawFragment ok y save accounts(DrawFragment, Repository)");
                                            accounts.save();
                                            Utils.writelogFile(context, "save DrawFragment ok(DrawFragment, Repository)");
                                        }
                                        shop = response.body().getShop();
                                        if (shop != null) {
                                            Utils.writelogFile(context, "shop != null y delete Shop(DrawFragment, Repository)");
                                            Delete.table(Shop.class);
                                            Utils.writelogFile(context, "delete Shop ok y save shop(DrawFragment, Repository)");
                                            shop.save();
                                            Utils.writelogFile(context, "save shop ok(DrawFragment, Repository)");
                                        }
                                        offers = response.body().getOffers();
                                        if (offers != null) {
                                            Utils.writelogFile(context, "offers != null(DrawFragment, Repository)");
                                            if (offers.size() > 0) {
                                                Utils.writelogFile(context, "offers.size() > 0 y delete Offer(DrawFragment, Repository)");
                                                Delete.table(Offer.class);
                                                Utils.writelogFile(context, "delete Offer ok y save offers(DrawFragment, Repository)");
                                                for (Offer offer : offers) {
                                                    Utils.writelogFile(context, "save offer: " + offer.getID_OFFER_KEY() + " (DrawFragment, Repository)");
                                                    offer.save();
                                                }
                                                Utils.writelogFile(context, "fin FOR offer y getNotification (DrawFragment, Repository)");
                                            } else {
                                                Utils.writelogFile(context, "offers == null y delete Offer(DrawFragment, Repository)");
                                                Delete.table(Offer.class);
                                            }
                                        }
                                        draws = response.body().getDraws();
                                        if (draws != null) {
                                            Utils.writelogFile(context, "draws != null(DrawFragment, Repository)");
                                            if (draws.size() > 0) {
                                                Utils.writelogFile(context, "draws.size() > 0 y delete Draw(DrawFragment, Repository)");
                                                Delete.table(Draw.class);
                                                Utils.writelogFile(context, "delete draws ok y save Draw(DrawFragment, Repository)");
                                                for (Draw draw : draws) {
                                                    Utils.writelogFile(context, "save draw: " + draw.getID_DRAW_KEY() + " (DrawFragment, Repository)");
                                                    draw.save();
                                                }
                                                Utils.writelogFile(context, "fin FOR draw y getNotification(DrawFragment, Repository)");
                                            } else {
                                                Utils.writelogFile(context, "draws == null y delete Draw(DrawFragment, Repository)");
                                                Delete.table(Draw.class);
                                            }
                                        }

                                        notification = response.body().getNotification();
                                        if (notification != null) {
                                            Utils.writelogFile(context, "notification != null y delete Notification(DrawFragment, Repository)");
                                            Delete.table(Notification.class);
                                            Utils.writelogFile(context, "delete Notification ok y save notification(DrawFragment, Repository)");
                                            notification.save();
                                            Utils.writelogFile(context, "save notification ok y getSupport(DrawFragment, Repository)");
                                        }

                                        support = response.body().getSupport();
                                        if (support != null) {
                                            Utils.writelogFile(context, "support != null y delete Support(DrawFragment, Repository)");
                                            Delete.table(Support.class);
                                            Utils.writelogFile(context, "delete Support ok y save support(DrawFragment, Repository)");
                                            support.save();
                                            Utils.writelogFile(context, "save Support ok y GOTONAV(DrawFragment, Repository)");
                                        }
                                        post(DrawFragmentEvent.UPDATESUCCESS);
                                    } else if (responseWS.getSuccess().equals("4")) {
                                        Utils.writelogFile(context, "getSuccess = 4 y post GOTONAV(DrawFragment, Repository)");
                                        post(DrawFragmentEvent.UPDATEWITHOUTCHANGE);
                                    } else {
                                        Utils.writelogFile(context, "getSuccess = error " + responseWS.getMessage() + "(DrawFragment, Repository)");
                                        post(DrawFragmentEvent.ERROR, responseWS.getMessage());
                                    }
                                }
                            } else {
                                Utils.writelogFile(context, "!isSuccess(DrawFragment, Repository)");
                                post(DrawFragmentEvent.ERROR, context.getString(R.string.error_data_base));
                            }
                        }

                        @Override
                        public void onFailure(Call<ResultUser> call, Throwable t) {
                            Utils.writelogFile(context, "Call error " + t.getMessage() + "(DrawFragment, Repository)");
                            post(DrawFragmentEvent.ERROR, t.getMessage());
                        }
                    });
                } catch (Exception e) {
                    Utils.writelogFile(context, "catch error " + e.getMessage() + "(DrawFragment, Repository)");
                    post(DrawFragmentEvent.ERROR, e.getMessage());
                }
            } else {
                Utils.writelogFile(context, "Internet error(DrawFragment, Repository)");
                post(DrawFragmentEvent.ERROR, context.getString(R.string.error_internet));
            }
        } else {
            Utils.writelogFile(context, "dateshop == null(DrawFragment, Repository)");
            post(DrawFragmentEvent.ERROR, context.getString(R.string.error_data_base));
        }
    }

    private DateShop getDateShop() {
        return SQLite.select().from(DateShop.class).querySingle();
    }

    private boolean broadcastIsWorking(Context context) {
        intentAlarm = new Intent(context, BroadcastDraw.class);
        return (PendingIntent.getBroadcast(context, REQUEST_CODE, intentAlarm, PendingIntent.FLAG_NO_CREATE) != null);
    }

    private void startBroadcast(Context context, Long time) {
        Log.i("DRAW","startBroadcast");
        intentAlarm = new Intent(context, BroadcastDraw.class);
        try {
            intentAlarm = new Intent(context, BroadcastDraw.class);
            pendingIntent = PendingIntent.getBroadcast(context, REQUEST_CODE, intentAlarm, PendingIntent.FLAG_UPDATE_CURRENT);
            alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            alarmManager.set(AlarmManager.RTC_WAKEUP, time, pendingIntent);
        } catch (Exception e) {
            Utils.writelogFile(context, "catch error " + e.getMessage() + "(DrawFragment, Repository)");
            post(DrawFragmentEvent.ERROR, e.getMessage());
        }
    }

    private List<Draw> getDrawsForEndDate(Context context) {
        Utils.writelogFile(context, "validateBroadcast(DrawFragment, Repository)");
        ConditionGroup conditions = ConditionGroup.clause();
        conditions.and(Condition.column(new NameAlias("IS_ACTIVE")).is(1));
        conditions.and(Condition.column(new NameAlias("ERROR_REPORTING")).is(0));
        return SQLite.select().from(Draw.class).where(conditions).orderBy(new NameAlias("Draw.END_DATE"), true).queryList();
    }

    private void post(int type) {
        post(type, null);
    }

    private void post(int type, String error) {
        post(type, null, error);
    }

    private void post(int type, boolean isCity, String city) {
        post(type, city, null);
    }

    private void post(int type, String city, String error) {
        DrawFragmentEvent event = new DrawFragmentEvent();
        event.setType(type);
        event.setCity(city);
        event.setError(error);
        eventBus.post(event);
    }
}
