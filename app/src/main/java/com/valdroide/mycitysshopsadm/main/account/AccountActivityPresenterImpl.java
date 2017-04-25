package com.valdroide.mycitysshopsadm.main.account;

import android.content.Context;

import com.valdroide.mycitysshopsadm.entities.shop.Account;
import com.valdroide.mycitysshopsadm.lib.base.EventBus;
import com.valdroide.mycitysshopsadm.main.account.events.AccountActivityEvent;
import com.valdroide.mycitysshopsadm.main.account.ui.AccountActivityView;

import org.greenrobot.eventbus.Subscribe;

public class AccountActivityPresenterImpl implements AccountActivityPresenter {
    private AccountActivityView view;
    private EventBus eventBus;
    private AccountActivityInteractor interactor;

    public AccountActivityPresenterImpl(AccountActivityView view, EventBus eventBus, AccountActivityInteractor interactor) {
        this.view = view;
        this.eventBus = eventBus;
        this.interactor = interactor;
    }

    @Override
    public void onCreate() {
        eventBus.register(this);
    }

    @Override
    public void onDestroy() {
        eventBus.unregister(this);
        view = null;
    }

    @Override
    public AccountActivityView getView() {
        return  this.view;
    }

    @Override
    public void getAccount(Context context) {
        interactor.getAccount(context);
    }

    @Override
    public void updateAccount(Context context, Account account) {
        interactor.updateAccount(context, account);
    }

    @Override
    @Subscribe
    public void onEventMainThread(AccountActivityEvent event) {
        if (this.view != null) {
            switch (event.getType()) {
                case AccountActivityEvent.GETACCOUNT:
                    view.setAccount(event.getAccount());
                    break;
                case AccountActivityEvent.SAVEACCOUNT:
                    view.saveSuccess();
                    break;
                case AccountActivityEvent.UPDATEACCOUNT:
                    view.saveSuccess();
                    break;
                case AccountActivityEvent.ERROR:
                    view.error(event.getError());
            }
        }
    }
}
