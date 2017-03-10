package com.valdroide.mycitysshopsadm.main.login.di;

import com.valdroide.mycitysshopsadm.MyCitysShopsAdmAppModule;
import com.valdroide.mycitysshopsadm.lib.di.LibsModule;
import com.valdroide.mycitysshopsadm.main.login.ui.LoginActivity;


import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {LoginActivityModule.class, LibsModule.class, MyCitysShopsAdmAppModule.class})
public interface LoginActivityComponent {
      void inject(LoginActivity activity);
}
