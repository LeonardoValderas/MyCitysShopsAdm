package com.valdroide.mycitysshopsadm.main.navigation.di;

import com.valdroide.mycitysshopsadm.MyCitysShopsAdmAppModule;
import com.valdroide.mycitysshopsadm.lib.di.LibsModule;
import com.valdroide.mycitysshopsadm.main.navigation.ui.NavigationActivity;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {NavigationActivityModule.class, LibsModule.class, MyCitysShopsAdmAppModule.class})
public interface NavigationActivityComponent {
      void inject(NavigationActivity activity);
}
