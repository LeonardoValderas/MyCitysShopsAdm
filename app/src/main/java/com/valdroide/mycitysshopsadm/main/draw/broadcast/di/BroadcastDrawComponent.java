package com.valdroide.mycitysshopsadm.main.draw.broadcast.di;

import com.valdroide.mycitysshopsadm.MyCitysShopsAdmAppModule;
import com.valdroide.mycitysshopsadm.lib.di.LibsModule;
import com.valdroide.mycitysshopsadm.main.draw.broadcast.ui.BroadcastDraw;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {BroadcastDrawModule.class, LibsModule.class, MyCitysShopsAdmAppModule.class})
public interface BroadcastDrawComponent {
    void inject(BroadcastDraw broadcastDraw);
}
