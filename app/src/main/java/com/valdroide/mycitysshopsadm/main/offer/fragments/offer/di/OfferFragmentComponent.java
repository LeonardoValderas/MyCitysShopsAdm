package com.valdroide.mycitysshopsadm.main.offer.fragments.offer.di;

import com.valdroide.mycitysshopsadm.MyCitysShopsAdmAppModule;
import com.valdroide.mycitysshopsadm.lib.di.LibsModule;
import com.valdroide.mycitysshopsadm.main.offer.fragments.offer.ui.OfferFragment;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {OfferFragmentModule.class, LibsModule.class, MyCitysShopsAdmAppModule.class})
public interface OfferFragmentComponent {
      void inject(OfferFragment fragment);
}
