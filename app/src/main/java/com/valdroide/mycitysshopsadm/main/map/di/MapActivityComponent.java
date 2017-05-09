package com.valdroide.mycitysshopsadm.main.map.di;

import android.app.Activity;

import com.valdroide.mycitysshopsadm.MyCitysShopsAdmAppModule;
import com.valdroide.mycitysshopsadm.lib.di.LibsModule;
import com.valdroide.mycitysshopsadm.main.map.ui.MapActivity;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {MapActivityModule.class, LibsModule.class, MyCitysShopsAdmAppModule.class})
public interface MapActivityComponent {
    void inject(MapActivity activity);
}
