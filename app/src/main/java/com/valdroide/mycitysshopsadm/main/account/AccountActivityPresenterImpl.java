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
        if (view != null)
            view.showProgressDialog();
    }

    @Override
    public void onDestroy() {
        eventBus.unregister(this);
        view = null;
    }

    @Override
    public AccountActivityView getView() {
        return this.view;
    }

    @Override
    public void getCity(Context context) {
        interactor.getCity(context);
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
    public void validateDateShop(Context context) {
        interactor.validateDateShop(context);
    }

    @Override
    @Subscribe
    public void onEventMainThread(AccountActivityEvent event) {
        if (this.view != null) {
            switch (event.getType()) {
                case AccountActivityEvent.GETACCOUNT:
                    view.setAccount(event.getAccount());
                    view.hidePorgressDialog();
                    break;
                case AccountActivityEvent.SAVEACCOUNT:
                    view.hidePorgressDialog();
                    view.saveSuccess();
                    break;
                case AccountActivityEvent.UPDATEACCOUNT:
                    view.hidePorgressDialog();
                    view.saveSuccess();
                    break;
                case AccountActivityEvent.ERROR:
                    view.hidePorgressDialog();
                    view.error(event.getError());
                    break;
                case AccountActivityEvent.CITY:
                    view.setCity(event.getCity());
                    break;
                case AccountActivityEvent.UPDATESUCCESS:
                    view.getAccount();
                    break;
//                case AccountActivityEvent.UPDATEWHITOUTCHANGE:
//                    view.hidePorgressDialog();
//                    break;
            }
        }
    }
}
