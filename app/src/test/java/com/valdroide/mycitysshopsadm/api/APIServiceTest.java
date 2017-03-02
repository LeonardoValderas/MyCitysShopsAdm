package com.valdroide.mycitysshopsadm.api;

import android.test.mock.MockContentResolver;

import com.valdroide.mycitysshopsadm.BaseTest;
import com.valdroide.mycitysshopsadm.entities.response.Result;

import org.junit.Test;
import org.junit.runner.RunWith;

import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertNotEquals;

/**
 * Created by LEO on 22/2/2017.
 */
@RunWith(RobolectricTestRunner.class)
@Config(manifest=Config.NONE)
public class APIServiceTest extends BaseTest {
    private APIService service;

    @Override
    public void setUp() throws IOException {
        super.setUp();
        ShopClient client = new ShopClient();
        service = client.getAPIService();
    }

    @Test
    public void insertAccountTest() throws IOException {
        int id_shop = 1;
        //String shop_name = "Name";
        String shop_name = "";
        String encode = "ksksksksk";
        String url_logo = "http//";
        String name_logo = "NameLogo";
        String description = "Description";
        String phone = "Phone";
        String email = "Email";
        String latitud = "Latitud";
        String longitud = "Longitud";
        String adrress = "Address";
        final boolean[] isOK = new boolean[1];
        Call<Result> accountService = service.insertAccount(id_shop, shop_name, encode, url_logo, name_logo, description, phone, email, latitud, longitud, adrress);
//        accountService.enqueue(new Callback<Result>() {
//            @Override
//            public void onResponse(Call<Result> call, Response<Result> response) {
//                isOK[0] = response.isSuccessful();
//            }
//
//            @Override
//            public void onFailure(Call<Result> call, Throwable t) {
//
//            }
//        });


        Response<Result> response = accountService.execute();
 //       assertFalse(isOK[0]);
       // ResponseWS responseWS = response.body().getResponseWS();
        //assertNotNull(responseWS);
        //assertEquals("0", responseWS.getSuccess());
       // assertNotEquals(0, responseWS.getId());

    }
}

