package com.valdroide.mycitysshopsadm.main.splash;

import android.content.Context;

import com.raizlabs.android.dbflow.sql.language.Delete;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.valdroide.mycitysshopsadm.R;
import com.valdroide.mycitysshopsadm.api.APIService;
import com.valdroide.mycitysshopsadm.entities.response.ResultPlace;
import com.valdroide.mycitysshopsadm.entities.response.ResultShop;
import com.valdroide.mycitysshopsadm.entities.response.ResultUser;
import com.valdroide.mycitysshopsadm.entities.shop.Account;
import com.valdroide.mycitysshopsadm.entities.shop.DateShop;
import com.valdroide.mycitysshopsadm.entities.shop.Draw;
import com.valdroide.mycitysshopsadm.entities.shop.Login;
import com.valdroide.mycitysshopsadm.entities.shop.MyPlace;
import com.valdroide.mycitysshopsadm.entities.place.City;
import com.valdroide.mycitysshopsadm.entities.place.Country;
import com.valdroide.mycitysshopsadm.entities.place.DatePlace;
import com.valdroide.mycitysshopsadm.entities.place.State;
import com.valdroide.mycitysshopsadm.entities.response.ResponseWS;
import com.valdroide.mycitysshopsadm.entities.shop.Notification;
import com.valdroide.mycitysshopsadm.entities.shop.Offer;
import com.valdroide.mycitysshopsadm.entities.shop.Shop;
import com.valdroide.mycitysshopsadm.entities.shop.Support;
import com.valdroide.mycitysshopsadm.lib.base.EventBus;
import com.valdroide.mycitysshopsadm.main.splash.events.SplashActivityEvent;
import com.valdroide.mycitysshopsadm.main.support.events.SupportActivityEvent;
import com.valdroide.mycitysshopsadm.utils.Utils;

import java.util.List;

