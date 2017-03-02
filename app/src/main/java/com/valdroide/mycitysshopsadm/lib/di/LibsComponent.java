package com.valdroide.mycitysshopsadm.lib.di;

import com.valdroide.mycitysshopsadm.MyCitysShopsAdmAppModule;

import javax.inject.Singleton;
import dagger.Component;

@Singleton
@Component(modules = {LibsModule.class, MyCitysShopsAdmAppModule.class})
public interface LibsComponent {
}
