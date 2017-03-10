package com.valdroide.mycitysshopsadm.main.login.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.valdroide.mycitysshopsadm.MyCitysShopsAdmApp;
import com.valdroide.mycitysshopsadm.R;
import com.valdroide.mycitysshopsadm.entities.user.User;
import com.valdroide.mycitysshopsadm.main.login.LoginActivityPresenter;
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

    @Inject
    LoginActivityPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        setupInjection();
        presenter.onCreate();
    }

    private void setupInjection() {
        MyCitysShopsAdmApp app = (MyCitysShopsAdmApp) getApplication();
        app.getLoginActivityComponent(this, this).inject(this);
    }

    @OnClick(R.id.buttonInto)
    public void validateLogin() {
        if (editTextUser.getText().toString().equals(""))
            Utils.showSnackBar(conteiner, getString(R.string.user_empty));
        else if (editTextPass.getText().toString().equals(""))
            Utils.showSnackBar(conteiner, getString(R.string.pass_empty));
        else if (Utils.getIdCity(this) == 0) {
            Utils.showSnackBar(conteiner, getString(R.string.error_id_city));
        } else {
            User user = new User();
            user.setUSER(editTextUser.getText().toString());
            user.setPASS(editTextPass.getText().toString());
            user.setID_CITY_FOREIGN(Utils.getIdCity(this));
            presenter.validateLogin(this, user);
        }
    }

    @OnClick(R.id.buttonChangePlace)
    public void changePlace() {
        presenter.changePlace(this);
    }

    @Override
    public void loginSuccess() {
        Intent intent = new Intent(this, SplashActivity.class);
        intent.putExtra("isLoguin", true);
        startActivity(intent);
    }

    @Override
    public void setError(String msg) {
        Utils.showSnackBar(conteiner, msg);
    }

    @Override
    public void goToPlace() {
        startActivity(new Intent(this, PlaceActivity.class));
    }

    @Override
    protected void onDestroy() {
        presenter.onDestroy();
        super.onDestroy();
    }
}
