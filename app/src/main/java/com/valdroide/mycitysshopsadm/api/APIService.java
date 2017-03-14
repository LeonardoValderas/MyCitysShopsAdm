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
//    @FormUrlEncoded
//    @POST("account/insertAccount.php")
//    Call<ResultPlace> insertAccount(@Field("id_shop_foreign") int id_shop, @Field("shop_name") String shop_name, @Field("encode") String encode,
//                                    @Field("url_logo") String url_logo, @Field("name_logo") String name_logo,
//                                    @Field("description") String description, @Field("phone") String phone, @Field("email") String email,
//                                    @Field("latitud") String latitud, @Field("longitud") String longitud, @Field("adrress") String adrress);

//    @FormUrlEncoded
//    @POST("account/updateAccount.php")
//    Call<ResultPlace> updateAccount(@Field("id_account") int id_account, @Field("id_shop_foreign") int id_shop, @Field("shop_name") String shop_name, @Field("encode") String encode,
//                                    @Field("url_logo") String url_logo, @Field("name_logo") String name_logo, @Field("name_before") String name_before,
//                                    @Field("description") String description, @Field("phone") String phone, @Field("email") String email,
//                                    @Field("latitud") String latitud, @Field("longitud") String longitud, @Field("adrress") String adrress);
    @FormUrlEncoded
    @POST("account/updateAccount.php")
    Call<ResultPlace> updateAccount(@Field("id_shop_foreign") int id_shop, @Field("id_account") int id_account, @Field("encode") String encode,
                                    @Field("url_logo") String url_logo, @Field("name_logo") String name_logo, @Field("name_before") String name_before,
                                    @Field("description") String description, @Field("phone") String phone, @Field("email") String email,
                                    @Field("latitud") String latitud, @Field("longitud") String longitud, @Field("adrress") String adrress, @Field("date_init") String date_init);
    //OFFER
    @FormUrlEncoded
    @POST("offer/insertOffer.php")
    Call<ResultPlace> insertOffer(@Field("id_shop_foreign") int id_shop, @Field("title") String title, @Field("offer") String offer,
                                  @Field("date_init") String date_init, @Field("date_end") String date_end, @Field("date_update") String date_update);

    @FormUrlEncoded
    @POST("offer/updateOffer.php")
    Call<ResultPlace> updateOffer(@Field("id_offer") int id_offer, @Field("id_shop_foreign") int id_shop, @Field("title") String title, @Field("offer") String offer,
                                  @Field("date_edit") String date_edit, @Field("date_update") String date_update);

    @FormUrlEncoded
    @POST("offer/deleteOffer.php")
    Call<ResultPlace> deleteOffer(@Field("id_offer") int id_offer, @Field("id_shop_foreign") int id_shop, @Field("date_edit") String date_edit, @Field("date_update") String date_update);

    //NOTIFICATION
    @FormUrlEncoded
    @POST("fcm/sendNotification.php")
    Call<ResultPlace> sendNotification(@Field("id_shop_foreign") int id_shop, @Field("title") String title, @Field("message") String message, @Field("date_init") String date_init);

    //SPLASH PLACE
    @FormUrlEncoded
    @POST("splash/validateDatePlace.php")
    Call<ResultPlace> validateDatePlace(@Field("date_country") String date_country, @Field("date_state") String date_state, @Field("date_city") String date_city, @Field("date_place") String date_place);

    @GET("splash/getPlace.php")
    Call<ResultPlace> getPlace();
    //SPLASH USER
    @FormUrlEncoded
    @POST("splash_user/validateDateShop.php")
    Call<ResultUser> validateDateShop(@Field("id_shop") int id_shop, @Field("date_account") String date_account, @Field("date_offer") String date_offer, @Field("date_notification") String date_notification, @Field("date_user_date") String date_user_date);

    @FormUrlEncoded
    @POST("splash_user/getShopData.php")
    Call<ResultUser> getShopData(@Field("id_shop") int id_shop);

    //LOGIN
    @FormUrlEncoded
    @POST("login/validateLogin.php")
    Call<ResultShop> validateLogin(@Field("user") String user, @Field("pass") String pass, @Field("id_city") int id_city);

    /*
    @GET("md")
    Call<List<Category>> getCategories();

    //CATEGORY
    @FormUrlEncoded
    @POST("category/insertCategory.php")
    Call<ResultPlace> insertCategory(@Field("category") String category, @Field("table_date") String table_date);

    @FormUrlEncoded
    @POST("category/updateCategory.php")
    Call<ResultPlace> updateCategory(@Field("id_category") int id_category, @Field("category") String category, @Field("table_date") String table_date);

    @FormUrlEncoded
    @POST("category/deleteCategory.php")
    Call<ResultPlace> deleteCategory(@Field("id_category") int id_category, @Field("table_date") String table_date);

    //SUBCATEGORY
    @FormUrlEncoded
    @POST("subcategory/insertSubCategory.php")
    Call<ResultPlace> insertSubCategory(@Field("subcategory") String subcategory, @Field("id_category") int id_category, @Field("table_date") String table_date);

    @FormUrlEncoded
    @POST("subcategory/updateSubCategory.php")
    Call<ResultPlace> updateSubCategory(@Field("id_subcategory") int id_subcategory, @Field("subcategory") String subcategory, @Field("id_category") int id_category, @Field("table_date") String table_date);

    @FormUrlEncoded
    @POST("subcategory/deleteSubCategory.php")
    Call<ResultPlace> deleteSubCategory(@Field("id_subcategory") int id_subcategory, @Field("table_date") String table_date);

    //CLOTHES
    @FormUrlEncoded
    @POST("clothes/insertClothes.php")
    Call<ResultPlace> insertClothes(@Field("id_category") int id_category, @Field("id_subcategory") int id_subcategory, @Field("url_photo") String url_photo, @Field("name_photo") String name_photo, @Field("description") String description, @Field("is_active") int is_active, @Field("encode") String encode, @Field("table_date") String table_date);

    @FormUrlEncoded
    @POST("clothes/updateClothes.php")
    Call<ResultPlace> updateClothes(@Field("id_clothes") int id_clothes, @Field("id_category") int id_category, @Field("id_subcategory") int id_subcategory, @Field("url_photo") String url_photo, @Field("name_photo") String name_photo, @Field("description") String description, @Field("is_active") int is_active, @Field("encode") String encode, @Field("name_before") String name_before, @Field("table_date") String table_date);

    @FormUrlEncoded
    @POST("clothes/deleteClothes.php")
    Call<ResultPlace> deleteClothes(@Field("id_clothes") int id_clothes, @Field("name_photo") String name_photo, @Field("table_date") String table_date);

    @FormUrlEncoded
    @POST("clothes/updateActiveClothes.php")
    Call<ResultPlace> updateActiveClothes(@Field("id_clothes") int id_clothes, @Field("is_active") int is_active, @Field("table_date") String table_date);

    //NOTIFICATION
    @FormUrlEncoded
    @POST("fcm/sendNotification.php")
    Call<ResultPlace> sendNotification(@Field("title") String title, @Field("message") String message);

    //SPLASH
    @FormUrlEncoded
    @POST("splash/splash.php")
    Call<ResultPlace> sendDateTable(@Field("date_table") String date_table, @Field("category") String category, @Field("subcategory") String subcategory, @Field("clothes") String clothes);
    //@FormUrlEncoded
    @POST("splash/splash.php")
    Call<ResultPlace> getAllData();

*/
}
