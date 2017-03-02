package com.valdroide.mycitysshopsadm.main.account;

import android.content.Context;

import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.valdroide.mycitysshopsadm.api.APIService;
import com.valdroide.mycitysshopsadm.entities.Account;
import com.valdroide.mycitysshopsadm.entities.response.ResponseWS;
import com.valdroide.mycitysshopsadm.entities.response.Result;
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
    private Account account = new Account();
    ResponseWS responseWS;
    int id;
    Utils utils = new Utils();


    public AccountActivityRepositoryImpl(EventBus eventBus, APIService service) {
        this.eventBus = eventBus;
        this.service = service;
    }

    @Override
    public void getAccount() {
        try {
            account = SQLite.select().from(Account.class).querySingle();
            if (account == null)
                account = new Account();

            post(AccountActivityEvent.GETACCOUNT, account);
        } catch (Exception e) {
            post(AccountActivityEvent.ERROR, e.getMessage());
        }

    }

    @Override
    public void saveAccount(Context context, final Account account) {
  // if (utils.isNetworkAvailableNonStatic(context)) {
        if (Utils.isNetworkAvailable(context)) {
            try {
                Call<Result> accountService = service.insertAccount(account.getID_SHOP_FOREIGN(),
                        account.getSHOP_NAME(), account.getEncode(),
                        account.getURL_LOGO(), account.getNAME_LOGO(),
                        account.getDESCRIPTION(), account.getPHONE(), account.getEMAIL(), account.getLATITUD(),
                        account.getLONGITUD(), account.getADDRESS());
                accountService.enqueue(new Callback<Result>() {
                    @Override
                    public void onResponse(Call<Result> call, Response<Result> response) {
                        if (response.isSuccessful()) {
                            responseWS = response.body().getResponseWS();
                            if (responseWS != null) {
                                if (responseWS.getSuccess().equals("0")) {
                                    id = responseWS.getId();
                                    if (id != 0) {
                                        account.setID_ACCOUNT_KEY(id);
                                        account.setEncode("");
                                        account.setNAME_BEFORE("");
                                        account.save();
                                        post(AccountActivityEvent.SAVEACCOUNT);
                                    } else {
                                        post(AccountActivityEvent.ERROR, Utils.ERROR_DATA_BASE);
                                    }
                                } else {
                                    post(AccountActivityEvent.ERROR, responseWS.getMessage());
                                }
                            } else {
                                post(AccountActivityEvent.ERROR, Utils.ERROR_DATA_BASE);
                            }
                        } else {
                            post(AccountActivityEvent.ERROR, Utils.ERROR_DATA_BASE);
                        }
                    }

                    @Override
                    public void onFailure(Call<Result> call, Throwable t) {
                        post(AccountActivityEvent.ERROR, t.getMessage());
                    }
                });
            } catch (Exception e) {
                post(AccountActivityEvent.ERROR, e.getMessage());
            }
        } else {
            post(AccountActivityEvent.ERROR, Utils.ERROR_INTERNET);
        }
    }

    @Override
    public void updateAccount(Context context, final Account account) {
        if (isUpdate(account)) {
            if (Utils.isNetworkAvailable(context)) {
                try {
                    Call<Result> accountService = service.updateAccount(account.getID_ACCOUNT_KEY(), account.getID_SHOP_FOREIGN(),
                            account.getSHOP_NAME(), account.getEncode(),
                            account.getURL_LOGO(), account.getNAME_LOGO(), account.getNAME_BEFORE(),
                            account.getDESCRIPTION(), account.getPHONE(), account.getEMAIL(), account.getLATITUD(),
                            account.getLONGITUD(), account.getADDRESS());
                    accountService.enqueue(new Callback<Result>() {
                        @Override
                        public void onResponse(Call<Result> call, Response<Result> response) {
                            if (response.isSuccessful()) {
                                responseWS = response.body().getResponseWS();
                                if (responseWS.getSuccess().equals("0")) {
                                    account.update();
                                    post(AccountActivityEvent.UPDATEACCOUNT);
                                } else {
                                    post(AccountActivityEvent.ERROR, responseWS.getMessage());
                                }
                            } else {
                                post(AccountActivityEvent.ERROR, Utils.ERROR_DATA_BASE);
                            }
                        }

                        @Override
                        public void onFailure(Call<Result> call, Throwable t) {
                            post(AccountActivityEvent.ERROR, t.getMessage());
                        }
                    });
                } catch (Exception e) {
                    post(AccountActivityEvent.ERROR, e.getMessage());
                }
            } else {
                post(AccountActivityEvent.ERROR, Utils.ERROR_INTERNET);
            }
        } else {
            post(AccountActivityEvent.UPDATEACCOUNT);
        }
    }

    public boolean isUpdate(Account accountValidate) {
        account = SQLite.select().from(Account.class).querySingle();
        if (accountValidate.getEncode().isEmpty()) {
            if (accountValidate.getSHOP_NAME().compareTo(account.getSHOP_NAME()) != 0)
                return true;
            else if (accountValidate.getPHONE().compareTo(account.getPHONE()) != 0)
                return true;
            else if (accountValidate.getEMAIL().compareTo(account.getEMAIL()) != 0)
                return true;
            else if (accountValidate.getADDRESS().compareTo(account.getADDRESS()) != 0)
                return true;
            else if (accountValidate.getLATITUD().compareTo(account.getLATITUD()) != 0)
                return true;
            else if (accountValidate.getLONGITUD().compareTo(account.getLONGITUD()) != 0)
                return true;
            else if (accountValidate.getDESCRIPTION().compareTo(account.getDESCRIPTION()) != 0)
                return true;
            else
                return false;
        }
        return true;
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
