package com.valdroide.mycitysshopsadm.main.login.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.valdroide.mycitysshopsadm.MyCitysShopsAdmApp;
import com.valdroide.mycitysshopsadm.R;
import com.valdroide.mycitysshopsadm.entities.shop.Notification;
import com.valdroide.mycitysshopsadm.main.login.LoginActivityPresenter;
import com.valdroide.mycitysshopsadm.main.notification.ui.NotificationActivity;
import com.valdroide.mycitysshopsadm.main.place.ui.PlaceActivity;
import com.valdroide.mycitysshopsadm.main.splash.ui.SplashActivity;
import com.valdroide.mycitysshopsadm.utils.Utils;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity implements LoginActivityView {

    @Bind(R.id.editTextUser)
    EditText editTextUser;
    @Bind(R.id.editTextPass)
    EditText editTextPass;
    @Bind(R.id.buttonInto)
    Button buttonInto;
    @Bind(R.id.buttonChangePlace)
    Button buttonChangePlace;
    @Bind(R.id.activity_login)
    RelativeLayout conteiner;
    private ProgressDialog pDialog;

    @Inject
    LoginActivityPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Utils.writelogFile(this, "Se inicia ButterKnife(Login)");
        ButterKnife.bind(this);
        Utils.writelogFile(this, "Se inicia Injection(Login)");
        setupInjection();
        Utils.writelogFile(this, "Se inicia presenter Oncreate(Login)");
        presenter.onCreate();
        initDialog();
    }

    private void setupInjection() {
        MyCitysShopsAdmApp app = (MyCitysShopsAdmApp) getApplication();
        app.getLoginActivityComponent(this, this).inject(this);
    }

    private void initDialog() {
        pDialog = new ProgressDialog(this);
        pDialog.setMessage(getString(R.string.processing));
        pDialog.setCancelable(false);
    }

    @OnClick(R.id.buttonInto)
    public void validateLogin() {
        Utils.writelogFile(this, "Metodo validateLogin on click buttonInto(Login)");
        try {
            if (editTextUser.getText().toString().equals(""))
                Utils.showSnackBar(conteiner, getString(R.string.user_empty));
            else if (editTextPass.getText().toString().equals(""))
                Utils.showSnackBar(conteiner, getString(R.string.pass_empty));
            else if (Utils.getIdCity(this) == 0) {
                Utils.writelogFile(this, "Utils.getIdCity es cero(Login)");
                Utils.showSnackBar(conteiner, getString(R.string.error_id_city));
            } else {
                showProgressDialog();
                Utils.writelogFile(this, " presenter.validateLogin(Login)");

                if (editTextUser.getText().toString().equalsIgnoreCase("MCS_ADMIN")) {
                    presenter.validateLoginAdm(this, editTextUser.getText().toString(),
                            editTextPass.getText().toString(), Utils.getIdCity(this));
                } else {
                    presenter.validateLogin(this, editTextUser.getText().toString(),
                            editTextPass.getText().toString(), Utils.getIdCity(this));
                }
            }
        } catch (Exception e) {
            setError(e.getMessage());
            Utils.writelogFile(this, "validateLogin error " + e.getMessage() + "(Login)");
        }
    }

    @OnClick(R.id.buttonChangePlace)
    public void changePlace() {
        Utils.writelogFile(this, "Metodo changePlace on click buttonChangePlace y presenter.changePlace(Login)");
        showProgressDialog();
        presenter.changePlace(this);
    }

    @Override
    public void loginSuccess() {
        Utils.writelogFile(this, "Metodo loginSuccess y intente Splash con extra true(Login)");
        Intent intent = new Intent(this, SplashActivity.class);
        intent.putExtra("isLoguin", true);
        startActivity(intent);
    }

    @Override
    public void setError(String msg) {
        Utils.writelogFile(this, "Metodo setError: " + msg + "(Login)");
        Utils.showSnackBar(conteiner, msg);
    }

    @Override
    public void goToPlace() {
        Utils.writelogFile(this, "Metodo goToPlace Intente Place(Login)");
        startActivity(new Intent(this, SplashActivity.class));
    }

    @Override
    public void goToNotification() {
        hidePorgressDialog();
        Intent intent = new Intent(this, NotificationActivity.class);
        intent.putExtra("isADM", true);
        startActivity(intent);
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
    protected void onDestroy() {
        Utils.writelogFile(this, "Metodo onDestroy(Login)");
        presenter.onDestroy();
        super.onDestroy();
    }
}
