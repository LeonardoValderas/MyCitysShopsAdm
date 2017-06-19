package com.valdroide.mycitysshopsadm.main.map.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RelativeLayout;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.valdroide.mycitysshopsadm.MyCitysShopsAdmApp;
import com.valdroide.mycitysshopsadm.R;
import com.valdroide.mycitysshopsadm.main.account.ui.AccountActivity;
import com.valdroide.mycitysshopsadm.main.map.MapActivityPresenter;
import com.valdroide.mycitysshopsadm.utils.Utils;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback, MapActivityView {
    @Bind(R.id.activity_map)
    RelativeLayout activityMap;
    private SupportMapFragment mapFragment;
    private GoogleMap mapa;
    public static double latCity = 0.00;
    public static double lonCity = 0.00;
    private Handler touchScreem;
    private boolean actualizar = false;
    private double longitudExtra = 0.00;
    private double latitudExtra = 0.00;
    private String name = "", uriExtra = "", phone = "", whatsapp = "", email = "", web = "", face = "", insta = "",
            twitter = "", snap = "", working = "", description = "", longitud = "", latitud = "", address = "";
    private CameraPosition cameraPosition;
    private CameraUpdate cu;
    @Inject
    MapActivityPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        Utils.writelogFile(this, "Se inicia ButterKnife(Map)");
        ButterKnife.bind(this);
        Utils.writelogFile(this, "onCreate(Map)");
        setupInjection();
        presenter.onCreate();
        Utils.writelogFile(this, "Se inicia toolbar Oncreate(Map)");
        getSupportActionBar().setTitle(R.string.my_location);
        presenter.getLatLong(this, Utils.getIdCity(this));
        actualizar = getIntent().getBooleanExtra("map", false);
        getExtraInfo();
        init();
    }

    private void init() {
        Utils.writelogFile(this, "Se inicia SupportMapFragment(Map)");
        try {
            mapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);
        } catch (Exception e) {
            Utils.writelogFile(this, " catch error " + e.getMessage() + "(Map)");
        }
    }

    private void setupInjection() {
        MyCitysShopsAdmApp app = (MyCitysShopsAdmApp) getApplication();
        app.getMapActivityComponent(this, this).inject(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Utils.writelogFile(this, "onMapReady (Map)");
        try {
            mapa = googleMap;
            touchScreem = new Handler();
            touchScreem.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mapa.getUiSettings().setAllGesturesEnabled(true);
                }
            }, 3000);
            mapa.getUiSettings().setAllGesturesEnabled(false);

            if (longitudExtra != 0.00 && latitudExtra != 0.00) {
                mapa.addMarker(new MarkerOptions().position(
                        new LatLng(latitudExtra, longitudExtra)).title(name));

                cameraPosition = new CameraPosition.Builder()
                        .target(new LatLng(latitudExtra, longitudExtra)).zoom(15).build();
            } else {
                cameraPosition = new CameraPosition.Builder()
                        .target(new LatLng(latCity, lonCity)).zoom(15).build();
            }

            cu = CameraUpdateFactory
                    .newCameraPosition(cameraPosition);
            mapa.animateCamera(cu);

            mapa.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                public void onMapClick(LatLng point) {
                    mapa.clear();

                    latitudExtra = point.latitude;
                    longitudExtra = point.longitude;
                    mapa.addMarker(new MarkerOptions().position(
                            new LatLng(latitudExtra, longitudExtra)).title(
                            name));
                }
            });

            mapa.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
                @Override
                public void onMapLongClick(LatLng latLng) {
                    mapa.clear();
                }
            });
        } catch (Exception e) {
            Utils.writelogFile(this, " catch error " + e.getMessage() + "(Map)");
        }
    }

    private void getExtraInfo() {
        if (actualizar) {
            name = getIntent().getStringExtra("name");
            uriExtra = getIntent().getStringExtra("uri");
            phone = getIntent().getStringExtra("phone");
            web = getIntent().getStringExtra("web");
            email = getIntent().getStringExtra("email");
            address = getIntent().getStringExtra("address");
            whatsapp = getIntent().getStringExtra("whatsaap");
            face = getIntent().getStringExtra("facebook");
            insta = getIntent().getStringExtra("instagram");
            twitter = getIntent().getStringExtra("twitter");
            snap = getIntent().getStringExtra("snapchat");
            working = getIntent().getStringExtra("working");
            description = getIntent().getStringExtra("description");
            longitud = getIntent().getStringExtra("longitud");
            latitud = getIntent().getStringExtra("latitud");
            if (!longitud.isEmpty() && !latitud.isEmpty()) {
                longitudExtra = Double.valueOf(longitud);
                latitudExtra = Double.valueOf(latitud);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Utils.writelogFile(this, "onCreateOptionsMenu (Map)");
        getMenuInflater().inflate(R.menu.map, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_map) {
            if (latitudExtra != 0.00 && longitudExtra != 0.00)
                volver();
            else
                Utils.showSnackBar(activityMap, getString(R.string.error_map));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void volver() {
        try {
            Intent intent = new Intent(this, AccountActivity.class);
            intent.putExtra("isMap", true);
            intent.putExtra("uri", uriExtra);
            intent.putExtra("phone", phone);
            intent.putExtra("whatsaap", whatsapp);
            intent.putExtra("web", web);
            intent.putExtra("facebook", face);
            intent.putExtra("instagram", insta);
            intent.putExtra("twitter", twitter);
            intent.putExtra("snapchat", snap);
            intent.putExtra("working", working);
            intent.putExtra("email", email);
            intent.putExtra("address", address);
            intent.putExtra("description", description);
            intent.putExtra("latitud", String.valueOf(latitudExtra));
            intent.putExtra("longitud", String.valueOf(longitudExtra));
            startActivity(intent);
        } catch (Exception e) {
            Utils.writelogFile(this, " catch error " + e.getMessage() + "(Map)");
        }
    }

    @Override
    public void setLatLong(String lat, String lon) {
        Utils.writelogFile(this, "setLatLong (Map)");
        latCity = Double.parseDouble(lat);
        lonCity = Double.parseDouble(lon);
    }

    @Override
    public void error(String error) {
        Utils.writelogFile(this, "error " + error + "(Map)");
        Utils.showSnackBar(activityMap, error);
    }
}