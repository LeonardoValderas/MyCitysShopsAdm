package com.valdroide.mycitysshopsadm.main.offer.di;

import com.valdroide.mycitysshopsadm.MyCitysShopsAdmAppModule;
import com.valdroide.mycitysshopsadm.lib.di.LibsModule;
import com.valdroide.mycitysshopsadm.main.account.ui.AccountActivity;
import com.valdroide.mycitysshopsadm.main.offer.ui.OfferActivity;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {OfferActivityModule.class, LibsModule.class, MyCitysShopsAdmAppModule.class})
public interface OfferActivityComponent {
      void inject(OfferActivity activity);
}
