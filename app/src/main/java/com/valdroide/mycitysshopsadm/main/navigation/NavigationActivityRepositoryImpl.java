package com.valdroide.mycitysshopsadm.main.navigation;

import android.content.Context;

import com.raizlabs.android.dbflow.sql.language.Delete;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.valdroide.mycitysshopsadm.entities.shop.Account;
import com.valdroide.mycitysshopsadm.entities.shop.Account_Table;
import com.valdroide.mycitysshopsadm.entities.shop.DateShop;
import com.valdroide.mycitysshopsadm.entities.shop.Login;
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
            Utils.writelogFile(context, "setIdFollow 0 y post GOTOOUT(Navigation, Repository)");
            Utils.setIdFollow(context, 0);
            Delete.table(Login.class);
            post(NavigationActivityEvent.LOGOUT);
        } catch (Exception e) {
            Utils.writelogFile(context, " catch error " + e.getMessage() + "(Splash, Repository)");
            post(NavigationActivityEvent.ERROR, e.getMessage());
        }
    }

    @Override
    public void getURLShop(Context context) {
        try {
            Utils.writelogFile(context, "getURLShop (Navigation, Repository)");
            String url = SQLite.select(Account_Table.URL_LOGO).from(Account.class).querySingle().getURL_LOGO();
            post(NavigationActivityEvent.URL, true, url);
        } catch (Exception e) {
            Utils.writelogFile(context, " catch error " + e.getMessage() + "(Splash, Repository)");
            post(NavigationActivityEvent.ERROR, e.getMessage());
        }
    }

    private void post(int type) {
        post(type, null, null);
    }

    private void post(int type, boolean isURL, String url) {
        post(type, null, url);
    }

    private void post(int type, String error) {
        post(type, error, null);
    }

    private void post(int type, String error, String url) {
        NavigationActivityEvent event = new NavigationActivityEvent();
        event.setType(type);
        event.setError(error);
        event.setUrl(url);
        eventBus.post(event);
    }
}
