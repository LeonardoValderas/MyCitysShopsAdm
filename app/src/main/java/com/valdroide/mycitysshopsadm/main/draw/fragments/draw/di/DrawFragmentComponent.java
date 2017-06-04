package com.valdroide.mycitysshopsadm.main.draw.fragments.draw.di;

import com.valdroide.mycitysshopsadm.MyCitysShopsAdmAppModule;
import com.valdroide.mycitysshopsadm.lib.di.LibsModule;
import com.valdroide.mycitysshopsadm.main.draw.fragments.draw.ui.DrawFragment;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {DrawFragmentModule.class, LibsModule.class, MyCitysShopsAdmAppModule.class})
public interface DrawFragmentComponent {
    void inject(DrawFragment drawFragment);
}
