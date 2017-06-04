package com.valdroide.mycitysshopsadm.main.draw.fragments.draw.di;

import android.support.v4.app.Fragment;

import com.valdroide.mycitysshopsadm.api.APIService;
import com.valdroide.mycitysshopsadm.api.ShopClient;
import com.valdroide.mycitysshopsadm.lib.base.EventBus;
import com.valdroide.mycitysshopsadm.main.draw.fragments.draw.DrawFragmentInteractor;
import com.valdroide.mycitysshopsadm.main.draw.fragments.draw.DrawFragmentInteractorImpl;
import com.valdroide.mycitysshopsadm.main.draw.fragments.draw.DrawFragmentPresenter;
import com.valdroide.mycitysshopsadm.main.draw.fragments.draw.DrawFragmentPresenterImpl;
import com.valdroide.mycitysshopsadm.main.draw.fragments.draw.DrawFragmentRepository;
import com.valdroide.mycitysshopsadm.main.draw.fragments.draw.DrawFragmentRepositoryImpl;
import com.valdroide.mycitysshopsadm.main.draw.fragments.draw.ui.DrawFragmentView;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class DrawFragmentModule {
    Fragment fragment;
    DrawFragmentView view;

    public DrawFragmentModule(Fragment fragment, DrawFragmentView view) {
        this.fragment = fragment;
        this.view = view;
    }
    @Provides
    @Singleton
    DrawFragmentView provideDrawFragmentView(){
        return this.view;
    }
    @Provides
    @Singleton
    DrawFragmentPresenter provideDrawFragmentPresenter(DrawFragmentView view, EventBus eventBus, DrawFragmentInteractor interactor){
        return new DrawFragmentPresenterImpl(view, eventBus, interactor);
    }
    @Provides
    @Singleton
    DrawFragmentInteractor provideDrawFragmentInteractor(DrawFragmentRepository repository){
        return new DrawFragmentInteractorImpl(repository);
    }
    @Provides
    @Singleton
    DrawFragmentRepository provideDrawFragmentRepository(EventBus eventBus, APIService service){
        return new DrawFragmentRepositoryImpl(eventBus, service);
    }

    @Provides
    @Singleton
    APIService provideAPIService () {
        ShopClient client = new ShopClient();
        return client.getAPIService();
    }
}
