package com.valdroide.mycitysshopsadm.account;

import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.valdroide.mycitysshopsadm.BaseTest;
import com.valdroide.mycitysshopsadm.BuildConfig;
import com.valdroide.mycitysshopsadm.MyCitysShopsAdmApp;
import com.valdroide.mycitysshopsadm.api.APIService;
import com.valdroide.mycitysshopsadm.entities.Account;
import com.valdroide.mycitysshopsadm.entities.response.ResponseWS;
import com.valdroide.mycitysshopsadm.entities.response.Result;
import com.valdroide.mycitysshopsadm.lib.base.EventBus;
import com.valdroide.mycitysshopsadm.main.account.AccountActivityRepository;
import com.valdroide.mycitysshopsadm.main.account.AccountActivityRepositoryImpl;
import com.valdroide.mycitysshopsadm.main.account.events.AccountActivityEvent;
import com.valdroide.mycitysshopsadm.main.account.ui.AccountActivity;
import com.valdroide.mycitysshopsadm.utils.Utils;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;
import org.robolectric.util.ActivityController;

import java.io.IOException;

import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.robolectric.Shadows.shadowOf;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class AccountActivityRepositoryTest extends BaseTest {
    @Mock
    private APIService service;
    @Mock
    private EventBus eventBus;
    @Mock
    ResponseWS responseWS;
    @Mock
    private Account account;
    @Mock
    private AccountActivityEvent event;
    @Mock
    Utils utils;
    private AccountActivityRepository repository;
    private ArgumentCaptor<AccountActivityEvent> captor;
    AccountActivity activity;
    ActivityController<AccountActivity> controller;
    MyCitysShopsAdmApp app;
    @Mock
    AccountActivityRepositoryImpl repositoryMock;

    @Override
    public void setUp() throws IOException {
        super.setUp();
        repository = new AccountActivityRepositoryImpl(eventBus, service);
        captor = ArgumentCaptor.forClass(AccountActivityEvent.class);
        AccountActivity accountActivity = new AccountActivity();
        controller = ActivityController.of(Robolectric.getShadowsAdapter(),accountActivity).create().visible();
        activity = controller.get();
        activity = Robolectric.buildActivity(AccountActivity.class).get();
        app = (MyCitysShopsAdmApp) RuntimeEnvironment.application;
        app.onCreate();
    }

    @After
    public void tearDown() throws Exception {
        app.onTerminate();
    }

    @Test
    public void getAccountTest() throws Exception {
        when(account.exists()).thenReturn(true);
        repository.getAccount();
        Account accountBD;
        accountBD = SQLite.select().from(Account.class).querySingle();
        if (accountBD == null) {
            accountBD = new Account();
            accountBD.setNAME_BEFORE("before");
            accountBD.setEncode("encode");
            accountBD.setADDRESS("address");
            accountBD.setDESCRIPTION("desc");
            accountBD.setEMAIL("mail");
            accountBD.setLATITUD("lat");
            accountBD.setLONGITUD("lot");
            accountBD.setNAME_LOGO("name_logo");
            accountBD.setPHONE("photo");
            accountBD.setSHOP_NAME("name");
            accountBD.setID_SHOP_FOREIGN(1);
            accountBD.setURL_LOGO("url");
        }
        assertNotNull(accountBD);
        verify(eventBus).post(captor.capture());
        AccountActivityEvent event = captor.getValue();
        assertEquals(AccountActivityEvent.GETACCOUNT, event.getType());
        assertNull(event.getError());
        assertNotNull(event.getAccount());
    }

    @Test
    public void getAccountFailedTest() throws Exception {
        when(account.exists()).thenReturn(true);

        repository.getAccount();
        Account accountBD;
        accountBD = SQLite.select().from(Account.class).querySingle();
        assertNull(accountBD);
        verify(eventBus).post(captor.capture());
        AccountActivityEvent event = captor.getValue();
        assertEquals(AccountActivityEvent.GETACCOUNT, event.getType());
        assertNull(event.getError());
        assertNotNull(event.getAccount());
    }

    @Test
    public void saveAccountTest() throws Exception {
        when(account.exists()).thenReturn(true);
        when(utils.isNetworkAvailableNonStatic(activity)).thenReturn(true);
        account.setNAME_BEFORE("before");
        account.setEncode("encode");
        account.setADDRESS("address");
        account.setDESCRIPTION("desc");
        account.setEMAIL("mail");
        account.setLATITUD("lat");
        account.setLONGITUD("lot");
        account.setNAME_LOGO("name_logo");
        account.setPHONE("photo");
        account.setSHOP_NAME("name");
        account.setID_SHOP_FOREIGN(1);
        account.setURL_LOGO("url");
        when(service.insertAccount(account.getID_SHOP_FOREIGN(), account.getSHOP_NAME(), account.getEncode(), account.getNAME_LOGO(), account.getDESCRIPTION(), account.getADDRESS(), account.getPHONE(), account.getEMAIL(), account.getLATITUD(), account.getLONGITUD(), account.getURL_LOGO())).thenReturn(callSuccess(true, null));

        repository.saveAccount(activity, account);
        //verify(utils).isNetworkAvailableNonStatic(activity);
        verify(service).insertAccount(account.getID_SHOP_FOREIGN(), account.getSHOP_NAME(), account.getEncode(), account.getNAME_LOGO(), account.getDESCRIPTION(), account.getADDRESS(), account.getPHONE(), account.getEMAIL(), account.getLATITUD(), account.getLONGITUD(), account.getURL_LOGO());
        verify(eventBus).post(captor.capture());
        AccountActivityEvent event = captor.getValue();
        assertEquals(AccountActivityEvent.SAVEACCOUNT, event.getType());
        assertNull(event.getError());
        assertNull(event.getAccount());
    }

    @Test
    public void updateAccountTest() throws Exception {
        when(account.exists()).thenReturn(true);

        //     when(utils.isNetworkAvailableNonStatic(activity)).thenReturn(true);
        account.setID_ACCOUNT_KEY(1);
        account.setNAME_BEFORE("before");
        account.setEncode("encode");
        account.setADDRESS("address");
        account.setDESCRIPTION("desc");
        account.setEMAIL("mail");
        account.setLATITUD("lat");
        account.setLONGITUD("lot");
        account.setNAME_LOGO("name_logo");
        account.setPHONE("photo");
        account.setSHOP_NAME("name");
        account.setID_SHOP_FOREIGN(1);
        account.setURL_LOGO("url");
        when(repositoryMock.isUpdate(account)).thenReturn(true);
        when(account.getEncode()).thenReturn("KAK");
        when(service.updateAccount(account.getID_ACCOUNT_KEY(), account.getID_SHOP_FOREIGN(), account.getSHOP_NAME(), account.getEncode(), account.getNAME_LOGO(), account.getDESCRIPTION(), account.getADDRESS(), account.getPHONE(), account.getEMAIL(), account.getLATITUD(), account.getLONGITUD(), account.getURL_LOGO(), account.getADDRESS())).thenReturn(callSuccess(true, null));

        repository.updateAccount(activity, account);
//        verify(utils).isNetworkAvailableNonStatic(activity);
        verify(service).updateAccount(account.getID_ACCOUNT_KEY(), account.getID_SHOP_FOREIGN(), account.getSHOP_NAME(), account.getEncode(), account.getNAME_LOGO(), account.getDESCRIPTION(), account.getADDRESS(), account.getPHONE(), account.getEMAIL(), account.getLATITUD(), account.getLONGITUD(), account.getURL_LOGO(), account.getADDRESS());
        verify(eventBus).post(captor.capture());
        AccountActivityEvent event = captor.getValue();
        assertEquals(AccountActivityEvent.UPDATEACCOUNT, event.getType());
        assertNull(event.getError());
        assertNull(event.getAccount());
    }
    @Test
    public void updateAccountIsUpdateTest() throws Exception {
        when(account.exists()).thenReturn(true);

        //     when(utils.isNetworkAvailableNonStatic(activity)).thenReturn(true);
        account.setID_ACCOUNT_KEY(1);
        account.setNAME_BEFORE("before");
        account.setEncode("encode");
        account.setADDRESS("address");
        account.setDESCRIPTION("desc");
        account.setEMAIL("mail");
        account.setLATITUD("lat");
        account.setLONGITUD("lot");
        account.setNAME_LOGO("name_logo");
        account.setPHONE("photo");
        account.setSHOP_NAME("name");
        account.setID_SHOP_FOREIGN(1);
        account.setURL_LOGO("url");
        when(repositoryMock.isUpdate(account)).thenReturn(false);
        when(account.getEncode()).thenReturn("ss");
        when(service.updateAccount(account.getID_ACCOUNT_KEY(), account.getID_SHOP_FOREIGN(), account.getSHOP_NAME(), account.getEncode(), account.getNAME_LOGO(), account.getDESCRIPTION(), account.getADDRESS(), account.getPHONE(), account.getEMAIL(), account.getLATITUD(), account.getLONGITUD(), account.getURL_LOGO(), account.getADDRESS())).thenReturn(callSuccess(true, null));

        repository.updateAccount(activity, account);
//        verify(utils).isNetworkAvailableNonStatic(activity);
        verify(service).updateAccount(account.getID_ACCOUNT_KEY(), account.getID_SHOP_FOREIGN(), account.getSHOP_NAME(), account.getEncode(), account.getNAME_LOGO(), account.getDESCRIPTION(), account.getADDRESS(), account.getPHONE(), account.getEMAIL(), account.getLATITUD(), account.getLONGITUD(), account.getURL_LOGO(), account.getADDRESS());
        verify(eventBus).post(captor.capture());
        AccountActivityEvent event = captor.getValue();
        assertEquals(AccountActivityEvent.UPDATEACCOUNT, event.getType());
        assertNull(event.getError());
        assertNull(event.getAccount());
    }

    @Test
    public void saveFailedAccountTest() throws Exception {
        when(account.exists()).thenReturn(true);
        //   when(utils.isNetworkAvailableNonStatic(activity)).thenReturn(true);
        account.setNAME_BEFORE("before");
        account.setEncode("encode");
        account.setADDRESS("address");
        account.setDESCRIPTION("desc");
        account.setEMAIL("mail");
        account.setLATITUD("lat");
        account.setLONGITUD("lot");
        account.setNAME_LOGO("name_logo");
        account.setPHONE("photo");
        account.setSHOP_NAME("name");
        account.setID_SHOP_FOREIGN(1);
        account.setURL_LOGO("url");
        when(service.insertAccount(account.getID_SHOP_FOREIGN(), account.getSHOP_NAME(), account.getEncode(), account.getNAME_LOGO(), account.getDESCRIPTION(), account.getADDRESS(), account.getPHONE(), account.getEMAIL(), account.getLATITUD(), account.getLONGITUD(), account.getURL_LOGO())).thenReturn(callSuccess(false, null));

        repository.saveAccount(activity, account);
//        verify(utils).isNetworkAvailableNonStatic(activity);
        verify(service).insertAccount(account.getID_SHOP_FOREIGN(), account.getSHOP_NAME(), account.getEncode(), account.getNAME_LOGO(), account.getDESCRIPTION(), account.getADDRESS(), account.getPHONE(), account.getEMAIL(), account.getLATITUD(), account.getLONGITUD(), account.getURL_LOGO());
        verify(eventBus).post(captor.capture());
        AccountActivityEvent event = captor.getValue();
        assertEquals(AccountActivityEvent.ERROR, event.getType());
        assertNull(event.getError());
        assertNull(event.getAccount());
    }

    private Call<Result> callSuccess(final boolean success, final String error) {
        Call<Result> callSuccess = new Call<Result>() {
            @Override
            public Response<Result> execute() throws IOException {
                Response<Result> result = null;
                if (success) {
                    ResponseWS rws = new ResponseWS();
                    rws.setId(1);
                    rws.setMessage("ok");
                    rws.setSuccess("0");
                    Result re = new Result();
                    re.setResponseWS(rws);
                    result = Response.success(re);


                } else {
                    result = Response.error(null, null);
                }
                return result;
            }

            @Override
            public void enqueue(Callback<Result> callback) {
                if (success) {
                    try {
                        callback.onResponse(this, execute());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    callback.onFailure(this, new Throwable(error));
                }
            }

            @Override
            public boolean isExecuted() {
                return true;
            }

            @Override
            public void cancel() {

            }

            @Override
            public boolean isCanceled() {
                return false;
            }

            @Override
            public Call<Result> clone() {
                return null;
            }

            @Override
            public Request request() {
                return null;
            }

        };
        return callSuccess;
    }
}
