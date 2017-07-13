package com.valdroide.mycitysshopsadm.main.offer.fragments.offer_list.ui;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.valdroide.mycitysshopsadm.MyCitysShopsAdmApp;
import com.valdroide.mycitysshopsadm.R;
import com.valdroide.mycitysshopsadm.entities.shop.Offer;
import com.valdroide.mycitysshopsadm.main.offer.activities.ui.OfferActivity;
import com.valdroide.mycitysshopsadm.main.offer.fragments.offer_list.OfferListFragmentPresenter;
import com.valdroide.mycitysshopsadm.main.offer.fragments.offer_list.ui.adapters.OfferListFragmentRecyclerAdapter;
import com.valdroide.mycitysshopsadm.main.offer.fragments.offer_list.ui.adapters.OnItemClickListener;
import com.valdroide.mycitysshopsadm.utils.Utils;

import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;

public class OfferListFragment extends Fragment implements OnItemClickListener, OfferListFragmentView {

    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;
    @Bind(R.id.conteiner)
    FrameLayout conteiner;
    @Bind(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;

    @Inject
    OfferListFragmentRecyclerAdapter adapter;
    @Inject
    OfferListFragmentPresenter presenter;

    private ProgressDialog pDialog;
    private int id_offer = 0;
    private String city = "";
    private List<Offer> offers;

    public OfferListFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_draw_list, container, false);
        ButterKnife.bind(this, view);
        initDialog();
        initSwipeRefreshLayout(getActivity());
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setupInjection();
        presenter.onCreate();
        presenter.getCity(getActivity());
        initRecyclerViewAdapter();
        presenter.getOffers(getActivity());
    }

    private void initDialog() {
        Utils.writelogFile(getActivity(), "initDialog(offerList)");
        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage(getString(R.string.processing));
        pDialog.setCancelable(false);
    }

    private void verifySwipeRefresh() {
        Utils.writelogFile(getActivity(), "verifySwipeRefresh(offerList)");
        try {
            if (swipeRefreshLayout != null) {
                if (swipeRefreshLayout.isRefreshing()) {
                    swipeRefreshLayout.setRefreshing(false);
                }
            }
        } catch (Exception e) {
            Utils.writelogFile(getActivity(), "verifySwipeRefresh catch error " + e.getMessage() + "(offerList)");
            error(e.getMessage());
        }
    }

    private void initSwipeRefreshLayout(final Context context) {
        Utils.writelogFile(getActivity(), "initSwipeRefreshLayout(offerList)");
        try {
            swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    Utils.writelogFile(context, "onRefresh(DrawFragment)");
                    presenter.validateDateShop(context);
                }
            });
        } catch (Exception e) {
            Utils.writelogFile(getActivity(), "initSwipeRefreshLayout catch error " + e.getMessage() + "(offerList)");
            error(e.getMessage());
        }
    }

    private void initRecyclerViewAdapter() {
        Utils.writelogFile(getActivity(), "initRecyclerViewAdapter(offerList)");
        try {
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            recyclerView.setHasFixedSize(true);
            recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
            recyclerView.setAdapter(adapter);
            // registerForContextMenu(recyclerView);
        } catch (Exception e) {
            Utils.writelogFile(getActivity(), "initRecyclerViewAdapter error " + e.getMessage() + " (offerList)");
            error(e.getMessage());
        }
    }

    @Override
    public void withoutChange() {
        verifySwipeRefresh();
    }

    private void setupInjection() {
        MyCitysShopsAdmApp app = (MyCitysShopsAdmApp) getActivity().getApplication();
        app.getOfferListFragmentComponent(this, this, this).inject(this);
    }

    public void refresh() {
        presenter.getOffers(getActivity());
    }

    @Override
    public void setOffers(List<Offer> offers) {
        Utils.writelogFile(getActivity(), "setOffers(offerList)");
        this.offers = offers;
        verifySwipeRefresh();
        adapter.setOffers(offers);
    }

    @Override
    public void switchOffer(Offer offer, int position) {
        Utils.writelogFile(getActivity(), "switchOffer (offerList)");
        adapter.updateAdapter(offer, position);
        Utils.showSnackBar(conteiner, getString(R.string.offer_update));
    }

    @Override
    public void error(String msg) {
        Utils.writelogFile(getActivity(), "error(offerList)");
        Utils.showSnackBar(conteiner, msg);
    }

    @Override
    public void showProgressDialog() {
        Utils.writelogFile(getActivity(), "showProgressDialog(offerList)");
        pDialog.show();
    }

    @Override
    public void hidePorgressDialog() {
        Utils.writelogFile(getActivity(), "hidePorgressDialog(offerList)");
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    @Override
    public void setCity(String city) {
        Utils.writelogFile(getActivity(), "setCity(offerList)");
        this.city = city;
    }

    @Override
    public void refreshAdapter() {
        Utils.writelogFile(getActivity(), "refreshAdapter(offerList)");
        presenter.getOffers(getActivity());
    }

    @Override
    public void onClick(View view, Offer offer) {
        Utils.writelogFile(getActivity(), "onClick adapter(offerList)");
        try {
            id_offer = offer.getID_OFFER_KEY();
            updateIntent(id_offer);
        } catch (Exception e) {
            Utils.writelogFile(getActivity(), "onClick adapter error: " + e.getMessage() + " (offerList)");
            error(e.getMessage());
        }
    }

    private void updateIntent(int id_offer) {
        Utils.writelogFile(getActivity(), "updateIntent(offerList)");
        Intent intent = new Intent(getActivity(), OfferActivity.class);
        intent.putExtra("is_update", true);
        intent.putExtra("id_offer", id_offer);
        startActivity(intent);
    }

    @Override
    public void onClickSwitch(int position, Offer offer) {
        Utils.writelogFile(getActivity(), "onClickSwitch adapter y presenter.switchOffer(offerList)");
        showProgressDialog();
        presenter.switchOffer(getActivity(), offer, position);
    }

    @Override
    public void onDestroy() {
        Utils.writelogFile(getActivity(), "onDestroy (offerList)");
        presenter.onDestroy();
        super.onDestroy();
    }

    private void validateDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }
}
