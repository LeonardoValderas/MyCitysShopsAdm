package com.valdroide.mycitysshopsadm.main.place.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.valdroide.mycitysshopsadm.MyCitysShopsAdmApp;
import com.valdroide.mycitysshopsadm.R;
import com.valdroide.mycitysshopsadm.entities.shop.MyPlace;
import com.valdroide.mycitysshopsadm.entities.place.City;
import com.valdroide.mycitysshopsadm.entities.place.Country;
import com.valdroide.mycitysshopsadm.entities.place.State;
import com.valdroide.mycitysshopsadm.main.login.ui.LoginActivity;
import com.valdroide.mycitysshopsadm.main.place.PlaceActivityPresenter;
import com.valdroide.mycitysshopsadm.main.place.ui.adapters.AdapterSpinnerCity;
import com.valdroide.mycitysshopsadm.main.place.ui.adapters.AdapterSpinnerCountry;
import com.valdroide.mycitysshopsadm.main.place.ui.adapters.AdapterSpinnerState;
import com.valdroide.mycitysshopsadm.utils.Utils;

import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PlaceActivity extends AppCompatActivity implements PlaceActivityView {

    @Bind(R.id.textCountry)
    TextView textCountry;
    @Bind(R.id.spinnerCountry)
    Spinner spinnerCountry;
    @Bind(R.id.textState)
    TextView textState;
    @Bind(R.id.spinnerState)
    Spinner spinnerState;
    @Bind(R.id.textCity)
    TextView textCity;
    @Bind(R.id.spinnerCity)
    Spinner spinnerCity;
    @Bind(R.id.buttonInto)
    Button buttonInto;
    @Bind(R.id.activity_place)
    RelativeLayout conteiner;

    private List<Country> countries;
    private List<State> states;
    private List<City> cities;
    private Country country;
    private State state;
    private City city;
    private int id_country = 0, id_state = 0;
    private ProgressDialog pDialog;

    @Inject
    PlaceActivityPresenter presenter;
    @Inject
    AdapterSpinnerCountry adapterSpinnerCountry;
    @Inject
    AdapterSpinnerState adapterSpinnerState;
    @Inject
    AdapterSpinnerCity adapterSpinnerCity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place);
        ButterKnife.bind(this);
        setupInjection();
        presenter.onCreate();
        initDialog();
        pDialog.show();
        initAdapterSpinner();
        setOnItemSelectedCountry();
        setOnItemSelectedState();
        presenter.getCountries();
    }

    private void setupInjection() {
        MyCitysShopsAdmApp app = (MyCitysShopsAdmApp) getApplication();
        app.getPlaceActivityComponent(this, this).inject(this);
    }

    public void initDialog() {
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Procesando...");
        pDialog.setCancelable(false);
    }

    public void initAdapterSpinner() {
        spinnerCountry.setAdapter(adapterSpinnerCountry);
        spinnerState.setAdapter(adapterSpinnerState);
        spinnerCity.setAdapter(adapterSpinnerCity);
    }

    public void setOnItemSelectedCountry() {
        pDialog.show();
        spinnerCountry.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                id_country = countries.get(position).getID_COUNTRY_KEY();
                presenter.getStateForCountry(id_country);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    public void setOnItemSelectedState() {
        pDialog.show();
        spinnerState.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                id_state = states.get(position).getID_STATE_KEY();
                presenter.getCitiesForState(id_state);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    @Override
    public void setCountry(List<Country> countries) {
        this.countries = countries;
        adapterSpinnerCountry.clear();
        adapterSpinnerCountry.addAll(countries);
        adapterSpinnerCountry.notifyDataSetChanged();
        if (pDialog.isShowing())
            pDialog.hide();
    }

    @Override
    public void setState(List<State> states) {
        this.states = states;
        adapterSpinnerState.clear();
        adapterSpinnerState.addAll(states);
        adapterSpinnerState.notifyDataSetChanged();
        if (pDialog.isShowing())
            pDialog.hide();
    }

    @Override
    public void setCity(List<City> cities) {
        this.cities = cities;
        adapterSpinnerCity.clear();
        adapterSpinnerCity.addAll(cities);
        adapterSpinnerCity.notifyDataSetChanged();
        if (pDialog.isShowing())
            pDialog.hide();
    }

    @Override
    public void setError(String mgs) {
        if (pDialog.isShowing())
            pDialog.hide();
        Utils.showSnackBar(conteiner, mgs);
    }

    @OnClick(R.id.buttonInto)
    @Override
    public void savePlace() {
        if (spinnerCountry.getSelectedItem() == null && spinnerState.getSelectedItem() == null &&
                spinnerCity.getSelectedItem() == null)
            Utils.showSnackBar(conteiner, getString(R.string.error_fill_place));
        else {
            pDialog.show();
            country = (Country) spinnerCountry.getSelectedItem();
            state = (State) spinnerState.getSelectedItem();
            city = (City) spinnerCity.getSelectedItem();
            MyPlace myPlace = new MyPlace();
            myPlace.setID_PLACE_KEY(1);
            myPlace.setID_COUNTRY_FOREIGN(country.getID_COUNTRY_KEY());
            myPlace.setID_STATE_FOREIGN(state.getID_STATE_KEY());
            myPlace.setID_CITY_FOREIGN(city.getID_CITY_KEY());

            presenter.savePlace(this, myPlace);
        }
    }

    @Override
    public void saveSuccess(MyPlace place) {
        Utils.setIdCity(this, place.getID_CITY_FOREIGN());
        if (pDialog.isShowing())
            pDialog.hide();
        startActivity(new Intent(this, LoginActivity.class));
    }
}
