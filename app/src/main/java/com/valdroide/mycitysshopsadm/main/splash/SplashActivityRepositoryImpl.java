package com.valdroide.mycitysshopsadm.main.splash;

import android.content.Context;

import com.raizlabs.android.dbflow.sql.language.Delete;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.valdroide.mycitysshopsadm.api.APIService;
import com.valdroide.mycitysshopsadm.entities.place.City;
import com.valdroide.mycitysshopsadm.entities.place.Country;
import com.valdroide.mycitysshopsadm.entities.place.DatePlace;
import com.valdroide.mycitysshopsadm.entities.place.State;
import com.valdroide.mycitysshopsadm.entities.response.ResponseWS;
import com.valdroide.mycitysshopsadm.entities.response.Result;
import com.valdroide.mycitysshopsadm.lib.base.EventBus;
import com.valdroide.mycitysshopsadm.main.account.events.AccountActivityEvent;
import com.valdroide.mycitysshopsadm.main.splash.events.SplashActivityEvent;
import com.valdroide.mycitysshopsadm.utils.Utils;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashActivityRepositoryImpl implements SplashActivityRepository {
    private EventBus eventBus;
    private APIService service;
    //private List<ResponseWS> responseWS;
    ResponseWS responseWS;
    private List<DatePlace> datePlaces;
    private String date = "", cou = "", sta = "", ci = "";
    private List<DatePlace> datePlacesWS;
    private List<Country> countries;
    private List<State> states;
    private List<City> cities;

    public SplashActivityRepositoryImpl(EventBus eventBus, APIService service) {
        this.eventBus = eventBus;
        this.service = service;
    }

    @Override
    public void validateDatePlace(Context context) {
        datePlaces = SQLite.select().from(DatePlace.class).queryList();
        if (datePlaces != null)
            if (datePlaces.size() > 0) {
                dateStringFill(datePlaces);
                validateDatePlace(context, date, cou, sta, ci);
            } else {
                getPlace(context);
            }
    }

    public void validateDatePlace(final Context context, String date, String cou, String sta, String ci) {
        if (Utils.isNetworkAvailable(context)) {
            try {
                Call<Result> validateDatePlace = service.validateDatePlace(cou, sta, ci, date);
                validateDatePlace.enqueue(new Callback<Result>() {
                    @Override
                    public void onResponse(Call<Result> call, Response<Result> response) {
                        if (response.isSuccessful()) {
                            responseWS = response.body().getResponseWS();
                            if (responseWS != null) {
                                if (responseWS.getSuccess().equals("0")) {
                                    datePlacesWS = response.body().getDatePlaces();
                                    if (datePlacesWS != null) {
                                        Delete.table(DatePlace.class);
                                        for (DatePlace datePlace : datePlacesWS) {
                                            datePlace.save();
                                        }
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
                            post(AccountActivityEvent.ERROR, Utils.ERROR_DATA_BASE);
                        }
                    }

                    @Override
                    public void onFailure(Call<Result> call, Throwable t) {
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
                Call<Result> getPlace = service.getPlace();
                getPlace.enqueue(new Callback<Result>() {
                    @Override
                    public void onResponse(Call<Result> call, Response<Result> response) {
                        if (response.isSuccessful()) {
                            responseWS = response.body().getResponseWS();
                            if (responseWS != null) {
                                if (responseWS.getSuccess().equals("0")) {
                                    datePlacesWS = response.body().getDatePlaces();
                                    if (datePlacesWS != null) {
                                        Delete.table(DatePlace.class);
                                        for (DatePlace datePlace : datePlacesWS) {
                                            datePlace.save();
                                        }
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
                            post(AccountActivityEvent.ERROR, Utils.ERROR_DATA_BASE);
                        }
                    }

                    @Override
                    public void onFailure(Call<Result> call, Throwable t) {
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

    public void dateStringFill(List<DatePlace> datePlace) {
        for (int i = 0; i < datePlace.size(); i++) {
            switch (datePlace.get(i).getPLACE()) {
                case Utils.PLACE:
                    date = datePlace.get(i).getDATE();
                    break;
                case Utils.COUNTRY:
                    cou = datePlace.get(i).getDATE();
                    break;
                case Utils.STATE:
                    sta = datePlace.get(i).getDATE();
                    break;
                case Utils.CITY:
                    ci = datePlace.get(i).getDATE();
                    break;
            }
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
