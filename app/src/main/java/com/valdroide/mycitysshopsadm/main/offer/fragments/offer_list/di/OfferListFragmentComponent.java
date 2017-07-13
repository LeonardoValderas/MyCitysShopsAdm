package com.valdroide.mycitysshopsadm.main.offer.fragments.offer_list.di;

import com.valdroide.mycitysshopsadm.MyCitysShopsAdmAppModule;
import com.valdroide.mycitysshopsadm.lib.di.LibsModule;
import com.valdroide.mycitysshopsadm.main.offer.fragments.offer_list.ui.OfferListFragment;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {OfferListFragmentModule.class, LibsModule.class, MyCitysShopsAdmAppModule.class})
public interface OfferListFragmentComponent {
      void inject(OfferListFragment fragment);
}
