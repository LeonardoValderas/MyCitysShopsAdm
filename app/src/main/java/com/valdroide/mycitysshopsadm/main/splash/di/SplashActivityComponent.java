package com.valdroide.mycitysshopsadm.main.splash.di;
import com.valdroide.mycitysshopsadm.MyCitysShopsAdmAppModule;
import com.valdroide.mycitysshopsadm.lib.di.LibsModule;
import com.valdroide.mycitysshopsadm.main.splash.ui.SplashActivity;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {SplashActivityModule.class, LibsModule.class, MyCitysShopsAdmAppModule.class})
public interface SplashActivityComponent {
    void inject(SplashActivity activity);
}
