package com.valdroide.mycitysshopsadm.main.place.ui;

import com.valdroide.mycitysshopsadm.entities.user.MyPlace;
import com.valdroide.mycitysshopsadm.entities.place.City;
import com.valdroide.mycitysshopsadm.entities.place.Country;
import com.valdroide.mycitysshopsadm.entities.place.State;

import java.util.List;

public interface PlaceActivityView {
    void setCountry(List<Country> countries);
    void setState(List<State> states);
    void setCity(List<City> cities);
    void setError(String mgs);
    void savePlace();
    void saveSuccess(MyPlace place);
}
