package com.valdroide.mycitysshopsadm.main.account.di;

import com.valdroide.mycitysshopsadm.MyCitysShopsAdmAppModule;
import com.valdroide.mycitysshopsadm.lib.di.LibsModule;
import com.valdroide.mycitysshopsadm.main.account.ui.AccountActivity;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {AccountActivityModule.class, LibsModule.class, MyCitysShopsAdmAppModule.class})
public interface AccountActivityComponent {
      void inject(AccountActivity activity);
}
