package com.valdroide.mycitysshopsadm.main.notification.di;

import com.valdroide.mycitysshopsadm.MyCitysShopsAdmAppModule;
import com.valdroide.mycitysshopsadm.lib.di.LibsModule;
import com.valdroide.mycitysshopsadm.main.notification.ui.NotificationActivity;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {NotificationActivityModule.class, LibsModule.class, MyCitysShopsAdmAppModule.class})
public interface NotificationActivityComponent {
      void inject(NotificationActivity activity);
}
