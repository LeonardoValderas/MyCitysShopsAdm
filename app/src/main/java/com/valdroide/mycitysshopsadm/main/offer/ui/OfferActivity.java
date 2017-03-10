package com.valdroide.mycitysshopsadm.main.offer.ui;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.valdroide.mycitysshopsadm.MyCitysShopsAdmApp;
import com.valdroide.mycitysshopsadm.R;
import com.valdroide.mycitysshopsadm.entities.user.Offer;
import com.valdroide.mycitysshopsadm.main.offer.OfferActivityPresenter;
import com.valdroide.mycitysshopsadm.main.offer.ui.adapters.OfferActivityRecyclerAdapter;
import com.valdroide.mycitysshopsadm.main.offer.ui.adapters.OnItemClickListener;
import com.valdroide.mycitysshopsadm.utils.Utils;

import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;

public class OfferActivity extends AppCompatActivity implements OnItemClickListener, OfferActivityView {


    @Bind(R.id.editTextTitle)
    EditText editTextTitle;
    @Bind(R.id.editTextDescription)
    EditText editTextDescription;
    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;
    @Bind(R.id.conteiner)
    LinearLayout conteiner;

    private List<Offer> offers;
    @Inject
    OfferActivityRecyclerAdapter adapter;
    @Inject
    OfferActivityPresenter presenter;

    private ProgressDialog pDialog;
    private Offer offer;
    private Menu menu;
    private int id_offer = 0, quantity_offer = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offer);
        ButterKnife.bind(this);

        setupInjection();
        presenter.onCreate();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("PROMO");
        initDialog();
        initRecyclerViewAdapter();
        pDialog.show();
        presenter.getOffer(this);
    }

    public void initDialog() {
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Procesando...");
        pDialog.setCancelable(false);
    }

    public void initRecyclerViewAdapter() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
        registerForContextMenu(recyclerView);
    }

    private void setupInjection() {
        MyCitysShopsAdmApp app = (MyCitysShopsAdmApp) getApplication();
        app.getOfferActivityComponent(this, this, this).inject(this);
    }

    public void cleanView() {
        editTextTitle.setText("");
        editTextDescription.setText("");
        id_offer = 0;
    }

    public Offer fillOffer(String title, String descrip, boolean isUpdate) {
        //Offer offer = new Offer();
        offer.setTITLE(title);
        offer.setOFFER(descrip);
        offer.setID_USER_FOREIGN(Utils.getIdUser(this));
        if (!isUpdate) {
            offer.setDATE_INIT(Utils.getFechaInit());
            offer.setDATE_END(Utils.getLastDateWeek());
        } else {
            offer.setID_OFFER_KEY(id_offer);
            offer.setDATE_EDIT(Utils.getFechaInit());
        }
        return offer;
    }

    @Override
    public void setOffers(List<Offer> offers) {
        this.offers = offers;
        quantity_offer = offers.size();
        adapter.setOffers(offers);
        if (pDialog.isShowing())
            pDialog.hide();
    }

    @Override
    public void saveOffer(Offer offer) {
        adapter.setOffer(offer);
        //   this.offers.add(offer);
        updateQuantity();
        menuAdd();
        if (pDialog.isShowing())
            pDialog.hide();
        cleanView();
        Utils.showSnackBar(conteiner, getString(R.string.offer_success));
    }

    @Override
    public void updateOffer(Offer offer) {
        adapter.updateAdapter(offer);
        menuAdd();
        if (pDialog.isShowing())
            pDialog.hide();
        cleanView();
        Utils.showSnackBar(conteiner, getString(R.string.offer_update));
    }

    @Override
    public void deleteOffer(Offer offer) {
        adapter.removeOffer(offer);
        this.offers.remove(offer);
        updateQuantity();
        menuAdd();
        if (pDialog.isShowing())
            pDialog.hide();
        Utils.showSnackBar(conteiner, getString(R.string.offer_delete));
    }

    @Override
    public void error(String msg) {
        if (pDialog.isShowing())
            pDialog.hide();
        Utils.showSnackBar(conteiner, msg);
    }

    @Override
    public void onClick(View view, int position, Offer offer) {
        editTextTitle.setText(offer.getTITLE());
        editTextDescription.setText(offer.getOFFER());
        id_offer = offer.getID_OFFER_KEY();
        this.offer = offer;
        menuUpdate();
    }

    public void updateQuantity() {
        quantity_offer = this.offers.size();
    }

    @Override
    public void onLongClick(View view, int position, Offer offer) {
        openContextMenu(view);
        this.offer = offer;
    }

    public void menuAdd() {
        menu.clear();
        getMenuInflater().inflate(R.menu.offer, menu);
        if (quantity_offer >= 3)
            menu.getItem(2).setVisible(false);// cancel
        menu.getItem(0).setVisible(false);// cancel
        menu.getItem(1).setVisible(false);// update
    }

    public void menuUpdate() {
        menu.clear();
        getMenuInflater().inflate(R.menu.offer, menu);
        menu.getItem(2).setVisible(false);// add
    }

    @Override
    public void onDestroy() {
        presenter.onDestroy();
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.offer, menu);
        this.menu = menu;
        if (quantity_offer >= 3)
            menu.getItem(2).setVisible(false);// update
        menu.getItem(0).setVisible(false);// cancel
        menu.getItem(1).setVisible(false);// update
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_add) {
            if (editTextTitle.getText().toString().isEmpty())
                Utils.showSnackBar(conteiner, getString(R.string.title_empty));
            else if (editTextDescription.getText().toString().isEmpty())
                Utils.showSnackBar(conteiner, getString(R.string.description_empty));
            else {
                pDialog.show();
                presenter.saveOffer(this, fillOffer(Utils.removeAccents(editTextTitle.getText().toString()),
                        Utils.removeAccents(editTextDescription.getText().toString()), false));
            }
        } else if (id == R.id.action_update) {
            if (editTextTitle.getText().toString().isEmpty())
                Utils.showSnackBar(conteiner, getString(R.string.title_empty));
            else if (editTextDescription.getText().toString().isEmpty())
                Utils.showSnackBar(conteiner, getString(R.string.description_empty));
            else {
                pDialog.show();
                presenter.updateOffer(this, fillOffer(Utils.removeAccents(editTextTitle.getText().toString()),
                        Utils.removeAccents(editTextDescription.getText().toString()), true));
            }
        } else if (id == R.id.action_cancel) {
            cleanView();
            menuAdd();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.delete, menu);
        super.onCreateContextMenu(menu, v, menuInfo);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_delete) {
            pDialog.show();
            this.offer.setDATE_EDIT(Utils.getFechaInit());
            presenter.deleteOffer(this, this.offer, true);
        }
        return true;
    }
}
