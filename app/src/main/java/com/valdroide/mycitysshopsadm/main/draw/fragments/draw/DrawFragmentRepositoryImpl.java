package com.valdroide.mycitysshopsadm.main.draw.fragments.draw;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.raizlabs.android.dbflow.sql.language.NameAlias;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.valdroide.mycitysshopsadm.R;
import com.valdroide.mycitysshopsadm.api.APIService;
import com.valdroide.mycitysshopsadm.entities.place.City;
import com.valdroide.mycitysshopsadm.entities.place.City_Table;
import com.valdroide.mycitysshopsadm.entities.response.ResponseWS;
import com.valdroide.mycitysshopsadm.entities.shop.Draw;
import com.valdroide.mycitysshopsadm.entities.shop.Draw_Table;
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
                if (!name.isEmpty()) {
                    if (Utils.isNetworkAvailable(context)) {
                        try {
                            Utils.writelogFile(context, "Call createDraw(DrawFragment, Repository)");
                            Call<ResponseWS> drawService = service.createDraw(Utils.getIdShop(context), Utils.getIdCity(context),
                                    draw.getDESCRIPTION(), draw.getFOR_FOLLOWING(), draw.getCONDITION(),
                                    draw.getSTART_DATE(), draw.getEND_DATE(), draw.getLIMIT_DATE(), draw.getURL_LOGO(),
                                    draw.getNAME_LOGO(), draw.getDATE_UNIQUE(), draw.getEncode(), name);
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

    private boolean dateEndExist(Context context, String dateEnd) {
        Utils.writelogFile(context, "dateEndExist(DrawFragment, Repository)");
        return SQLite.select().from(Draw.class).where(Draw_Table.END_DATE.eq(dateEnd)).query() != null;
    }

    @Override
    public void validateBroadcast(Context context) {
        Utils.writelogFile(context, "validateBroadcast(DrawFragment, Repository)");
        List<Draw> draws = getDrawsForEndDate(context);
        Long time = null;
        if (draws != null)
            if (draws.size() > 0) {
                if (draws.get(0).getID_DRAW_KEY() != Utils.getIdDrawEnd(context)) {
                    Utils.setIdDrawEnd(context, draws.get(0).getID_DRAW_KEY());
                    time = Utils.dateEndTimeFormat(draws.get(0).getEND_DATE());
                    if (broadcastIsWorking(context)) {
                        pendingIntent.cancel();
                        alarmManager.cancel(pendingIntent);
                        startBroadcast(context, time);
                    } else {
                        startBroadcast(context, time);
                    }
                } else {
                    if (!broadcastIsWorking(context)) {
                        time = Utils.dateEndTimeFormat(draws.get(0).getEND_DATE());
                        startBroadcast(context, time);
                    }
                }
            }
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

    private boolean broadcastIsWorking(Context context) {
        intentAlarm = new Intent(context, BroadcastDraw.class);
        return (PendingIntent.getBroadcast(context, REQUEST_CODE, intentAlarm, PendingIntent.FLAG_NO_CREATE) != null);
    }

    private void startBroadcast(Context context, Long time) {
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
        return SQLite.select().from(Draw.class).where(Draw_Table.IS_ACTIVE.is(1)).orderBy(new NameAlias("Draw.END_DATE"), true).queryList();
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
