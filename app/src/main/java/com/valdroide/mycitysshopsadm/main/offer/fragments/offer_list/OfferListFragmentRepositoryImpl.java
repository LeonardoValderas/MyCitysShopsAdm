package com.valdroide.mycitysshopsadm.main.offer.fragments.offer_list;

import android.content.Context;

import com.raizlabs.android.dbflow.sql.language.Delete;
import com.raizlabs.android.dbflow.sql.language.NameAlias;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.valdroide.mycitysshopsadm.R;
import com.valdroide.mycitysshopsadm.api.APIService;
import com.valdroide.mycitysshopsadm.entities.place.City;
import com.valdroide.mycitysshopsadm.entities.place.City_Table;
import com.valdroide.mycitysshopsadm.entities.response.ResponseWS;
import com.valdroide.mycitysshopsadm.entities.response.ResultPlace;
import com.valdroide.mycitysshopsadm.entities.response.ResultUser;
import com.valdroide.mycitysshopsadm.entities.shop.Account;
import com.valdroide.mycitysshopsadm.entities.shop.DateShop;
import com.valdroide.mycitysshopsadm.entities.shop.Draw;
import com.valdroide.mycitysshopsadm.entities.shop.Login;
import com.valdroide.mycitysshopsadm.entities.shop.Login_Table;
import com.valdroide.mycitysshopsadm.entities.shop.Notification;
import com.valdroide.mycitysshopsadm.entities.shop.Offer;
import com.valdroide.mycitysshopsadm.entities.shop.Shop;
import com.valdroide.mycitysshopsadm.entities.shop.Shop_Table;
import com.valdroide.mycitysshopsadm.entities.shop.Support;
import com.valdroide.mycitysshopsadm.lib.base.EventBus;
import com.valdroide.mycitysshopsadm.main.offer.fragments.offer_list.events.OfferListFragmentEvent;
import com.valdroide.mycitysshopsadm.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OfferListFragmentRepositoryImpl implements OfferListFragmentRepository {
    private EventBus eventBus;
    private APIService service;
    private List<Offer> offers;
    private ResponseWS responseWS;
    private int id;
    private DateShop dateShop;
    private DateShop dateUserWS;
    private Account account;
    private Notification notification;
    private List<Offer> offerss;
    private List<Draw> draws;
    private Support support;
    private Shop shop;

    public OfferListFragmentRepositoryImpl(EventBus eventBus, APIService service) {
        this.eventBus = eventBus;
        this.service = service;
    }

    @Override
    public void getOffers(Context context) {
        Utils.writelogFile(context, "Metodo getOffer (OfferList, Repository)");
        try {
            Utils.writelogFile(context, "select COUNT_OFFER from Shop(OfferList, Repository)");
            int max = SQLite.select(Shop_Table.COUNT_OFFER).from(Shop.class).querySingle().getCOUNT_OFFER();
            Utils.writelogFile(context, "select OfferList(OfferList, Repository)");
            offers = SQLite.select().from(Offer.class).orderBy(new NameAlias("ID_OFFER_KEY"), false).queryList();
            if (offers != null) {
                Utils.writelogFile(context, "offers != null(OfferList, Repository)");
                int offer_size = offers.size();
                Utils.writelogFile(context, "validate max offer for(OfferList, Repository)");
                if (offer_size > max) {
                    Utils.writelogFile(context, "offer_size > max true(OfferList, Repository)");
                    //int diff = offer_size - max;
                    //Utils.writelogFile(context, "diff " + diff + " (OfferList, Repository)");

                    List<Integer> ids = new ArrayList<>();
                    for (int i = offer_size - 1; max <= i; i--) {
                        ids.add(offers.get(i).getID_OFFER_KEY());
                    }
                    Utils.writelogFile(context, "fin FOR(OfferList, Repository)");
                    if (ids.size() > 0) {
                        Utils.writelogFile(context, "ids.size() > 0, delete(OfferList, Repository)");
                        deleteOffer(context, ids, max);
                    }
                } else {
                    Utils.writelogFile(context, "post GETOFFER(OfferList, Repository)");
                    post(OfferListFragmentEvent.GETOFFER, offers);
                }
            } else {
                Utils.writelogFile(context, " Base de datos error(OfferList, Repository)");
                post(OfferListFragmentEvent.ERROR, context.getString(R.string.error_data_base));
            }
        } catch (Exception e) {
            Utils.writelogFile(context, " catch error " + e.getMessage() + "(OfferList, Repository)");
            post(OfferListFragmentEvent.ERROR, e.getMessage());
        }
    }

    @Override
    public void switchOffer(final Context context, final Offer offer, final int position) {
        Utils.writelogFile(context, "Metodo switchOffer y validate internet (OfferList, Repository)");
        if (Utils.isNetworkAvailable(context)) {
            String user = getUser(context);
            if (user != null && !user.isEmpty()) {
                try {
                    Utils.writelogFile(context, "Call switchOffer(OfferList, Repository)");
                    offer.setDATE_UNIQUE(Utils.getFechaOficialSeparate());
                    Call<ResultPlace> offerService = service.switchOffer(offer.getID_OFFER_KEY(), offer.getID_SHOP_FOREIGN(),
                            Utils.getIdCity(context), offer.getIS_ACTIVE(), offer.getDATE_UNIQUE(), user);
                    offerService.enqueue(new Callback<ResultPlace>() {
                        @Override
                        public void onResponse(Call<ResultPlace> call, Response<ResultPlace> response) {
                            if (response.isSuccessful()) {
                                Utils.writelogFile(context, "Response Successful y get ResponseWS(OfferList, Repository)");
                                responseWS = response.body().getResponseWS();
                                if (responseWS != null) {
                                    Utils.writelogFile(context, "ResponseWS != null y valida getSuccess(OfferList, Repository)");
                                    if (responseWS.getSuccess().equals("0")) {
                                        Utils.writelogFile(context, " getSuccess = 0 y offer.update()(OfferList, Repository)");
                                        offer.update();
                                        getDateShop(context, offer.getDATE_UNIQUE());
                                        Utils.writelogFile(context, " dateShop.update() y post SWITCHOFFER (OfferList, Repository)");
                                        dateShop.update();
                                        post(OfferListFragmentEvent.SWITCHOFFER, offer, position);
                                    } else {
                                        Utils.writelogFile(context, " getSuccess = error " + responseWS.getMessage() + "(OfferList, Repository)");
                                        post(OfferListFragmentEvent.ERROR, responseWS.getMessage());
                                    }
                                } else {
                                    Utils.writelogFile(context, "Base de datos error(OfferList, Repository)");
                                    post(OfferListFragmentEvent.ERROR, context.getString(R.string.error_data_base));
                                }
                            } else {
                                Utils.writelogFile(context, "Base de datos error(OfferList, Repository)");
                                post(OfferListFragmentEvent.ERROR, context.getString(R.string.error_data_base));
                            }
                        }

                        @Override
                        public void onFailure(Call<ResultPlace> call, Throwable t) {
                            Utils.writelogFile(context, "Call error " + t.getMessage() + "(OfferList, Repository)");
                            post(OfferListFragmentEvent.ERROR, t.getMessage());
                        }
                    });
                } catch (Exception e) {
                    Utils.writelogFile(context, " catch error " + e.getMessage() + "(OfferList, Repository)");
                    post(OfferListFragmentEvent.ERROR, e.getMessage());
                }
            } else {
                Utils.writelogFile(context, "Base de datos error(OfferList, Repository)");
                post(OfferListFragmentEvent.ERROR, context.getString(R.string.error_data_base));
            }
        } else {
            Utils.writelogFile(context, "Internet error(OfferList, Repository)");
            post(OfferListFragmentEvent.ERROR, context.getString(R.string.error_internet));
        }
    }

    @Override
    public void getCity(Context context) {
        Utils.writelogFile(context, "getCity(OfferList, Repository)");
        try {
            String city = SQLite.select(City_Table.CITY).from(City.class).where(City_Table.ID_CITY_KEY.eq(Utils.getIdCity(context))).querySingle().getCITY();
            post(OfferListFragmentEvent.CITY, true, city);
        } catch (Exception e) {
            Utils.writelogFile(context, "getCity catch error " + e.getMessage() + " (OfferList, Repository)");
            post(OfferListFragmentEvent.ERROR, e.getMessage());
        }
    }

    private String getUser(Context context) {
        Utils.writelogFile(context, "getUser(OfferList, Repository)");
        return SQLite.select(Login_Table.USER).from(Login.class).querySingle().getUSER();
    }

    private void deleteOffer(final Context context, final List<Integer> ids, final int max) {
        Utils.writelogFile(context, "Metodo deleteOffer y Se valida conexion a internet(OfferList, Repository)");
        if (Utils.isNetworkAvailable(context)) {
            try {
                Utils.writelogFile(context, "Call deleteOffer(OfferList, Repository)");
                final String date = Utils.getFechaOficialSeparate();
                Call<ResultPlace> offerService = service.deleteOffer(ids, Utils.getIdShop(context),
                        Utils.getIdCity(context), date);
                offerService.enqueue(new Callback<ResultPlace>() {
                    @Override
                    public void onResponse(Call<ResultPlace> call, Response<ResultPlace> response) {
                        if (response.isSuccessful()) {
                            Utils.writelogFile(context, "Response Successful y get ResponseWS(OfferList, Repository)");
                            responseWS = response.body().getResponseWS();
                            if (responseWS != null) {
                                Utils.writelogFile(context, "ResponseWS != null y valida getSuccess(OfferList, Repository)");
                                if (responseWS.getSuccess().equals("0")) {
                                    getDateShop(context, date);
                                    Utils.writelogFile(context, "dateShop.update()(OfferList, Repository)");
                                    dateShop.update();
                                    Utils.writelogFile(context, "offers.remove(offer)(OfferList, Repository)");
                                    for (int i = 0; i < ids.size(); i++) {

                                        for (int j = 0; j < offers.size(); j++) {
                                            if (offers.get(j).getID_OFFER_KEY() == ids.get(i)) {
                                                offers.get(j).delete();
                                                offers.remove(j);
                                                break;
                                            }
                                        }
                                    }
                                    Utils.writelogFile(context, "post GETOFFER(OfferList, Repository)");
                                    post(OfferListFragmentEvent.GETOFFER, offers);
                                } else {
                                    Utils.writelogFile(context, " getSuccess = error " + responseWS.getMessage() + "(OfferList, Repository)");
                                    post(OfferListFragmentEvent.ERROR, responseWS.getMessage());
                                }
                            } else {
                                Utils.writelogFile(context, "Base de datos error(OfferList, Repository)");
                                post(OfferListFragmentEvent.ERROR, context.getString(R.string.error_data_base));
                            }
                        } else {
                            Utils.writelogFile(context, "Base de datos error(OfferList, Repository)");
                            post(OfferListFragmentEvent.ERROR, context.getString(R.string.error_data_base));
                        }
                    }

                    @Override
                    public void onFailure(Call<ResultPlace> call, Throwable t) {
                        Utils.writelogFile(context, "Call error " + t.getMessage() + "(OfferList, Repository)");
                        post(OfferListFragmentEvent.ERROR, t.getMessage());
                    }
                });

            } catch (Exception e) {
                Utils.writelogFile(context, "catch error " + e.getMessage() + "(OfferList, Repository)");
                post(OfferListFragmentEvent.ERROR, e.getMessage());
            }
        } else {
            Utils.writelogFile(context, "Internet error(OfferList, Repository)");
            post(OfferListFragmentEvent.ERROR, context.getString(R.string.error_internet));
        }
    }

    private void getDateShop(Context context, String date_edit) {
        Utils.writelogFile(context, "Metodo getDateShop (OfferList, Repository)");
        try {
            dateShop = getDateShop();
            if (dateShop != null) {
                dateShop.setOFFER_DATE(date_edit);
                dateShop.setDATE_SHOP_DATE(date_edit);
            }
        } catch (Exception e) {
            Utils.writelogFile(context, " catch error " + e.getMessage() + "(OfferList, Repository)");
            post(OfferListFragmentEvent.ERROR, e.getMessage());
        }
    }

    @Override
    public void validateDateShop(final Context context) {
        Utils.writelogFile(context, "Metodo validateDateShop y Se valida conexion a internet(OfferList, Repository)");
        DateShop dateShop = getDateShop();
        if (dateShop != null) {
            if (Utils.isNetworkAvailable(context)) {
                try {
                    Utils.writelogFile(context, "Call validateDateShop(OfferList, Repository)");
                    Call<ResultUser> validateDateShop = service.validateDateShop(dateShop.getID_SHOP_FOREIGN(), Utils.getIdCity(context),
                            dateShop.getACCOUNT_DATE(), dateShop.getOFFER_DATE(), dateShop.getNOTIFICATION_DATE(), dateShop.getDRAW_DATE(),
                            dateShop.getDATE_SHOP_DATE(), dateShop.getSUPPORT_DATE());
                    validateDateShop.enqueue(new Callback<ResultUser>() {
                        @Override
                        public void onResponse(Call<ResultUser> call, Response<ResultUser> response) {
                            if (response.isSuccessful()) {
                                Utils.writelogFile(context, "Response Successful y get ResponseWS(OfferList, Repository)");
                                responseWS = response.body().getResponseWS();
                                if (responseWS != null) {
                                    Utils.writelogFile(context, "ResponseWS != null y valida getSuccess(OfferList, Repository)");
                                    if (responseWS.getSuccess().equals("0")) {
                                        Utils.writelogFile(context, " getSuccess = 0 y getDateShop(OfferList, Repository)");
                                        dateUserWS = response.body().getDateShop();
                                        if (dateUserWS != null) {
                                            Utils.writelogFile(context, "dateUserWS != null y delete DateShop(OfferList, Repository)");
                                            Delete.table(DateShop.class);
                                            Utils.writelogFile(context, "delete DateShop ok y save dateUserWS(OfferList, Repository)");
                                            dateUserWS.save();
                                            Utils.writelogFile(context, "save dateUserWS y getAccount(OfferList, Repository)");
                                        }
                                        account = response.body().getAccount();
                                        if (account != null) {
                                            Utils.writelogFile(context, "accounts != null y delete OfferList(OfferList, Repository)");
                                            Delete.table(Account.class);
                                            Utils.writelogFile(context, "delete Account ok y save accounts(OfferList, Repository)");
                                            account.save();
                                            Utils.writelogFile(context, "save Account ok(OfferList, Repository)");
                                        }

                                        shop = response.body().getShop();
                                        if (shop != null) {
                                            Utils.writelogFile(context, "shop != null y delete Shop(OfferList, Repository)");
                                            Delete.table(Shop.class);
                                            Utils.writelogFile(context, "delete Shop ok y save shop(OfferList, Repository)");
                                            shop.save();
                                            Utils.writelogFile(context, "save shop ok(DrawFragment, OfferList)");
                                        }

                                        offerss = response.body().getOffers();
                                        if (offerss != null) {
                                            Utils.writelogFile(context, "offers != null(OfferList, Repository)");
                                            if (offerss.size() > 0) {
                                                Utils.writelogFile(context, "offers.size() > 0 y delete OfferList(OfferList, Repository)");
                                                Delete.table(Offer.class);
                                                Utils.writelogFile(context, "delete OfferList ok y save offers(OfferList, Repository)");
                                                for (Offer offer : offerss) {
                                                    Utils.writelogFile(context, "save offer: " + offer.getID_OFFER_KEY() + " (OfferList, Repository)");
                                                    offer.save();
                                                }
                                                Utils.writelogFile(context, "fin FOR offer y getNotification (OfferList, Repository)");
                                            } else {
                                                Utils.writelogFile(context, "offers == null y delete OfferList(OfferList, Repository)");
                                                Delete.table(Offer.class);
                                            }
                                        }

                                        draws = response.body().getDraws();
                                        if (draws != null) {
                                            Utils.writelogFile(context, "draws != null(OfferList, Repository)");
                                            if (draws.size() > 0) {
                                                Utils.writelogFile(context, "draws.size() > 0 y delete Draw(OfferList, Repository)");
                                                Delete.table(Draw.class);
                                                Utils.writelogFile(context, "delete draws ok y save Draw(OfferList, Repository)");
                                                for (Draw draw : draws) {
                                                    Utils.writelogFile(context, "save draw: " + draw.getID_DRAW_KEY() + " (OfferList, Repository)");
                                                    draw.save();
                                                }
                                                Utils.writelogFile(context, "fin FOR draw y getNotification(OfferList, Repository)");
                                            } else {
                                                Utils.writelogFile(context, "draws == null y delete Draw(OfferList, Repository)");
                                                Delete.table(Draw.class);
                                            }
                                        }

                                        notification = response.body().getNotification();
                                        if (notification != null) {
                                            Utils.writelogFile(context, "notification != null y delete Notification(OfferList, Repository)");
                                            Delete.table(Notification.class);
                                            Utils.writelogFile(context, "delete Notification ok y save notification(OfferList, Repository)");
                                            notification.save();
                                            Utils.writelogFile(context, "save notification ok y getSupport(OfferList, Repository)");
                                        }

                                        support = response.body().getSupport();
                                        if (support != null) {
                                            Utils.writelogFile(context, "support != null y delete Support(OfferList, Repository)");
                                            Delete.table(Support.class);
                                            Utils.writelogFile(context, "delete Support ok y save support(OfferList, Repository)");
                                            support.save();
                                            Utils.writelogFile(context, "save Support ok y GOTONAV(OfferList, Repository)");
                                        }
                                        post(OfferListFragmentEvent.UPDATESUCCESS);
                                    } else if (responseWS.getSuccess().equals("4")) {
                                        Utils.writelogFile(context, "getSuccess = 4(OfferList, Repository)");
                                        post(OfferListFragmentEvent.WITHOUTCHANGE);
                                    } else {
                                        Utils.writelogFile(context, "getSuccess = error " + responseWS.getMessage() + "(OfferList, Repository)");
                                        post(OfferListFragmentEvent.ERROR, responseWS.getMessage());
                                    }
                                }
                            } else {
                                Utils.writelogFile(context, "!isSuccess(OfferList, Repository)");
                                post(OfferListFragmentEvent.ERROR, context.getString(R.string.error_data_base));
                            }
                        }

                        @Override
                        public void onFailure(Call<ResultUser> call, Throwable t) {
                            Utils.writelogFile(context, "Call error " + t.getMessage() + "(OfferList, Repository)");
                            post(OfferListFragmentEvent.ERROR, t.getMessage());
                        }
                    });
                } catch (Exception e) {
                    Utils.writelogFile(context, "catch error " + e.getMessage() + "(OfferList, Repository)");
                    post(OfferListFragmentEvent.ERROR, e.getMessage());
                }
            } else {
                Utils.writelogFile(context, "Internet error(OfferList, Repository)");
                post(OfferListFragmentEvent.ERROR, context.getString(R.string.error_internet));
            }
        } else {
            Utils.writelogFile(context, "dateshop == null(OfferList, Repository)");
            post(OfferListFragmentEvent.ERROR, context.getString(R.string.error_data_base));
        }
    }

    private DateShop getDateShop() {
        return SQLite.select().from(DateShop.class).querySingle();
    }

    private void post(int type) {
        post(type, null, 0, null, null, null);
    }

    private void post(int type, Offer offer, int position) {
        post(type, offer, position, null, null, null);
    }

    private void post(int type, boolean isCity, String city) {
        post(type, null, 0, null, city, null);
    }

    private void post(int type, List<Offer> offers) {
        post(type, null, 0, offers, null, null);
    }

    private void post(int type, String error) {
        post(type, null, 0, null, null, error);
    }

    private void post(int type, Offer offer, int position, List<Offer> offers, String city, String error) {
        OfferListFragmentEvent event = new OfferListFragmentEvent();
        event.setType(type);
        event.setOffer(offer);
        event.setOffers(offers);
        event.setCity(city);
        event.setPosition(position);
        event.setError(error);
        eventBus.post(event);
    }
}
