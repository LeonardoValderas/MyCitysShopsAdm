package com.valdroide.mycitysshopsadm.main.draw.fragments.draw_list.di;

import android.support.v4.app.Fragment;

import com.valdroide.mycitysshopsadm.api.APIService;
import com.valdroide.mycitysshopsadm.api.ShopClient;
import com.valdroide.mycitysshopsadm.entities.shop.Draw;
import com.valdroide.mycitysshopsadm.entities.shop.Shop;
import com.valdroide.mycitysshopsadm.lib.base.EventBus;
import com.valdroide.mycitysshopsadm.main.draw.fragments.draw_list.DrawListFragmentInteractor;
import com.valdroide.mycitysshopsadm.main.draw.fragments.draw_list.DrawListFragmentInteractorImpl;
import com.valdroide.mycitysshopsadm.main.draw.fragments.draw_list.DrawListFragmentPresenter;
import com.valdroide.mycitysshopsadm.main.draw.fragments.draw_list.DrawListFragmentPresenterImpl;
import com.valdroide.mycitysshopsadm.main.draw.fragments.draw_list.DrawListFragmentRepository;
import com.valdroide.mycitysshopsadm.main.draw.fragments.draw_list.DrawListFragmentRepositoryImpl;
import com.valdroide.mycitysshopsadm.main.draw.fragments.draw_list.ui.DrawListFragmentView;
import com.valdroide.mycitysshopsadm.main.draw.fragments.draw_list.ui.adapters.DrawListFragmentAdapter;
import com.valdroide.mycitysshopsadm.main.draw.fragments.draw_list.ui.adapters.OnItemClickListener;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class DrawListFragmentModule {
    DrawListFragmentView view;
    Fragment fragment;
    OnItemClickListener onItemClickListener;

    public DrawListFragmentModule(DrawListFragmentView view, Fragment fragment, OnItemClickListener onItemClickListener) {
        this.view = view;
        this.fragment = fragment;
        this.onItemClickListener = onItemClickListener;
    }

    @Singleton
    @Provides
    DrawListFragmentView provideDrawListFragmentView() {
        return this.view;
    }

    @Singleton
    @Provides
    DrawListFragmentPresenter provideDrawListFragmentPresenter(EventBus eventBus, DrawListFragmentView view, DrawListFragmentInteractor interactor) {
        return new DrawListFragmentPresenterImpl(eventBus, view, interactor);
    }

    @Singleton
    @Provides
    DrawListFragmentInteractor provideDrawListFragmentInteractor(DrawListFragmentRepository repository) {
        return new DrawListFragmentInteractorImpl(repository);
    }

    @Singleton
    @Provides
    DrawListFragmentRepository provideDrawListFragmentRepository(EventBus eventBus, APIService service) {
        return new DrawListFragmentRepositoryImpl(eventBus, service);
    }

    @Singleton
    @Provides
    DrawListFragmentAdapter provideDrawListFragmentAdapter(List<Draw> drawList, OnItemClickListener onItemClickListener, Fragment fragment) {
        return new DrawListFragmentAdapter(drawList, onItemClickListener, fragment);
    }

    @Singleton
    @Provides
    List<Draw> provideListDraw() {
        return new ArrayList<>();
    }

    @Singleton
    @Provides
    OnItemClickListener provideOnItemClickListener() {
        return this.onItemClickListener;
    }
    @Singleton
    @Provides
    APIService provideAPIService() {
        ShopClient client = new ShopClient();
        return client.getAPIService();
    }
}
