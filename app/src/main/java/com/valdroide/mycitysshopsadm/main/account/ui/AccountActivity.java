package com.valdroide.mycitysshopsadm.main.account.ui;

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
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;
import com.valdroide.mycitysshopsadm.MyCitysShopsAdmApp;
import com.valdroide.mycitysshopsadm.R;
import com.valdroide.mycitysshopsadm.entities.shop.Account;
import com.valdroide.mycitysshopsadm.main.account.AccountActivityPresenter;
import com.valdroide.mycitysshopsadm.main.map.ui.MapActivity;
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
    @Bind(R.id.editTextWhatsaap)
    EditText editTextWhatsaap;
    @Bind(R.id.editTextWeb)
    EditText editTextWeb;
    @Bind(R.id.editTextFace)
    EditText editTextFace;
    @Bind(R.id.editTextInsta)
    EditText editTextInsta;
    @Bind(R.id.editTextTwitter)
    EditText editTextTwitter;
    @Bind(R.id.editTextSnap)
    EditText editTextSnap;

    public static final int PERMISSION_GALERY = 1;
    public static final int GALERY = 1;
    @Bind(R.id.editTextWorking)
    EditText editTextWorking;
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
        Utils.writelogFile(this, "Se inicia ButterKnife(Account)");
        ButterKnife.bind(this);
        Utils.writelogFile(this, "Se inicia Injection(Account)");
        setupInjection();
        Utils.writelogFile(this, "Se inicia presenter Oncreate(Account)");
        presenter.onCreate();
        Utils.writelogFile(this, "Se inicia toolbar setDisplayHomeAsUpEnabled(Account)");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Utils.writelogFile(this, "Se inicia toolbar setTitle(Account)");
        getSupportActionBar().setTitle(R.string.my_account_title);
        setEnable(false);
        Utils.writelogFile(this, "inicia dialog (Account)");
        initDialog();
        Utils.writelogFile(this, "show dialog (Account)");
        pDialog.show();
        Utils.writelogFile(this, "presenter.getAccount() (Account)");
        presenter.getAccount(this);
        Utils.writelogFile(this, "getBooleanExtra() isMap; (Account)");
        isMap = getIntent().getBooleanExtra("isMap", false);
        if (isMap) {
            Utils.writelogFile(this, "isMap true y getStringExtra(uri) (Account)");
            if (getIntent().getStringExtra("uri") != null) {
                Utils.writelogFile(this, "getStringExtra(uri) != null y parse Uri(Account)");
                uriExtra = Uri.parse(getIntent().getStringExtra("uri"));
                if (uriExtra != null) {
                    Utils.writelogFile(this, "parse Uri != null y assignImage (Account)");
                    assignImage(uriExtra);
                }
            }
            Utils.writelogFile(this, "fillExtraData(); (Account)");
            fillExtraData();

        }
    }

    public void initDialog() {
        pDialog = new ProgressDialog(this);
        pDialog.setMessage(getString(R.string.processing));
        pDialog.setCancelable(false);
    }

    public void setEnable(boolean isEnable) {
        Utils.writelogFile(this, "setEnable todos los componentes (Account)");
        try {
            imageViewLogo.setEnabled(isEnable);

            setAttributeEdit(editTextPhone, isEnable);
            setAttributeEdit(editTextWhatsaap, isEnable);
            setAttributeEdit(editTextEmail, isEnable);
            setAttributeEdit(editTextWeb, isEnable);
            setAttributeEdit(editTextFace, isEnable);
            setAttributeEdit(editTextInsta, isEnable);
            setAttributeEdit(editTextTwitter, isEnable);
            setAttributeEdit(editTextSnap, isEnable);
            setAttributeEdit(editTextDescription, isEnable);
            setAttributeEdit(editTextAddress, isEnable);
            setAttributeEdit(editTextWorking, isEnable);
        } catch (Exception e) {
            error(e.getMessage());
            Utils.writelogFile(this, "setEnable error " + e.getMessage() + " (Account)");
        }
    }

    public void fillExtraData() {
        Utils.writelogFile(this, "fillExtraData todos los componentes (Account)");
        try {
            editTextPhone.setText(getIntent().getStringExtra("phone"));
            editTextWhatsaap.setText(getIntent().getStringExtra("whatsaap"));
            editTextEmail.setText(getIntent().getStringExtra("email"));
            editTextWeb.setText(getIntent().getStringExtra("web"));
            editTextFace.setText(getIntent().getStringExtra("facebook"));
            editTextInsta.setText(getIntent().getStringExtra("instagram"));
            editTextTwitter.setText(getIntent().getStringExtra("twitter"));
            editTextSnap.setText(getIntent().getStringExtra("snapchat"));
            editTextAddress.setText(getIntent().getStringExtra("address"));
            editTextDescription.setText(getIntent().getStringExtra("description"));
            editTextWorking.setText(getIntent().getStringExtra("working"));
            latitud = getIntent().getStringExtra("latitud");
            longitud = getIntent().getStringExtra("longitud");
        } catch (Exception e) {
            error(e.getMessage());
            Utils.writelogFile(this, "fillExtraData error " + e.getMessage() + " (Account)");
        }
        setEnable(true);
    }

    public void setAttributeEdit(EditText editText, boolean isEnable) {
        editText.setEnabled(isEnable);
        editText.setFocusable(isEnable);
        editText.setFocusableInTouchMode(isEnable);
    }

    @Override
    @OnClick(R.id.imageViewLogo)
    public void getPhoto() {
        Utils.writelogFile(this, "getPhoto() click imageViewLogo y validate oldPhones(Account)");
        if (!Utils.oldPhones())
            checkForPermission();
        Utils.writelogFile(this, "hasPermission() y ImageDialogLogo(Account)");
        if (hasPermission())
            ImageDialogLogo();
    }

    private void setupInjection() {
        MyCitysShopsAdmApp app = (MyCitysShopsAdmApp) getApplication();
        app.getAccountActivityComponent(this, this).inject(this);
    }

    @Override
    public void error(String mgs) {
        Utils.writelogFile(this, "error " + mgs + " (Account)");
        dismissDialog();
        Utils.showSnackBar(activityAccount, mgs);
    }

    public void dismissDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    @Override
    public void saveSuccess() {
        Utils.writelogFile(this, "saveSuccess() y cleanData()(Account)");
        cleanData();
        Utils.writelogFile(this, "menuEdit() y setEnable(false)(Account)");
        menuEdit();
        setEnable(false);
        Utils.writelogFile(this, "pDialog.dismiss() y showSnackBar(Account)");
        pDialog.dismiss();
        Utils.showSnackBar(activityAccount, getString(R.string.account_save));
    }

    private void cleanData() {
        imageByte = null;
        encode = "";
        presenter.getAccount(this);
    }

    @Override
    public void setAccount(Account account) {
        Utils.writelogFile(this, "setAccount() y setAccont a los componentes, try(Account)");
        try {
            id_account = account.getID_ACCOUNT_KEY();
            textViewName.setText(account.getSHOP_NAME());
            url_logo = account.getURL_LOGO() == null ? "" : account.getURL_LOGO();
            if (!url_logo.isEmpty())
                Utils.setPicasso(this, url_logo, android.R.drawable.ic_menu_crop, imageViewLogo);
            name_logo = account.getNAME_LOGO();
            name_before = name_logo;
            editTextPhone.setText(account.getPHONE());
            editTextWhatsaap.setText(account.getWHATSAAP());
            editTextEmail.setText(account.getEMAIL());
            editTextWeb.setText(account.getWEB());
            editTextFace.setText(account.getFACEBOOK());
            editTextInsta.setText(account.getINSTAGRAM());
            editTextTwitter.setText(account.getTWITTER());
            editTextSnap.setText(account.getSNAPCHAT());
            editTextDescription.setText(account.getDESCRIPTION());
            editTextAddress.setText(account.getADDRESS());
            editTextWorking.setText(account.getWORKING_HOURS());
            latitud = account.getLATITUD() == null ? "" : account.getLATITUD();
            longitud = account.getLONGITUD() == null ? "" : account.getLONGITUD();
        } catch (Exception e) {
            Utils.writelogFile(this, "error catch " + e.getMessage());
        }
        dismissDialog();
    }

    public Account prepareAccount() {
        Utils.writelogFile(this, "prepareAccount() y show dialog(Account)");
        pDialog.show();
        Utils.writelogFile(this, "fill account object(Account)");
        try {
            if (imageByte != null) {
                try {
                    encode = Base64.encodeToString(imageByte,
                            Base64.DEFAULT);
                } catch (Exception e) {
                    encode = "";
                }
                String name = textViewName.getText().toString().trim();
                if (name != null) {
                    if (!name.isEmpty())
                        name = name.replace(" ", "");
                }
                name_logo = Utils.removeAccents(name) + Utils.getFechaOficial() + ".PNG";
                url_logo = Utils.URL_IMAGE + name_logo;

            }

            return new Account(id_account, textViewName.getText().toString(), url_logo, name_logo,
                    name_before, encode, Utils.removeAccents(editTextDescription.getText().toString()),
                    Utils.removeAccents(editTextWorking.getText().toString()),
                    editTextPhone.getText().toString(), Utils.removeAccents(editTextEmail.getText().toString()),
                    Utils.removeAccents(editTextWeb.getText().toString()), Utils.removeAccents(editTextWhatsaap.getText().toString()),
                    Utils.removeAccents(editTextFace.getText().toString()), Utils.removeAccents(editTextInsta.getText().toString()),
                    Utils.removeAccents(editTextTwitter.getText().toString()), Utils.removeAccents(editTextSnap.getText().toString()),
                    latitud, longitud, Utils.removeAccents(editTextAddress.getText().toString()), Utils.getFechaOficialSeparate());
        } catch (Exception e) {
            Utils.writelogFile(this, "fill account object error:" + e.getMessage() + "(Account)");
            return null;
        }
    }

    private void checkForPermission() {
        Utils.writelogFile(this, "is not oldPhones(Account)");
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
        Utils.writelogFile(this, "ImageDialogLogo() (Account)");
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
        Utils.writelogFile(this, "setPicasso() (Account)");
        Utils.setPicasso(this, uri.toString(), android.R.drawable.ic_menu_crop, imageViewLogo);
        try {
            imageByte = Utils.readBytes(uri, this);
        } catch (IOException e) {
            Utils.writelogFile(this, "setPicasso() error : " + e.getMessage() + "(Account)");
            e.printStackTrace();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Utils.writelogFile(this, "onActivityResult image() (Account)");
        try {
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
        } catch (Exception e) {
            Utils.writelogFile(this, "onActivityResult image() error: " + e.getMessage() + " (Account)");
            error(e.getMessage());
        }
    }

    private void startCropImageActivity(Uri imageUri) {
        CropImage.activity(imageUri)
                .setGuidelines(CropImageView.Guidelines.ON)
                .setMultiTouchEnabled(true)
                .start(AccountActivity.this);
    }

    public void menuSave() {
        Utils.writelogFile(this, "menuSave()(Account)");
        try {
            menu.clear();
            getMenuInflater().inflate(R.menu.account, menu);
            menu.getItem(0).setVisible(true);// map
            menu.getItem(1).setVisible(true);// save
            menu.getItem(2).setVisible(false);// edit
        } catch (Exception e) {
            Utils.writelogFile(this, "menuSave()Error" + e.getMessage() + "(Account)");
            error(e.getMessage());
        }
    }

    public void menuEdit() {
        Utils.writelogFile(this, "menuEdit()(Account)");
        try {
            menu.clear();
            getMenuInflater().inflate(R.menu.account, menu);
            menu.getItem(0).setVisible(false);// map
            menu.getItem(1).setVisible(false);// save
            menu.getItem(2).setVisible(true);// edit
        } catch (Exception e) {
            Utils.writelogFile(this, "menuEdit()Error" + e.getMessage() + "(Account)");
            error(e.getMessage());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.account, menu);
        this.menu = menu;
        if (isMap) {
            menuSave();
        } else {
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
            else if (editTextWorking.getText().toString().isEmpty())
                Utils.showSnackBar(activityAccount, getString(R.string.error_working));
            else
                presenter.updateAccount(this, prepareAccount());
        } else if (id == R.id.action_map) {
            Utils.writelogFile(this, "action_map click Intent set extras(Account)");
            if (Utils.getIdCity(this) != 0) {
                Utils.writelogFile(this, "id_city != 0(Account)");
                try {
                    Intent intent = new Intent(this, MapActivity.class);
                    intent.putExtra("map", true);
                    intent.putExtra("name", textViewName.getText().toString());
                    if (uriExtra != null)
                        intent.putExtra("uri", uriExtra.toString());
                    intent.putExtra("phone", editTextPhone.getText().toString());
                    intent.putExtra("email", editTextEmail.getText().toString());
                    intent.putExtra("facebook", editTextFace.getText().toString());
                    intent.putExtra("web", editTextWeb.getText().toString());
                    intent.putExtra("whatsaap", editTextWhatsaap.getText().toString());
                    intent.putExtra("instagram", editTextInsta.getText().toString());
                    intent.putExtra("twitter", editTextTwitter.getText().toString());
                    intent.putExtra("snapchat", editTextSnap.getText().toString());
                    intent.putExtra("working", editTextWorking.getText().toString());
                    intent.putExtra("address", editTextAddress.getText().toString());
                    intent.putExtra("description", editTextDescription.getText().toString());
                    intent.putExtra("latitud", latitud);
                    intent.putExtra("longitud", longitud);
                    startActivity(intent);

                } catch (Exception e) {
                    Utils.writelogFile(this, "action_map click Intent error: " + e.getMessage() + " (Account)");
                    error(e.getMessage());
                }
            } else {
                Utils.writelogFile(this, "id_city == 0 (Account)");
                error(getString(R.string.id_city));
            }
        } else if (id == R.id.action_edit) {
            Utils.writelogFile(this, "action_edit click(Account)");
            menuSave();
            setEnable(true);
        }
        return super.onOptionsItemSelected(item);
    }
}
