package com.valdroide.mycitysshopsadm.main.account;

import android.content.Context;

import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.valdroide.mycitysshopsadm.R;
import com.valdroide.mycitysshopsadm.api.APIService;
import com.valdroide.mycitysshopsadm.entities.response.ResultPlace;
import com.valdroide.mycitysshopsadm.entities.shop.Account;
import com.valdroide.mycitysshopsadm.entities.response.ResponseWS;
import com.valdroide.mycitysshopsadm.entities.shop.DateShop;
import com.valdroide.mycitysshopsadm.lib.base.EventBus;
import com.valdroide.mycitysshopsadm.main.account.events.AccountActivityEvent;
import com.valdroide.mycitysshopsadm.utils.Utils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class AccountActivityRepositoryImpl implements AccountActivityRepository {
    private EventBus eventBus;
    private APIService service;
    private Account account = new Account();
    private ResponseWS responseWS;
    private DateShop dateShop;

    public AccountActivityRepositoryImpl(EventBus eventBus, APIService service) {
        this.eventBus = eventBus;
        this.service = service;
    }

    @Override
    public void getAccount(Context context) {
        Utils.writelogFile(context, "getAccount() (Account- Repository)");
        try {
            account = SQLite.select().from(Account.class).querySingle();
            if (account == null) {
                Utils.writelogFile(context, "account == null es decir que no hay cargado nada y post GETACCOUNT (Account- Repository)");
                account = new Account();
            }

            post(AccountActivityEvent.GETACCOUNT, account);
        } catch (Exception e) {
            Utils.writelogFile(context, "account catch error " + e.getMessage() + " (Account- Repository)");
            post(AccountActivityEvent.ERROR, e.getMessage());
        }
    }

    @Override
    public void updateAccount(final Context context, final Account account) {
        Utils.writelogFile(context, "Metodo updateAccount y Se valida conexion a internet(Account, Repository)");
        if (Utils.isNetworkAvailable(context)) {
            try {
                Utils.writelogFile(context, "Call updateAccount(Splash, Repository)");
                Call<ResultPlace> accountService = service.updateAccount(Utils.getIdShop(context), Utils.getIdCity(context),
                        account.getID_ACCOUNT_KEY(), account.getEncode(), account.getURL_LOGO(),
                        account.getNAME_LOGO(), account.getNAME_BEFORE(), account.getDESCRIPTION(),
                        account.getWORKING_HOURS(), account.getPHONE(), account.getEMAIL(),
                        account.getWEB(), account.getWHATSAAP(), account.getFACEBOOK(), account.getINSTAGRAM(),
                        account.getTWITTER(), account.getSNAPCHAT(), account.getLATITUD(), account.getLONGITUD(),
                        account.getADDRESS(), account.getDATE_UNIQUE());
                accountService.enqueue(new Callback<ResultPlace>() {
                    @Override
                    public void onResponse(Call<ResultPlace> call, Response<ResultPlace> response) {
                        if (response.isSuccessful()) {
                            Utils.writelogFile(context, "Response Successful y get ResponseWS(Account, Repository)");
                            responseWS = response.body().getResponseWS();
                            if (responseWS != null) {
                                Utils.writelogFile(context, "ResponseWS != null y valida getSuccess(Account, Repository)");
                                if (responseWS.getSuccess().equals("0")) {
                                    Utils.writelogFile(context, "getSuccess = 0 y account.update()(Account, Repository)");
                                    account.update();
                                    Utils.writelogFile(context, "account.update() ok y getDateShop(Account, Repository)");
                                    getDateShop(context, account.getDATE_UNIQUE());
                                    Utils.writelogFile(context, "dateShop.update()(Account, Repository)");
                                    dateShop.update();
                                    Utils.writelogFile(context, "dateShop.update() ok y post UPDATEACCOUNT (Account, Repository)");
                                    post(AccountActivityEvent.UPDATEACCOUNT);
                                } else {
                                    Utils.writelogFile(context, " getSuccess = error " + responseWS.getMessage() + "(Account, Repository)");
                                    post(AccountActivityEvent.ERROR, responseWS.getMessage());
                                }
                            } else {
                                Utils.writelogFile(context, " Base de datos error " + context.getString(R.string.error_data_base) + "(Account, Repository)");
                                post(AccountActivityEvent.ERROR, context.getString(R.string.error_data_base));
                            }
                        } else {
                            Utils.writelogFile(context, " Base de datos error " + context.getString(R.string.error_data_base) + "(Account, Repository)");
                            post(AccountActivityEvent.ERROR, context.getString(R.string.error_data_base));
                        }
                    }

                    @Override
                    public void onFailure(Call<ResultPlace> call, Throwable t) {
                        Utils.writelogFile(context, " Call error " + t.getMessage() + "(Account, Repository)");
                        post(AccountActivityEvent.ERROR, t.getMessage());
                    }
                });
            } catch (Exception e) {
                Utils.writelogFile(context, " catch error " + e.getMessage() + "(Account, Repository)");
                post(AccountActivityEvent.ERROR, e.getMessage());
            }
        } else {
            Utils.writelogFile(context, " Internet error " + context.getString(R.string.error_internet) + "(Account, Repository)");
            post(AccountActivityEvent.ERROR, context.getString(R.string.error_internet));
        }
    }

    private void getDateShop(Context context, String date_edit) {
        try {
            Utils.writelogFile(context, "getDateShop y get DateShop(Account, Repository)");
            dateShop = SQLite.select().from(DateShop.class).querySingle();
            if (dateShop != null) {
                Utils.writelogFile(context, "dateShop != null y set setACCOUNT_DATE, setDATE_SHOP_DATE(Account, Repository)");
                dateShop.setACCOUNT_DATE(date_edit);
                dateShop.setDATE_SHOP_DATE(date_edit);
            } else {
                Utils.writelogFile(context, " Base de datos error " + context.getString(R.string.error_data_base) + "(Account, Repository)");
                post(AccountActivityEvent.ERROR, context.getString(R.string.error_data_base));
            }
        } catch (Exception e) {
            Utils.writelogFile(context, " Call error " + e.getMessage() + "(Account, Repository)");
            post(AccountActivityEvent.ERROR, e.getMessage());
        }
    }

    public void post(int type, Account account) {
        post(type, account, null);
    }

    public void post(int type) {
        post(type, null, null);
    }

    public void post(int type, String error) {
        post(type, null, error);
    }

    public void post(int type, Account account, String error) {
        AccountActivityEvent event = new AccountActivityEvent();
        event.setType(type);
        event.setError(error);
        event.setAccount(account);
        eventBus.post(event);
    }
}
