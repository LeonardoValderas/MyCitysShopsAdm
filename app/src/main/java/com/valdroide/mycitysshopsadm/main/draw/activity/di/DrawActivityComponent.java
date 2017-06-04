package com.valdroide.mycitysshopsadm.main.draw.activity.di;

import com.valdroide.mycitysshopsadm.MyCitysShopsAdmAppModule;
import com.valdroide.mycitysshopsadm.lib.di.LibsModule;
import com.valdroide.mycitysshopsadm.main.draw.activity.ui.DrawActivity;

import javax.inject.Singleton;

import dagger.Component;


@Singleton
@Component(modules = {DrawActivityModule.class, LibsModule.class, MyCitysShopsAdmAppModule.class})
public interface DrawActivityComponent {
   // void inject(DrawActivity activity);
}
