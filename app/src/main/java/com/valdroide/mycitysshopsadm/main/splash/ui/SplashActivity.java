package com.valdroide.mycitysshopsadm.main.splash.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.valdroide.mycitysshopsadm.MyCitysShopsAdmApp;
import com.valdroide.mycitysshopsadm.R;
import com.valdroide.mycitysshopsadm.main.login.ui.LoginActivity;
import com.valdroide.mycitysshopsadm.main.navigation.ui.NavigationActivity;
import com.valdroide.mycitysshopsadm.main.place.ui.PlaceActivity;
import com.valdroide.mycitysshopsadm.main.splash.SplashActivityPresenter;
import com.valdroide.mycitysshopsadm.main.support.SupportActivityPresenter;
import com.valdroide.mycitysshopsadm.main.support.SupportActivityPresenterImpl;
import com.valdroide.mycitysshopsadm.main.support.events.SupportActivityEvent;
import com.valdroide.mycitysshopsadm.main.support.ui.SupportActivity;
import com.valdroide.mycitysshopsadm.utils.Utils;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;

public class SplashActivity extends AppCompatActivity implements SplashActivityView {

    @Bind(R.id.imageViewLogo)
    ImageView imageViewLogo;
    @Bind(R.id.progressBar)
    ProgressBar progressBar;
    @Bind(R.id.activity_splash)
    LinearLayout conteiner;
    @Bind(R.id.textViewDownload)
    TextView textViewDownload;
    @Inject
    SplashActivityPresenter presenter;
    private boolean isLogin = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        if (validateLogFile(this)) {
            Utils.writelogFile(this, "Se inicia ButterKnife(Splash)");
            ButterKnife.bind(this);
            Utils.writelogFile(this, "Se inicia Injection(Splash)");
            setupInjection();
            Utils.writelogFile(this, "Se inicia presenter Oncreate(Splash)");
            presenter.onCreate();
            Utils.writelogFile(this, "Se valida Log File(Splash)");
            isLogin = isLogin();
            if (!isLogin) {
                Utils.writelogFile(this, "Se valida date place(Splash)");
                presenter.validateDatePlace(this);
            } else {
                Utils.writelogFile(this, "Se valida date shop(Splash)");
                presenter.validateDateShop(this);
            }
        } else {
            Utils.writelogFile(this, getString(R.string.error_file_log));
            setError(getString(R.string.error_file_log));
        }
    }

    public boolean isLogin() {
        Utils.writelogFile(this, "Se toma extra isLogin(Splash)");
        return getIntent().getBooleanExtra("isLoguin", false);
    }

    private void setupInjection() {
        MyCitysShopsAdmApp app = (MyCitysShopsAdmApp) getApplication();
        app.getSplashActivityComponent(this, this).inject(this);
    }

    private boolean validateLogFile(Context context) {
        return Utils.validateLogFile(context);
    }

    @Override
    public void goToLog() {
        Utils.writelogFile(this, "Intent a LoginActivity(Splash)");
        try {
            progressBar.setVisibility(View.INVISIBLE);
            startActivity(new Intent(this, LoginActivity.class));
        } catch (Exception e) {
            Utils.writelogFile(this, "Intent a LoginActivity error: " + e.getMessage() + "(Splash)");
            setError(e.getMessage());
        }
    }

    @Override
    public void goToPlace() {
        Utils.writelogFile(this, "Intent a PlaceActivity(Splash)");
        try {
            progressBar.setVisibility(View.INVISIBLE);
            startActivity(new Intent(this, PlaceActivity.class));
        } catch (Exception e) {
            Utils.writelogFile(this, "Intent a PlaceActivity error: " + e.getMessage() + "(Splash)");
            setError(e.getMessage());
        }
    }

    @Override
    public void goToNav() {
        Utils.writelogFile(this, "Intent a NavigationActivity(Splash)");
        try {
            progressBar.setVisibility(View.INVISIBLE);
            startActivity(new Intent(this, NavigationActivity.class));
        } catch (Exception e) {
            Utils.writelogFile(this, "Intent a NavigationActivity error: " + e.getMessage() + "(Splash)");
            setError(e.getMessage());
        }
    }

    @Override
    public void setError(String msg) {
        Utils.writelogFile(this, "Error: " + msg + " (Splash)");
        progressBar.setVisibility(View.INVISIBLE);
        presenter.sendEmail(this, "Error Splash, Email Automatico.");
        textViewDownload.setText(msg + "\n" + getString(R.string.sent_emal_auto));
    }

    @Override
    protected void onDestroy() {
        Utils.writelogFile(this, "Destroy(Splash)");
        presenter.onDestroy();
        super.onDestroy();
    }
}
