package com.valdroide.mycitysshopsadm.main.draw.fragments.draw_list;

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
import com.valdroide.mycitysshopsadm.entities.response.ResponseWS;
import com.valdroide.mycitysshopsadm.entities.response.ResultDraw;
import com.valdroide.mycitysshopsadm.entities.response.ResultUser;
import com.valdroide.mycitysshopsadm.entities.shop.Account;
import com.valdroide.mycitysshopsadm.entities.shop.DateShop;
import com.valdroide.mycitysshopsadm.entities.shop.Draw;
import com.valdroide.mycitysshopsadm.entities.shop.Login;
import com.valdroide.mycitysshopsadm.entities.shop.Login_Table;
import com.valdroide.mycitysshopsadm.entities.shop.Notification;
import com.valdroide.mycitysshopsadm.entities.shop.Offer;
import com.valdroide.mycitysshopsadm.entities.shop.Shop;
import com.valdroide.mycitysshopsadm.entities.shop.Support;
import com.valdroide.mycitysshopsadm.lib.base.EventBus;
import com.valdroide.mycitysshopsadm.main.draw.broadcast.ui.BroadcastDraw;
import com.valdroide.mycitysshopsadm.main.draw.fragments.draw_list.events.DrawListFragmentEvent;
import com.valdroide.mycitysshopsadm.main.offer.fragments.offer.events.OfferFragmentEvent;
import com.valdroide.mycitysshopsadm.utils.Utils;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DrawListFragmentRepositoryImpl implements DrawListFragmentRepository {
    private EventBus eventBus;
    private APIService service;
    private DateShop dateShop;
    private ResponseWS responseWS;
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
    
    public DrawListFragmentRepositoryImpl(EventBus eventBus, APIService service) {
        this.eventBus = eventBus;
        this.service = service;
    }

    @Override
    public void getDraws(Context context) {
        Utils.writelogFile(context, "Metodo getDraws, draws(DrawListFragment, Repository)");
        try {
            List<Draw> draws = SQLite.select().from(Draw.class).orderBy(new NameAlias("ID_DRAW_KEY"), false).queryList();
            if (draws != null) {
                Utils.writelogFile(context, "draws != null, post(DrawListFragment, Repository)");
                if (draws.size() > 0) {
                    for (int i = 0; i < draws.size(); i++) {
                        if (draws.get(i).getIS_ACTIVE() == 1) {
                            if (Utils.validateExpirateCurrentTime(draws.get(i).getEND_DATE(), context.getString(R.string.format_draw))) {
                                draws.get(i).setERROR_REPORTING(1);
                            }
                        }
                    }
                }
                post(DrawListFragmentEvent.DRAWS, draws);
            } else {
                Utils.writelogFile(context, "draws == null, post(DrawListFragment, Repository)");
                post(DrawListFragmentEvent.ERROR, context.getString(R.string.error_data_base));
            }
        } catch (Exception e) {
            Utils.writelogFile(context, "catch error, post(DrawListFragment, Repository)");
            post(DrawListFragmentEvent.ERROR, e.getMessage());
        }
    }

    @Override
    public void cancelDraw(final Context context, final Draw draw) {
        Utils.writelogFile(context, "Metodo cancelDraw y se valida draw(DrawListFragment, Repository)");
        if (draw != null) {
            Utils.writelogFile(context, "draw != null y Se valida conexion a internet(DrawListFragment, Repository)");
            String name = Utils.getNameShop(context);
            String user = getUser(context);
            if (!name.isEmpty() && user != null && !user.isEmpty()) {
                if (Utils.isNetworkAvailable(context)) {
                    try {
                        Utils.writelogFile(context, "Call createDraw(DrawListFragment, Repository)");
                        draw.setDATE_UNIQUE(Utils.getFechaOficialSeparate());
                        Call<ResponseWS> drawService = service.cancelDraw(Utils.getIdShop(context), Utils.getIdCity(context),
                                draw.getID_DRAW_KEY(), draw.getIS_CANCEL(), draw.getIS_TAKE(),
                                draw.getIS_LIMIT(), draw.getIS_ZERO(), draw.getFOR_FOLLOWING(), name, draw.getDATE_UNIQUE(),
                                user);
                        drawService.enqueue(new Callback<ResponseWS>() {
                            @Override
                            public void onResponse(Call<ResponseWS> call, Response<ResponseWS> response) {
                                if (response != null) {
                                    Utils.writelogFile(context, "response != null y validate Response is Successful(DrawListFragment, Repository)");
                                    if (response.isSuccessful()) {
                                        Utils.writelogFile(context, "Response Successful y get getSuccess()(DrawListFragment, Repository)");
                                        if (response.body().getSuccess().equals("0")) {
                                            Utils.writelogFile(context, "getSuccess = 0 y updateDateShop(DrawListFragment, Repository)");
                                            updateDateShop(context, draw.getDATE_UNIQUE());
                                            Utils.writelogFile(context, "updateDateShop ok y draw.delete()(DrawListFragment, Repository)");
                                            draw.delete();
                                            post(DrawListFragmentEvent.CANCELSUCCESS);
                                        } else {
                                            Utils.writelogFile(context, "getSuccess = error " + response.body().getMessage() + "(DrawListFragment, Repository)");
                                            post(DrawListFragmentEvent.ERROR, response.body().getMessage());
                                        }
                                    } else {
                                        Utils.writelogFile(context, "!response.isSuccessful()(DrawListFragment, Repository)");
                                        post(DrawListFragmentEvent.ERROR, context.getString(R.string.error_data_base));
                                    }
                                } else {
                                    Utils.writelogFile(context, "response == null(DrawListFragment, Repository)");
                                    post(DrawListFragmentEvent.ERROR, context.getString(R.string.error_data_base));
                                }
                            }

                            @Override
                            public void onFailure(Call<ResponseWS> call, Throwable t) {
                                Utils.writelogFile(context, " Call error " + t.getMessage() + "(DrawListFragment, Repository)");
                                post(DrawListFragmentEvent.ERROR, t.getMessage());
                            }
                        });
                    } catch (Exception e) {
                        Utils.writelogFile(context, "catch error " + e.getMessage() + "(DrawListFragment, Repository)");
                        post(DrawListFragmentEvent.ERROR, e.getMessage());
                    }
                } else {
                    Utils.writelogFile(context, "Internet error(DrawListFragment, Repository)");
                    post(DrawListFragmentEvent.ERROR, context.getString(R.string.error_internet));
                }
            } else {
                Utils.writelogFile(context, "Name isEmpty(DrawListFragment, Repository)");
                post(DrawListFragmentEvent.ERROR, context.getString(R.string.error_draw_close));
            }
        } else {
            Utils.writelogFile(context, "Draw null(DrawListFragment, Repository)");
            post(DrawListFragmentEvent.ERROR, context.getString(R.string.error_draw_close));
        }
    }

    private String getUser(Context context) {
        Utils.writelogFile(context, "getUser(Account, Repository)");
        return SQLite.select(Login_Table.USER).from(Login.class).querySingle().getUSER();
    }

    @Override
    public void forceDraw(final Context context, final Draw draw) {
        Utils.writelogFile(context, "Metodo forceDraw y validate internet (DrawListFragment, Repository)");
        String name = Utils.getNameShop(context);
        if (!name.isEmpty()) {
            if (Utils.isNetworkAvailable(context)) {
                try {
                    Utils.writelogFile(context, "getFechaOficialSeparate(DrawListFragment, Repository)");
                    final String date = Utils.getFechaOficialSeparate();
                    Utils.writelogFile(context, "Call getWinnerDraw(DrawListFragment, Repository)");
                    Call<ResultDraw> DrawListFragmentService = service.getWinnerDraw(draw.getID_SHOP_FOREIGN(), Utils.getIdCity(context),
                            draw.getID_DRAW_KEY(), name, date);
                    DrawListFragmentService.enqueue(new Callback<ResultDraw>() {
                        @Override
                        public void onResponse(Call<ResultDraw> call, Response<ResultDraw> response) {
                            if (response.isSuccessful()) {
                                Utils.writelogFile(context, "Response Successful y get ResponseWS(DrawListFragment, Repository)");
                                responseWS = response.body().getResponseWS();
                                if (responseWS != null) {
                                    Utils.writelogFile(context, "ResponseWS != null y valida getSuccess(DrawListFragment, Repository)");
                                    if (responseWS.getSuccess().equals("0")) {
                                        Utils.writelogFile(context, "getSuccess = 0 y getDraw()(DrawListFragment, Repository)");
                                        Draw drawAux = response.body().getDraw();
                                        if (drawAux != null) {
                                            Utils.writelogFile(context, "drawAux != null(DrawListFragment, Repository)");
                                            if (drawAux.getNAME() != null && !drawAux.getNAME().isEmpty() &&
                                                    drawAux.getDNI() != null && !drawAux.getDNI().isEmpty()) {
                                                draw.setNAME(drawAux.getNAME());
                                                draw.setDNI(drawAux.getDNI());
                                                draw.setIS_ACTIVE(0);
                                                draw.setERROR_REPORTING(0);
                                                draw.setDATE_UNIQUE(date);
                                                draw.update();
                                                Utils.writelogFile(context, "draw.update()(DrawListFragment, Repository)");
                                                updateDateShop(context, date);
                                                Utils.writelogFile(context, "post FORCEDRAW(DrawListFragment, Repository)");
                                                post(DrawListFragmentEvent.FORCEDRAW, draw);
                                            } else {
                                                Utils.writelogFile(context, "drawAux data isEmpty or null(DrawListFragment, Repository)");
                                                post(DrawListFragmentEvent.ERROR, context.getString(R.string.error_data_base));
                                            }
                                        } else {
                                            Utils.writelogFile(context, "drawAux == null (DrawListFragment, Repository)");
                                            post(DrawListFragmentEvent.ERROR, context.getString(R.string.error_data_base));
                                        }
                                    } else if (responseWS.getSuccess().equals("4")) {
                                        Utils.writelogFile(context, "responseWS.getSuccess().equals(4)(DrawListFragment, Repository)");
                                        draw.setIS_ZERO(1);
                                        draw.setERROR_REPORTING(0);
                                        draw.setDATE_UNIQUE(date);
                                        draw.update();
                                        post(DrawListFragmentEvent.FORCEDRAW, draw);
                                    } else {
                                        Utils.writelogFile(context, "getSuccess = error " + responseWS.getMessage() + "(DrawListFragment, Repository)");
                                        post(DrawListFragmentEvent.ERROR, responseWS.getMessage());
                                    }
                                } else {
                                    Utils.writelogFile(context, "responseWS == null (DrawListFragment, Repository)");
                                    post(DrawListFragmentEvent.ERROR, context.getString(R.string.error_data_base));
                                }
                            } else {
                                Utils.writelogFile(context, "!response.isSuccessful()(DrawListFragment, Repository)");
                                post(DrawListFragmentEvent.ERROR, context.getString(R.string.error_data_base));
                            }
                        }

                        @Override
                        public void onFailure(Call<ResultDraw> call, Throwable t) {
                            Utils.writelogFile(context, "Call error " + t.getMessage() + "(DrawListFragment, Repository)");
                            post(DrawListFragmentEvent.ERROR, t.getMessage());
                        }
                    });
                } catch (Exception e) {
                    Utils.writelogFile(context, "catch error " + e.getMessage() + "(DrawListFragment, Repository)");
                    post(DrawListFragmentEvent.ERROR, e.getMessage());
                }
            } else {
                Utils.writelogFile(context, "Internet error(DrawListFragment, Repository)");
                post(DrawListFragmentEvent.ERROR, context.getString(R.string.error_internet));
            }
        } else {
            Utils.writelogFile(context, "Name isEmpty(DrawListFragment, Repository)");
            post(DrawListFragmentEvent.ERROR, context.getString(R.string.error_data_base));
        }
    }

    private void updateDateShop(Context context, String date_edit) {
        Utils.writelogFile(context, "Metodo updateDateShop (DrawListFragment, Repository)");
        try {
            dateShop = SQLite.select().from(DateShop.class).querySingle();
            if (dateShop != null) {
                dateShop.setDRAW_DATE(date_edit);
                dateShop.setDATE_SHOP_DATE(date_edit);
                dateShop.update();
            }
        } catch (Exception e) {
            Utils.writelogFile(context, "catch error " + e.getMessage() + "(DrawListFragment, Repository)");
            post(OfferFragmentEvent.ERROR, e.getMessage());
        }
    }

    @Override
    public void validateBroadcast(Context context) {
        Utils.writelogFile(context, "validateBroadcast(DrawFragment, Repository)");
        Log.i("DRAW", "validateBroadcast, fragmentList");
        List<Draw> draws = getDrawsForEndDate(context);
        Long time = null;
        if (draws != null)
            if (draws.size() > 0) {
                Log.i("DRAW", "draws.size() > 0");
                Log.i("DRAW", draws.get(0).getID_DRAW_KEY() + "");
                Log.i("DRAW", Utils.getIdDrawEnd(context) + "");
                if (draws.get(0).getID_DRAW_KEY() != Utils.getIdDrawEnd(context)) {
                    Utils.setIdDrawEnd(context, draws.get(0).getID_DRAW_KEY());
                    time = Utils.dateEndTimeFormat(draws.get(0).getEND_DATE());
                    if (broadcastIsWorking(context)) {
                        Log.i("DRAW", "broadcastIsWorking true");
                        //cancelAlarmManager(context);
                        startBroadcast(context, time);
                    } else {
                        Log.i("DRAW", "broadcastIsWorking false");
                        startBroadcast(context, time);
                    }
                } else {
                    Log.i("DRAW", "draws.get(0).getID_DRAW_KEY() == Utils.getIdDrawEnd(context)");
                    if (!broadcastIsWorking(context)) {
                        Log.i("DRAW", "broadcastIsWorking false");
                        time = Utils.dateEndTimeFormat(draws.get(0).getEND_DATE());
                        startBroadcast(context, time);
                    }
                }
            } else {
                Log.i("DRAW", "draws.size() = 0");
                //cancelAlarmManager(context);
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
                                        post(DrawListFragmentEvent.UPDATESUCCESS);
                                    } else if (responseWS.getSuccess().equals("4")) {
                                        Utils.writelogFile(context, "getSuccess = 4 y(DrawFragment, Repository)");
                                        post(DrawListFragmentEvent.WITHOUTCHANGE);
                                    } else {
                                        Utils.writelogFile(context, "getSuccess = error " + responseWS.getMessage() + "(DrawFragment, Repository)");
                                        post(DrawListFragmentEvent.ERROR, responseWS.getMessage());
                                    }
                                }
                            } else {
                                Utils.writelogFile(context, "!isSuccess(DrawFragment, Repository)");
                                post(DrawListFragmentEvent.ERROR, context.getString(R.string.error_data_base));
                            }
                        }

                        @Override
                        public void onFailure(Call<ResultUser> call, Throwable t) {
                            Utils.writelogFile(context, "Call error " + t.getMessage() + "(DrawFragment, Repository)");
                            post(DrawListFragmentEvent.ERROR, t.getMessage());
                        }
                    });
                } catch (Exception e) {
                    Utils.writelogFile(context, "catch error " + e.getMessage() + "(DrawFragment, Repository)");
                    post(DrawListFragmentEvent.ERROR, e.getMessage());
                }
            } else {
                Utils.writelogFile(context, "Internet error(DrawFragment, Repository)");
                post(DrawListFragmentEvent.ERROR, context.getString(R.string.error_internet));
            }
        } else {
            Utils.writelogFile(context, "dateshop == null(DrawFragment, Repository)");
            post(DrawListFragmentEvent.ERROR, context.getString(R.string.error_data_base));
        }
    }

    private DateShop getDateShop() {
        return SQLite.select().from(DateShop.class).querySingle();
    }

    private void cancelAlarmManager(Context context) {
        intentAlarm = new Intent(context, BroadcastDraw.class);
        pendingIntent = PendingIntent.getBroadcast(context, REQUEST_CODE, intentAlarm, PendingIntent.FLAG_UPDATE_CURRENT);
        pendingIntent.cancel();
        alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);
    }

    private boolean broadcastIsWorking(Context context) {
        intentAlarm = new Intent(context, BroadcastDraw.class);
        return (PendingIntent.getBroadcast(context, REQUEST_CODE, intentAlarm, PendingIntent.FLAG_NO_CREATE) != null);
    }

    private void startBroadcast(Context context, Long time) {
        Log.i("DRAW", "startBroadcast");
        intentAlarm = new Intent(context, BroadcastDraw.class);
        try {
            intentAlarm = new Intent(context, BroadcastDraw.class);
            pendingIntent = PendingIntent.getBroadcast(context, REQUEST_CODE, intentAlarm, PendingIntent.FLAG_UPDATE_CURRENT);
            alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            alarmManager.set(AlarmManager.RTC_WAKEUP, time, pendingIntent);
        } catch (Exception e) {
            Utils.writelogFile(context, "catch error " + e.getMessage() + "(DrawFragment, Repository)");
            post(DrawListFragmentEvent.ERROR, e.getMessage());
        }
    }

    private List<Draw> getDrawsForEndDate(Context context) {
        Utils.writelogFile(context, "getDrawsForEndDate(DrawFragment, Repository)");
        ConditionGroup conditions = ConditionGroup.clause();
        conditions.and(Condition.column(new NameAlias("IS_ACTIVE")).is(1));
        conditions.and(Condition.column(new NameAlias("ERROR_REPORTING")).is(0));
        return SQLite.select().from(Draw.class).where(conditions).orderBy(new NameAlias("Draw.END_DATE"), true).queryList();
    }

    private void post(int type) {
        post(type, null, null, null);
    }

    private void post(int type, List<Draw> draws) {
        post(type, draws, null, null);
    }

    private void post(int type, Draw draw) {
        post(type, null, draw, null);
    }

    private void post(int type, String error) {
        post(type, null, null, error);
    }

    private void post(int type, List<Draw> draws, Draw draw, String error) {
        DrawListFragmentEvent event = new DrawListFragmentEvent();
        event.setType(type);
        event.setDrawList(draws);
        event.setDraw(draw);
        event.setError(error);
        eventBus.post(event);
    }
}
