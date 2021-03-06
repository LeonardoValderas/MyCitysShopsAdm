package com.valdroide.mycitysshopsadm;

import android.app.Activity;
import android.app.Application;
import android.content.BroadcastReceiver;
import android.support.v4.app.Fragment;

import com.facebook.stetho.Stetho;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.valdroide.mycitysshopsadm.lib.di.LibsModule;
import com.valdroide.mycitysshopsadm.main.account.di.AccountActivityComponent;
import com.valdroide.mycitysshopsadm.main.account.di.AccountActivityModule;
import com.valdroide.mycitysshopsadm.main.account.di.DaggerAccountActivityComponent;
import com.valdroide.mycitysshopsadm.main.account.ui.AccountActivityView;
import com.valdroide.mycitysshopsadm.main.draw.broadcast.di.BroadcastDrawComponent;
import com.valdroide.mycitysshopsadm.main.draw.broadcast.di.BroadcastDrawModule;
import com.valdroide.mycitysshopsadm.main.draw.broadcast.di.DaggerBroadcastDrawComponent;
import com.valdroide.mycitysshopsadm.main.draw.broadcast.ui.BroadcastDrawView;
import com.valdroide.mycitysshopsadm.main.draw.fragments.draw.di.DaggerDrawFragmentComponent;
import com.valdroide.mycitysshopsadm.main.draw.fragments.draw.di.DrawFragmentComponent;
import com.valdroide.mycitysshopsadm.main.draw.fragments.draw.di.DrawFragmentModule;
import com.valdroide.mycitysshopsadm.main.draw.fragments.draw.ui.DrawFragmentView;
import com.valdroide.mycitysshopsadm.main.draw.fragments.draw_list.di.DaggerDrawListFragmentComponent;
import com.valdroide.mycitysshopsadm.main.draw.fragments.draw_list.di.DrawListFragmentComponent;
import com.valdroide.mycitysshopsadm.main.draw.fragments.draw_list.di.DrawListFragmentModule;
import com.valdroide.mycitysshopsadm.main.draw.fragments.draw_list.ui.DrawListFragmentView;
import com.valdroide.mycitysshopsadm.main.login.di.DaggerLoginActivityComponent;
import com.valdroide.mycitysshopsadm.main.login.di.LoginActivityComponent;
import com.valdroide.mycitysshopsadm.main.login.di.LoginActivityModule;
import com.valdroide.mycitysshopsadm.main.login.ui.LoginActivityView;
import com.valdroide.mycitysshopsadm.main.map.di.DaggerMapActivityComponent;
import com.valdroide.mycitysshopsadm.main.map.di.MapActivityComponent;
import com.valdroide.mycitysshopsadm.main.map.di.MapActivityModule;
import com.valdroide.mycitysshopsadm.main.map.ui.MapActivityView;
import com.valdroide.mycitysshopsadm.main.navigation.di.DaggerNavigationActivityComponent;
import com.valdroide.mycitysshopsadm.main.navigation.di.NavigationActivityComponent;
import com.valdroide.mycitysshopsadm.main.navigation.di.NavigationActivityModule;
import com.valdroide.mycitysshopsadm.main.navigation.ui.NavigationActivityView;
import com.valdroide.mycitysshopsadm.main.notification.di.DaggerNotificationActivityComponent;
import com.valdroide.mycitysshopsadm.main.notification.di.NotificationActivityComponent;
import com.valdroide.mycitysshopsadm.main.notification.di.NotificationActivityModule;
import com.valdroide.mycitysshopsadm.main.notification.ui.NotificationActivityView;
import com.valdroide.mycitysshopsadm.main.offer.fragments.offer.di.DaggerOfferFragmentComponent;
import com.valdroide.mycitysshopsadm.main.offer.fragments.offer.di.OfferFragmentComponent;
import com.valdroide.mycitysshopsadm.main.offer.fragments.offer.di.OfferFragmentModule;
import com.valdroide.mycitysshopsadm.main.offer.fragments.offer.ui.OfferFragmentView;
import com.valdroide.mycitysshopsadm.main.offer.fragments.offer_list.di.DaggerOfferListFragmentComponent;
import com.valdroide.mycitysshopsadm.main.offer.fragments.offer_list.di.OfferListFragmentComponent;
import com.valdroide.mycitysshopsadm.main.offer.fragments.offer_list.di.OfferListFragmentModule;
import com.valdroide.mycitysshopsadm.main.offer.fragments.offer_list.ui.OfferListFragmentView;
import com.valdroide.mycitysshopsadm.main.offer.fragments.offer_list.ui.adapters.OnItemClickListener;
import com.valdroide.mycitysshopsadm.main.place.di.DaggerPlaceActivityComponent;
import com.valdroide.mycitysshopsadm.main.place.di.PlaceActivityComponent;
import com.valdroide.mycitysshopsadm.main.place.di.PlaceActivityModule;
import com.valdroide.mycitysshopsadm.main.place.ui.PlaceActivityView;
import com.valdroide.mycitysshopsadm.main.splash.di.DaggerSplashActivityComponent;
import com.valdroide.mycitysshopsadm.main.splash.di.SplashActivityComponent;
import com.valdroide.mycitysshopsadm.main.splash.di.SplashActivityModule;
import com.valdroide.mycitysshopsadm.main.splash.ui.SplashActivityView;
import com.valdroide.mycitysshopsadm.main.support.di.DaggerSupportActivityComponent;
import com.valdroide.mycitysshopsadm.main.support.di.SupportActivityComponent;
import com.valdroide.mycitysshopsadm.main.support.di.SupportActivityModule;
import com.valdroide.mycitysshopsadm.main.support.ui.SupportActivityView;

