package com.valdroide.mycitysshopsadm.main.offer.fragments.offer.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.InputFilter;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.theartofdev.edmodo.cropper.CropImage;
import com.valdroide.mycitysshopsadm.MyCitysShopsAdmApp;
import com.valdroide.mycitysshopsadm.R;
import com.valdroide.mycitysshopsadm.entities.shop.Offer;
import com.valdroide.mycitysshopsadm.main.offer.Communicator;
import com.valdroide.mycitysshopsadm.main.offer.fragments.offer.OfferFragmentPresenter;
import com.valdroide.mycitysshopsadm.utils.Utils;

import java.io.IOException;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.app.Activity.RESULT_OK;

public class OfferFragment extends Fragment implements OfferFragmentView {


    @Bind(R.id.editTextTitle)
    EditText editTextTitle;
    @Bind(R.id.editTextDescription)
    EditText editTextDescription;
    @Bind(R.id.conteiner)
    LinearLayout conteiner;
    @Bind(R.id.imageViewImage)
    ImageView imageViewImage;
    @Bind(R.id.textViewQuantity)
    TextView textViewQuantity;

    @Inject
    OfferFragmentPresenter presenter;

    private ProgressDialog pDialog;
    private Menu menu;
    private int id_offer = 0, quantity_offer = 0, max_offer = 0;
    public static final int PERMISSION_GALERY = 1;
    public static final int GALERY = 1;
    private byte[] imageByte;
    private String encode = "", name_image = "", url_image = "", name_before = "", city = "";
    private int is_active;
    private Communicator communication;
    private boolean is_update;

