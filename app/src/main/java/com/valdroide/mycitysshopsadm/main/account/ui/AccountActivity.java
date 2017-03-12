package com.valdroide.mycitysshopsadm.main.account.ui;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
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
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;
import com.valdroide.mycitysshopsadm.MyCitysShopsAdmApp;
import com.valdroide.mycitysshopsadm.R;
import com.valdroide.mycitysshopsadm.entities.user.Account;
import com.valdroide.mycitysshopsadm.main.account.AccountActivityPresenter;
import com.valdroide.mycitysshopsadm.main.map.MapActivity;
import com.valdroide.mycitysshopsadm.utils.Utils;

import java.io.IOException;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AccountActivity extends AppCompatActivity implements AccountActivityView {

    @Bind(R.id.textViewName)
    TextView textViewName;
    @Bind(R.id.imageViewLogo)
    ImageView imageViewLogo;
    @Bind(R.id.editTextPhone)
    EditText editTextPhone;
    @Bind(R.id.editTextEmail)
    EditText editTextEmail;
    @Bind(R.id.editTextAddress)
    EditText editTextAddress;
    @Bind(R.id.editTextDescription)
    EditText editTextDescription;
    @Bind(R.id.activity_account)
    RelativeLayout activityAccount;

    public static final int PERMISSION_GALERY = 1;
    public static final int GALERY = 1;
    private byte[] imageByte;
    private String latitud = "", longitud = "", name_before = "", name_logo = "", url_logo = "", encode = "";
    private int id_account = 0;
    private boolean isMap = false;
    private ProgressDialog pDialog;
    private Uri uriExtra;
    private Menu menu;

    @Inject
    AccountActivityPresenter presenter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        ButterKnife.bind(this);
        setEnable(false);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.my_account_title);
        setupInjection();
        presenter.onCreate();
        initDialog();
        pDialog.show();
        presenter.getAccount();
        isMap = getIntent().getBooleanExtra("isMap", false);
        if (isMap) {
            if (getIntent().getStringExtra("uri") != null)
                uriExtra = Uri.parse(getIntent().getStringExtra("uri"));
            if (uriExtra != null)
                assignImage(uriExtra);

            editTextPhone.setText(getIntent().getStringExtra("phone"));
            editTextEmail.setText(getIntent().getStringExtra("email"));
            editTextAddress.setText(getIntent().getStringExtra("address"));
            editTextDescription.setText(getIntent().getStringExtra("description"));
            latitud = getIntent().getStringExtra("latitud");
            longitud = getIntent().getStringExtra("longitud");
            setEnable(true);
        }
    }

    public void initDialog() {
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Procesando...");
        pDialog.setCancelable(false);
    }

    public void setEnable(boolean isEnable) {
        imageViewLogo.setEnabled(isEnable);
        editTextPhone.setEnabled(isEnable);
        editTextPhone.setFocusable(isEnable);
        editTextPhone.setFocusableInTouchMode(isEnable);
        editTextEmail.setEnabled(isEnable);
        editTextEmail.setFocusable(isEnable);
        editTextEmail.setFocusableInTouchMode(isEnable);
        editTextAddress.setEnabled(isEnable);
        editTextAddress.setFocusable(isEnable);
        editTextAddress.setFocusableInTouchMode(isEnable);
        editTextDescription.setEnabled(isEnable);
        editTextDescription.setFocusable(isEnable);
        editTextDescription.setFocusableInTouchMode(isEnable);
    }

    @Override
    @OnClick(R.id.imageViewLogo)
    public void getPhoto() {
        if (!Utils.oldPhones())
            checkForPermission();

        if (hasPermission())
            ImageDialogLogo();
    }

    private void setupInjection() {
        MyCitysShopsAdmApp app = (MyCitysShopsAdmApp) getApplication();
        app.getAccountActivityComponent(this, this).inject(this);
    }

    @Override
    public void error(String mgs) {
        pDialog.hide();
        Utils.showSnackBar(activityAccount, mgs);
    }

    @Override
    public void saveSuccess() {
        pDialog.hide();
        cleanData();
        menuEdit();
        setEnable(false);
        Utils.showSnackBar(activityAccount, getString(R.string.account_save));
    }

    private void cleanData() {
        imageByte = null;
        encode = "";
        presenter.getAccount();
    }

    @Override
    public void setAccount(Account account) {
        id_account = account.getID_ACCOUNT_KEY();
        textViewName.setText(account.getSHOP_NAME());
        Utils.setPicasso(this, account.getURL_LOGO(), android.R.drawable.ic_menu_crop, imageViewLogo);
        name_logo = account.getNAME_LOGO();
        name_before = name_logo;
        url_logo = account.getURL_LOGO() == null ? "" : account.getURL_LOGO();
        editTextPhone.setText(account.getPHONE());
        editTextEmail.setText(account.getEMAIL());
        editTextAddress.setText(account.getADDRESS());
        latitud = account.getLATITUD() == null ? "" : account.getLATITUD();
        longitud = account.getLONGITUD() == null ? "" : account.getLONGITUD();
        editTextDescription.setText(account.getDESCRIPTION());
        if (pDialog.isShowing())
            pDialog.hide();
    }

    public Account prepareAccount() {
        pDialog.show();
        if (imageByte != null) {
            try {
                encode = Base64.encodeToString(imageByte,
                        Base64.DEFAULT);
            } catch (Exception e) {
                encode = "";
            }
            name_logo = Utils.removeAccents(textViewName.getText().toString()) + Utils.getFechaOficial() + ".PNG";
            url_logo = Utils.URL_IMAGE + name_logo;

        }
        return new Account(id_account, textViewName.getText().toString(), url_logo, name_logo,
                name_before, encode, editTextDescription.getText().toString(), editTextPhone.getText().toString(),
                editTextEmail.getText().toString(), latitud, longitud, editTextAddress.getText().toString());
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
        Utils.setPicasso(this, uri.toString(), android.R.drawable.ic_menu_crop, imageViewLogo);
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
                uriExtra = result.getUri();
                assignImage(uriExtra);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                if (!result.getError().toString().contains("ENOENT"))
                    Utils.showSnackBar(activityAccount, "Error al asignar imagen: " + result.getError());
            }
        }
    }

    private void startCropImageActivity(Uri imageUri) {
        CropImage.activity(imageUri)
                .setGuidelines(CropImageView.Guidelines.ON)
                .setMultiTouchEnabled(true)
                .start(AccountActivity.this);
    }

    public void menuSave() {
        menu.clear();
        getMenuInflater().inflate(R.menu.account, menu);
        menu.getItem(0).setVisible(true);// map
        menu.getItem(1).setVisible(true);// save
        menu.getItem(2).setVisible(false);// edit
    }

    public void menuEdit() {
        menu.clear();
        getMenuInflater().inflate(R.menu.account, menu);
        menu.getItem(0).setVisible(false);// map
        menu.getItem(1).setVisible(false);// save
        menu.getItem(2).setVisible(true);// edit
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.account, menu);
        this.menu = menu;
        if(isMap){
            menuSave();
        }
        else {
            menu.getItem(0).setVisible(false);// map
            menu.getItem(1).setVisible(false);// save
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_save) {
            if (imageByte == null && url_logo.isEmpty())
                Utils.showSnackBar(activityAccount, getString(R.string.error_logo));
            else if (editTextAddress.getText().toString().isEmpty())
                Utils.showSnackBar(activityAccount, getString(R.string.error_address));
            else if (latitud.isEmpty() || longitud.isEmpty())
                Utils.showSnackBar(activityAccount, getString(R.string.error_map));
            else if (editTextDescription.getText().toString().isEmpty())
                Utils.showSnackBar(activityAccount, getString(R.string.error_description));
            else
                presenter.updateAccount(this, prepareAccount());
        } else if (id == R.id.action_map) {

            Intent intent = new Intent(this, MapActivity.class);
            intent.putExtra("map", true);
            intent.putExtra("name", textViewName.getText().toString());
            if (uriExtra != null)
                intent.putExtra("uri", uriExtra.toString());
            intent.putExtra("phone", editTextPhone.getText().toString());
            intent.putExtra("email", editTextEmail.getText().toString());
            intent.putExtra("address", editTextAddress.getText().toString());
            intent.putExtra("description", editTextDescription.getText().toString());
            intent.putExtra("latitud", latitud);
            intent.putExtra("longitud", longitud);
            startActivity(intent);
        } else if (id == R.id.action_edit) {
            menuSave();
            setEnable(true);
        }
        return super.onOptionsItemSelected(item);
    }
}
