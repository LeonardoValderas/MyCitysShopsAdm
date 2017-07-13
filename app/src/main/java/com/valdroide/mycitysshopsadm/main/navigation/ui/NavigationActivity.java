package com.valdroide.mycitysshopsadm.main.navigation.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.valdroide.mycitysshopsadm.MyCitysShopsAdmApp;
import com.valdroide.mycitysshopsadm.R;
import com.valdroide.mycitysshopsadm.main.account.ui.AccountActivity;
import com.valdroide.mycitysshopsadm.main.draw.activities.ui.DrawActivity;
import com.valdroide.mycitysshopsadm.main.login.ui.LoginActivity;
import com.valdroide.mycitysshopsadm.main.navigation.NavigationActivityPresenter;
import com.valdroide.mycitysshopsadm.main.notification.ui.NotificationActivity;
import com.valdroide.mycitysshopsadm.main.offer.activities.ui.OfferActivity;
import com.valdroide.mycitysshopsadm.main.offer.fragments.offer.ui.OfferFragment;
import com.valdroide.mycitysshopsadm.main.support.ui.SupportActivity;
import com.valdroide.mycitysshopsadm.utils.Utils;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;

public class NavigationActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, NavigationActivityView {
    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.content_navigation)
    RelativeLayout content;
    @Bind(R.id.nav_view)
    NavigationView navigationView;
    @Bind(R.id.drawer_layout)
    DrawerLayout drawer;
    @Inject
    NavigationActivityPresenter presenter;
    @Bind(R.id.textViewFollow)
    TextView textViewFollow;
    @Bind(R.id.imageViewShop)
    ImageView imageViewShop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);
        Utils.writelogFile(this, "Se inicia ButterKnife(Navigation)");
        ButterKnife.bind(this);
        Utils.writelogFile(this, "Se inicia Injection(Navigation)");
        setupInjection();
        Utils.writelogFile(this, "Se inicia presenter Oncreate(Navigation)");
        presenter.onCreate();
        Utils.writelogFile(this, "Se inicia toolbar Oncreate(Navigation)");
        setSupportActionBar(toolbar);
        Utils.writelogFile(this, "Se inicia ActionBarDrawerToggle Oncreate(Navigation)");
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        Utils.writelogFile(this, "set addDrawerListener Oncreate(Navigation)");
        drawer.addDrawerListener(toggle);
        Utils.writelogFile(this, "toggle.syncState(); Oncreate(Navigation)");
        toggle.syncState();
        Utils.writelogFile(this, "setNavigationItemSelectedListener Oncreate(Navigation)");
        navigationView.setNavigationItemSelectedListener(this);
        presenter.getURLShop(this);
        setFollow();
    }

    private void setFollow() {
        Utils.writelogFile(this, "set follow textview Oncreate(Navigation)");
        try {
            textViewFollow.setText(String.valueOf(Utils.getIdFollow(this)));
        } catch (Exception e) {
            setError(e.getMessage());
            Utils.writelogFile(this, " catch error " + e.getMessage() + "(Navigation)");
        }
    }

    @Override
    public void setURLShop(String url) {
        if (url != null)
            if (!url.isEmpty()) {
                Utils.setPicasso(this, url, android.R.drawable.ic_menu_crop, imageViewShop);
            }
    }

    private void setupInjection() {
        MyCitysShopsAdmApp app = (MyCitysShopsAdmApp) getApplication();
        app.getNavigationActivityComponent(this, this).inject(this);
    }

    @Override
    public void onBackPressed() {
        Utils.writelogFile(this, "onBackPressed (Navigation)");
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Utils.writelogFile(this, "onCreateOptionsMenu (Navigation)");
        getMenuInflater().inflate(R.menu.navigation, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_log_out) {
            Utils.writelogFile(this, "action_log_out click (Navigation)");
            presenter.logOut(this);
            return true;
        } else if (id == R.id.action_support) {
            Utils.writelogFile(this, "action_support click Intent SupportActivity(Navigation)");
            startActivity(new Intent(this, SupportActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.my_account) {
            Utils.writelogFile(this, "my_account click Intent AccountActivity(Navigation)");
            startActivity(new Intent(this, AccountActivity.class));
        } else if (id == R.id.offers) {
            Utils.writelogFile(this, "offers click Intent OfferFragment(Navigation)");
            startActivity(new Intent(this, OfferActivity.class));
        } else if (id == R.id.notification) {
            Utils.writelogFile(this, "notification click Intent NotificationActivity(Navigation)");
            startActivity(new Intent(this, NotificationActivity.class));
        } else if (id == R.id.draw) {
            Utils.writelogFile(this, "draw click Intent DrawActivity(Navigation)");
            startActivity(new Intent(this, DrawActivity.class));
        }
        Utils.writelogFile(this, "closeDrawer(Navigation)");
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onDestroy() {
        Utils.writelogFile(this, "onDestroy(Navigation)");
        presenter.onDestroy();
        super.onDestroy();
    }

    @Override
    public void logOut() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK |
                Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    @Override
    public void setError(String error) {
        Utils.writelogFile(this, "setError " + error + "(Navigation)");
        Utils.showSnackBar(content, error);
    }
}
