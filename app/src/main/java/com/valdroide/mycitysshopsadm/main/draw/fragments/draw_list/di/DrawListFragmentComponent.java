package com.valdroide.mycitysshopsadm.main.draw.fragments.draw_list.di;

import com.valdroide.mycitysshopsadm.MyCitysShopsAdmAppModule;
import com.valdroide.mycitysshopsadm.lib.di.LibsComponent;
import com.valdroide.mycitysshopsadm.lib.di.LibsModule;
import com.valdroide.mycitysshopsadm.main.draw.fragments.draw_list.ui.DrawListFragment;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {DrawListFragmentModule.class, LibsModule.class, MyCitysShopsAdmAppModule.class})
public interface DrawListFragmentComponent {
   void inject(DrawListFragment fragment);
}