import javax.mail.BodyPart;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;

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
    private List<Draw> draws;
    private Shop shop;
    private Support support;

    public SplashActivityRepositoryImpl(EventBus eventBus, APIService service) {
        this.eventBus = eventBus;
        this.service = service;
    }

    @Override
    public void validateDatePlace(Context context) {
        try {
            Utils.writelogFile(context, "Metodo validateDatePlace y Se trae MyPlace(Splash, Repository)");
            place = SQLite.select().from(MyPlace.class).querySingle();
            if (place == null) { // es la primera vez que entra o quiere cambiar de lugar
                Utils.writelogFile(context, " MyPlace = null y Se trae DatePlace(Splash, Repository)");
                datePlace = SQLite.select().from(DatePlace.class).querySingle();
                if (datePlace != null) {
                    Utils.writelogFile(context, " MyPlace != null y se llama al metodo validateDatePlace(Splash, Repository)");
                    validateDatePlace(context, datePlace.getTABLE_DATE(), datePlace.getCOUNTRY_DATE(),
                            datePlace.getSTATE_DATE(), datePlace.getCITY_DATE());
                } else {  // traemos los datos sin validar fechas
                    Utils.writelogFile(context, " DatePlace = null y se llama al metodo getPlace(Splash, Repository)");
                    getPlace(context);
                }
            } else {
                Utils.writelogFile(context, " MyPlace != null y Se trae Shop(Splash, Repository)");
                shop = SQLite.select().from(Shop.class).querySingle();
                if (shop != null) {
                    Utils.writelogFile(context, " shop != null y se llama al metodo validateLogin(Splash, Repository)");
                    validateLogin(context, shop);
                } else {
                    Utils.writelogFile(context, " shop = null y se llama al post GOTOLOG(Splash, Repository)");
                    post(SplashActivityEvent.GOTOLOG);
                }
            }
        } catch (Exception e) {
            Utils.writelogFile(context, e.getMessage() + " (Splash, Repository)");
            post(SplashActivityEvent.ERROR, e.getMessage());
        }
    }

    private void validateDatePlace(final Context context, String date, String cou, String sta, String ci) {
        Utils.writelogFile(context, "Metodo validateDatePlace y Se valida conexion a internet(Splash, Repository)");
        if (Utils.isNetworkAvailable(context)) {
            try {
                Utils.writelogFile(context, "Call validateDatePlace(Splash, Repository)");
                Call<ResultPlace> validateDatePlace = service.validateDatePlace(cou, sta, ci, date);
                validateDatePlace.enqueue(new Callback<ResultPlace>() {
                    @Override
                    public void onResponse(Call<ResultPlace> call, Response<ResultPlace> response) {
                        if (response.isSuccessful()) {
                            Utils.writelogFile(context, "Response Successful y get ResponseWS(Splash, Repository)");
                            responseWS = response.body().getResponseWS();
                            if (responseWS != null) {
                                Utils.writelogFile(context, "ResponseWS != null y valida getSuccess(Splash, Repository)");
                                if (responseWS.getSuccess().equals("0")) {
                                    Utils.writelogFile(context, " getSuccess = 0 y getDatePlace(Splash, Repository)");
                                    datePlacesWS = response.body().getDatePlace();
                                    if (datePlacesWS != null) {
                                        Utils.writelogFile(context, "getDatePlace != null y delete DatePlace(Splash, Repository)");
                                        Delete.table(DatePlace.class);
                                        Utils.writelogFile(context, "delete DatePlace ok y save datePlacesWS(Splash, Repository)");
                                        datePlacesWS.save();
                                        Utils.writelogFile(context, "save datePlacesWS y getCountries(Splash, Repository)");
                                    }
                                    countries = response.body().getCountries();
                                    if (countries != null) {
                                        Utils.writelogFile(context, "countries != null y delete Country(Splash, Repository)");
                                        Delete.table(Country.class);
                                        Utils.writelogFile(context, "delete Country ok y save countries(Splash, Repository)");
                                        for (Country country : countries) {
                                            Utils.writelogFile(context, "save country: " + country.getID_COUNTRY_KEY() + " (Splash, Repository)");
                                            country.save();
                                        }
                                        Utils.writelogFile(context, "fin FOR country y getStates (Splash, Repository)");
                                    }
                                    states = response.body().getStates();
                                    if (states != null) {
                                        Utils.writelogFile(context, "states != null y delete State(Splash, Repository)");
                                        Delete.table(State.class);
                                        Utils.writelogFile(context, "delete State ok y save states(Splash, Repository)");
                                        for (State state : states) {
                                            Utils.writelogFile(context, "save state: " + state.getID_STATE_KEY() + " (Splash, Repository)");
                                            state.save();
                                        }
                                        Utils.writelogFile(context, "fin FOR state y getCities (Splash, Repository)");
                                    }
                                    cities = response.body().getCities();
                                    if (cities != null) {
                                        Utils.writelogFile(context, "cities != null y delete City(Splash, Repository)");
                                        Delete.table(City.class);
                                        Utils.writelogFile(context, "delete City ok y save city(Splash, Repository)");
                                        for (City city : cities) {
                                            Utils.writelogFile(context, "save city: " + city.getID_CITY_KEY() + " (Splash, Repository)");
                                            city.save();
                                        }
                                        Utils.writelogFile(context, "fin FOR city y post GOTOPLACE (Splash, Repository)");
                                    }
                                    post(SplashActivityEvent.GOTOPLACE);
                                } else if (responseWS.getSuccess().equals("4")) {
                                    Utils.writelogFile(context, "getSuccess = 4 y post GOTOPLACE(Splash, Repository)");
                                    post(SplashActivityEvent.GOTOPLACE);
                                } else {
                                    Utils.writelogFile(context, " getSuccess = error " + responseWS.getMessage() + "(Splash, Repository)");
                                    post(SplashActivityEvent.ERROR, responseWS.getMessage());
                                }
                            }
                        } else {
                            Utils.writelogFile(context, "Base de datos error(Splash, Repository)");
                            post(SplashActivityEvent.ERROR, context.getString(R.string.error_data_base));
                        }
                    }

                    @Override
                    public void onFailure(Call<ResultPlace> call, Throwable t) {
                        Utils.writelogFile(context, " Call error " + t.getMessage() + "(Splash, Repository)");
                        post(SplashActivityEvent.ERROR, t.getMessage());
                    }
                });
            } catch (Exception e) {
                Utils.writelogFile(context, " catch error " + e.getMessage() + "(Splash, Repository)");
                post(SplashActivityEvent.ERROR, e.getMessage());
            }
        } else {
            Utils.writelogFile(context, "Internet error (Splash, Repository)");
            post(SplashActivityEvent.ERROR, context.getString(R.string.error_internet));
        }
    }

    private void getPlace(final Context context) {
        Utils.writelogFile(context, "Metodo getPlace y Se valida conexion a internet(Splash, Repository)");
        if (Utils.isNetworkAvailable(context)) {
            try {
                Utils.writelogFile(context, "Call getPlace(Splash, Repository)");
                Call<ResultPlace> getPlace = service.getPlace();
                getPlace.enqueue(new Callback<ResultPlace>() {
                    @Override
                    public void onResponse(Call<ResultPlace> call, Response<ResultPlace> response) {
                        if (response.isSuccessful()) {
                            Utils.writelogFile(context, "Response Successful y get ResponseWS(Splash, Repository)");
                            responseWS = response.body().getResponseWS();
                            if (responseWS != null) {
                                Utils.writelogFile(context, "ResponseWS != null y valida getSuccess(Splash, Repository)");
                                if (responseWS.getSuccess().equals("0")) {
                                    Utils.writelogFile(context, " getSuccess = 0 y datePlacesWS(Splash, Repository)");
                                    datePlacesWS = response.body().getDatePlace();
                                    if (datePlacesWS != null) {
                                        Utils.writelogFile(context, "getDatePlace != null y delete DatePlace(Splash, Repository)");
                                        Delete.table(DatePlace.class);
                                        Utils.writelogFile(context, "delete DatePlace ok y save datePlacesWS(Splash, Repository)");
                                        datePlacesWS.save();
                                        Utils.writelogFile(context, "save datePlacesWS y getCountries(Splash, Repository)");
                                    }
                                    countries = response.body().getCountries();
                                    if (countries != null) {
                                        Utils.writelogFile(context, "countries != null y delete Country(Splash, Repository)");
                                        Delete.table(Country.class);
                                        Utils.writelogFile(context, "delete Country ok y save countries(Splash, Repository)");
                                        for (Country country : countries) {
                                            Utils.writelogFile(context, "save country: " + country.getID_COUNTRY_KEY() + " (Splash, Repository)");
                                            country.save();
                                        }
                                        Utils.writelogFile(context, "fin FOR country y getStates (Splash, Repository)");
                                    }
                                    states = response.body().getStates();
                                    if (states != null) {
                                        Utils.writelogFile(context, "states != null y delete State(Splash, Repository)");
                                        Delete.table(State.class);
                                        Utils.writelogFile(context, "delete State ok y save states(Splash, Repository)");
                                        for (State state : states) {
                                            Utils.writelogFile(context, "save state: " + state.getID_STATE_KEY() + " (Splash, Repository)");
                                            state.save();
                                        }
                                        Utils.writelogFile(context, "fin FOR state y getCities (Splash, Repository)");
                                    }
                                    cities = response.body().getCities();
                                    if (cities != null) {
                                        Utils.writelogFile(context, "cities != null y delete City(Splash, Repository)");
                                        Delete.table(City.class);
                                        Utils.writelogFile(context, "delete City ok y save city(Splash, Repository)");
                                        for (City city : cities) {
                                            Utils.writelogFile(context, "save city: " + city.getID_CITY_KEY() + " (Splash, Repository)");
                                            city.save();
                                        }
                                        Utils.writelogFile(context, "fin FOR city y post GOTOPLACE (Splash, Repository)");
                                    }
                                    post(SplashActivityEvent.GOTOPLACE);
                                } else {
                                    Utils.writelogFile(context, "getSuccess = error " + responseWS.getMessage() + "(Splash, Repository)");
                                    post(SplashActivityEvent.ERROR, responseWS.getMessage());
                                }
                            }
                        } else {
                            Utils.writelogFile(context, "Base de datos error(Splash, Repository)");
                            post(SplashActivityEvent.ERROR, context.getString(R.string.error_data_base));
                        }
                    }

                    @Override
                    public void onFailure(Call<ResultPlace> call, Throwable t) {
                        Utils.writelogFile(context, " Call error " + t.getMessage() + "(Splash, Repository)");
                        post(SplashActivityEvent.ERROR, t.getMessage());
                    }
                });
            } catch (Exception e) {
                Utils.writelogFile(context, " catch error " + e.getMessage() + "(Splash, Repository)");
                post(SplashActivityEvent.ERROR, e.getMessage());
            }
        } else {
            Utils.writelogFile(context, "Internet error(Splash, Repository)");
            post(SplashActivityEvent.ERROR, context.getString(R.string.error_internet));
        }
    }

    @Override
    public void validateDateShop(Context context) {
        try {
            Utils.writelogFile(context, "Metodo validateDateShop y Se trae DateShop(Splash, Repository)");
            dateShop = SQLite.select().from(DateShop.class).querySingle();
            if (dateShop != null) {
                Utils.writelogFile(context, "dateShop != null y Se llama al metodo validateDateShop(Splash, Repository)");
                validateDateShop(context, dateShop);
            } else { // traemos los datos sin validar fechas
                Utils.writelogFile(context, "dateShop == null y Se trae Shop(Splash, Repository)");
                shop = SQLite.select().from(Shop.class).querySingle();
                if (shop != null) {
                    Utils.writelogFile(context, "shop != null, setIdFollow y Se llama al metodo getShopData(Splash, Repository)");
                    Utils.setIdFollow(context, shop.getFOLLOW());
                    getShopData(context, shop.getID_SHOP_KEY());
                } else {
                    Utils.writelogFile(context, "shop == null(Splash, Repository)");
                    post(SplashActivityEvent.ERROR, context.getString(R.string.error_data_base));
                }
            }
        } catch (Exception e) {
            Utils.writelogFile(context, "catch " + e.getMessage() + "(Splash, Repository)");
            post(SplashActivityEvent.ERROR, e.getMessage());
        }
    }

    @Override
    public void sendEmail(final Context context, final String comment) {
        Utils.writelogFile(context, "sendEmail y getSupport(Splash, Repository)");
        support = getSupport();
        if (support == null) {
            Utils.writelogFile(context, "support==null(Splash, Repository)");
            support = new Support();
            support.setFROM("valdroide.soporte@gmail.com");
            support.setTO("aplicacionesandroid15@gmail.com");
            support.setPASS("Vsupport2017");
        }
        Utils.writelogFile(context, "getShopName(Splash, Repository)");
        String name = Utils.getNameShop(context);
        if (name.isEmpty()) {
            Utils.writelogFile(context, "name.isEmpty()(Splash, Repository)");
            name = context.getString(R.string.shop_name_empty);
        }
        final String finalName = name;
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                Utils.writelogFile(context, "sendEmail(Splash, Repository)");
                sendEmail(context, support, finalName, comment);
            }
        };
        new Thread(runnable).start();
    }

    private void sendEmail(Context context, final Support support, String Shop_name, String comment) {
        if (Utils.isNetworkAvailable(context)) {
            String to = support.getTO();
            final String from = support.getFROM();
            final String password = support.getPASS();
            try {
                Utils.createMailcapCommandMap();
                Session session = Utils.createPropertiesAndSession(from, password);
                MimeMessage message = Utils.createMimeMessage(session, from, to, Shop_name, context);
                BodyPart messageBodyPart1 = Utils.createMimeBodyPart(comment);
                MimeBodyPart messageBodyPart2 = Utils.createMimeBodyPart2(context.getFilesDir() + "/" + context.getResources().getString(R.string.log_file_name));
                Multipart multipart = Utils.createMultipart(messageBodyPart1, messageBodyPart2);
                message.setContent(multipart);
                Transport.send(message);
            } catch (MessagingException ex) {
            }
        } else
            post(SupportActivityEvent.ERROR, context.getString(R.string.error_internet));
    }

    private Support getSupport() {
        return SQLite.select().from(Support.class).querySingle();
    }

    private void validateDateShop(final Context context, final DateShop dateShop) {
        Utils.writelogFile(context, "Metodo validateDateShop y Se valida conexion a internet(Splash, Repository)");
        if (Utils.isNetworkAvailable(context)) {
            try {
                Utils.writelogFile(context, "Call validateDateShop(Splash, Repository)");
                Call<ResultUser> validateDateShop = service.validateDateShop(dateShop.getID_SHOP_FOREIGN(), Utils.getIdCity(context),
                        dateShop.getACCOUNT_DATE(), dateShop.getOFFER_DATE(), dateShop.getNOTIFICATION_DATE(), dateShop.getDRAW_DATE(),
                        dateShop.getDATE_SHOP_DATE(), dateShop.getSUPPORT_DATE());
                validateDateShop.enqueue(new Callback<ResultUser>() {
                    @Override
                    public void onResponse(Call<ResultUser> call, Response<ResultUser> response) {
                        if (response.isSuccessful()) {
                            Utils.writelogFile(context, "Response Successful y get ResponseWS(Splash, Repository)");
                            responseWS = response.body().getResponseWS();
                            if (responseWS != null) {
                                Utils.writelogFile(context, "ResponseWS != null y valida getSuccess(Splash, Repository)");
                                if (responseWS.getSuccess().equals("0")) {
                                    Utils.writelogFile(context, " getSuccess = 0 y getDateShop(Splash, Repository)");
                                    dateUserWS = response.body().getDateShop();
                                    if (dateUserWS != null) {
                                        Utils.writelogFile(context, "dateUserWS != null y delete DateShop(Splash, Repository)");
                                        Delete.table(DateShop.class);
                                        Utils.writelogFile(context, "delete DateShop ok y save dateUserWS(Splash, Repository)");
                                        dateUserWS.save();
                                        Utils.writelogFile(context, "save dateUserWS y getAccount(Splash, Repository)");
                                    }
                                    account = response.body().getAccount();
                                    if (account != null) {
                                        Utils.writelogFile(context, "account != null y delete Account(Splash, Repository)");
                                        Delete.table(Account.class);
                                        Utils.writelogFile(context, "delete Account ok y save account(Splash, Repository)");
                                        account.save();
                                        Utils.writelogFile(context, "save Account ok(Splash, Repository)");
                                    }
                                    shop = response.body().getShop();
                                    if (shop != null) {
                                        Utils.writelogFile(context, "shop != null y delete Shop(Splash, Repository)");
                                        Delete.table(Shop.class);
                                        Utils.writelogFile(context, "delete Shop ok y save shop(Splash, Repository)");
                                        shop.save();
                                        Utils.writelogFile(context, "save shop ok(Splash, Repository)");
                                    }


                                    offers = response.body().getOffers();
                                    if (offers != null) {
                                        Utils.writelogFile(context, "offers != null(Splash, Repository)");
                                        if (offers.size() > 0) {
                                            Utils.writelogFile(context, "offers.size() > 0 y delete Offer(Splash, Repository)");
                                            Delete.table(Offer.class);
                                            Utils.writelogFile(context, "delete Offer ok y save offers(Splash, Repository)");
                                            for (Offer offer : offers) {
                                                Utils.writelogFile(context, "save offer: " + offer.getID_OFFER_KEY() + " (Splash, Repository)");
                                                offer.save();
                                            }
                                            Utils.writelogFile(context, "fin FOR offer y getNotification (Splash, Repository)");
                                        } else {
                                            Utils.writelogFile(context, "offers == null y delete Offer(Splash, Repository)");
                                            Delete.table(Offer.class);
                                        }
                                    }

                                    draws = response.body().getDraws();
                                    if (draws != null) {
                                        Utils.writelogFile(context, "draws != null(Splash, Repository)");
                                        if (draws.size() > 0) {
                                            Utils.writelogFile(context, "draws.size() > 0 y delete Draw(Splash, Repository)");
                                            Delete.table(Draw.class);
                                            Utils.writelogFile(context, "delete draws ok y save Draw(Splash, Repository)");
                                            for (Draw draw : draws) {
                                                Utils.writelogFile(context, "save draw: " + draw.getID_DRAW_KEY() + " (Splash, Repository)");
                                                draw.save();
                                            }
                                            Utils.writelogFile(context, "fin FOR draw y getNotification(Splash, Repository)");
                                        } else {
                                            Utils.writelogFile(context, "draws == null y delete Draw(Splash, Repository)");
                                            Delete.table(Draw.class);
                                        }
                                    }

                                    notification = response.body().getNotification();
                                    if (notification != null) {
                                        Utils.writelogFile(context, "notification != null y delete Notification(Splash, Repository)");
                                        Delete.table(Notification.class);
                                        Utils.writelogFile(context, "delete Notification ok y save notification(Splash, Repository)");
                                        notification.save();
                                        Utils.writelogFile(context, "save notification ok y getSupport(Splash, Repository)");
                                    }

                                    support = response.body().getSupport();
                                    if (support != null) {
                                        Utils.writelogFile(context, "support != null y delete Support(Splash, Repository)");
                                        Delete.table(Support.class);
                                        Utils.writelogFile(context, "delete Support ok y save support(Splash, Repository)");
                                        support.save();
                                        Utils.writelogFile(context, "save Support ok y GOTONAV(Splash, Repository)");
                                    }
                                    post(SplashActivityEvent.GOTONAV);
                                } else if (responseWS.getSuccess().equals("4")) {
                                    Utils.writelogFile(context, "getSuccess = 4 y post GOTONAV(Splash, Repository)");
                                    post(SplashActivityEvent.GOTONAV);
                                } else {
                                    Utils.writelogFile(context, "getSuccess = error " + responseWS.getMessage() + "(Splash, Repository)");
                                    post(SplashActivityEvent.ERROR, responseWS.getMessage());
                                }
                            }
                        } else {
                            Utils.writelogFile(context, "Base de datos error(Splash, Repository)");
                            post(SplashActivityEvent.ERROR, context.getString(R.string.error_data_base));
                        }
                    }

                    @Override
                    public void onFailure(Call<ResultUser> call, Throwable t) {
                        Utils.writelogFile(context, " Call error " + t.getMessage() + "(Splash, Repository)");
                        post(SplashActivityEvent.ERROR, t.getMessage());
                    }
                });
            } catch (Exception e) {
                Utils.writelogFile(context, " catch error " + e.getMessage() + "(Splash, Repository)");
                post(SplashActivityEvent.ERROR, e.getMessage());
            }
        } else {
            Utils.writelogFile(context, "Internet error(Splash, Repository)");
            post(SplashActivityEvent.ERROR, context.getString(R.string.error_internet));
        }
    }

    private void getShopData(final Context context, int id_user) {
        Utils.writelogFile(context, "Metodo getShopData y Se valida conexion a internet(Splash, Repository)");
        if (Utils.isNetworkAvailable(context)) {
            try {
                Utils.writelogFile(context, "Call getShopData(Splash, Repository)");
                Call<ResultUser> getShopData = service.getShopData(id_user, Utils.getIdCity(context));
                getShopData.enqueue(new Callback<ResultUser>() {
                    @Override
                    public void onResponse(Call<ResultUser> call, Response<ResultUser> response) {
                        if (response.isSuccessful()) {
                            Utils.writelogFile(context, "Response Successful y get ResponseWS(Splash, Repository)");
                            responseWS = response.body().getResponseWS();
                            if (responseWS != null) {
                                Utils.writelogFile(context, "ResponseWS != null y valida getSuccess(Splash, Repository)");
                                if (responseWS.getSuccess().equals("0")) {
                                    Utils.writelogFile(context, " getSuccess = 0 y getDateShop(Splash, Repository)");
                                    dateUserWS = response.body().getDateShop();
                                    if (dateUserWS != null) {
                                        Utils.writelogFile(context, "dateUserWS != null y delete DateShop(Splash, Repository)");
                                        Delete.table(DateShop.class);
                                        Utils.writelogFile(context, "delete DateShop ok y save dateUserWS(Splash, Repository)");
                                        dateUserWS.save();
                                        Utils.writelogFile(context, "save dateUserWS y getAccount(Splash, Repository)");
                                    }
                                    account = response.body().getAccount();
                                    if (account != null) {
                                        Utils.writelogFile(context, "account != null y delete Account(Splash, Repository)");
                                        Delete.table(Account.class);
                                        Utils.writelogFile(context, "delete Account ok y save account(Splash, Repository)");
                                        account.save();
                                        Utils.writelogFile(context, "save account y save Follow en shared(Splash, Repository)");
                                        Utils.writelogFile(context, "save Follow ok y getOffers(Splash, Repository)");
                                    }
                                    offers = response.body().getOffers();
                                    if (offers != null) {
                                        Utils.writelogFile(context, "offers != null y delete Offer(Splash, Repository)");
                                        Delete.table(Offer.class);
                                        Utils.writelogFile(context, "delete Offer ok y save offers(Splash, Repository)");
                                        for (Offer offer : offers) {
                                            Utils.writelogFile(context, "save offer: " + offer.getID_OFFER_KEY() + " (Splash, Repository)");
                                            offer.save();
                                        }
                                        Utils.writelogFile(context, "fin FOR offer y getNotification (Splash, Repository)");
                                    }

                                    draws = response.body().getDraws();
                                    if (draws != null) {
                                        Utils.writelogFile(context, "draws != null y delete Draw(Splash, Repository)");
                                        Delete.table(Draw.class);
                                        Utils.writelogFile(context, "delete draws ok y save Draw(Splash, Repository)");
                                        for (Draw draw : draws) {
                                            Utils.writelogFile(context, "save draw: " + draw.getID_DRAW_KEY() + " (Splash, Repository)");
                                            draw.save();
                                        }
                                        Utils.writelogFile(context, "fin FOR draw y getNotification(Splash, Repository)");
                                    }

                                    notification = response.body().getNotification();
                                    if (notification != null) {
                                        Utils.writelogFile(context, "notification != null y delete Notification(Splash, Repository)");
                                        Delete.table(Notification.class);
                                        Utils.writelogFile(context, "delete Notification ok y save notification(Splash, Repository)");
                                        notification.save();
                                        Utils.writelogFile(context, "save notification ok y getSupport(Splash, Repository)");
                                    }
                                    support = response.body().getSupport();
                                    if (support != null) {
                                        Utils.writelogFile(context, "support != null y delete Support(Splash, Repository)");
                                        Delete.table(Support.class);
                                        Utils.writelogFile(context, "delete Support ok y save support(Splash, Repository)");
                                        support.save();
                                        Utils.writelogFile(context, "save support y GOTONAV(Splash, Repository)");
                                    }
                                    post(SplashActivityEvent.GOTONAV);
                                } else {
                                    Utils.writelogFile(context, " getSuccess = error " + responseWS.getMessage() + "(Splash, Repository)");
                                    post(SplashActivityEvent.ERROR, responseWS.getMessage());
                                }
                            }
                        } else {
                            Utils.writelogFile(context, "Base de datos error(Splash, Repository)");
                            post(SplashActivityEvent.ERROR, context.getString(R.string.error_data_base));
                        }
                    }

                    @Override
                    public void onFailure(Call<ResultUser> call, Throwable t) {
                        Utils.writelogFile(context, "Call error " + t.getMessage() + "(Splash, Repository)");
                        post(SplashActivityEvent.ERROR, t.getMessage());
                    }
                });
            } catch (Exception e) {
                Utils.writelogFile(context, "catch error " + e.getMessage() + "(Splash, Repository)");
                post(SplashActivityEvent.ERROR, e.getMessage());
            }
        } else {
            Utils.writelogFile(context, "Internet error(Splash, Repository)");
            post(SplashActivityEvent.ERROR, context.getString(R.string.error_internet));
        }
    }

    private void validateLogin(final Context context, final Shop shop) {
        Utils.writelogFile(context, "Metodo validateLogin y Se valida conexion a internet(Splash, Repository)");
        if (Utils.isNetworkAvailable(context)) {
            try {
                Utils.writelogFile(context, "Call validateLogin(Splash, Repository)");
                Login login = getLogin(context);
                Call<ResultShop> loginService = service.validateLogin(login.getUSER(),
                        login.getPASS(), shop.getID_CITY_FOREIGN());
                loginService.enqueue(new Callback<ResultShop>() {
                    @Override
                    public void onResponse(Call<ResultShop> call, Response<ResultShop> response) {
                        if (response.isSuccessful()) {
                            Utils.writelogFile(context, "Response Successful y get ResponseWS(Splash, Repository)");
                            responseWS = response.body().getResponseWS();
                            if (responseWS != null) {
                                Utils.writelogFile(context, "ResponseWS != null y valida getSuccess(Splash, Repository)");
                                if (responseWS.getSuccess().equals("0")) {
                                    Utils.writelogFile(context, "getSuccess = 0 y getAccount(Splash, Repository)");
                                    Shop shop = response.body().getShop();
                                    if (shop != null) {
                                        Utils.writelogFile(context, "shop !=  null y update shop(Splash, Repository)");
                                        shop.update();
                                        Utils.writelogFile(context, "set follow shared(Splash, Repository)");
                                        Utils.setIdFollow(context, shop.getFOLLOW());
                                        Utils.writelogFile(context, "update shop ok y GOTONAV(Splash, Repository)");
                                    } else {
                                        Utils.writelogFile(context, "shop == null (Splash, Repository)");
                                        post(SplashActivityEvent.ERROR, context.getString(R.string.error_data_base));
                                    }
                                    post(SplashActivityEvent.GOTONAV);
                                } else {
                                    Utils.writelogFile(context, "GOTOLOG(Splash, Repository)");
                                    post(SplashActivityEvent.GOTOLOG);
                                }
                            } else {
                                Utils.writelogFile(context, "responseWS == null (Splash, Repository)");
                                post(SplashActivityEvent.ERROR, context.getString(R.string.error_data_base));
                            }
                        } else {
                            Utils.writelogFile(context, "!response.isSuccessful()(Splash, Repository)");
                            post(SplashActivityEvent.ERROR, context.getString(R.string.error_data_base));
                        }
                    }

                    @Override
                    public void onFailure(Call<ResultShop> call, Throwable t) {
                        Utils.writelogFile(context, "Call error " + t.getMessage() + "(Splash, Repository)");
                        post(SplashActivityEvent.ERROR, t.getMessage());
                    }
                });
            } catch (Exception e) {
                Utils.writelogFile(context, "catch error " + e.getMessage() + "(Splash, Repository)");
                post(SplashActivityEvent.ERROR, e.getMessage());
            }
        } else {
            Utils.writelogFile(context, "Internet error(Splash, Repository)");
            post(SplashActivityEvent.ERROR, context.getString(R.string.error_internet));
        }
    }

    private Login getLogin(Context context){
        Utils.writelogFile(context, "Internet error(Splash, Repository)");
        return SQLite.select().from(Login.class).querySingle();
    }

    private void post(int type) {
        post(type, null);
    }

    private void post(int type, String error) {
        SplashActivityEvent event = new SplashActivityEvent();
        event.setType(type);
        event.setError(error);
        eventBus.post(event);
    }
}
