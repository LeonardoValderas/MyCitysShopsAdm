package com.valdroide.mycitysshopsadm.main.offer.fragments.offer;

import android.content.Context;

import com.raizlabs.android.dbflow.sql.language.Delete;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.valdroide.mycitysshopsadm.R;
import com.valdroide.mycitysshopsadm.api.APIService;
import com.valdroide.mycitysshopsadm.entities.place.City;
import com.valdroide.mycitysshopsadm.entities.place.City_Table;
import com.valdroide.mycitysshopsadm.entities.response.ResultPlace;
import com.valdroide.mycitysshopsadm.entities.response.ResultUser;
import com.valdroide.mycitysshopsadm.entities.shop.Account;
import com.valdroide.mycitysshopsadm.entities.shop.Login;
import com.valdroide.mycitysshopsadm.entities.shop.Login_Table;
import com.valdroide.mycitysshopsadm.entities.shop.Offer;
import com.valdroide.mycitysshopsadm.entities.shop.DateShop;
import com.valdroide.mycitysshopsadm.entities.shop.Draw;
import com.valdroide.mycitysshopsadm.entities.shop.Notification;
import com.valdroide.mycitysshopsadm.entities.response.ResponseWS;
import com.valdroide.mycitysshopsadm.entities.shop.Offer_Table;
import com.valdroide.mycitysshopsadm.entities.shop.Shop;
import com.valdroide.mycitysshopsadm.entities.shop.Shop_Table;
import com.valdroide.mycitysshopsadm.entities.shop.Support;
import com.valdroide.mycitysshopsadm.lib.base.EventBus;
import com.valdroide.mycitysshopsadm.main.offer.fragments.offer.events.OfferFragmentEvent;
import com.valdroide.mycitysshopsadm.utils.Utils;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OfferFragmentRepositoryImpl implements OfferFragmentRepository {
    private EventBus eventBus;
    private APIService service;
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
    private Offer offer;

    public OfferFragmentRepositoryImpl(EventBus eventBus, APIService service) {
        this.eventBus = eventBus;
        this.service = service;
    }

    @Override
    public void getOfferForId(Context context, int id_offer) {
        try {
            Utils.writelogFile(context, "getOfferForId(Offer, Repository)");
            offer = SQLite.select().from(Offer.class).where(Offer_Table.ID_OFFER_KEY.eq(id_offer)).querySingle();
            post(OfferFragmentEvent.GETOFFERFORID, offer);
        } catch (Exception e) {
            Utils.writelogFile(context, " catch error " + e.getMessage() + "(Offer, Repository)");
            post(OfferFragmentEvent.ERROR, e.getMessage());
        }
    }

    @Override
    public void getQuantity(Context context) {
        try {
            Utils.writelogFile(context, "getQuantity(Offer, Repository)");
            int max = SQLite.select(Shop_Table.COUNT_OFFER).from(Shop.class).querySingle().getCOUNT_OFFER();
            List<Offer> offers = SQLite.select().from(Offer.class).queryList();

            int diff = max - offers.size();

            if (diff < 0)
                diff = 0;

            post(OfferFragmentEvent.GETQUANTITY, diff);
        } catch (Exception e) {
            Utils.writelogFile(context, " catch error " + e.getMessage() + "(Offer, Repository)");
            post(OfferFragmentEvent.ERROR, e.getMessage());
        }
    }

    @Override
    public void saveOffer(final Context context, final Offer offer) {
        Utils.writelogFile(context, "Metodo saveOffer y validate internet (Offer, Repository)");
        if (Utils.isNetworkAvailable(context)) {
            String user = getUser(context);
            if (user != null && !user.isEmpty()) {
                try {
                    Utils.writelogFile(context, "Call insertOffer(Offer, Repository)");
                    Call<ResultPlace> offerService = service.insertOffer(offer.getID_SHOP_FOREIGN(), Utils.getIdCity(context),
                            offer.getTITLE(), offer.getOFFER(),
                            offer.getURL_IMAGE(), offer.getNAME_IMAGE(), offer.getDATE_UNIQUE(), offer.getEncode(), user);
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
                                            Utils.writelogFile(context, "id != 0 y set id offer (Offer, Repository)");
                                            offer.setID_OFFER_KEY(id);
                                            Utils.writelogFile(context, "save offer (Offer, Repository)");
                                            offer.save();
                                            getDateShop(context, offer.getDATE_UNIQUE());
                                            Utils.writelogFile(context, "dateShop update (Offer, Repository)");
                                            dateShop.update();
                                            Utils.writelogFile(context, "post SAVEOFFER (Offer, Repository)");
                                            post(OfferFragmentEvent.SAVEOFFER);
                                        } else {
                                            Utils.writelogFile(context, "Base de datos error(Offer, Repository)");
                                            post(OfferFragmentEvent.ERROR, context.getString(R.string.error_data_base));
                                        }
                                    } else {
                                        Utils.writelogFile(context, "getSuccess = error " + responseWS.getMessage() + "(Offer, Repository)");
                                        post(OfferFragmentEvent.ERROR, responseWS.getMessage());
                                    }
                                } else {
                                    Utils.writelogFile(context, "Base de datos error(Offer, Repository)");
                                    post(OfferFragmentEvent.ERROR, context.getString(R.string.error_data_base));
                                }
                            } else {
                                Utils.writelogFile(context, "Base de datos error(Offer, Repository)");
                                post(OfferFragmentEvent.ERROR, context.getString(R.string.error_data_base));
                            }
                        }

                        @Override
                        public void onFailure(Call<ResultPlace> call, Throwable t) {
                            Utils.writelogFile(context, "Call error " + t.getMessage() + "(Offer, Repository)");
                            post(OfferFragmentEvent.ERROR, t.getMessage());
                        }
                    });
                } catch (Exception e) {
                    Utils.writelogFile(context, "catch error " + e.getMessage() + "(Offer, Repository)");
                    post(OfferFragmentEvent.ERROR, e.getMessage());
                }
            } else {
                Utils.writelogFile(context, "Base de datos error(Offer, Repository)");
                post(OfferFragmentEvent.ERROR, context.getString(R.string.error_data_base));
            }
        } else {
            Utils.writelogFile(context, "Internet error (Offer, Repository)");
            post(OfferFragmentEvent.ERROR, context.getString(R.string.error_internet));
        }
    }

    @Override
    public void updateOffer(final Context context, final Offer offer) {
        Utils.writelogFile(context, "Metodo updateOffer y validate internet (Offer, Repository)");
        if (Utils.isNetworkAvailable(context)) {
            String user = getUser(context);
            if (user != null && !user.isEmpty()) {
                try {
                    Utils.writelogFile(context, "Call updateOffer(Offer, Repository)");
                    Call<ResultPlace> offerService = service.updateOffer(offer.getID_OFFER_KEY(), offer.getID_SHOP_FOREIGN(),
                            Utils.getIdCity(context), offer.getTITLE(), offer.getOFFER(), offer.getURL_IMAGE(),
                            offer.getNAME_IMAGE(), offer.getIS_ACTIVE(), offer.getDATE_UNIQUE(), offer.getEncode(),
                            offer.getNAME_BEFORE(), user);
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
                                    post(OfferFragmentEvent.UPDATEOFFERSUCCESS);
                                } else {
                                    Utils.writelogFile(context, " getSuccess = error " + responseWS.getMessage() + "(Offer, Repository)");
                                    post(OfferFragmentEvent.ERROR, responseWS.getMessage());
                                }
                            } else {
                                Utils.writelogFile(context, " Base de datos error(Offer, Repository)");
                                post(OfferFragmentEvent.ERROR, context.getString(R.string.error_data_base));
                            }
                        }

                        @Override
                        public void onFailure(Call<ResultPlace> call, Throwable t) {
                            Utils.writelogFile(context, " Call error " + t.getMessage() + "(Offer, Repository)");
                            post(OfferFragmentEvent.ERROR, t.getMessage());
                        }
                    });
                } catch (Exception e) {
                    Utils.writelogFile(context, " catch error " + e.getMessage() + "(Offer, Repository)");
                    post(OfferFragmentEvent.ERROR, e.getMessage());
                }
            } else {
                Utils.writelogFile(context, "Base de datos error(Offer, Repository)");
                post(OfferFragmentEvent.ERROR, context.getString(R.string.error_data_base));
            }
        } else {
            Utils.writelogFile(context, "Internet error(Offer, Repository)");
            post(OfferFragmentEvent.ERROR, context.getString(R.string.error_internet));
        }
    }

    @Override
    public void getCity(Context context) {
        Utils.writelogFile(context, "getCity(Offer, Repository)");
        try {
            String city = SQLite.select(City_Table.CITY).from(City.class).where(City_Table.ID_CITY_KEY.eq(Utils.getIdCity(context))).querySingle().getCITY();
            post(OfferFragmentEvent.CITY, true, city);
        } catch (Exception e) {
            Utils.writelogFile(context, "getCity catch error " + e.getMessage() + " (Offer, Repository)");
            post(OfferFragmentEvent.ERROR, e.getMessage());
        }
    }

    private String getUser(Context context) {
        Utils.writelogFile(context, "getUser(Offer, Repository)");
        return SQLite.select(Login_Table.USER).from(Login.class).querySingle().getUSER();
    }

    private void getDateShop(Context context, String date_edit) {
        Utils.writelogFile(context, "Metodo getDateShop (Offer, Repository)");
        try {
            dateShop = getDateShop();
            if (dateShop != null) {
                dateShop.setOFFER_DATE(date_edit);
                dateShop.setDATE_SHOP_DATE(date_edit);
            }
        } catch (Exception e) {
            Utils.writelogFile(context, " catch error " + e.getMessage() + "(Offer, Repository)");
            post(OfferFragmentEvent.ERROR, e.getMessage());
        }
    }

    @Override
    public void validateDateShop(final Context context) {
        Utils.writelogFile(context, "Metodo validateDateShop y Se valida conexion a internet(Offer, Repository)");
        DateShop dateShop = getDateShop();
        if (dateShop != null) {
            if (Utils.isNetworkAvailable(context)) {
                try {
                    Utils.writelogFile(context, "Call validateDateShop(Offer, Repository)");
                    Call<ResultUser> validateDateShop = service.validateDateShop(dateShop.getID_SHOP_FOREIGN(), Utils.getIdCity(context),
                            dateShop.getACCOUNT_DATE(), dateShop.getOFFER_DATE(), dateShop.getNOTIFICATION_DATE(), dateShop.getDRAW_DATE(),
                            dateShop.getDATE_SHOP_DATE(), dateShop.getSUPPORT_DATE());
                    validateDateShop.enqueue(new Callback<ResultUser>() {
                        @Override
                        public void onResponse(Call<ResultUser> call, Response<ResultUser> response) {
                            if (response.isSuccessful()) {
                                Utils.writelogFile(context, "Response Successful y get ResponseWS(Offer, Repository)");
                                responseWS = response.body().getResponseWS();
                                if (responseWS != null) {
                                    Utils.writelogFile(context, "ResponseWS != null y valida getSuccess(Offer, Repository)");
                                    if (responseWS.getSuccess().equals("0")) {
                                        Utils.writelogFile(context, " getSuccess = 0 y getDateShop(Offer, Repository)");
                                        dateUserWS = response.body().getDateShop();
                                        if (dateUserWS != null) {
                                            Utils.writelogFile(context, "dateUserWS != null y delete DateShop(Offer, Repository)");
                                            Delete.table(DateShop.class);
                                            Utils.writelogFile(context, "delete DateShop ok y save dateUserWS(Offer, Repository)");
                                            dateUserWS.save();
                                            Utils.writelogFile(context, "save dateUserWS y getAccount(Offer, Repository)");
                                        }
                                        account = response.body().getAccount();
                                        if (account != null) {
                                            Utils.writelogFile(context, "accounts != null y delete Offer(Offer, Repository)");
                                            Delete.table(Account.class);
                                            Utils.writelogFile(context, "delete Account ok y save accounts(Offer, Repository)");
                                            account.save();
                                            Utils.writelogFile(context, "save Account ok(Offer, Repository)");
                                        }

                                        shop = response.body().getShop();
                                        if (shop != null) {
                                            Utils.writelogFile(context, "shop != null y delete Shop(Offer, Repository)");
                                            Delete.table(Shop.class);
                                            Utils.writelogFile(context, "delete Shop ok y save shop(Offer, Repository)");
                                            shop.save();
                                            Utils.writelogFile(context, "save shop ok(DrawFragment, Offer)");
                                        }

                                        offerss = response.body().getOffers();
                                        if (offerss != null) {
                                            Utils.writelogFile(context, "offers != null(Offer, Repository)");
                                            if (offerss.size() > 0) {
                                                Utils.writelogFile(context, "offers.size() > 0 y delete Offer(Offer, Repository)");
                                                Delete.table(Offer.class);
                                                Utils.writelogFile(context, "delete Offer ok y save offers(Offer, Repository)");
                                                for (Offer offer : offerss) {
                                                    Utils.writelogFile(context, "save offer: " + offer.getID_OFFER_KEY() + " (Offer, Repository)");
                                                    offer.save();
                                                }
                                                Utils.writelogFile(context, "fin FOR offer y getNotification (Offer, Repository)");
                                            } else {
                                                Utils.writelogFile(context, "offers == null y delete Offer(Offer, Repository)");
                                                Delete.table(Offer.class);
                                            }
                                        }

                                        draws = response.body().getDraws();
                                        if (draws != null) {
                                            Utils.writelogFile(context, "draws != null(Offer, Repository)");
                                            if (draws.size() > 0) {
                                                Utils.writelogFile(context, "draws.size() > 0 y delete Draw(Offer, Repository)");
                                                Delete.table(Draw.class);
                                                Utils.writelogFile(context, "delete draws ok y save Draw(Offer, Repository)");
                                                for (Draw draw : draws) {
                                                    Utils.writelogFile(context, "save draw: " + draw.getID_DRAW_KEY() + " (Offer, Repository)");
                                                    draw.save();
                                                }
                                                Utils.writelogFile(context, "fin FOR draw y getNotification(Offer, Repository)");
                                            } else {
                                                Utils.writelogFile(context, "draws == null y delete Draw(Offer, Repository)");
                                                Delete.table(Draw.class);
                                            }
                                        }

                                        notification = response.body().getNotification();
                                        if (notification != null) {
                                            Utils.writelogFile(context, "notification != null y delete Notification(Offer, Repository)");
                                            Delete.table(Notification.class);
                                            Utils.writelogFile(context, "delete Notification ok y save notification(Offer, Repository)");
                                            notification.save();
                                            Utils.writelogFile(context, "save notification ok y getSupport(Offer, Repository)");
                                        }

                                        support = response.body().getSupport();
                                        if (support != null) {
                                            Utils.writelogFile(context, "support != null y delete Support(Offer, Repository)");
                                            Delete.table(Support.class);
                                            Utils.writelogFile(context, "delete Support ok y save support(Offer, Repository)");
                                            support.save();
                                            Utils.writelogFile(context, "save Support ok y GOTONAV(Offer, Repository)");
                                        }
                                        post(OfferFragmentEvent.UPDATEDATESUCCESS);
                                    } else if (responseWS.getSuccess().equals("4")) {
                                        Utils.writelogFile(context, "getSuccess = 4 y post GOTONAV(Offer, Repository)");
                                        post(OfferFragmentEvent.UPDATEDATESUCCESS);
                                    } else {
                                        Utils.writelogFile(context, "getSuccess = error " + responseWS.getMessage() + "(Offer, Repository)");
                                        post(OfferFragmentEvent.ERROR, responseWS.getMessage());
                                    }
                                }
                            } else {
                                Utils.writelogFile(context, "!isSuccess(Offer, Repository)");
                                post(OfferFragmentEvent.ERROR, context.getString(R.string.error_data_base));
                            }
                        }

                        @Override
                        public void onFailure(Call<ResultUser> call, Throwable t) {
                            Utils.writelogFile(context, "Call error " + t.getMessage() + "(Offer, Repository)");
                            post(OfferFragmentEvent.ERROR, t.getMessage());
                        }
                    });
                } catch (Exception e) {
                    Utils.writelogFile(context, "catch error " + e.getMessage() + "(Offer, Repository)");
                    post(OfferFragmentEvent.ERROR, e.getMessage());
                }
            } else {
                Utils.writelogFile(context, "Internet error(Offer, Repository)");
                post(OfferFragmentEvent.ERROR, context.getString(R.string.error_internet));
            }
        } else {
            Utils.writelogFile(context, "dateshop == null(Offer, Repository)");
            post(OfferFragmentEvent.ERROR, context.getString(R.string.error_data_base));
        }
    }

    private DateShop getDateShop() {
        return SQLite.select().from(DateShop.class).querySingle();
    }

    private void post(int type) {
        post(type, null, 0, null, null);
    }

    private void post(int type, Offer offer) {
        post(type, offer, 0, null, null);
    }

    private void post(int type, boolean isCity, String city) {
        post(type, null, 0, city, null);
    }

    private void post(int type, int max) {
        post(type, null, max, null, null);
    }

    private void post(int type, String error) {
        post(type, null, 0, null, error);
    }

    private void post(int type, Offer offer, int max_offer, String city, String error) {
        OfferFragmentEvent event = new OfferFragmentEvent();
        event.setType(type);
        event.setOffer(offer);
        event.setCity(city);
        event.setMax_offer(max_offer);
        event.setError(error);
        eventBus.post(event);
    }
}
