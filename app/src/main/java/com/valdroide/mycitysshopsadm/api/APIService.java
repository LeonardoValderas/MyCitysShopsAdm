package com.valdroide.mycitysshopsadm.api;

import com.valdroide.mycitysshopsadm.entities.response.ResponseWS;
import com.valdroide.mycitysshopsadm.entities.response.ResultDraw;
import com.valdroide.mycitysshopsadm.entities.response.ResultPlace;
import com.valdroide.mycitysshopsadm.entities.response.ResultShop;
import com.valdroide.mycitysshopsadm.entities.response.ResultUser;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;


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
                                    @Field("adrress") String adrress, @Field("date_unique") String date_unique, @Field("user_change") String user_change);

    //OFFER
    @FormUrlEncoded
    @POST("offer/insertOffer.php")
    Call<ResultPlace> insertOffer(@Field("id_shop_foreign") int id_shop, @Field("id_city_foreign") int id_city_foreign,
                                  @Field("title") String title, @Field("offer") String offer,
                                  @Field("url_image") String url_image, @Field("name_image") String name_image,
                                  @Field("date_unique") String date_unique, @Field("encode") String encode,
                                  @Field("user_change") String user_change);

    @FormUrlEncoded
    @POST("offer/updateOffer.php")
    Call<ResultPlace> updateOffer(@Field("id_offer") int id_offer, @Field("id_shop_foreign") int id_shop,
                                  @Field("id_city_foreign") int id_city_foreign, @Field("title") String title,
                                  @Field("offer") String offer, @Field("url_image") String url_image,
                                  @Field("name_image") String name_image, @Field("is_active") int is_active,
                                  @Field("date_unique") String date_edit, @Field("encode") String encode,
                                  @Field("name_before") String name_before, @Field("user_change") String user_change);

    @FormUrlEncoded
    @POST("offer/switchOffer.php")
    Call<ResultPlace> switchOffer(@Field("id_offer") int id_offer, @Field("id_shop_foreign") int id_shop,
                                  @Field("id_city_foreign") int id_city_foreign, @Field("is_active") int is_active,
                                  @Field("date_unique") String date_edit, @Field("user_change") String user_change);

//    @FormUrlEncoded
//    @POST("offer/deleteOffer.php")
//    Call<ResultPlace> deleteOffer(@Field("id_offer") int id_offer, @Field("id_shop_foreign") int id_shop,
//                                  @Field("id_city_foreign") int id_city, @Field("date_unique") String date_unique);

    @FormUrlEncoded
    @POST("offer/deleteOffer.php")
    Call<ResultPlace> deleteOffer(@Field("ids[]") List<Integer> ids, @Field("id_shop_foreign") int id_shop,
                                  @Field("id_city_foreign") int id_city, @Field("date_unique") String date_unique);

    //NOTIFICATION
    @FormUrlEncoded
    @POST("fcm/sendNotification.php")
    Call<ResultPlace> sendNotification(@Field("id_shop_foreign") int id_shop, @Field("id_city_foreign") int id_city,
                                       @Field("shop") String shop, @Field("message") String message,
                                       @Field("url_shop") String url_shop, @Field("date_end") String date_end,
                                       @Field("date_unique") String date_unique, @Field("user_change") String user_change);
    @FormUrlEncoded
    @POST("fcm/sendNotificationAdm.php")
    Call<ResponseWS> sendNotificationAdm(@Field("id_city_foreign") int id_city,
                                       @Field("message") String message);
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
                                      @Field("date_draw") String date_draw,
                                      @Field("date_shop_date") String date_user_date, @Field("date_support") String date_support);

    @FormUrlEncoded
    @POST("splash_user/getShopData.php")
    Call<ResultUser> getShopData(@Field("id_shop") int id_shop, @Field("id_city") int id_city);

    //LOGIN
    @FormUrlEncoded
    @POST("login/validateLogin.php")
    Call<ResultShop> validateLogin(@Field("user") String user, @Field("pass") String pass, @Field("id_city") int id_city);
    @FormUrlEncoded
    @POST("login/validateLoginAdm.php")
    Call<ResponseWS> validateLoginAdm(@Field("user") String user, @Field("pass") String pass, @Field("id_city") int id_city);

    //DRAW
    @FormUrlEncoded
    @POST("draw/createDraw.php")
    Call<ResponseWS> createDraw(@Field("id_shop_foreign") int id_shop, @Field("id_city_foreign") int id_city_foreign,
                                @Field("description") String description, @Field("for_following") int for_following,
                                @Field("condition") String condition,  @Field("start_date") String start_date,
                                @Field("end_date") String end_date, @Field("limit_date") String limit_date,
                                @Field("url_logo") String url_logo, @Field("name_logo") String name_logo,
                                @Field("date_unique") String date_unique, @Field("encode") String encode,
                                @Field("shop_name") String shop_name, @Field("user_change") String user_change);
    @FormUrlEncoded
    @POST("draw/cancelDraw.php")
    Call<ResponseWS> cancelDraw(@Field("id_shop_foreign") int id_shop, @Field("id_city_foreign") int id_city_foreign,
                                @Field("id_draw") int id_draw, @Field("is_cancel") int is_cancel,
                                @Field("is_take") int is_take,  @Field("is_limit") int is_limit, @Field("is_zero") int zero,
                                @Field("for_following") int for_following, @Field("shop_name") String shop_name,
                                @Field("date_unique") String date_unique, @Field("user_change") String user_change);

    @FormUrlEncoded
    @POST("draw/getWinnerDraw.php")
    Call<ResultDraw> getWinnerDraw(@Field("id_shop_foreign") int id_shop, @Field("id_city_foreign") int id_city_foreign,
                                   @Field("id_draw") int id_draw, @Field("shop_name") String shop_name,
                                   @Field("date_unique") String date_unique);

}
