package com.valdroide.mycitysshopsadm.main.support.di;

import com.valdroide.mycitysshopsadm.MyCitysShopsAdmAppModule;
import com.valdroide.mycitysshopsadm.lib.di.LibsModule;
import com.valdroide.mycitysshopsadm.main.support.ui.SupportActivity;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {SupportActivityModule.class, LibsModule.class, MyCitysShopsAdmAppModule.class})
public interface SupportActivityComponent {
      void inject(SupportActivity activity);
}
