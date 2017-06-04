package com.valdroide.mycitysshopsadm.main.offer.ui;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputFilter;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;
import com.valdroide.mycitysshopsadm.MyCitysShopsAdmApp;
import com.valdroide.mycitysshopsadm.R;
import com.valdroide.mycitysshopsadm.entities.shop.Offer;
import com.valdroide.mycitysshopsadm.main.offer.OfferActivityPresenter;
import com.valdroide.mycitysshopsadm.main.offer.ui.adapters.OfferActivityRecyclerAdapter;
import com.valdroide.mycitysshopsadm.main.offer.ui.adapters.OnItemClickListener;
import com.valdroide.mycitysshopsadm.utils.Utils;

import java.io.IOException;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class OfferActivity extends AppCompatActivity implements OnItemClickListener, OfferActivityView {


    @Bind(R.id.editTextTitle)
    EditText editTextTitle;
    @Bind(R.id.editTextDescription)
    EditText editTextDescription;
    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;
    @Bind(R.id.conteiner)
    LinearLayout conteiner;
    @Bind(R.id.imageViewImage)
    ImageView imageViewImage;
    @Bind(R.id.textViewQuantity)
    TextView textViewQuantity;

    private List<Offer> offers;
    @Inject
    OfferActivityRecyclerAdapter adapter;
    @Inject
    OfferActivityPresenter presenter;

    private ProgressDialog pDialog;
    private Menu menu;
    private int id_offer = 0, quantity_offer = 0, position = 0, max_offer = 0;
    public static final int PERMISSION_GALERY = 1;
    public static final int GALERY = 1;
    private byte[] imageByte;
    private String encode = "", name_image = "", url_image = "", name_before = "";
    private int is_active;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offer);
        Utils.writelogFile(this, "Se inicia ButterKnife(offer)");
        ButterKnife.bind(this);
        Utils.writelogFile(this, "Se inicia Injection(offer)");
        setupInjection();
        Utils.writelogFile(this, "Se inicia presenter Oncreate(offer)");
        presenter.onCreate();
        Utils.writelogFile(this, "Se inicia toolbar Oncreate(offer)");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.offer_title);
        Utils.writelogFile(this, "Se inicia Dialog(offer)");
        initDialog();
        Utils.writelogFile(this, "Se inicia RecyclerAdapter(offer)");
        initRecyclerViewAdapter();
        Utils.writelogFile(this, "dialog show(offer)");
        pDialog.show();
        Utils.writelogFile(this, "presenter.getOffer()(offer)");
        presenter.getOffer(this);
        editTextTitle.setFilters(new InputFilter[]{new InputFilter.AllCaps(), new InputFilter.LengthFilter(20)});
    }

    public void initDialog() {
        pDialog = new ProgressDialog(this);
        pDialog.setMessage(getString(R.string.processing));
        pDialog.setCancelable(false);
    }

    public void initRecyclerViewAdapter() {
        try {
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setHasFixedSize(true);
            recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
            recyclerView.setAdapter(adapter);
            // registerForContextMenu(recyclerView);
        } catch (Exception e) {
            Utils.writelogFile(this, "initRecyclerViewAdapter error " + e.getMessage() + " (offer)");
            error(e.getMessage());
        }
    }

    public void setTextQuantity(String quantity) {
        textViewQuantity.setText(getString(R.string.offer_available) + " " + quantity);
    }

    private void setupInjection() {
        MyCitysShopsAdmApp app = (MyCitysShopsAdmApp) getApplication();
        app.getOfferActivityComponent(this, this, this).inject(this);
    }

    @OnClick(R.id.imageViewImage)
    public void getPhoto() {
        Utils.writelogFile(this, "getPhoto click imageViewImage y oldPhones(offer)");
        if (!Utils.oldPhones())
            Utils.checkForPermission(this, this, null, PERMISSION_GALERY);
        Utils.writelogFile(this, "hasPermission(offer)");
        if (Utils.hasPermission(this))
            Utils.ImageDialogLogo(this, null, PERMISSION_GALERY);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_GALERY)
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Utils.ImageDialogLogo(this, null, PERMISSION_GALERY);
            }
    }

    public void assignImage(Uri uri) {
        Utils.writelogFile(this, "assignImage y setPicasso(offer)");
        try {
            Utils.setPicasso(this, uri.toString(), android.R.drawable.ic_menu_crop, imageViewImage);
            Utils.writelogFile(this, "readBytes(offer)");
            imageByte = Utils.readBytes(uri, this);
        } catch (IOException e) {
            Utils.writelogFile(this, "readBytes error: " + e.getMessage() + " (offer)");
            e.printStackTrace();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Utils.writelogFile(this, "onActivityResult(offer)");
        try {
            if (requestCode == GALERY) {
                Uri imageUri = CropImage.getPickImageResultUri(this, data);
                Utils.startCropImageActivity(this, null, imageUri);
            }
            if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
                CropImage.ActivityResult result = CropImage.getActivityResult(data);

                if (resultCode == RESULT_OK) {
                    assignImage(result.getUri());
                } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                    if (!result.getError().toString().contains("ENOENT"))
                        Utils.showSnackBar(conteiner, "Error al asignar imagen: " + result.getError());
                }
            }
        } catch (Exception e) {
            Utils.writelogFile(this, "onActivityResult error: " + e.getMessage() + " (offer)");
            error(e.getMessage());
        }
    }

    public void cleanView() {
        Utils.writelogFile(this, "cleanView (offer)");
        try {
            editTextTitle.setText("");
            editTextDescription.setText("");
            id_offer = 0;
            encode = "";
            name_image = "";
            url_image = "";
            name_before = "";
            imageByte = null;
            imageViewImage.setImageResource(android.R.drawable.ic_menu_crop);
        } catch (Exception e) {
            Utils.writelogFile(this, "cleanView error: " + e.getMessage() + " (offer)");
            error(e.getMessage());
        }
    }

    public Offer fillOffer(String title, String descrip) {
        pDialog.show();
        Utils.writelogFile(this, "fillOffer(offer)");
        try {
            if (imageByte != null) {
                try {
                    encode = Base64.encodeToString(imageByte,
                            Base64.DEFAULT);
                } catch (Exception e) {
                    encode = "";
                }
                name_image = Utils.randomNumber() + Utils.getFechaOficial() + ".PNG";
                url_image = Utils.URL_IMAGE_OFFER + name_image;
            }

            return new Offer(id_offer, Utils.getIdShop(this), Utils.getIdCity(this), title, descrip, url_image,
                    name_image, is_active, Utils.getFechaOficialSeparate(), encode, name_before);
        } catch (Exception e) {
            Utils.writelogFile(this, "fillOffer error: " + e.getMessage() + " (offer)");
            return null;
        }
    }

    @Override
    public void setOffers(List<Offer> offers, int max) {
        Utils.writelogFile(this, "setOffers : " + offers.size() + " offers y max: " + max + "(offer)");
        this.offers = offers;
        quantity_offer = offers.size();
        max_offer = max;
        updateQuantity();
        Utils.writelogFile(this, "adapter.setOffers (offer)");
        adapter.setOffers(offers);
        validateDialog();
    }

    @Override
    public void saveOffer(Offer offer) {
        Utils.writelogFile(this, "saveOffer (offer)");
        adapter.setOffer(offer);
        updateQuantity();
        menuAdd();
        validateDialog();
        cleanView();
        Utils.showSnackBar(conteiner, getString(R.string.offer_success));
    }

    @Override
    public void updateOffer(Offer offer) {
        Utils.writelogFile(this, "updateOffer (offer)");
        adapter.updateAdapter(offer, position);
        menuAdd();
        validateDialog();
        cleanView();
        Utils.showSnackBar(conteiner, getString(R.string.offer_update));
    }

    @Override
    public void switchOffer(Offer offer) {
        Utils.writelogFile(this, "switchOffer (offer)");
        adapter.updateAdapter(offer, position);
        validateDialog();
        Utils.showSnackBar(conteiner, getString(R.string.offer_update));
    }

    @Override
    public void error(String msg) {
        validateDialog();
        Utils.showSnackBar(conteiner, msg);
    }

    private void validateDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    @Override
    public void onClick(View view, int position, Offer offer) {
        Utils.writelogFile(this, "onClick adapter(offer)");
        try {
            if (!editTextTitle.isEnabled() || !editTextDescription.isEnabled())
                setEnable(true);
            editTextTitle.setText(offer.getTITLE());
            editTextDescription.setText(offer.getOFFER());
            id_offer = offer.getID_OFFER_KEY();
            name_image = offer.getNAME_IMAGE();
            name_before = name_image;
            url_image = offer.getURL_IMAGE();
            is_active = offer.getIS_ACTIVE();
            Utils.setPicasso(this, url_image, android.R.drawable.ic_menu_crop, imageViewImage);
            this.position = position;
            menuUpdate();
        } catch (Exception e) {
            Utils.writelogFile(this, "onClick adapter error: " + e.getMessage() + " (offer)");
            error(e.getMessage());
        }
    }

    @Override
    public void onClickSwitch(int position, Offer offer) {
        Utils.writelogFile(this, "onClickSwitch adapter y presenter.switchOffer(offer)");
        pDialog.show();
        this.position = position;
        presenter.switchOffer(this, offer);
    }

    public void updateQuantity() {
        Utils.writelogFile(this, "updateQuantity: " + this.offers.size() + " (offer)");
        quantity_offer = this.offers.size();
        setTextQuantity(String.valueOf(max_offer - quantity_offer));
    }

    public void menuAdd() {
        Utils.writelogFile(this, "menuAdd (offer)");
        try {
            menu.clear();
            getMenuInflater().inflate(R.menu.offer, menu);
            if (quantity_offer >= max_offer) {
                hideAllMenu(menu);
                if (editTextTitle.isEnabled() || editTextDescription.isEnabled())
                    setEnable(false);
            } else {
                hideUpdateMenu(menu);
                if (!editTextTitle.isEnabled() || !editTextDescription.isEnabled())
                    setEnable(true);
            }
        } catch (Exception e) {
            Utils.writelogFile(this, "menuAdd error: " + e.getMessage() + " (offer)");
            error(e.getMessage());
        }
    }

    public void hideUpdateMenu(Menu menu) {
        Utils.writelogFile(this, "hideUpdateMenu (offer)");
        try {
            menu.getItem(0).setVisible(false);// cancel
            menu.getItem(1).setVisible(false);// update
        } catch (Exception e) {
            Utils.writelogFile(this, "hideUpdateMenu error: " + e.getMessage() + " (offer)");
            error(e.getMessage());
        }
    }

    public void hideAllMenu(Menu menu) {
        Utils.writelogFile(this, "hideAllMenu (offer)");
        try {
            menu.getItem(0).setVisible(false);// cancel
            menu.getItem(1).setVisible(false);// update
            menu.getItem(2).setVisible(false);// add
        } catch (Exception e) {
            Utils.writelogFile(this, "hideAllMenu error: " + e.getMessage() + " (offer)");
            error(e.getMessage());
        }
    }

    public void menuUpdate() {
        Utils.writelogFile(this, "menuUpdate (offer)");
        try {
            menu.clear();
            getMenuInflater().inflate(R.menu.offer, menu);
            menu.getItem(2).setVisible(false);// add
        } catch (Exception e) {
            Utils.writelogFile(this, "menuUpdate error: " + e.getMessage() + " (offer)");
            error(e.getMessage());
        }
    }

    public void setEnable(boolean isEnable) {
        Utils.writelogFile(this, "setEnable (offer)");
        try {
            imageViewImage.setEnabled(isEnable);
            editTextTitle.setEnabled(isEnable);
            editTextTitle.setFocusable(isEnable);
            editTextTitle.setFocusableInTouchMode(isEnable);
            editTextDescription.setEnabled(isEnable);
            editTextDescription.setFocusable(isEnable);
            editTextDescription.setFocusableInTouchMode(isEnable);
        } catch (Exception e) {
            Utils.writelogFile(this, "setEnable error: " + e.getMessage() + " (offer)");
            error(e.getMessage());
        }
    }

    @Override
    public void onDestroy() {
        Utils.writelogFile(this, "onDestroy (offer)");
        presenter.onDestroy();
        pDialog.dismiss();
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Utils.writelogFile(this, "onCreateOptionsMenu (offer)");
        getMenuInflater().inflate(R.menu.offer, menu);
        this.menu = menu;
        if (quantity_offer >= max_offer) {
            hideAllMenu(menu);
            if (editTextTitle.isEnabled() || editTextDescription.isEnabled())
                setEnable(false);
        } else {
            hideUpdateMenu(menu);
        }

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_add) {
            try {
                Utils.writelogFile(this, "action_add click y  presenter.saveOffer(offer)");
                if (imageByte == null)
                    Utils.showSnackBar(conteiner, getString(R.string.error_image));
                else if (editTextTitle.getText().toString().isEmpty())
                    Utils.showSnackBar(conteiner, getString(R.string.title_empty));
                else if (editTextDescription.getText().toString().isEmpty())
                    Utils.showSnackBar(conteiner, getString(R.string.description_empty));
                else {
                    pDialog.show();
                    is_active = 1;
                    presenter.saveOffer(this, fillOffer(editTextTitle.getText().toString(),
                            editTextDescription.getText().toString()));
                }
            } catch (Exception e) {
                Utils.writelogFile(this, "action_add click error: " + e.getMessage() + " (offer)");
                error(e.getMessage());
            }
        } else if (id == R.id.action_update) {
            try {
                Utils.writelogFile(this, "action_update click y  presenter.saveOffer(offer)");
                if (imageByte == null && url_image.isEmpty())
                    Utils.showSnackBar(conteiner, getString(R.string.error_image));
                else if (editTextTitle.getText().toString().isEmpty())
                    Utils.showSnackBar(conteiner, getString(R.string.title_empty));
                else if (editTextDescription.getText().toString().isEmpty())
                    Utils.showSnackBar(conteiner, getString(R.string.description_empty));
                else {
                    pDialog.show();
                    presenter.updateOffer(this, fillOffer(editTextTitle.getText().toString(),
                            editTextDescription.getText().toString()));
                }
            } catch (Exception e) {
                Utils.writelogFile(this, "action_update click error: " + e.getMessage() + " (offer)");
                error(e.getMessage());
            }
        } else if (id == R.id.action_cancel) {
            Utils.writelogFile(this, "action_cancel click (offer)");
            cleanView();
            menuAdd();
        }
        return super.onOptionsItemSelected(item);
    }

//    @Override
//    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.delete, menu);
//        super.onCreateContextMenu(menu, v, menuInfo);
//    }

//    @Override
//    public boolean onContextItemSelected(MenuItem item) {
//        if (item.getItemId() == R.id.action_delete) {
////            pDialog.show();
////            this.offer.setDATE_EDIT(Utils.getFechaInit());
////            presenter.deleteOffer(this, this.offer, true);
//        }
//        return true;
//    }
}
