package com.valdroide.mycitysshopsadm.main.offer;

import android.content.Context;

import com.raizlabs.android.dbflow.sql.language.NameAlias;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.valdroide.mycitysshopsadm.R;
import com.valdroide.mycitysshopsadm.api.APIService;
import com.valdroide.mycitysshopsadm.entities.place.City;
import com.valdroide.mycitysshopsadm.entities.place.City_Table;
import com.valdroide.mycitysshopsadm.entities.response.ResultPlace;
import com.valdroide.mycitysshopsadm.entities.shop.DateShop;
import com.valdroide.mycitysshopsadm.entities.shop.Offer;
import com.valdroide.mycitysshopsadm.entities.response.ResponseWS;
import com.valdroide.mycitysshopsadm.entities.shop.Shop;
import com.valdroide.mycitysshopsadm.entities.shop.Shop_Table;
import com.valdroide.mycitysshopsadm.lib.base.EventBus;
import com.valdroide.mycitysshopsadm.main.account.events.AccountActivityEvent;
import com.valdroide.mycitysshopsadm.main.offer.events.OfferActivityEvent;
import com.valdroide.mycitysshopsadm.utils.Utils;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OfferActivityRepositoryImpl implements OfferActivityRepository {
    private EventBus eventBus;
    private APIService service;
    private List<Offer> offers;
    private ResponseWS responseWS;
    private int id;
    private DateShop dateShop;


    public OfferActivityRepositoryImpl(EventBus eventBus, APIService service) {
        this.eventBus = eventBus;
        this.service = service;
    }

    @Override
    public void getOffer(Context context) {
        Utils.writelogFile(context, "Metodo getOffer (Offer, Repository)");
        try {
            Utils.writelogFile(context, "select COUNT_OFFER from Shop(Offer, Repository)");
            int max = SQLite.select(Shop_Table.COUNT_OFFER).from(Shop.class).querySingle().getCOUNT_OFFER();
            Utils.writelogFile(context, "select Offer(Offer, Repository)");
            offers = SQLite.select().from(Offer.class).orderBy(new NameAlias("ID_OFFER_KEY"), false).queryList();
            if (offers != null) {
                Utils.writelogFile(context, "offers != null(Offer, Repository)");
                int offer_size = offers.size();
                Utils.writelogFile(context, "validate max offer for(Offer, Repository)");
                if (offer_size > max) {
                    Utils.writelogFile(context, "offer_size > max true(Offer, Repository)");
                    int diff = offer_size - max;
                    Utils.writelogFile(context, "diff " + diff + " (Offer, Repository)");
                    for (int i = offer_size - 1; diff < i; i--) {
                        Offer offer = offers.get(i);
                        Utils.writelogFile(context, "deleteOffer: id: " + offer.getID_OFFER_KEY() + " position: " + i +
                                "(Offer, Repository)");
                        deleteOffer(context, offer);
                        Utils.writelogFile(context, "offers.remove(offer)(Offer, Repository)");
                        offers.remove(offer);
                    }
                    Utils.writelogFile(context, "fin FOR(Offer, Repository)");
                }
                Utils.writelogFile(context, "post GETOFFER(Offer, Repository)");
                post(OfferActivityEvent.GETOFFER, offers, max);
            } else {
                Utils.writelogFile(context, " Base de datos error " + context.getString(R.string.error_data_base) + "(Offer, Repository)");
                post(OfferActivityEvent.ERROR, context.getString(R.string.error_data_base));
            }
        } catch (Exception e) {
            Utils.writelogFile(context, " catch error " + e.getMessage() + "(Offer, Repository)");
            post(AccountActivityEvent.ERROR, e.getMessage());
        }
    }

    @Override
    public void saveOffer(final Context context, final Offer offer) {
        Utils.writelogFile(context, "Metodo saveOffer y validate internet (Offer, Repository)");
        if (Utils.isNetworkAvailable(context)) {
            try {
                Utils.writelogFile(context, "Call insertOffer(Offer, Repository)");
                Call<ResultPlace> offerService = service.insertOffer(offer.getID_SHOP_FOREIGN(), Utils.getIdCity(context),
                        offer.getTITLE(), offer.getOFFER(),
                        offer.getURL_IMAGE(), offer.getNAME_IMAGE(), offer.getDATE_UNIQUE(), offer.getEncode());
                offerService.enqueue(new Callback<ResultPlace>() {
                    @Override
                    public void onResponse(Call<ResultPlace> call, Response<ResultPlace> response) {
                        if (response.isSuccessful()) {
                            Utils.writelogFile(context, "Response Successful y get ResponseWS(Offer, Repository)");
                            responseWS = response.body().getResponseWS();
                            if (responseWS != null) {
                                Utils.writelogFile(context, "ResponseWS != null y valida getSuccess(Offer, Repository)");
                                if (responseWS.getSuccess().equals("0")) {
                                    Utils.writelogFile(context, " getSuccess = 0 y getId(Offer, Repository)");
                                    id = responseWS.getId();
                                    if (id != 0) {
                                        Utils.writelogFile(context, " id != 0 y set id offer (Offer, Repository)");
                                        offer.setID_OFFER_KEY(id);
                                        Utils.writelogFile(context, " save offer (Offer, Repository)");
                                        offer.save();
                                        getDateShop(context, offer.getDATE_UNIQUE());
                                        Utils.writelogFile(context, " dateShop update (Offer, Repository)");
                                        dateShop.update();
                                        Utils.writelogFile(context, " post SAVEOFFER (Offer, Repository)");
                                        post(OfferActivityEvent.SAVEOFFER, offer);
                                    } else {
                                        Utils.writelogFile(context, " Base de datos error " + context.getString(R.string.error_data_base) + "(Offer, Repository)");
                                        post(OfferActivityEvent.ERROR, context.getString(R.string.error_data_base));
                                    }
                                } else {
                                    Utils.writelogFile(context, " getSuccess = error " + responseWS.getMessage() + "(Offer, Repository)");
                                    post(OfferActivityEvent.ERROR, responseWS.getMessage());
                                }
                            } else {
                                Utils.writelogFile(context, " Base de datos error " + context.getString(R.string.error_data_base) + "(Offer, Repository)");
                                post(OfferActivityEvent.ERROR, context.getString(R.string.error_data_base));
                            }
                        } else {
                            Utils.writelogFile(context, " Base de datos error " + context.getString(R.string.error_data_base) + "(Offer, Repository)");
                            post(OfferActivityEvent.ERROR, context.getString(R.string.error_data_base));
                        }
                    }

                    @Override
                    public void onFailure(Call<ResultPlace> call, Throwable t) {
                        Utils.writelogFile(context, " Call error " + t.getMessage() + "(Offer, Repository)");
                        post(OfferActivityEvent.ERROR, t.getMessage());
                    }
                });
            } catch (Exception e) {
                Utils.writelogFile(context, " catch error " + e.getMessage() + "(Offer, Repository)");
                post(OfferActivityEvent.ERROR, e.getMessage());
            }
        } else {
            Utils.writelogFile(context, " Internet error " + context.getString(R.string.error_internet) + "(Offer, Repository)");
            post(OfferActivityEvent.ERROR, context.getString(R.string.error_internet));
        }
    }

    @Override
    public void updateOffer(final Context context, final Offer offer) {
        Utils.writelogFile(context, "Metodo updateOffer y validate internet (Offer, Repository)");
        if (Utils.isNetworkAvailable(context)) {
            try {
                Utils.writelogFile(context, "Call updateOffer(Offer, Repository)");
                Call<ResultPlace> offerService = service.updateOffer(offer.getID_OFFER_KEY(), offer.getID_SHOP_FOREIGN(),
                        Utils.getIdCity(context), offer.getTITLE(), offer.getOFFER(), offer.getURL_IMAGE(),
                        offer.getNAME_IMAGE(), offer.getIS_ACTIVE(), offer.getDATE_UNIQUE(), offer.getEncode(),
                        offer.getNAME_BEFORE());
                offerService.enqueue(new Callback<ResultPlace>() {
                    @Override
                    public void onResponse(Call<ResultPlace> call, Response<ResultPlace> response) {
                        if (response.isSuccessful()) {
                            Utils.writelogFile(context, "Response Successful y get ResponseWS(Offer, Repository)");
                            responseWS = response.body().getResponseWS();
                            if (responseWS.getSuccess().equals("0")) {
                                Utils.writelogFile(context, "getSuccess = 0 y offer.update(Offer, Repository)");
                                offer.update();
                                getDateShop(context, offer.getDATE_UNIQUE());
                                Utils.writelogFile(context, "dateShop.update y post UPDATEOFFER(Offer, Repository)");
                                dateShop.update();
                                post(OfferActivityEvent.UPDATEOFFER, offer);
                            } else {
                                Utils.writelogFile(context, " getSuccess = error " + responseWS.getMessage() + "(Offer, Repository)");
                                post(OfferActivityEvent.ERROR, responseWS.getMessage());
                            }
                        } else {
                            Utils.writelogFile(context, " Base de datos error " + context.getString(R.string.error_data_base) + "(Offer, Repository)");
                            post(OfferActivityEvent.ERROR, context.getString(R.string.error_data_base));
                        }
                    }

                    @Override
                    public void onFailure(Call<ResultPlace> call, Throwable t) {
                        Utils.writelogFile(context, " Call error " + t.getMessage() + "(Offer, Repository)");
                        post(OfferActivityEvent.ERROR, t.getMessage());
                    }
                });
            } catch (Exception e) {
                Utils.writelogFile(context, " catch error " + e.getMessage() + "(Offer, Repository)");
                post(OfferActivityEvent.ERROR, e.getMessage());
            }
        } else {
            Utils.writelogFile(context, " Internet error " + context.getString(R.string.error_internet) + "(Offer, Repository)");
            post(OfferActivityEvent.ERROR, context.getString(R.string.error_internet));
        }
    }

    @Override
    public void switchOffer(final Context context, final Offer offer) {
        Utils.writelogFile(context, "Metodo switchOffer y validate internet (Offer, Repository)");
        if (Utils.isNetworkAvailable(context)) {
            try {
                Utils.writelogFile(context, "Call switchOffer(Offer, Repository)");
                offer.setDATE_UNIQUE(Utils.getFechaOficialSeparate());
                Call<ResultPlace> offerService = service.switchOffer(offer.getID_OFFER_KEY(), offer.getID_SHOP_FOREIGN(),
                        Utils.getIdCity(context), offer.getIS_ACTIVE(), offer.getDATE_UNIQUE());
                offerService.enqueue(new Callback<ResultPlace>() {
                    @Override
                    public void onResponse(Call<ResultPlace> call, Response<ResultPlace> response) {
                        if (response.isSuccessful()) {
                            Utils.writelogFile(context, "Response Successful y get ResponseWS(Offer, Repository)");
                            responseWS = response.body().getResponseWS();
                            if (responseWS != null) {
                                Utils.writelogFile(context, "ResponseWS != null y valida getSuccess(Offer, Repository)");
                                if (responseWS.getSuccess().equals("0")) {
                                    Utils.writelogFile(context, " getSuccess = 0 y offer.update()(Offer, Repository)");
                                    offer.update();
                                    getDateShop(context, offer.getDATE_UNIQUE());
                                    Utils.writelogFile(context, " dateShop.update() y post SWITCHOFFER (Offer, Repository)");
                                    dateShop.update();
                                    post(OfferActivityEvent.SWITCHOFFER, offer);
                                } else {
                                    Utils.writelogFile(context, " getSuccess = error " + responseWS.getMessage() + "(Offer, Repository)");
                                    post(OfferActivityEvent.ERROR, responseWS.getMessage());
                                }
                            } else {
                                Utils.writelogFile(context, " Base de datos error " + context.getString(R.string.error_data_base) + "(Offer, Repository)");
                                post(OfferActivityEvent.ERROR, context.getString(R.string.error_data_base));
                            }
                        } else {
                            Utils.writelogFile(context, " Base de datos error " + context.getString(R.string.error_data_base) + "(Offer, Repository)");
                            post(OfferActivityEvent.ERROR, context.getString(R.string.error_data_base));
                        }
                    }

                    @Override
                    public void onFailure(Call<ResultPlace> call, Throwable t) {
                        Utils.writelogFile(context, " Call error " + t.getMessage() + "(Offer, Repository)");
                        post(OfferActivityEvent.ERROR, t.getMessage());
                    }
                });
            } catch (Exception e) {
                Utils.writelogFile(context, " catch error " + e.getMessage() + "(Offer, Repository)");
                post(OfferActivityEvent.ERROR, e.getMessage());
            }
        } else {
            Utils.writelogFile(context, " Internet error " + context.getString(R.string.error_internet) + "(Offer, Repository)");
            post(OfferActivityEvent.ERROR, context.getString(R.string.error_internet));
        }
    }

    @Override
    public void getCity(Context context) {
        Utils.writelogFile(context, "getCity(Offer, Repository)");
        try {
            String city = SQLite.select(City_Table.CITY).from(City.class).where(City_Table.ID_CITY_KEY.eq(Utils.getIdCity(context))).querySingle().getCITY();
            post(OfferActivityEvent.CITY, true, city);
        }catch (Exception e){
            Utils.writelogFile(context, "getCity catch error " + e.getMessage() + " (Offer, Repository)");
            post(AccountActivityEvent.ERROR, e.getMessage());
        }
    }

    private void deleteOffer(final Context context, final Offer offer) {
        Utils.writelogFile(context, "Metodo deleteOffer y Se valida conexion a internet(Offer, Repository)");
        if (Utils.isNetworkAvailable(context)) {
            try {
                Utils.writelogFile(context, "Call deleteOffer(Offer, Repository)");
                Call<ResultPlace> offerService = service.deleteOffer(offer.getID_OFFER_KEY(), offer.getID_SHOP_FOREIGN(),
                        Utils.getIdCity(context), Utils.getFechaOficialSeparate());
                offerService.enqueue(new Callback<ResultPlace>() {
                    @Override
                    public void onResponse(Call<ResultPlace> call, Response<ResultPlace> response) {
                        if (response.isSuccessful()) {
                            Utils.writelogFile(context, "Response Successful y get ResponseWS(Offer, Repository)");
                            responseWS = response.body().getResponseWS();
                            if (responseWS != null) {
                                Utils.writelogFile(context, "ResponseWS != null y valida getSuccess(Offer, Repository)");
                                if (responseWS.getSuccess().equals("0")) {
                                    Utils.writelogFile(context, "offer.delete()(Offer, Repository)");
                                    offer.delete();
                                    getDateShop(context, offer.getDATE_UNIQUE());
                                    Utils.writelogFile(context, "dateShop.update()(Offer, Repository)");
                                    dateShop.update();
                                } else {
                                    Utils.writelogFile(context, " getSuccess = error " + responseWS.getMessage() + "(Offer, Repository)");
                                    post(OfferActivityEvent.ERROR, responseWS.getMessage());
                                }
                            } else {
                                Utils.writelogFile(context, " Base de datos error " + context.getString(R.string.error_data_base) + "(Offer, Repository)");
                                post(OfferActivityEvent.ERROR, context.getString(R.string.error_data_base));
                            }
                        } else {
                            Utils.writelogFile(context, " Base de datos error " + context.getString(R.string.error_data_base) + "(Offer, Repository)");
                            post(OfferActivityEvent.ERROR, context.getString(R.string.error_data_base));
                        }
                    }

                    @Override
                    public void onFailure(Call<ResultPlace> call, Throwable t) {
                        Utils.writelogFile(context, " Call error " + t.getMessage() + "(Offer, Repository)");
                        post(OfferActivityEvent.ERROR, t.getMessage());
                    }
                });
            } catch (Exception e) {
                Utils.writelogFile(context, " catch error " + e.getMessage() + "(Offer, Repository)");
                post(OfferActivityEvent.ERROR, e.getMessage());
            }
        } else {
            Utils.writelogFile(context, " Internet error " + context.getString(R.string.error_internet) + "(Offer, Repository)");
            post(OfferActivityEvent.ERROR, context.getString(R.string.error_internet));
        }
    }

    private void getDateShop(Context context, String date_edit) {
        Utils.writelogFile(context, "Metodo getDateShop (Offer, Repository)");
        try {
            dateShop = SQLite.select().from(DateShop.class).querySingle();
            if (dateShop != null) {
                dateShop.setOFFER_DATE(date_edit);
                dateShop.setDATE_SHOP_DATE(date_edit);
            }
        } catch (Exception e) {
            Utils.writelogFile(context, " catch error " + e.getMessage() + "(Offer, Repository)");
            post(OfferActivityEvent.ERROR, e.getMessage());
        }
    }

    private void post(int type, Offer offer) {
        post(type, offer, null, 0, null, null);
    }

    private void post(int type, boolean isCity, String city) {
        post(type, null, null, 0, city, null);
    }

    private void post(int type, List<Offer> offers, int max) {
        post(type, null, offers, max, null, null);
    }

    private void post(int type, String error) {
        post(type, null, null, 0, null, error);
    }

    private void post(int type, Offer offer, List<Offer> offers, int max_offer,  String city, String error) {
        OfferActivityEvent event = new OfferActivityEvent();
        event.setType(type);
        event.setOffer(offer);
        event.setOffers(offers);
        event.setCity(city);
        event.setMax_offer(max_offer);
        event.setError(error);
        eventBus.post(event);
    }
}
