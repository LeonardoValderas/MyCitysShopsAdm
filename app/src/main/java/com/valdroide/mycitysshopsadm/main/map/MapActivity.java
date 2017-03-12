package com.valdroide.mycitysshopsadm.main.map;

import android.content.Intent;
import android.graphics.Point;
import android.net.Uri;
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
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.valdroide.mycitysshopsadm.R;
import com.valdroide.mycitysshopsadm.main.account.ui.AccountActivity;
import com.valdroide.mycitysshopsadm.utils.Utils;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {
    @Bind(R.id.activity_map)
    RelativeLayout activityMap;
    private SupportMapFragment mapFragment;
    private GoogleMap mapa;
    public static double latBache = -41.139755445793554;
    public static double lonBache = -71.296729134343434;
    private Handler touchScreem;
    private boolean actualizar = false;
    private double longitudExtra = 0.00;
    private double latitudExtra = 0.00;
    private String name = "", uriExtra = "", phone = "", email = "", address = "", description = "", longitud = "", latitud = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        ButterKnife.bind(this);
        //  getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("MI UBICACIÓN");

        init();
    }

    public void init() {
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        actualizar = getIntent().getBooleanExtra("map", false);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mapa = googleMap;
        touchScreem = new Handler();
        touchScreem.postDelayed(new Runnable() {
            @Override
            public void run() {
                mapa.getUiSettings().setAllGesturesEnabled(true);
            }
        }, 3000);
        mapa.getUiSettings().setAllGesturesEnabled(false);
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(latBache, lonBache)).zoom(15).build();
        CameraUpdate cu = CameraUpdateFactory
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
        if (actualizar) {
            name = getIntent().getStringExtra("name");
            uriExtra = getIntent().getStringExtra("uri");
            phone = getIntent().getStringExtra("phone");
            email = getIntent().getStringExtra("email");
            address = getIntent().getStringExtra("address");
            description = getIntent().getStringExtra("description");
            longitud = getIntent().getStringExtra("longitud");
            latitud = getIntent().getStringExtra("latitud");
            if (!longitud.isEmpty() && !latitud.isEmpty()) {
                longitudExtra = Double.valueOf(longitud);
                latitudExtra = Double.valueOf(latitud);
                mapa.addMarker(new MarkerOptions().position(
                        new LatLng(latitudExtra, longitudExtra)).title(name));
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.map, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_map) {
            if (latitudExtra != 0.00 && latitudExtra != 0.00)
                volver();
            else
                Utils.showSnackBar(activityMap, getString(R.string.error_map));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void volver() {
        Intent intent = new Intent(this, AccountActivity.class);
        intent.putExtra("isMap", true);
        intent.putExtra("uri", uriExtra);
        intent.putExtra("phone", phone);
        intent.putExtra("email", email);
        intent.putExtra("address", address);
        intent.putExtra("description", description);
        intent.putExtra("latitud", String.valueOf(latitudExtra));
        intent.putExtra("longitud", String.valueOf(longitudExtra));
        startActivity(intent);
    }
}