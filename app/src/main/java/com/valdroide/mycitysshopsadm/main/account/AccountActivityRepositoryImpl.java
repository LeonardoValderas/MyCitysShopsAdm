package com.valdroide.mycitysshopsadm.main.account;

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
import com.valdroide.mycitysshopsadm.entities.response.ResponseWS;
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
import com.valdroide.mycitysshopsadm.main.account.events.AccountActivityEvent;
import com.valdroide.mycitysshopsadm.utils.Utils;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class AccountActivityRepositoryImpl implements AccountActivityRepository {
    private EventBus eventBus;
    private APIService service;
    private Account accounts = new Account();
    private ResponseWS responseWS;
    private DateShop dateShop;
    private DateShop dateUserWS;
    private Account account;
    private Shop shop;
    private Notification notification;
    private List<Offer> offers;
    private List<Draw> draws;
    private Support support;

    public AccountActivityRepositoryImpl(EventBus eventBus, APIService service) {
        this.eventBus = eventBus;
        this.service = service;
    }

    @Override
    public void getAccount(Context context) {
        Utils.writelogFile(context, "getAccount() (Account- Repository)");
        try {
            accounts = SQLite.select().from(Account.class).querySingle();
            if (accounts == null) {
                Utils.writelogFile(context, "accounts == null es decir que no hay cargado nada y post GETACCOUNT (Account- Repository)");
                accounts = new Account();
            }

            post(AccountActivityEvent.GETACCOUNT, accounts);
        } catch (Exception e) {
            Utils.writelogFile(context, "accounts catch error " + e.getMessage() + " (Account, Repository)");
            post(AccountActivityEvent.ERROR, e.getMessage());
        }
    }

    @Override
    public void getCity(Context context) {
        Utils.writelogFile(context, "getCity(Account, Repository)");
        try {
            String city = SQLite.select(City_Table.CITY).from(City.class).where(City_Table.ID_CITY_KEY.eq(Utils.getIdCity(context))).querySingle().getCITY();
            post(AccountActivityEvent.CITY, true, city);
        } catch (Exception e) {
            Utils.writelogFile(context, "getCity catch error " + e.getMessage() + " (Account, Repository)");
            post(AccountActivityEvent.ERROR, e.getMessage());
        }

    }

    @Override
    public void updateAccount(final Context context, final Account account) {
        Utils.writelogFile(context, "Metodo updateAccount y Se valida conexion a internet(Account, Repository)");
        if (Utils.isNetworkAvailable(context)) {
            String user = getUser(context);
            if (user != null && !user.isEmpty()) {
                try {
                    Utils.writelogFile(context, "Call updateAccount(Account, Repository)");
                    Call<ResultPlace> accountService = service.updateAccount(Utils.getIdShop(context), Utils.getIdCity(context),
                            account.getID_ACCOUNT_KEY(), account.getEncode(), account.getURL_LOGO(),
                            account.getNAME_LOGO(), account.getNAME_BEFORE(), account.getDESCRIPTION(),
                            account.getWORKING_HOURS(), account.getPHONE(), account.getEMAIL(),
                            account.getWEB(), account.getWHATSAAP(), account.getFACEBOOK(), account.getINSTAGRAM(),
                            account.getTWITTER(), account.getSNAPCHAT(), account.getLATITUD(), account.getLONGITUD(),
                            account.getADDRESS(), account.getDATE_UNIQUE(), user);
                    accountService.enqueue(new Callback<ResultPlace>() {
                        @Override
                        public void onResponse(Call<ResultPlace> call, Response<ResultPlace> response) {
                            if (response.isSuccessful()) {
                                Utils.writelogFile(context, "Response Successful y get ResponseWS(Account, Repository)");
                                responseWS = response.body().getResponseWS();
                                if (responseWS != null) {
                                    Utils.writelogFile(context, "ResponseWS != null y valida getSuccess(Account, Repository)");
                                    if (responseWS.getSuccess().equals("0")) {
                                        Utils.writelogFile(context, "getSuccess = 0 y accounts.update()(Account, Repository)");
                                        account.update();
                                        Utils.writelogFile(context, "accounts.update() ok y getDateShop(Account, Repository)");
                                        getDateShop(context, account.getDATE_UNIQUE());
                                        Utils.writelogFile(context, "dateShop.update()(Account, Repository)");
                                        dateShop.update();
                                        Utils.writelogFile(context, "dateShop.update() ok y post UPDATEACCOUNT (Account, Repository)");
                                        post(AccountActivityEvent.UPDATEACCOUNT);
                                    } else {
                                        Utils.writelogFile(context, "getSuccess = error " + responseWS.getMessage() + "(Account, Repository)");
                                        post(AccountActivityEvent.ERROR, responseWS.getMessage());
                                    }
                                } else {
                                    Utils.writelogFile(context, "Base de datos error(Account, Repository)");
                                    post(AccountActivityEvent.ERROR, context.getString(R.string.error_data_base));
                                }
                            } else {
                                Utils.writelogFile(context, "Base de datos error(Account, Repository)");
                                post(AccountActivityEvent.ERROR, context.getString(R.string.error_data_base));
                            }
                        }

                        @Override
                        public void onFailure(Call<ResultPlace> call, Throwable t) {
                            Utils.writelogFile(context, "Call error " + t.getMessage() + "(Account, Repository)");
                            post(AccountActivityEvent.ERROR, t.getMessage());
                        }
                    });
                } catch (Exception e) {
                    Utils.writelogFile(context, "catch error " + e.getMessage() + "(Account, Repository)");
                    post(AccountActivityEvent.ERROR, e.getMessage());
                }
            } else {
                Utils.writelogFile(context, "Base de datos error(Account, Repository)");
                post(AccountActivityEvent.ERROR, context.getString(R.string.error_data_base));
            }
        } else {
            Utils.writelogFile(context, "Internet error(Account, Repository)");
            post(AccountActivityEvent.ERROR, context.getString(R.string.error_internet));
        }
    }

    private String getUser(Context context) {
        Utils.writelogFile(context, "getUser(Account, Repository)");
        return SQLite.select(Login_Table.USER).from(Login.class).querySingle().getUSER();
    }

    private void getDateShop(Context context, String date_edit) {
        try {
            Utils.writelogFile(context, "getDateShop y get DateShop(Account, Repository)");
            dateShop = getDateShop();
            if (dateShop != null) {
                Utils.writelogFile(context, "dateShop != null y set setACCOUNT_DATE, setDATE_SHOP_DATE(Account, Repository)");
                dateShop.setACCOUNT_DATE(date_edit);
                dateShop.setDATE_SHOP_DATE(date_edit);
            } else {
                Utils.writelogFile(context, " Base de datos error(Account, Repository)");
                post(AccountActivityEvent.ERROR, context.getString(R.string.error_data_base));
            }
        } catch (Exception e) {
            Utils.writelogFile(context, " Call error " + e.getMessage() + "(Account, Repository)");
            post(AccountActivityEvent.ERROR, e.getMessage());
        }
    }

    private DateShop getDateShop() {
        return SQLite.select().from(DateShop.class).querySingle();
    }

    @Override
    public void validateDateShop(final Context context) {
        Utils.writelogFile(context, "Metodo validateDateShop y Se valida conexion a internet(Account, Repository)");
        DateShop dateShop = getDateShop();
        if (dateShop != null) {
            if (Utils.isNetworkAvailable(context)) {
                try {
                    Utils.writelogFile(context, "Call validateDateShop(Account, Repository)");
                    Call<ResultUser> validateDateShop = service.validateDateShop(dateShop.getID_SHOP_FOREIGN(), Utils.getIdCity(context),
                            dateShop.getACCOUNT_DATE(), dateShop.getOFFER_DATE(), dateShop.getNOTIFICATION_DATE(), dateShop.getDRAW_DATE(),
                            dateShop.getDATE_SHOP_DATE(), dateShop.getSUPPORT_DATE());
                    validateDateShop.enqueue(new Callback<ResultUser>() {
                        @Override
                        public void onResponse(Call<ResultUser> call, Response<ResultUser> response) {
                            if (response.isSuccessful()) {
                                Utils.writelogFile(context, "Response Successful y get ResponseWS(Account, Repository)");
                                responseWS = response.body().getResponseWS();
                                if (responseWS != null) {
                                    Utils.writelogFile(context, "ResponseWS != null y valida getSuccess(Account, Repository)");
                                    if (responseWS.getSuccess().equals("0")) {
                                        Utils.writelogFile(context, " getSuccess = 0 y getDateShop(Account, Repository)");
                                        dateUserWS = response.body().getDateShop();
                                        if (dateUserWS != null) {
                                            Utils.writelogFile(context, "dateUserWS != null y delete DateShop(Account, Repository)");
                                            Delete.table(DateShop.class);
                                            Utils.writelogFile(context, "delete DateShop ok y save dateUserWS(Account, Repository)");
                                            dateUserWS.save();
                                            Utils.writelogFile(context, "save dateUserWS y getAccount(Account, Repository)");
                                        }
                                        account = response.body().getAccount();
                                        if (account != null) {
                                            Utils.writelogFile(context, "accounts != null y delete Account(Account, Repository)");
                                            Delete.table(Account.class);
                                            Utils.writelogFile(context, "delete Account ok y save accounts(Account, Repository)");
                                            account.save();
                                            Utils.writelogFile(context, "save Account ok(Account, Repository)");
                                        }
                                        shop = response.body().getShop();
                                        if (shop != null) {
                                            Utils.writelogFile(context, "shop != null y delete Shop(Account, Repository)");
                                            Delete.table(Shop.class);
                                            Utils.writelogFile(context, "delete Shop ok y save shop(Account, Repository)");
                                            shop.save();
                                            Utils.writelogFile(context, "save shop ok(Account, Repository)");
                                        }

                                        offers = response.body().getOffers();
                                        if (offers != null) {
                                            Utils.writelogFile(context, "offers != null(Account, Repository)");
                                            if (offers.size() > 0) {
                                                Utils.writelogFile(context, "offers.size() > 0 y delete Offer(Account, Repository)");
                                                Delete.table(Offer.class);
                                                Utils.writelogFile(context, "delete Offer ok y save offers(Account, Repository)");
                                                for (Offer offer : offers) {
                                                    Utils.writelogFile(context, "save offer: " + offer.getID_OFFER_KEY() + " (Account, Repository)");
                                                    offer.save();
                                                }
                                                Utils.writelogFile(context, "fin FOR offer y getNotification (Account, Repository)");
                                            } else {
                                                Utils.writelogFile(context, "offers == null y delete Offer(Account, Repository)");
                                                Delete.table(Offer.class);
                                            }
                                        }
                                        draws = response.body().getDraws();
                                        if (draws != null) {
                                            Utils.writelogFile(context, "draws != null(Account, Repository)");
                                            if (draws.size() > 0) {
                                                Utils.writelogFile(context, "draws.size() > 0 y delete Draw(Account, Repository)");
                                                Delete.table(Draw.class);
                                                Utils.writelogFile(context, "delete draws ok y save Draw(Account, Repository)");
                                                for (Draw draw : draws) {
                                                    Utils.writelogFile(context, "save draw: " + draw.getID_DRAW_KEY() + " (Account, Repository)");
                                                    draw.save();
                                                }
                                                Utils.writelogFile(context, "fin FOR draw y getNotification(Account, Repository)");
                                            } else {
                                                Utils.writelogFile(context, "draws == null y delete Draw(Account, Repository)");
                                                Delete.table(Draw.class);
                                            }
                                        }

                                        notification = response.body().getNotification();
                                        if (notification != null) {
                                            Utils.writelogFile(context, "notification != null y delete Notification(Account, Repository)");
                                            Delete.table(Notification.class);
                                            Utils.writelogFile(context, "delete Notification ok y save notification(Account, Repository)");
                                            notification.save();
                                            Utils.writelogFile(context, "save notification ok y getSupport(Account, Repository)");
                                        }

                                        support = response.body().getSupport();
                                        if (support != null) {
                                            Utils.writelogFile(context, "support != null y delete Support(Account, Repository)");
                                            Delete.table(Support.class);
                                            Utils.writelogFile(context, "delete Support ok y save support(Account, Repository)");
                                            support.save();
                                            Utils.writelogFile(context, "save Support ok y GOTONAV(Account, Repository)");
                                        }
                                        post(AccountActivityEvent.UPDATESUCCESS);
                                    } else if (responseWS.getSuccess().equals("4")) {
                                        Utils.writelogFile(context, "getSuccess = 4 y post GOTONAV(Account, Repository)");
                                        post(AccountActivityEvent.UPDATESUCCESS);
                                    } else {
                                        Utils.writelogFile(context, "getSuccess = error " + responseWS.getMessage() + "(Account, Repository)");
                                        post(AccountActivityEvent.ERROR, responseWS.getMessage());
                                    }
                                }
                            } else {
                                Utils.writelogFile(context, "!isSuccess(Account, Repository)");
                                post(AccountActivityEvent.ERROR, context.getString(R.string.error_data_base));
                            }
                        }

                        @Override
                        public void onFailure(Call<ResultUser> call, Throwable t) {
                            Utils.writelogFile(context, "Call error " + t.getMessage() + "(Account, Repository)");
                            post(AccountActivityEvent.ERROR, t.getMessage());
                        }
                    });
                } catch (Exception e) {
                    Utils.writelogFile(context, "catch error " + e.getMessage() + "(Account, Repository)");
                    post(AccountActivityEvent.ERROR, e.getMessage());
                }
            } else {
                Utils.writelogFile(context, "Internet error(Account, Repository)");
                post(AccountActivityEvent.ERROR, context.getString(R.string.error_internet));
            }
        } else {
            Utils.writelogFile(context, "dateshop == null(Account, Repository)");
            post(AccountActivityEvent.ERROR, context.getString(R.string.error_data_base));
        }
    }

    private void post(int type, Account account) {
        post(type, account, null, null);
    }

    private void post(int type) {
        post(type, null, null, null);
    }

    private void post(int type, boolean iscity, String city) {
        post(type, null, city, null);
    }

    private void post(int type, String error) {
        post(type, null, null, error);
    }

    private void post(int type, Account account, String city, String error) {
        AccountActivityEvent event = new AccountActivityEvent();
        event.setType(type);
        event.setError(error);
        event.setCity(city);
        event.setAccount(account);
        eventBus.post(event);
    }
}
