package com.valdroide.mycitysshopsadm.api;

import com.valdroide.mycitysshopsadm.entities.response.ResultPlace;
import com.valdroide.mycitysshopsadm.entities.response.ResultShop;
import com.valdroide.mycitysshopsadm.entities.response.ResultUser;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;


public interface APIService {

    //ACCOUNT
    @FormUrlEncoded
    @POST("account/updateAccount.php")
    Call<ResultPlace> updateAccount(@Field("id_shop_foreign") int id_shop, @Field("id_city_foreign") int id_city_foreign,
                                    @Field("id_account") int id_account, @Field("encode") String encode, @Field("url_logo") String url_logo,
                                    @Field("name_logo") String name_logo, @Field("name_before") String name_before,
                                    @Field("description") String description, @Field("working") String working,
                                    @Field("phone") String phone, @Field("email") String email,
                                    @Field("web") String web, @Field("whatsaap") String whatsaap,
                                    @Field("facebook") String facebook, @Field("instagram") String instagram,
                                    @Field("twitter") String twitter, @Field("snapchat") String snapchat,
                                    @Field("latitud") String latitud, @Field("longitud") String longitud,
                                    @Field("adrress") String adrress, @Field("date_unique") String date_unique);

    //OFFER
    @FormUrlEncoded
    @POST("offer/insertOffer.php")
    Call<ResultPlace> insertOffer(@Field("id_shop_foreign") int id_shop, @Field("id_city_foreign") int id_city_foreign,
                                  @Field("title") String title, @Field("offer") String offer,
                                  @Field("url_image") String url_image, @Field("name_image") String name_image,
                                  @Field("date_unique") String date_unique, @Field("encode") String encode);

    @FormUrlEncoded
    @POST("offer/updateOffer.php")
    Call<ResultPlace> updateOffer(@Field("id_offer") int id_offer, @Field("id_shop_foreign") int id_shop,
                                  @Field("id_city_foreign") int id_city_foreign, @Field("title") String title,
                                  @Field("offer") String offer, @Field("url_image") String url_image,
                                  @Field("name_image") String name_image, @Field("is_active") int is_active,
                                  @Field("date_unique") String date_edit, @Field("encode") String encode,
                                  @Field("name_before") String name_before);

    @FormUrlEncoded
    @POST("offer/switchOffer.php")
    Call<ResultPlace> switchOffer(@Field("id_offer") int id_offer, @Field("id_shop_foreign") int id_shop,
                                  @Field("id_city_foreign") int id_city_foreign, @Field("is_active") int is_active,
                                  @Field("date_unique") String date_edit);

    @FormUrlEncoded
    @POST("offer/deleteOffer.php")
    Call<ResultPlace> deleteOffer(@Field("id_offer") int id_offer, @Field("id_shop_foreign") int id_shop,
                                  @Field("id_city_foreign") int id_city, @Field("date_unique") String date_unique);

    //NOTIFICATION
    @FormUrlEncoded
    @POST("fcm/sendNotification.php")
    Call<ResultPlace> sendNotification(@Field("id_shop_foreign") int id_shop, @Field("id_city_foreign") int id_city,
                                       @Field("shop") String shop, @Field("message") String message,
                                       @Field("url_shop") String url_shop, @Field("date_end") String date_end,
                                       @Field("date_unique") String date_unique);

    //SPLASH PLACE
    @FormUrlEncoded
    @POST("splash/validateDatePlace.php")
    Call<ResultPlace> validateDatePlace(@Field("date_country") String date_country, @Field("date_state") String date_state,
                                        @Field("date_city") String date_city, @Field("date_place") String date_place);

    @GET("splash/getPlace.php")
    Call<ResultPlace> getPlace();

    //SPLASH USER
    @FormUrlEncoded
    @POST("splash_user/validateDateShop.php")
    Call<ResultUser> validateDateShop(@Field("id_shop") int id_shop, @Field("id_city") int id_city,
                                      @Field("date_account") String date_account,
                                      @Field("date_offer") String date_offer,
                                      @Field("date_notification") String date_notification,
                                      @Field("date_shop_date") String date_user_date);

    @FormUrlEncoded
    @POST("splash_user/getShopData.php")
    Call<ResultUser> getShopData(@Field("id_shop") int id_shop, @Field("id_city") int id_city);

    //LOGIN
    @FormUrlEncoded
    @POST("login/validateLogin.php")
    Call<ResultShop> validateLogin(@Field("user") String user, @Field("pass") String pass, @Field("id_city") int id_city);

}
