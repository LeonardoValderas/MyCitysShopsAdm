package com.valdroide.mycitysshopsadm.main.navigation;

import android.content.Context;

import com.raizlabs.android.dbflow.sql.language.Delete;
import com.valdroide.mycitysshopsadm.entities.shop.DateShop;
import com.valdroide.mycitysshopsadm.entities.shop.Shop;
import com.valdroide.mycitysshopsadm.lib.base.EventBus;
import com.valdroide.mycitysshopsadm.main.navigation.events.NavigationActivityEvent;
import com.valdroide.mycitysshopsadm.utils.Utils;

public class NavigationActivityRepositoryImpl implements NavigationActivityRepository {
    private EventBus eventBus;

    public NavigationActivityRepositoryImpl(EventBus eventBus) {
        this.eventBus = eventBus;
    }

    @Override
    public void logOut(Context context) {
        try {
            Utils.writelogFile(context, "logOut y DeleteDateShop (Navigation, Repository)");
            Delete.table(DateShop.class);
            Utils.writelogFile(context, "DeleteDateShop ok y DeleteShop(Navigation, Repository)");
            Delete.table(Shop.class);
            Utils.writelogFile(context, "DeleteShop ok y post GOTOOUT(Navigation, Repository)");
            post(NavigationActivityEvent.LOGOUT);
        } catch (Exception e) {
            Utils.writelogFile(context, " catch error " + e.getMessage() + "(Splash, Repository)");
            post(NavigationActivityEvent.ERROR, e.getMessage());
        }

    }

    public void post(int type) {
        post(type, null);
    }

    public void post(int type, String error) {
        NavigationActivityEvent event = new NavigationActivityEvent();
        event.setType(type);
        event.setError(error);
        eventBus.post(event);
    }
}
