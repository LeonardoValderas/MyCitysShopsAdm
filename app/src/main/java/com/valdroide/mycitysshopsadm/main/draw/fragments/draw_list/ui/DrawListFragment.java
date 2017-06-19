package com.valdroide.mycitysshopsadm.main.draw.fragments.draw_list.ui;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.valdroide.mycitysshopsadm.MyCitysShopsAdmApp;
import com.valdroide.mycitysshopsadm.R;
import com.valdroide.mycitysshopsadm.entities.shop.Draw;
import com.valdroide.mycitysshopsadm.main.draw.broadcast.RefreshAdapterDraw;
import com.valdroide.mycitysshopsadm.main.draw.fragments.draw_list.DrawListFragmentPresenter;
import com.valdroide.mycitysshopsadm.main.draw.fragments.draw_list.ui.adapters.DrawListFragmentAdapter;
import com.valdroide.mycitysshopsadm.main.draw.fragments.draw_list.ui.adapters.OnItemClickListener;
import com.valdroide.mycitysshopsadm.utils.Utils;

import java.text.SimpleDateFormat;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;

public class DrawListFragment extends Fragment implements DrawListFragmentView, OnItemClickListener, RefreshAdapterDraw {

    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;
    @Bind(R.id.conteiner)
    FrameLayout conteiner;
    private ProgressDialog pDialog;
    @Inject
    DrawListFragmentAdapter adapter;
    @Inject
    DrawListFragmentPresenter presenter;
    AlertDialog.Builder builder;
    private int position;

    public DrawListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_draw_list, container, false);
        ButterKnife.bind(this, view);
        return view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setupInjection();
        initDialog();
        presenter.onCreate();
        initRecyclerViewAdapter();
        presenter.getDraws(getActivity());
    }

    public void setupInjection() {
        Utils.writelogFile(getActivity(), "setupInjection(DrawListFragment)");
        MyCitysShopsAdmApp app = (MyCitysShopsAdmApp) getActivity().getApplication();
        app.getDrawListFragmentComponent(this, this, this).inject(this);
    }

    private void initRecyclerViewAdapter() {
        try {
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            recyclerView.setHasFixedSize(true);
            recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
            recyclerView.setAdapter(adapter);
            // registerForContextMenu(recyclerView);
        } catch (Exception e) {
            Utils.writelogFile(getActivity(), "initRecyclerViewAdapter error " + e.getMessage() + " (DrawListFragment)");
            setError(e.getMessage());
        }
    }

    public void refresh(boolean isBroadcast) {
        presenter.getDraws(getActivity());
        if(isBroadcast)
            presenter.validateBroadcast(getActivity());
    }

    private void initDialog() {
        Utils.writelogFile(getActivity(), "initDialog(DrawListFragment)");
        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage(getString(R.string.processing));
        pDialog.setCancelable(false);
    }

    @Override
    public void setDraws(List<Draw> draws) {
        Utils.writelogFile(getActivity(), "setDraws(DrawListFragment)");
        adapter.setDraw(draws);
    }

    @Override
    public void setError(String error) {
        Utils.writelogFile(getActivity(), "setError: " + error + " (DrawListFragment)");
        Utils.showSnackBar(conteiner, error);
    }

    @Override
    public void cancelSuccess() {
        adapter.deleteDraw(position);
        Utils.showSnackBar(conteiner, getString(R.string.draw_cancel_success));
    }

    @Override
    public void forceSuccess(Draw draw) {
        adapter.setUpdateDraw(position, draw);
        Utils.showSnackBar(conteiner, getString(R.string.draw_force_list));
    }

    @Override
    public void showProgressDialog() {
        pDialog.show();
    }

    @Override
    public void hidePorgressDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    @Override
    public void onDestroy() {
        Utils.writelogFile(getActivity(), "onDestroy(DrawListFragment)");
        presenter.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onDestroyView() {
        Utils.writelogFile(getActivity(), "onDestroyView(DrawListFragment)");
        super.onDestroyView();
        ButterKnife.unbind(getActivity());
    }

    @Override
    public void onClickDraw(int position, Draw draw) {
        this.position = position;
        showDialogs(draw);
    }

    private void showDialogs(final Draw draw) {
        Utils.writelogFile(getActivity(), "showDialogs(DrawListFragment)");
        if (draw != null) {
            if (draw.getERROR_REPORTING() == 1) {
                showProgressDialog();
                presenter.forceDraw(getActivity(), draw);
            } else if (draw.getIS_ZERO() == 1) {
                showProgressDialog();
                draw.setIS_ACTIVE(0);
                closeDraw(draw);
            } else {
                createDialog("Desea cerra el sorteo?", draw, 0);
            }
        }
    }

    private boolean validateDateEndDraw(String date) {
        Utils.writelogFile(getActivity(), "validateDateEndDraw(DrawListFragment)");
        return Utils.validateExpirateCurrentTime(date, getString(R.string.format_draw));
    }

    private boolean validateDateLimitDraw(String date) {
        Utils.writelogFile(getActivity(), "validateDateLimitDraw(DrawListFragment)");
        return Utils.validateExpirateVsLimit(Utils.getFechaLogFile("dd-MM-yyyy"), date, "dd-MM-yyyy");
    }

    private void createDialog(String msg, final Draw draw, final int type) {//0 init //1 cancel // 2 take/limit
        Utils.writelogFile(getActivity(), "createDialog(DrawListFragment)");
        builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(msg);
        builder.setPositiveButton("SI", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (!validateDateEndDraw(draw.getEND_DATE())) { // is false the draw is open
                    switch (type) {
                        case 0:
                            createDialog("El sorte no ha finalizado, desea cancelarlo?", draw, 1);
                            break;
                        case 1:
                            draw.setIS_ACTIVE(0);
                            draw.setIS_CANCEL(1);
                            closeDraw(draw);
                    }
                } else { // the draw is close validate if the alward was take.
                    if (!validateDateLimitDraw(draw.getLIMIT_DATE())) {// is true when the date limit is before that today
                        switch (type) {
                            case 0:
                                createDialog("El ganador usó o retiró su premio?", draw, 2);
                                break;
                            case 2:
                                draw.setIS_ACTIVE(0);
                                draw.setIS_TAKE(1);
                                closeDraw(draw);
                        }
                    } else {
                        draw.setIS_ACTIVE(0);
                        draw.setIS_LIMIT(1);
                        closeDraw(draw);
                    }
                }
            }
        });
        if (type != 2) {
            builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
        } else {
            builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                    Utils.showSnackBar(conteiner, "Debe aguardar hasta la fecha limite de uso o retiro del premio.");
                }
            });
        }
        builder.show();
    }

    private void closeDraw(Draw draw) {
        showProgressDialog();
        presenter.cancelDraw(getActivity(), draw);
    }

    @Override
    public void refreshAdapter() {
        adapter.notifyDataSetChanged();
    }
}
