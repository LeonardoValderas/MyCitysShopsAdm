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
        Utils.writelogFile(this, "Se inicia ButterKnife(Place)");
        ButterKnife.bind(this);
        Utils.writelogFile(this, "Se inicia Injection(Place)");
        setupInjection();
        Utils.writelogFile(this, "Se inicia Dialog(Place)");
        initDialog();
        Utils.writelogFile(this, "Se inicia presenter Oncreate(Place)");
        presenter.onCreate();
        Utils.writelogFile(this, "Se inicia AdapterSpinner(Place)");
        initAdapterSpinner();
        Utils.writelogFile(this, "Se inicia setOnItemSelectedCountry(Place)");
        setOnItemSelectedCountry();
        Utils.writelogFile(this, "Se inicia setOnItemSelectedState(Place)");
        setOnItemSelectedState();
        Utils.writelogFile(this, "Se presenter.getCountries()(Place)");
        presenter.getCountries(this);
    }

    private void setupInjection() {
        MyCitysShopsAdmApp app = (MyCitysShopsAdmApp) getApplication();
        app.getPlaceActivityComponent(this, this).inject(this);
    }

    private void initDialog() {
        pDialog = new ProgressDialog(this);
        pDialog.setMessage(getString(R.string.processing));
        pDialog.setCancelable(false);
    }

    private void initAdapterSpinner() {
        try {
            spinnerCountry.setAdapter(adapterSpinnerCountry);
            spinnerState.setAdapter(adapterSpinnerState);
            spinnerCity.setAdapter(adapterSpinnerCity);
        } catch (Exception e) {
            Utils.writelogFile(this, "initAdapterSpinner error: " + e.getMessage() + "(Place)");
            setError(e.getMessage());
        }
    }

    private void setOnItemSelectedCountry() {
        try {
            spinnerCountry.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    id_country = countries.get(position).getID_COUNTRY_KEY();
                    presenter.getStateForCountry(PlaceActivity.this, id_country);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });
        } catch (Exception e) {
            Utils.writelogFile(this, "setOnItemSelectedCountry error: " + e.getMessage() + "(Place)");
            setError(e.getMessage());
        }
    }

    private void setOnItemSelectedState() {
        try {
            spinnerState.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    id_state = states.get(position).getID_STATE_KEY();
                    presenter.getCitiesForState(PlaceActivity.this, id_state);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });
        } catch (Exception e) {
            Utils.writelogFile(this, "setOnItemSelectedState error: " + e.getMessage() + "(Place)");
            setError(e.getMessage());
        }
    }

    @Override
    public void setCountry(List<Country> countries) {
        Utils.writelogFile(this, "Metodo setCountry se obtiene " + countries.size() + " Countries(Place)");
        this.countries = countries;
        Utils.writelogFile(this, "clear, addall, and notify adapterCountry(Place)");
        try {
            adapterSpinnerCountry.clear();
            adapterSpinnerCountry.addAll(countries);
            adapterSpinnerCountry.notifyDataSetChanged();
        } catch (Exception e) {
            Utils.writelogFile(this, "setCountry error: " + e.getMessage() + "(Place)");
            setError(e.getMessage());
        }
    }

    @Override
    public void setState(List<State> states) {
        Utils.writelogFile(this, "Metodo setState se obtiene " + states.size() + " States(Place)");
        this.states = states;
        Utils.writelogFile(this, "clear, addall, and notify adapterState(Place)");
        try {
            adapterSpinnerState.clear();
            adapterSpinnerState.addAll(states);
            adapterSpinnerState.notifyDataSetChanged();
            adapterSpinnerState.setStates(states);
        } catch (Exception e) {
            Utils.writelogFile(this, "setState error: " + e.getMessage() + "(Place)");
            setError(e.getMessage());
        }
        Utils.writelogFile(this, "id_state get(0) de la lista(Place)");
        id_state = states.get(0).getID_STATE_KEY();
        Utils.writelogFile(this, " presenter.getCitiesForState(id_state)(Place)");
        presenter.getCitiesForState(PlaceActivity.this, id_state);
        Utils.writelogFile(this, "dialog dismiss(Place)");
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    @Override
    public void setCity(List<City> cities) {
        Utils.writelogFile(this, "Metodo setCity se obtiene " + cities.size() + " cities(Place)");
        this.cities = cities;
        Utils.writelogFile(this, "clear, addall, and notify adapterCity(Place)");
        try {
            adapterSpinnerCity.clear();
            adapterSpinnerCity.addAll(cities);
            adapterSpinnerCity.notifyDataSetChanged();
        } catch (Exception e) {
            Utils.writelogFile(this, "setCity error: " + e.getMessage() + "(Place)");
            setError(e.getMessage());
        }
    }

    @Override
    public void setError(String mgs) {
        Utils.writelogFile(this, "Error, dialog dismiss: " + mgs + "(Place)");
        Utils.showSnackBar(conteiner, mgs);
    }

    @OnClick(R.id.buttonInto)
    @Override
    public void savePlace() {
        Utils.writelogFile(this, "Metodo savePlace on click buttonInto(Place)");
        if (spinnerCountry.getSelectedItem() == null && spinnerState.getSelectedItem() == null &&
                spinnerCity.getSelectedItem() == null) {
            Utils.writelogFile(this, "Spinner null(Place)");
            Utils.showSnackBar(conteiner, getString(R.string.error_fill_place));
        } else {
            Utils.writelogFile(this, "dialog show(Place)");
            showProgressDialog();
            Utils.writelogFile(this, "cast country spinner(Place)");
            MyPlace myPlace = null;
            try {
                country = (Country) spinnerCountry.getSelectedItem();
                state = (State) spinnerState.getSelectedItem();
                city = (City) spinnerCity.getSelectedItem();
                Utils.writelogFile(this, "create and fill MyPlace object(Place)");
                myPlace = new MyPlace();
                myPlace.setID_PLACE_KEY(1);
                myPlace.setID_COUNTRY_FOREIGN(country.getID_COUNTRY_KEY());
                myPlace.setID_STATE_FOREIGN(state.getID_STATE_KEY());
                myPlace.setID_CITY_FOREIGN(city.getID_CITY_KEY());
            } catch (Exception e) {
                Utils.writelogFile(this, "setCity error: " + e.getMessage() + "(Place)");
                setError(e.getMessage());
            }
            Utils.writelogFile(this, "call presenter.savePlace(this, myPlace)(Place)");
            presenter.savePlace(this, myPlace);
        }
    }

    @Override
    public void saveSuccess(MyPlace place) {
        Utils.writelogFile(this, "Metodo saveSuccess(Place)");
        try {
            if (place != null) {
                Utils.writelogFile(this, "place != null y setIdCity shared(Place)");
                Utils.setIdCity(this, place.getID_CITY_FOREIGN());
                Utils.writelogFile(this, "dialog dismiss(Place)");
                Utils.writelogFile(this, "Intent Login(Place)");
                startActivity(new Intent(this, LoginActivity.class));
            } else {
                Utils.writelogFile(this, "place == null(Place)");
                setError("Error al guardar la ciudad.");
            }
        } catch (Exception e) {
            Utils.writelogFile(this, "saveSuccess " + e.getMessage() + "(Place)");
            setError(e.getMessage());
        }
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
        Utils.writelogFile(this, "Ondestroy(Place)");
        super.onDestroy();
    }
}
