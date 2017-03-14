package com.valdroide.mycitysshopsadm.main.splash;

import android.content.Context;

import com.raizlabs.android.dbflow.sql.language.Delete;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.valdroide.mycitysshopsadm.api.APIService;
import com.valdroide.mycitysshopsadm.entities.response.ResultPlace;
import com.valdroide.mycitysshopsadm.entities.response.ResultShop;
import com.valdroide.mycitysshopsadm.entities.response.ResultUser;
import com.valdroide.mycitysshopsadm.entities.shop.Account;
import com.valdroide.mycitysshopsadm.entities.shop.DateShop;
import com.valdroide.mycitysshopsadm.entities.shop.MyPlace;
import com.valdroide.mycitysshopsadm.entities.place.City;
import com.valdroide.mycitysshopsadm.entities.place.Country;
import com.valdroide.mycitysshopsadm.entities.place.DatePlace;
import com.valdroide.mycitysshopsadm.entities.place.State;
import com.valdroide.mycitysshopsadm.entities.response.ResponseWS;
import com.valdroide.mycitysshopsadm.entities.shop.Notification;
import com.valdroide.mycitysshopsadm.entities.shop.Offer;
import com.valdroide.mycitysshopsadm.entities.shop.Shop;
import com.valdroide.mycitysshopsadm.lib.base.EventBus;
import com.valdroide.mycitysshopsadm.main.splash.events.SplashActivityEvent;
import com.valdroide.mycitysshopsadm.utils.Utils;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashActivityRepositoryImpl implements SplashActivityRepository {
    private EventBus eventBus;
    private APIService service;
    private ResponseWS responseWS;
    private DatePlace datePlace;
    private DatePlace datePlacesWS;
    private List<Country> countries;
    private List<State> states;
    private List<City> cities;
    private MyPlace place;
    private DateShop dateShop;
    private DateShop dateUserWS;
    private Account account;
    private Notification notification;
    private List<Offer> offers;
    private Shop shop;

    public SplashActivityRepositoryImpl(EventBus eventBus, APIService service) {
        this.eventBus = eventBus;
        this.service = service;
    }

    @Override
    public void validateDatePlace(Context context) {
        try {
            place = SQLite.select().from(MyPlace.class).querySingle();
            if (place == null) { // es la primera vez que entra o quiere cambiar de lugar
                datePlace = SQLite.select().from(DatePlace.class).querySingle();
                if (datePlace != null)
                    validateDatePlace(context, datePlace.getTABLE_DATE(), datePlace.getCOUNTRY_DATE(),
                            datePlace.getSTATE_DATE(), datePlace.getCITY_DATE());
                else  // traemos los datos sin validar fechas
                    getPlace(context);
            } else {
                shop = SQLite.select().from(Shop.class).querySingle();
                if (shop != null)
                    validateLogin(context, shop);
                else
                    post(SplashActivityEvent.GOTOLOG);
            }
        } catch (Exception e) {
            post(SplashActivityEvent.ERROR, e.getMessage());
        }
    }

    public void validateDatePlace(final Context context, String date, String cou, String sta, String ci) {
        if (Utils.isNetworkAvailable(context)) {
            try {
                Call<ResultPlace> validateDatePlace = service.validateDatePlace(cou, sta, ci, date);
                validateDatePlace.enqueue(new Callback<ResultPlace>() {
                    @Override
                    public void onResponse(Call<ResultPlace> call, Response<ResultPlace> response) {
                        if (response.isSuccessful()) {
                            responseWS = response.body().getResponseWS();
                            if (responseWS != null) {
                                if (responseWS.getSuccess().equals("0")) {
                                    datePlacesWS = response.body().getDatePlace();
                                    if (datePlacesWS != null) {
                                        Delete.table(DatePlace.class);
                                        datePlacesWS.save();
                                    }
                                    countries = response.body().getCountries();
                                    if (countries != null) {
                                        Delete.table(Country.class);
                                        for (Country country : countries) {
                                            country.save();
                                        }
                                    }
                                    states = response.body().getStates();
                                    if (states != null) {
                                        Delete.table(State.class);
                                        for (State state : states) {
                                            state.save();
                                        }
                                    }
                                    cities = response.body().getCities();
                                    if (cities != null) {
                                        Delete.table(City.class);
                                        for (City city : cities) {
                                            city.save();
                                        }
                                    }
                                    post(SplashActivityEvent.GOTOPLACE);
                                } else if (responseWS.getSuccess().equals("4")) {
                                    post(SplashActivityEvent.GOTOPLACE);
                                } else {
                                    post(SplashActivityEvent.ERROR, responseWS.getMessage());
                                }
                            }
                        } else {
                            post(SplashActivityEvent.ERROR, Utils.ERROR_DATA_BASE);
                        }
                    }
                    @Override
                    public void onFailure(Call<ResultPlace> call, Throwable t) {
                        post(SplashActivityEvent.ERROR, t.getMessage());
                    }
                });
            } catch (Exception e) {
                post(SplashActivityEvent.ERROR, e.getMessage());
            }
        } else {
            post(SplashActivityEvent.ERROR, Utils.ERROR_INTERNET);
        }
    }

    public void getPlace(final Context context) {
        if (Utils.isNetworkAvailable(context)) {
            try {
                Call<ResultPlace> getPlace = service.getPlace();
                getPlace.enqueue(new Callback<ResultPlace>() {
                    @Override
                    public void onResponse(Call<ResultPlace> call, Response<ResultPlace> response) {
                        if (response.isSuccessful()) {
                            responseWS = response.body().getResponseWS();
                            if (responseWS != null) {
                                if (responseWS.getSuccess().equals("0")) {
                                    datePlacesWS = response.body().getDatePlace();
                                    if (datePlacesWS != null) {
                                        Delete.table(DatePlace.class);
                                        datePlacesWS.save();
                                    }
                                    countries = response.body().getCountries();
                                    if (countries != null) {
                                        Delete.table(Country.class);
                                        for (Country country : countries) {
                                            country.save();
                                        }
                                    }
                                    states = response.body().getStates();
                                    if (states != null) {
                                        Delete.table(State.class);
                                        for (State state : states) {
                                            state.save();
                                        }
                                    }
                                    cities = response.body().getCities();
                                    if (cities != null) {
                                        Delete.table(City.class);
                                        for (City city : cities) {
                                            city.save();
                                        }
                                    }
                                    post(SplashActivityEvent.GOTOPLACE);
                                } else {
                                    post(SplashActivityEvent.ERROR, responseWS.getMessage());
                                }
                            }
                        } else {
                            post(SplashActivityEvent.ERROR, Utils.ERROR_DATA_BASE);
                        }
                    }
                    @Override
                    public void onFailure(Call<ResultPlace> call, Throwable t) {
                        post(SplashActivityEvent.ERROR, t.getMessage());
                    }
                });
            } catch (Exception e) {
                post(SplashActivityEvent.ERROR, e.getMessage());
            }
        } else {
            post(SplashActivityEvent.ERROR, Utils.ERROR_INTERNET);
        }
    }

    @Override
    public void validateDateShop(Context context) {
        try {
            dateShop = SQLite.select().from(DateShop.class).querySingle();
            if (dateShop != null) {
                validateDateShop(context, dateShop.getID_SHOP_FOREIGN(), dateShop.getDATE_USER_DATE(), dateShop.getACCOUNT_DATE(),
                        dateShop.getOFFER_DATE(),dateShop.getNOTIFICATION_DATE());
            } else { // traemos los datos sin validar fechas
                shop = SQLite.select().from(Shop.class).querySingle();
                getShopData(context, shop.getID_SHOP_KEY());
            }
        } catch (Exception e) {
            post(SplashActivityEvent.ERROR, e.getMessage());
        }
    }

    public void validateDateShop(final Context context, int id_shop, String date, String acount, String offer, String notification_s) {
        if (Utils.isNetworkAvailable(context)) {
            try {
                Call<ResultUser> validateDateShop = service.validateDateShop(id_shop, acount, offer, notification_s, date);
                validateDateShop.enqueue(new Callback<ResultUser>() {
                    @Override
                    public void onResponse(Call<ResultUser> call, Response<ResultUser> response) {
                        if (response.isSuccessful()) {
                            responseWS = response.body().getResponseWS();
                            if (responseWS != null) {
                                if (responseWS.getSuccess().equals("0")) {
                                    dateUserWS = response.body().getDateUser();
                                    if (dateUserWS != null) {
                                        Delete.table(DateShop.class);
                                        dateUserWS.save();
                                    }
                                    account = response.body().getAccount();
                                    if (account != null) {
                                        Delete.table(Account.class);
                                        account.save();
                                    }

                                    offers = response.body().getOffers();
                                    if (offers != null) {
                                        Delete.table(Offer.class);
                                        for (Offer offer : offers) {
                                            offer.save();
                                        }
                                    } else
                                        Delete.table(Offer.class);

                                    notification = response.body().getNotification();
                                    if (notification != null) {
                                        Delete.table(Notification.class);
                                        notification.save();
                                    }
                                    post(SplashActivityEvent.GOTONAV);
                                } else if (responseWS.getSuccess().equals("4")) {
                                    post(SplashActivityEvent.GOTONAV);
                                } else {
                                    post(SplashActivityEvent.ERROR, responseWS.getMessage());
                                }
                            }
                        } else {
                            post(SplashActivityEvent.ERROR, Utils.ERROR_DATA_BASE);
                        }
                    }
                    @Override
                    public void onFailure(Call<ResultUser> call, Throwable t) {
                        post(SplashActivityEvent.ERROR, t.getMessage());
                    }
                });
            } catch (Exception e) {
                post(SplashActivityEvent.ERROR, e.getMessage());
            }
        } else {
            post(SplashActivityEvent.ERROR, Utils.ERROR_INTERNET);
        }
    }

    public void getShopData(final Context context, int id_user) {
        if (Utils.isNetworkAvailable(context)) {
            try {
                Call<ResultUser> getShopData = service.getShopData(id_user);
                getShopData.enqueue(new Callback<ResultUser>() {
                    @Override
                    public void onResponse(Call<ResultUser> call, Response<ResultUser> response) {
                        if (response.isSuccessful()) {
                            responseWS = response.body().getResponseWS();
                            if (responseWS != null) {
                                if (responseWS.getSuccess().equals("0")) {
                                    dateUserWS = response.body().getDateUser();
                                    if (dateUserWS != null) {
                                        Delete.table(DateShop.class);
                                        dateUserWS.save();
                                    }
                                    account = response.body().getAccount();
                                    if (account != null) {
                                        Delete.table(Account.class);
                                        account.save();
                                    }
                                    offers = response.body().getOffers();
                                    if (offers != null) {
                                        Delete.table(Offer.class);
                                        for (Offer offer : offers) {
                                            offer.save();
                                        }
                                    }
                                    notification = response.body().getNotification();
                                    if (notification != null) {
                                        Delete.table(Notification.class);
                                        notification.save();
                                    }
                                    post(SplashActivityEvent.GOTONAV);
                                } else {
                                    post(SplashActivityEvent.ERROR, responseWS.getMessage());
                                }
                            }
                        } else {
                            post(SplashActivityEvent.ERROR, Utils.ERROR_DATA_BASE);
                        }
                    }

                    @Override
                    public void onFailure(Call<ResultUser> call, Throwable t) {
                        post(SplashActivityEvent.ERROR, t.getMessage());
                    }
                });
            } catch (Exception e) {
                post(SplashActivityEvent.ERROR, e.getMessage());
            }
        } else {
            post(SplashActivityEvent.ERROR, Utils.ERROR_INTERNET);
        }
    }

    public void validateLogin(Context context, final Shop shop) {
        if (Utils.isNetworkAvailable(context)) {
            try {
                Call<ResultShop> loginService = service.validateLogin(shop.getUSER(),
                        shop.getPASS(), shop.getID_CITY_FOREIGN());
                loginService.enqueue(new Callback<ResultShop>() {
                    @Override
                    public void onResponse(Call<ResultShop> call, Response<ResultShop> response) {
                        if (response.isSuccessful()) {
                            responseWS = response.body().getResponseWS();
                            if (responseWS != null) {
                                if (responseWS.getSuccess().equals("0")) {
                                    Shop shop = response.body().getShop();
                                    shop.update();
                                    post(SplashActivityEvent.GOTONAV);
                                } else {
                                    post(SplashActivityEvent.GOTOLOG);
                                }
                            } else {
                                post(SplashActivityEvent.ERROR, Utils.ERROR_DATA_BASE);
                            }
                        } else {
                            post(SplashActivityEvent.ERROR, Utils.ERROR_DATA_BASE);
                        }
                    }

                    @Override
                    public void onFailure(Call<ResultShop> call, Throwable t) {
                        post(SplashActivityEvent.ERROR, t.getMessage());
                    }
                });
            } catch (Exception e) {
                post(SplashActivityEvent.ERROR, e.getMessage());
            }
        } else {
            post(SplashActivityEvent.ERROR, Utils.ERROR_INTERNET);
        }
    }

    public void post(int type) {
        post(type, null);
    }

    public void post(int type, String error) {
        SplashActivityEvent event = new SplashActivityEvent();
        event.setType(type);
        event.setError(error);
        eventBus.post(event);
    }
}
