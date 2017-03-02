package com.valdroide.mycitysshopsadm.main.account.di;

import com.valdroide.mycitysshopsadm.MyCitysShopsAdmAppModule;
import com.valdroide.mycitysshopsadm.lib.di.LibsModule;
import com.valdroide.mycitysshopsadm.main.account.ui.AccountActivity;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by LEO on 30/1/2017.
 */
@Singleton
@Component(modules = {AccountActivityModule.class, LibsModule.class, MyCitysShopsAdmAppModule.class})
public interface AccountActivityComponent {
      void inject(AccountActivity activity);
}
