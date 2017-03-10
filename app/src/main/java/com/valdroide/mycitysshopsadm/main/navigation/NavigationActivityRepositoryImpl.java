package com.valdroide.mycitysshopsadm.main.navigation;

import com.raizlabs.android.dbflow.sql.language.Delete;
import com.valdroide.mycitysshopsadm.entities.user.User;
import com.valdroide.mycitysshopsadm.lib.base.EventBus;
import com.valdroide.mycitysshopsadm.main.navigation.events.NavigationActivityEvent;

public class NavigationActivityRepositoryImpl implements NavigationActivityRepository {
    private EventBus eventBus;

    public NavigationActivityRepositoryImpl(EventBus eventBus) {
        this.eventBus = eventBus;
    }

    @Override
    public void logOut() {
        try {
            Delete.table(User.class);
            post(NavigationActivityEvent.LOGOUT);
        } catch (Exception e) {
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
