package com.valdroide.mycitysshopsadm.main.place.ui;

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
import com.valdroide.mycitysshopsadm.entities.place.City;
import com.valdroide.mycitysshopsadm.entities.place.Country;
import com.valdroide.mycitysshopsadm.entities.place.State;
import com.valdroide.mycitysshopsadm.main.place.PlaceActivityPresenter;
import com.valdroide.mycitysshopsadm.main.place.ui.adapters.AdapterSpinnerCity;
import com.valdroide.mycitysshopsadm.main.place.ui.adapters.AdapterSpinnerCountry;
import com.valdroide.mycitysshopsadm.main.place.ui.adapters.AdapterSpinnerState;
import com.valdroide.mycitysshopsadm.utils.Utils;

import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;

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
    private int id_country = 0, id_state = 0;

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
        initAdapterSpinner();
        setOnItemSelectedCountry();
        setOnItemSelectedState();
        presenter.getCountries();
    }

    private void setupInjection() {
        MyCitysShopsAdmApp app = (MyCitysShopsAdmApp) getApplication();
        app.getPlaceActivityComponent(this, this).inject(this);
    }

    public void initAdapterSpinner() {
        spinnerCountry.setAdapter(adapterSpinnerCountry);
        spinnerState.setAdapter(adapterSpinnerState);
        spinnerCity.setAdapter(adapterSpinnerCity);
    }

    public void setOnItemSelectedCountry() {
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
    }

    @Override
    public void setState(List<State> states) {
        this.states = states;
        adapterSpinnerState.clear();
        adapterSpinnerState.addAll(states);
        adapterSpinnerState.notifyDataSetChanged();
    }

    @Override
    public void setCity(List<City> cities) {
        this.cities = cities;
        adapterSpinnerCity.clear();
        adapterSpinnerCity.addAll(cities);
        adapterSpinnerCity.notifyDataSetChanged();
    }

    @Override
    public void setError(String mgs) {
        Utils.showSnackBar(conteiner, mgs);
    }
}
