package com.valdroide.mycitysshopsadm.main.place.di;

import com.valdroide.mycitysshopsadm.MyCitysShopsAdmAppModule;
import com.valdroide.mycitysshopsadm.lib.di.LibsModule;
import com.valdroide.mycitysshopsadm.main.place.ui.PlaceActivity;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {PlaceActivityModule.class, LibsModule.class, MyCitysShopsAdmAppModule.class})
public interface PlaceActivityComponent {
    void inject(PlaceActivity activity);
}
