package com.valdroide.mycitysshopsadm.main.offer;

import android.content.Context;

import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.valdroide.mycitysshopsadm.api.APIService;
import com.valdroide.mycitysshopsadm.entities.response.ResultPlace;
import com.valdroide.mycitysshopsadm.entities.user.Offer;
import com.valdroide.mycitysshopsadm.entities.response.ResponseWS;
import com.valdroide.mycitysshopsadm.lib.base.EventBus;
import com.valdroide.mycitysshopsadm.main.account.events.AccountActivityEvent;
import com.valdroide.mycitysshopsadm.main.offer.events.OfferActivityEvent;
import com.valdroide.mycitysshopsadm.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OfferActivityRepositoryImpl implements OfferActivityRepository {
    private EventBus eventBus;
    private APIService service;
    private List<Offer> offers;
    private ResponseWS responseWS;
    int id;

    public OfferActivityRepositoryImpl(EventBus eventBus, APIService service) {
        this.eventBus = eventBus;
        this.service = service;
    }

    @Override
    public void getOffer(Context context) {
        try {
            offers = SQLite.select().from(Offer.class).queryList();
            if (offers.size() > 0) {
                String dateEnd = offers.get(0).getDATE_END();
                if (dateEnd != null) {
                    if (!dateEnd.isEmpty()) {
                        if (validateExpirateOffer(dateEnd)) {
                            for (Offer offer : offers) {
                                deleteOffer(context, offer, false);
                            }
                            post(OfferActivityEvent.GETOFFER, new ArrayList<Offer>());
                        } else {
                            post(OfferActivityEvent.GETOFFER, offers);
                        }
                    } else {
                        post(OfferActivityEvent.ERROR, Utils.ERROR_OFFER_VALIDATE);
                    }
                } else {
                    post(OfferActivityEvent.ERROR, Utils.ERROR_OFFER_VALIDATE);
                }
            } else {
                post(OfferActivityEvent.GETOFFER, offers);
            }
        } catch (Exception e) {
            post(AccountActivityEvent.ERROR, e.getMessage());
        }
    }

    @Override
    public void saveOffer(Context context, final Offer offer) {
        if (Utils.isNetworkAvailable(context)) {
            try {
                Call<ResultPlace> offerService = service.insertOffer(offer.getID_USER_FOREIGN(),
                        offer.getTITLE(), offer.getOFFER(),
                        offer.getDATE_INIT(), offer.getDATE_END());
                offerService.enqueue(new Callback<ResultPlace>() {
                    @Override
                    public void onResponse(Call<ResultPlace> call, Response<ResultPlace> response) {
                        if (response.isSuccessful()) {
                            responseWS = response.body().getResponseWS();
                            if (responseWS != null) {
                                if (responseWS.getSuccess().equals("0")) {
                                    id = responseWS.getId();
                                    if (id != 0) {
                                        offer.setID_OFFER_KEY(id);
                                        offer.save();
                                        post(OfferActivityEvent.SAVEOFFER, offer);
                                    } else {
                                        post(OfferActivityEvent.ERROR, Utils.ERROR_DATA_BASE);
                                    }
                                } else {
                                    post(OfferActivityEvent.ERROR, responseWS.getMessage());
                                }
                            } else {
                                post(OfferActivityEvent.ERROR, Utils.ERROR_DATA_BASE);
                            }
                        } else {
                            post(OfferActivityEvent.ERROR, Utils.ERROR_DATA_BASE);
                        }
                    }

                    @Override
                    public void onFailure(Call<ResultPlace> call, Throwable t) {
                        post(OfferActivityEvent.ERROR, t.getMessage());
                    }
                });
            } catch (Exception e) {
                post(OfferActivityEvent.ERROR, e.getMessage());
            }
        } else {
            post(OfferActivityEvent.ERROR, Utils.ERROR_INTERNET);
        }
    }

    @Override
    public void updateOffer(Context context, final Offer offer) {
        if (Utils.isNetworkAvailable(context)) {
            try {
                Call<ResultPlace> offerService = service.updateOffer(offer.getID_OFFER_KEY(), offer.getID_USER_FOREIGN(),
                        offer.getTITLE(), offer.getOFFER(),
                        offer.getDATE_EDIT());
                offerService.enqueue(new Callback<ResultPlace>() {
                    @Override
                    public void onResponse(Call<ResultPlace> call, Response<ResultPlace> response) {
                        if (response.isSuccessful()) {
                            responseWS = response.body().getResponseWS();
                            if (responseWS.getSuccess().equals("0")) {
                                offer.update();
                                post(OfferActivityEvent.UPDATEOFFER, offer);
                            } else {
                                post(OfferActivityEvent.ERROR, responseWS.getMessage());
                            }
                        } else {
                            post(OfferActivityEvent.ERROR, Utils.ERROR_DATA_BASE);
                        }
                    }

                    @Override
                    public void onFailure(Call<ResultPlace> call, Throwable t) {
                        post(OfferActivityEvent.ERROR, t.getMessage());
                    }
                });
            } catch (Exception e) {
                post(OfferActivityEvent.ERROR, e.getMessage());
            }
        } else {
            post(OfferActivityEvent.ERROR, Utils.ERROR_INTERNET);
        }
    }

    @Override
    public void deleteOffer(Context context, final Offer offer, final boolean isDelete) {
        if (Utils.isNetworkAvailable(context)) {
            try {
                Call<ResultPlace> offerService = service.deleteOffer(offer.getID_OFFER_KEY(), offer.getID_USER_FOREIGN(), offer.getDATE_EDIT());
                offerService.enqueue(new Callback<ResultPlace>() {
                    @Override
                    public void onResponse(Call<ResultPlace> call, Response<ResultPlace> response) {
                        if (response.isSuccessful()) {
                            responseWS = response.body().getResponseWS();
                            if (responseWS.getSuccess().equals("0")) {
                                offer.delete();
                                if (isDelete)
                                    post(OfferActivityEvent.DELETEOFFER, offer);
                            } else {
                                post(OfferActivityEvent.ERROR, responseWS.getMessage());
                            }
                        } else {
                            post(OfferActivityEvent.ERROR, Utils.ERROR_DATA_BASE);
                        }
                    }

                    @Override
                    public void onFailure(Call<ResultPlace> call, Throwable t) {
                        post(OfferActivityEvent.ERROR, t.getMessage());
                    }
                });
            } catch (Exception e) {
                post(OfferActivityEvent.ERROR, e.getMessage());
            }
        } else {
            post(OfferActivityEvent.ERROR, Utils.ERROR_INTERNET);
        }
    }

    public boolean validateExpirateOffer(String dateEnd) {
        return Utils.validateExpirateOffer(dateEnd);
    }

    public void post(int type) {
        post(type, null, null, null);
    }

    public void post(int type, Offer offer) {
        post(type, offer, null, null);
    }

    public void post(int type, List<Offer> offers) {
        post(type, null, offers, null);
    }

    public void post(int type, String error) {
        post(type, null, null, error);
    }

    public void post(int type, Offer offer, List<Offer> offers, String error) {
        OfferActivityEvent event = new OfferActivityEvent();
        event.setType(type);
        event.setOffer(offer);
        event.setOffers(offers);
        event.setError(error);
        eventBus.post(event);
    }
}
