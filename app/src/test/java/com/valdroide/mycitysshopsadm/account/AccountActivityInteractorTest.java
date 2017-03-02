package com.valdroide.mycitysshopsadm.account;

import android.content.Context;

import com.valdroide.mycitysshopsadm.BaseTest;
import com.valdroide.mycitysshopsadm.entities.Account;
import com.valdroide.mycitysshopsadm.lib.base.EventBus;
import com.valdroide.mycitysshopsadm.main.account.AccountActivityInteractor;
import com.valdroide.mycitysshopsadm.main.account.AccountActivityInteractorImpl;
import com.valdroide.mycitysshopsadm.main.account.AccountActivityPresenterImpl;
import com.valdroide.mycitysshopsadm.main.account.AccountActivityRepository;
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


/**
 * Created by LEO on 23/2/2017.
 */
public class AccountActivityInteractorTest extends BaseTest {
    @Mock
    private AccountActivityRepository repository;
    @Mock
    Account account;
    private AccountActivityInteractor interactor;
    Context context;

    @Override
    public void setUp() throws IOException {
        super.setUp();
        interactor = new AccountActivityInteractorImpl(repository);
    }

    @Test
    public void getAccountTest() throws Exception {
        interactor.getAccount();
        verify(repository).getAccount();
    }

    @Test
    public void saveAccountTest() throws Exception {
        interactor.saveAccount(context, account);
        verify(repository).saveAccount(context, account);
    }

    @Test
    public void updateAccountTest() throws Exception {
        interactor.updateAccount(context, account);
        verify(repository).updateAccount(context, account);
    }
}
