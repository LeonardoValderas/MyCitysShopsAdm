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
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

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

    private List<Offer> offers;
    @Inject
    OfferActivityRecyclerAdapter adapter;
    @Inject
    OfferActivityPresenter presenter;

    private ProgressDialog pDialog;
    private Menu menu;
    private int id_offer = 0, quantity_offer = 0, position = 0;
    public static final int PERMISSION_GALERY = 1;
    public static final int GALERY = 1;
    private byte[] imageByte;
    private String encode = "", name_image = "", url_image = "", name_before = "";
    private int is_active;


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

    //  @Override
    @OnClick(R.id.imageViewImage)
    public void getPhoto() {
        if (!Utils.oldPhones())
            checkForPermission();

        if (hasPermission())
            ImageDialogLogo();
    }

    private void checkForPermission() {
        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_GALERY);
        }
    }

    private boolean hasPermission() {
        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        return permissionCheck == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_GALERY)
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                ImageDialogLogo();
            }
    }

    public void ImageDialogLogo() {
        AlertDialog.Builder myAlertDialog = new AlertDialog.Builder(
                this);
        myAlertDialog.setTitle("Galeria");
        myAlertDialog.setMessage("Seleccione una foto.");

        myAlertDialog.setPositiveButton("Galeria",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        Intent pickIntent = new Intent(
                                Intent.ACTION_GET_CONTENT, null);
                        pickIntent.setType("image/*");
                        pickIntent.putExtra(
                                "return-data", true);
                        startActivityForResult(
                                pickIntent,
                                GALERY);
                    }
                });
        myAlertDialog.show();
    }

    public void assignImage(Uri uri) {
        Utils.setPicasso(this, uri.toString(), android.R.drawable.ic_menu_crop, imageViewImage);
        try {
            imageByte = Utils.readBytes(uri, this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALERY) {
            Uri imageUri = CropImage.getPickImageResultUri(this, data);
            startCropImageActivity(imageUri);
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
    }

    private void startCropImageActivity(Uri imageUri) {
        CropImage.activity(imageUri)
                .setGuidelines(CropImageView.Guidelines.ON)
                .setMultiTouchEnabled(true)
                .start(OfferActivity.this);
    }

    public void cleanView() {
        editTextTitle.setText("");
        editTextDescription.setText("");
        id_offer = 0;
        encode = "";
        name_image = "";
        url_image = "";
        name_before = "";
        imageByte = null;
        imageViewImage.setImageResource(android.R.drawable.ic_menu_crop);
    }

    public Offer fillOffer(String title, String descrip) {
        pDialog.show();

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

        return new Offer(id_offer, Utils.getIdShop(this), title, descrip, url_image,
                name_image, is_active, Utils.getFechaOficialSeparate(), encode, name_before);
    }

    @Override
    public void setOffers(List<Offer> offers) {
        this.offers = offers;
        quantity_offer = offers.size();
        adapter.setOffers(offers);
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    @Override
    public void saveOffer(Offer offer) {
        adapter.setOffer(offer);
        updateQuantity();
        menuAdd();
        if (pDialog.isShowing())
            pDialog.dismiss();
        cleanView();
        Utils.showSnackBar(conteiner, getString(R.string.offer_success));
    }

    @Override
    public void updateOffer(Offer offer) {
        adapter.updateAdapter(offer, position);
        menuAdd();
        if (pDialog.isShowing())
            pDialog.dismiss();
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
    public void switchOffer(Offer offer) {
        adapter.updateAdapter(offer, position);
        if (pDialog.isShowing())
            pDialog.hide();
        Utils.showSnackBar(conteiner, getString(R.string.offer_update));
    }

    @Override
    public void error(String msg) {
        if (pDialog.isShowing())
            pDialog.dismiss();
        Utils.showSnackBar(conteiner, msg);
    }

    @Override
    public void onClick(View view, int position, Offer offer) {
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
    }

    @Override
    public void onClickSwitch(int position, Offer offer) {
        pDialog.show();
        this.position = position;
        presenter.switchOffer(this, offer);
    }

    public void updateQuantity() {
        quantity_offer = this.offers.size();
    }

//    @Override
//    public void onLongClick(View view, int position, Offer offer) {
//        openContextMenu(view);
//    }

    public void menuAdd() {
        menu.clear();
        getMenuInflater().inflate(R.menu.offer, menu);
        if (quantity_offer >= 3) {
            hideAllMenu(menu);
            if (editTextTitle.isEnabled() || editTextDescription.isEnabled())
                setEnable(false);
        } else {
            hideUpdateMenu(menu);
            if (!editTextTitle.isEnabled() || !editTextDescription.isEnabled())
                setEnable(true);
        }
    }

    public void hideUpdateMenu(Menu menu) {
        menu.getItem(0).setVisible(false);// cancel
        menu.getItem(1).setVisible(false);// update
    }

    public void hideAllMenu(Menu menu) {
        menu.getItem(0).setVisible(false);// cancel
        menu.getItem(1).setVisible(false);// update
        menu.getItem(2).setVisible(false);// add
    }

    public void menuUpdate() {
        menu.clear();
        getMenuInflater().inflate(R.menu.offer, menu);
        menu.getItem(2).setVisible(false);// add
    }

    public void setEnable(boolean isEnable) {
        editTextTitle.setEnabled(isEnable);
        editTextTitle.setFocusable(isEnable);
        editTextTitle.setFocusableInTouchMode(isEnable);
        editTextDescription.setEnabled(isEnable);
        editTextDescription.setFocusable(isEnable);
        editTextDescription.setFocusableInTouchMode(isEnable);
    }

    @Override
    public void onDestroy() {
        presenter.onDestroy();
        pDialog.dismiss();
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.offer, menu);
        this.menu = menu;
        if (quantity_offer >= 3) {
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
            if (imageByte == null)
                Utils.showSnackBar(conteiner, getString(R.string.error_image));
            else if (editTextTitle.getText().toString().isEmpty())
                Utils.showSnackBar(conteiner, getString(R.string.title_empty));
            else if (editTextDescription.getText().toString().isEmpty())
                Utils.showSnackBar(conteiner, getString(R.string.description_empty));
            else {
                pDialog.show();
                is_active = 1;
                presenter.saveOffer(this, fillOffer(Utils.removeAccents(editTextTitle.getText().toString()),
                        Utils.removeAccents(editTextDescription.getText().toString())));
            }
        } else if (id == R.id.action_update) {
            if (imageByte == null && url_image.isEmpty())
                Utils.showSnackBar(conteiner, getString(R.string.error_image));
            else if (editTextTitle.getText().toString().isEmpty())
                Utils.showSnackBar(conteiner, getString(R.string.title_empty));
            else if (editTextDescription.getText().toString().isEmpty())
                Utils.showSnackBar(conteiner, getString(R.string.description_empty));
            else {
                pDialog.show();
                presenter.updateOffer(this, fillOffer(Utils.removeAccents(editTextTitle.getText().toString()),
                        Utils.removeAccents(editTextDescription.getText().toString())));
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
//            pDialog.show();
//            this.offer.setDATE_EDIT(Utils.getFechaInit());
//            presenter.deleteOffer(this, this.offer, true);
        }
        return true;
    }
}