public class MyCitysShopsAdmApp extends Application {
    private LibsModule libsModule;
    private MyCitysShopsAdmAppModule myCitysShopsAdmAppModule;

    @Override
    public void onCreate() {
        super.onCreate();
        initModules();
        initDB();
        Stetho.initializeWithDefaults(this);

    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        DBTearDown();
    }

    private void DBTearDown() {
        FlowManager.destroy();
    }

    private void initDB() {
        FlowManager.init(this);
    }


    private void initModules() {
        libsModule = new LibsModule();
        myCitysShopsAdmAppModule = new MyCitysShopsAdmAppModule(this);
    }

    public BroadcastDrawComponent getBroadcastDrawComponent(BroadcastDrawView view, BroadcastReceiver receiver) {
        return DaggerBroadcastDrawComponent
                .builder()
                .myCitysShopsAdmAppModule(myCitysShopsAdmAppModule)
                .libsModule(new LibsModule(receiver))
                .broadcastDrawModule(new BroadcastDrawModule(receiver, view))
                .build();
    }


    public MapActivityComponent getMapActivityComponent(MapActivityView view, Activity activity) {
        return DaggerMapActivityComponent
                .builder()
                .myCitysShopsAdmAppModule(myCitysShopsAdmAppModule)
                .libsModule(new LibsModule(activity))
                .mapActivityModule(new MapActivityModule(activity, view))
                .build();
    }

    public DrawFragmentComponent getDrawFragmentComponent(DrawFragmentView view, Fragment fragment) {
        return DaggerDrawFragmentComponent
                .builder()
                .myCitysShopsAdmAppModule(myCitysShopsAdmAppModule)
                .libsModule(new LibsModule(fragment))
                .drawFragmentModule(new DrawFragmentModule(fragment, view))
                .build();
    }

    public SupportActivityComponent getSupportActivityComponent(SupportActivityView view, Activity activity) {
        return DaggerSupportActivityComponent
                .builder()
                .myCitysShopsAdmAppModule(myCitysShopsAdmAppModule)
                .libsModule(new LibsModule(activity))
                .supportActivityModule(new SupportActivityModule(view, activity))
                .build();
    }

    public AccountActivityComponent getAccountActivityComponent(AccountActivityView view, Activity activity) {
        return DaggerAccountActivityComponent
                .builder()
                .myCitysShopsAdmAppModule(myCitysShopsAdmAppModule)
                .libsModule(new LibsModule(activity))
                .accountActivityModule(new AccountActivityModule(view, activity))
                .build();
    }

    public OfferFragmentComponent getOfferFragmentComponent(OfferFragmentView view, Fragment fragment) {
        return DaggerOfferFragmentComponent
                .builder()
                .myCitysShopsAdmAppModule(myCitysShopsAdmAppModule)
                .libsModule(new LibsModule(fragment))
                .offerFragmentModule(new OfferFragmentModule(view, fragment))
                .build();
    }
    public OfferListFragmentComponent getOfferListFragmentComponent(OfferListFragmentView view, Fragment fragment, OnItemClickListener onItemClickListener) {
        return DaggerOfferListFragmentComponent
                .builder()
                .myCitysShopsAdmAppModule(myCitysShopsAdmAppModule)
                .libsModule(new LibsModule(fragment))
                .offerListFragmentModule(new OfferListFragmentModule(view, fragment, onItemClickListener))
                .build();
    }

    public NotificationActivityComponent getNotificationActivityComponent(NotificationActivityView view, Activity activity) {
        return DaggerNotificationActivityComponent
                .builder()
                .myCitysShopsAdmAppModule(myCitysShopsAdmAppModule)
                .libsModule(new LibsModule(activity))
                .notificationActivityModule(new NotificationActivityModule(view, activity))
                .build();
    }

    public DrawListFragmentComponent getDrawListFragmentComponent(DrawListFragmentView view, Fragment fragment, com.valdroide.mycitysshopsadm.main.draw.fragments.draw_list.ui.adapters.OnItemClickListener onItemClickListener) {
        return DaggerDrawListFragmentComponent
                .builder()
                .myCitysShopsAdmAppModule(myCitysShopsAdmAppModule)
                .libsModule(new LibsModule(fragment))
                .drawListFragmentModule(new DrawListFragmentModule(view, fragment, onItemClickListener))
                .build();
    }

    public SplashActivityComponent getSplashActivityComponent(SplashActivityView view, Activity activity) {
        return DaggerSplashActivityComponent
                .builder()
                .myCitysShopsAdmAppModule(myCitysShopsAdmAppModule)
                .libsModule(new LibsModule(activity))
                .splashActivityModule(new SplashActivityModule(view, activity))
                .build();
    }

    public PlaceActivityComponent getPlaceActivityComponent(PlaceActivityView view, Activity activity) {
        return DaggerPlaceActivityComponent
                .builder()
                .myCitysShopsAdmAppModule(myCitysShopsAdmAppModule)
                .libsModule(new LibsModule(activity))
                .placeActivityModule(new PlaceActivityModule(view, activity))
                .build();
    }

    public LoginActivityComponent getLoginActivityComponent(LoginActivityView view, Activity activity) {
        return DaggerLoginActivityComponent
                .builder()
                .myCitysShopsAdmAppModule(myCitysShopsAdmAppModule)
                .libsModule(new LibsModule(activity))
                .loginActivityModule(new LoginActivityModule(view, activity))
                .build();
    }

    public NavigationActivityComponent getNavigationActivityComponent(NavigationActivityView view, Activity activity) {
        return DaggerNavigationActivityComponent
                .builder()
                .myCitysShopsAdmAppModule(myCitysShopsAdmAppModule)
                .libsModule(new LibsModule(activity))
                .navigationActivityModule(new NavigationActivityModule(view, activity))
                .build();
    }
}