    public OfferFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_offer, container, false);
        ButterKnife.bind(this, view);
        initDialog();
        editTextTitle.setFilters(new InputFilter[]{new InputFilter.AllCaps(), new InputFilter.LengthFilter(20)});
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        communication = (Communicator) getActivity();
    }

    @Override
    public void onActivityCreated(@android.support.annotation.Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setupInjection();
        presenter.onCreate();
        validateDateShop();
        presenter.getQuantity(getActivity());
        presenter.getCity(getActivity());
        is_update = isUpdate();
        if (is_update) {
            getOfferForId();
        }
    }

    private boolean isUpdate() {
        try {
            return getActivity().getIntent().getBooleanExtra("is_update", false);
        } catch (Exception e) {
            return false;
        }
    }

    private void initDialog() {
        Utils.writelogFile(getActivity(), "initDialog(offer)");
        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage(getString(R.string.processing));
        pDialog.setCancelable(false);
    }

    private void validateDateShop() {
        Utils.writelogFile(getActivity(), "validateDateShop(offer)");
        showProgressDialog();
        presenter.validateDateShop(getActivity());
    }

    private void setTextQuantity(String quantity) {
        textViewQuantity.setText(getString(R.string.offer_available) + " " + quantity);
    }

    private void setupInjection() {
        MyCitysShopsAdmApp app = (MyCitysShopsAdmApp) getActivity().getApplication();
        app.getOfferFragmentComponent(this, this).inject(this);
    }

    @OnClick(R.id.imageViewImage)
    public void getPhoto() {
        Utils.writelogFile(getActivity(), "getPhoto click imageViewImage y oldPhones(offer)");
        if (!Utils.oldPhones())
            Utils.checkForPermission(getActivity(), null, this, PERMISSION_GALERY);
        Utils.writelogFile(getActivity(), "hasPermission(offer)");
        if (Utils.hasPermission(getActivity()))
            Utils.ImageDialogLogo(null, this, GALERY);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_GALERY)
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Utils.ImageDialogLogo(null, this, GALERY);
            }
    }

    private void assignImage(Uri uri) {
        Utils.writelogFile(getActivity(), "assignImage y setPicasso(offer)");
        Utils.setPicasso(getActivity(), uri.toString(), android.R.drawable.ic_menu_crop, imageViewImage);
        try {
            imageByte = Utils.readBytes(uri, getActivity());
        } catch (IOException e) {
            Utils.writelogFile(getActivity(), "readBytes error: " + e.getMessage() + " (offer)");
            e.printStackTrace();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Utils.writelogFile(getActivity(), "onActivityResult(offer)");
        try {
            if (requestCode == GALERY) {
                Uri imageUri = CropImage.getPickImageResultUri(getActivity(), data);
                Utils.startCropImageActivity(null, this, imageUri);
            }
            if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
                CropImage.ActivityResult result = CropImage.getActivityResult(data);
                if (result != null) {
                    if (resultCode == RESULT_OK) {
                        assignImage(result.getUri());
                    } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                        if (!result.getError().toString().contains("ENOENT"))
                            Utils.showSnackBar(conteiner, "Error al asignar imagen: " + result.getError());
                    }
                }
            }
        } catch (Exception e) {
            Utils.writelogFile(getActivity(), "onActivityResult error: " + e.getMessage() + " (offer)");
            error(e.getMessage());
        }
    }

    private void cleanView() {
        Utils.writelogFile(getActivity(), "cleanView (offer)");
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
            Utils.writelogFile(getActivity(), "cleanView error: " + e.getMessage() + " (offer)");
            error(e.getMessage());
        }
    }

    private Offer fillOffer(String title, String descrip) {
        showProgressDialog();
        Utils.writelogFile(getActivity(), "fillOffer(offer)");
        if (city.isEmpty())
            presenter.getCity(getActivity());
        try {
            if (imageByte != null) {
                try {
                    encode = Base64.encodeToString(imageByte,
                            Base64.DEFAULT);
                } catch (Exception e) {
                    encode = "";
                }
                name_image = Utils.randomNumber() + Utils.getFechaOficial() + ".PNG";
                url_image = Utils.URL_IMAGE_OFFER + city + "/" + name_image;
            }

            return new Offer(id_offer, Utils.getIdShop(getActivity()), Utils.getIdCity(getActivity()), title, descrip, url_image,
                    name_image, is_active, Utils.getFechaOficialSeparate(), encode, name_before);
        } catch (Exception e) {
            Utils.writelogFile(getActivity(), "fillOffer error: " + e.getMessage() + " (offer)");
            return null;
        }
    }

    @Override
    public void setQuantity(int diff) {
        quantity_offer = diff;
        setTextQuantity(String.valueOf(quantity_offer));
        menuClean();
    }

    @Override
    public void saveOffer() {
        Utils.writelogFile(getActivity(), "saveOffer (offer)");
        presenter.getQuantity(getActivity());
        //menuClean();
        communication.refresh();
        Utils.showSnackBar(conteiner, getString(R.string.offer_success));
    }

    @Override
    public void updateOffer() {
        Utils.writelogFile(getActivity(), "updateOffer (offer)");
        is_update = false;
        menuClean();
        communication.refresh();
        Utils.showSnackBar(conteiner, getString(R.string.offer_update));
    }

    public void menuClean() {
        menuAdd();
        cleanView();
    }

    @Override
    public void error(String msg) {
        Utils.writelogFile(getActivity(), "error(offer)");
        Utils.showSnackBar(conteiner, msg);
    }

    @Override
    public void showProgressDialog() {
        Utils.writelogFile(getActivity(), "showProgressDialog(offer)");
        if (!pDialog.isShowing())
            pDialog.show();
    }

    @Override
    public void hidePorgressDialog() {
        Utils.writelogFile(getActivity(), "hidePorgressDialog(offer)");
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    @Override
    public void setCity(String city) {
        Utils.writelogFile(getActivity(), "setCity(offer)");
        this.city = city;
    }

    @Override
    public void setOfferForId(Offer offer) {
        editTextTitle.setText(offer.getTITLE());
        editTextDescription.setText(offer.getOFFER());
        id_offer = offer.getID_OFFER_KEY();
        name_image = offer.getNAME_IMAGE();
        name_before = name_image;
        url_image = offer.getURL_IMAGE();
        is_active = offer.getIS_ACTIVE();
        Utils.setPicasso(getActivity(), url_image, android.R.drawable.ic_menu_crop, imageViewImage);
        menuUpdate();
    }

    @Override
    public void validateSuccess() {
        hidePorgressDialog();
    }

    private void getOfferForId() {
        Utils.writelogFile(getActivity(), "isUpdateFillDate(offer)");
        try {
            if (!editTextTitle.isEnabled() || !editTextDescription.isEnabled())
                setEnable(true);

            showProgressDialog();
            presenter.getOfferForId(getActivity(), getActivity().getIntent().getIntExtra("id_offer", 0));
        } catch (Exception e) {
            Utils.writelogFile(getActivity(), "onClick adapter error: " + e.getMessage() + " (offer)");
            error(e.getMessage());
        }
    }

    private void menuAdd() {
        Utils.writelogFile(getActivity(), "menuAdd (offer)");
        if (menu != null) {
            try {
                menu.clear();
                getActivity().getMenuInflater().inflate(R.menu.offer, menu);
                if (quantity_offer == 0) {
                    hideAllMenu(menu);
                    if (editTextTitle.isEnabled() || editTextDescription.isEnabled())
                        setEnable(false);
                } else {
                    hideUpdateMenu(menu);
                    if (!editTextTitle.isEnabled() || !editTextDescription.isEnabled())
                        setEnable(true);
                }
            } catch (Exception e) {
                Utils.writelogFile(getActivity(), "menuAdd error: " + e.getMessage() + " (offer)");
                error(e.getMessage());
            }
        }
    }

    private void hideUpdateMenu(Menu menu) {
        Utils.writelogFile(getActivity(), "hideUpdateMenu (offer)");
        try {
            menu.getItem(0).setVisible(false);// cancel
            menu.getItem(1).setVisible(false);// update
        } catch (Exception e) {
            Utils.writelogFile(getActivity(), "hideUpdateMenu error: " + e.getMessage() + " (offer)");
            error(e.getMessage());
        }
    }

    private void hideAllMenu(Menu menu) {
        Utils.writelogFile(getActivity(), "hideAllMenu (offer)");
        try {
            menu.getItem(0).setVisible(false);// cancel
            menu.getItem(1).setVisible(false);// update
            menu.getItem(2).setVisible(false);// add
        } catch (Exception e) {
            Utils.writelogFile(getActivity(), "hideAllMenu error: " + e.getMessage() + " (offer)");
            error(e.getMessage());
        }
    }

    private void menuUpdate() {
        Utils.writelogFile(getActivity(), "menuUpdate (offer)");
        if (menu != null) {
            try {
                menu.clear();
                getActivity().getMenuInflater().inflate(R.menu.offer, menu);
                menu.getItem(2).setVisible(false);// add
            } catch (Exception e) {
                Utils.writelogFile(getActivity(), "menuUpdate error: " + e.getMessage() + " (offer)");
                error(e.getMessage());
            }
        }
    }

    private void setEnable(boolean isEnable) {
        Utils.writelogFile(getActivity(), "setEnable (offer)");
        try {
            imageViewImage.setEnabled(isEnable);
            editTextTitle.setEnabled(isEnable);
            editTextTitle.setFocusable(isEnable);
            editTextTitle.setFocusableInTouchMode(isEnable);
            editTextDescription.setEnabled(isEnable);
            editTextDescription.setFocusable(isEnable);
            editTextDescription.setFocusableInTouchMode(isEnable);
        } catch (Exception e) {
            Utils.writelogFile(getActivity(), "setEnable error: " + e.getMessage() + " (offer)");
            error(e.getMessage());
        }
    }

    @Override
    public void onDestroy() {
        Utils.writelogFile(getActivity(), "onDestroy (offer)");
        presenter.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        Utils.writelogFile(getActivity(), "onCreateOptionsMenu (offer)");
        inflater.inflate(R.menu.offer, menu);
        this.menu = menu;

        if (is_update) {
            menuUpdate();
        } else {
            if (quantity_offer == max_offer) {
                hideAllMenu(menu);
                if (editTextTitle.isEnabled() || editTextDescription.isEnabled())
                    setEnable(false);
            } else {
                hideUpdateMenu(menu);
            }
        }
        super.onCreateOptionsMenu(menu, inflater);
    }

    private void validateDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_add) {
            try {
                Utils.writelogFile(getActivity(), "action_add click y  presenter.saveOffer(offer)");
                if (imageByte == null)
                    Utils.showSnackBar(conteiner, getString(R.string.error_image));
                else if (editTextTitle.getText().toString().isEmpty())
                    Utils.showSnackBar(conteiner, getString(R.string.title_empty));
                else if (editTextDescription.getText().toString().isEmpty())
                    Utils.showSnackBar(conteiner, getString(R.string.description_empty));
                else {
                    //        validateDateShop();
                    validateDialog();
                    is_active = 1;
                    presenter.saveOffer(getActivity(), fillOffer(editTextTitle.getText().toString(),
                            editTextDescription.getText().toString()));
                }
            } catch (Exception e) {
                Utils.writelogFile(getActivity(), "action_add click error: " + e.getMessage() + " (offer)");
                error(e.getMessage());
            }
        } else if (id == R.id.action_update) {
            try {
                Utils.writelogFile(getActivity(), "action_update click y  presenter.saveOffer(offer)");
                if (imageByte == null && url_image.isEmpty())
                    Utils.showSnackBar(conteiner, getString(R.string.error_image));
                else if (editTextTitle.getText().toString().isEmpty())
                    Utils.showSnackBar(conteiner, getString(R.string.title_empty));
                else if (editTextDescription.getText().toString().isEmpty())
                    Utils.showSnackBar(conteiner, getString(R.string.description_empty));
                else {
                    validateDateShop();
                    validateDialog();
                    presenter.updateOffer(getActivity(), fillOffer(editTextTitle.getText().toString(),
                            editTextDescription.getText().toString()));
                }
            } catch (Exception e) {
                Utils.writelogFile(getActivity(), "action_update click error: " + e.getMessage() + " (offer)");
                error(e.getMessage());
            }
        } else if (id == R.id.action_cancel) {
            Utils.writelogFile(getActivity(), "action_cancel click (offer)");
            is_update = false;
            cleanView();
            menuAdd();
        }
        return super.onOptionsItemSelected(item);
    }
}
