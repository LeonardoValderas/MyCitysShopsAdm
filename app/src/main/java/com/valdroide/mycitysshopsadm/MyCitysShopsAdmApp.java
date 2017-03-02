package com.valdroide.mycitysshopsadm;

import android.app.Activity;
import android.app.Application;

import com.facebook.stetho.Stetho;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.valdroide.mycitysshopsadm.lib.di.LibsModule;
import com.valdroide.mycitysshopsadm.main.account.di.AccountActivityComponent;
import com.valdroide.mycitysshopsadm.main.account.di.AccountActivityModule;
import com.valdroide.mycitysshopsadm.main.account.di.DaggerAccountActivityComponent;
import com.valdroide.mycitysshopsadm.main.account.ui.AccountActivityView;
import com.valdroide.mycitysshopsadm.main.notification.di.DaggerNotificationActivityComponent;
import com.valdroide.mycitysshopsadm.main.notification.di.NotificationActivityComponent;
import com.valdroide.mycitysshopsadm.main.notification.di.NotificationActivityModule;
import com.valdroide.mycitysshopsadm.main.notification.ui.NotificationActivityView;
import com.valdroide.mycitysshopsadm.main.offer.di.DaggerOfferActivityComponent;
import com.valdroide.mycitysshopsadm.main.offer.di.OfferActivityComponent;
import com.valdroide.mycitysshopsadm.main.offer.di.OfferActivityModule;
import com.valdroide.mycitysshopsadm.main.offer.ui.OfferActivityView;
import com.valdroide.mycitysshopsadm.main.offer.ui.adapters.OnItemClickListener;
import com.valdroide.mycitysshopsadm.main.place.di.DaggerPlaceActivityComponent;
import com.valdroide.mycitysshopsadm.main.place.di.PlaceActivityComponent;
import com.valdroide.mycitysshopsadm.main.place.di.PlaceActivityModule;
import com.valdroide.mycitysshopsadm.main.place.ui.PlaceActivityView;
import com.valdroide.mycitysshopsadm.main.splash.di.DaggerSplashActivityComponent;
import com.valdroide.mycitysshopsadm.main.splash.di.SplashActivityComponent;
import com.valdroide.mycitysshopsadm.main.splash.di.SplashActivityModule;
import com.valdroide.mycitysshopsadm.main.splash.ui.SplashActivityView;

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

    public AccountActivityComponent getAccountActivityComponent(AccountActivityView view, Activity activity) {
        return DaggerAccountActivityComponent
                .builder()
                .myCitysShopsAdmAppModule(myCitysShopsAdmAppModule)
                .libsModule(new LibsModule(activity))
                .accountActivityModule(new AccountActivityModule(view, activity))
                .build();
    }
    public OfferActivityComponent getOfferActivityComponent(OfferActivityView view, Activity activity, OnItemClickListener onItemClickListener) {
        return DaggerOfferActivityComponent
                .builder()
                .myCitysShopsAdmAppModule(myCitysShopsAdmAppModule)
                .libsModule(new LibsModule(activity))
                .offerActivityModule(new OfferActivityModule(view, activity, onItemClickListener))
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

/*

    public ActivityCategoryComponent getActivityCategoryComponent(ActivityCategoryView view, Activity activity, OnItemClickListener onItemClickListener) {
        return DaggerActivityCategoryComponent
                .builder()
                .gonzalezDanielaAdmAppModule(gonzalezDanielaAdmAppModule)
                .libsModule(new LibsModule(activity))
                .activityCategoryModule(new ActivityCategoryModule(view, onItemClickListener))
                .build();
    }

    public ActivitySubCategoryComponent getActivitySubCategoryComponent(ActivitySubCategoryView view, Activity activity, com.valdroide.gonzalezdanielaadm.main.subcategory.ui.adapters.OnItemClickListener onItemClickListener, Context context) {
        return DaggerActivitySubCategoryComponent
                .builder()
                .gonzalezDanielaAdmAppModule(gonzalezDanielaAdmAppModule)
                .libsModule(new LibsModule(activity))
                .activitySubCategoryModule(new ActivitySubCategoryModule(context, view, onItemClickListener))
                .build();
    }

    public NotificationActivityComponent getNotificationActivityComponent(NotificationActivityView view, Activity activity) {
        return DaggerNotificationActivityComponent
                .builder()
                .gonzalezDanielaAdmAppModule(gonzalezDanielaAdmAppModule)
                .libsModule(new LibsModule(activity))
                .notificationActivityModule(new NotificationActivityModule(view))
                .build();
    }

    public SplashActivityComponent getSplashActivityComponent(SplashActivityView view, Activity activity) {
        return DaggerSplashActivityComponent
                .builder()
                .gonzalezDanielaAdmAppModule(gonzalezDanielaAdmAppModule)
                .libsModule(new LibsModule(activity))
                .splashActivityModule(new SplashActivityModule(view))
                .build();
    }
    */
}
