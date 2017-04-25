package com.valdroide.mycitysshopsadm.account;

import android.content.Context;

import com.valdroide.mycitysshopsadm.BaseTest;
import com.valdroide.mycitysshopsadm.entities.shop.Account;
import com.valdroide.mycitysshopsadm.lib.base.EventBus;
import com.valdroide.mycitysshopsadm.main.account.AccountActivityInteractor;
import com.valdroide.mycitysshopsadm.main.account.AccountActivityPresenter;
import com.valdroide.mycitysshopsadm.main.account.AccountActivityPresenterImpl;
import com.valdroide.mycitysshopsadm.main.account.events.AccountActivityEvent;
import com.valdroide.mycitysshopsadm.main.account.ui.AccountActivityView;

import org.junit.Test;
import org.mockito.Mock;

import java.io.IOException;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.robolectric.Shadows.shadowOf;


/**
 * Created by LEO on 23/2/2017.
 */
public class AccountActivityPresenterTest extends BaseTest {
    @Mock
    private AccountActivityView view;
    @Mock
    private EventBus eventBus;
    @Mock
    private AccountActivityInteractor interactor;
    @Mock
    private Account account;
    @Mock
    private AccountActivityEvent event;
    private AccountActivityPresenter presenter;
    Context context;

    @Override
    public void setUp() throws IOException {
        super.setUp();
        presenter = new AccountActivityPresenterImpl(view, eventBus, interactor);
    }

    @Test
    public void onCreateEventBus() throws Exception {
        presenter.onCreate();
        verify(eventBus).register(presenter);
    }

    @Test
    public void onDestroyEventBus() throws Exception {
        presenter.onDestroy();
        verify(eventBus).unregister(presenter);
        assertNull(presenter.getView());
    }

    @Test
    public void getAccountTest() throws Exception {
//        presenter.getAccount();
//        verify(interactor).getAccount();
    }

//    @Test
//    public void saveAccountTest() throws Exception {
//        presenter.saveAccount(context, account);
//        verify(interactor).saveAccount(context, account);
//    }

    @Test
    public void updateAccountTest() throws Exception {
        presenter.updateAccount(context, account);
        verify(interactor).updateAccount(context, account);
    }

    @Test
    public void getViewAccountTest() throws Exception {
        assertEquals(view, presenter.getView());
    }

    @Test
    public void eventErrorAccountTest() throws Exception {
        String error = "Error";
        when(event.getType()).thenReturn(AccountActivityEvent.ERROR);
        when(event.getError()).thenReturn(error);
        presenter.onEventMainThread(event);
        assertNotNull(presenter.getView());
        verify(view).error(event.getError());
    }

    @Test
    public void eventGetAccountTest() throws Exception {
        when(event.getType()).thenReturn(AccountActivityEvent.GETACCOUNT);
        when(event.getAccount()).thenReturn(account);
        presenter.onEventMainThread(event);
        assertNotNull(presenter.getView());
        verify(view).setAccount(event.getAccount());
    }

    @Test
    public void eventSaveAccountTest() throws Exception {
        when(event.getType()).thenReturn(AccountActivityEvent.SAVEACCOUNT);
        presenter.onEventMainThread(event);
        assertNotNull(presenter.getView());
        verify(view).saveSuccess();
    }

    @Test
    public void eventUpdateAccountTest() throws Exception {
        when(event.getType()).thenReturn(AccountActivityEvent.UPDATEACCOUNT);
        presenter.onEventMainThread(event);
        assertNotNull(presenter.getView());
        verify(view).saveSuccess();
    }

}
