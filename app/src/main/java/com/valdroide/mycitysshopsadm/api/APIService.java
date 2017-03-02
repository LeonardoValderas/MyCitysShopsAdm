package com.valdroide.mycitysshopsadm.api;

import com.valdroide.mycitysshopsadm.entities.response.ResponseWS;
import com.valdroide.mycitysshopsadm.entities.response.Result;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;


public interface APIService {

    //ACCOUNT
    @FormUrlEncoded
    @POST("account/insertAccount.php")
    Call<Result> insertAccount(@Field("id_shop_foreign") int id_shop, @Field("shop_name") String shop_name, @Field("encode") String encode,
                               @Field("url_logo") String url_logo, @Field("name_logo") String name_logo,
                               @Field("description") String description, @Field("phone") String phone, @Field("email") String email,
                               @Field("latitud") String latitud, @Field("longitud") String longitud, @Field("adrress") String adrress);

    @FormUrlEncoded
    @POST("account/updateAccount.php")
    Call<Result> updateAccount(@Field("id_account") int id_account, @Field("id_shop_foreign") int id_shop, @Field("shop_name") String shop_name, @Field("encode") String encode,
                               @Field("url_logo") String url_logo, @Field("name_logo") String name_logo, @Field("name_before") String name_before,
                               @Field("description") String description, @Field("phone") String phone, @Field("email") String email,
                               @Field("latitud") String latitud, @Field("longitud") String longitud, @Field("adrress") String adrress);

    //OFFER
    @FormUrlEncoded
    @POST("offer/insertOffer.php")
    Call<Result> insertOffer(@Field("id_shop_foreign") int id_shop, @Field("title") String title, @Field("offer") String offer,
                             @Field("date_init") String date_init, @Field("date_end") String date_end);

    @FormUrlEncoded
    @POST("offer/updateOffer.php")
    Call<Result> updateOffer(@Field("id_offer") int id_offer, @Field("id_shop_foreign") int id_shop, @Field("title") String title, @Field("offer") String offer,
                             @Field("date_edit") String date_edit);

    @FormUrlEncoded
    @POST("offer/deleteOffer.php")
    Call<Result> deleteOffer(@Field("id_offer") int id_offer);

    //NOTIFICATION
    @FormUrlEncoded
    @POST("fcm/sendNotification.php")
    Call<Result> sendNotification(@Field("title") String title, @Field("message") String message);

    //SPLASH
    @FormUrlEncoded
    @POST("splash/validateDatePlace.php")
    Call<Result> validateDatePlace(@Field("date_country") String date_country, @Field("date_state") String date_state, @Field("date_city") String date_city, @Field("date_place") String date_place);

  //  @FormUrlEncoded
    @GET("splash/getPlace.php")
    Call<Result> getPlace();

    /*
    @GET("md")
    Call<List<Category>> getCategories();

    //CATEGORY
    @FormUrlEncoded
    @POST("category/insertCategory.php")
    Call<Result> insertCategory(@Field("category") String category, @Field("table_date") String table_date);

    @FormUrlEncoded
    @POST("category/updateCategory.php")
    Call<Result> updateCategory(@Field("id_category") int id_category, @Field("category") String category, @Field("table_date") String table_date);

    @FormUrlEncoded
    @POST("category/deleteCategory.php")
    Call<Result> deleteCategory(@Field("id_category") int id_category, @Field("table_date") String table_date);

    //SUBCATEGORY
    @FormUrlEncoded
    @POST("subcategory/insertSubCategory.php")
    Call<Result> insertSubCategory(@Field("subcategory") String subcategory, @Field("id_category") int id_category, @Field("table_date") String table_date);

    @FormUrlEncoded
    @POST("subcategory/updateSubCategory.php")
    Call<Result> updateSubCategory(@Field("id_subcategory") int id_subcategory, @Field("subcategory") String subcategory, @Field("id_category") int id_category, @Field("table_date") String table_date);

    @FormUrlEncoded
    @POST("subcategory/deleteSubCategory.php")
    Call<Result> deleteSubCategory(@Field("id_subcategory") int id_subcategory, @Field("table_date") String table_date);

    //CLOTHES
    @FormUrlEncoded
    @POST("clothes/insertClothes.php")
    Call<Result> insertClothes(@Field("id_category") int id_category, @Field("id_subcategory") int id_subcategory, @Field("url_photo") String url_photo, @Field("name_photo") String name_photo, @Field("description") String description, @Field("is_active") int is_active, @Field("encode") String encode, @Field("table_date") String table_date);

    @FormUrlEncoded
    @POST("clothes/updateClothes.php")
    Call<Result> updateClothes(@Field("id_clothes") int id_clothes, @Field("id_category") int id_category, @Field("id_subcategory") int id_subcategory, @Field("url_photo") String url_photo, @Field("name_photo") String name_photo, @Field("description") String description, @Field("is_active") int is_active, @Field("encode") String encode, @Field("name_before") String name_before, @Field("table_date") String table_date);

    @FormUrlEncoded
    @POST("clothes/deleteClothes.php")
    Call<Result> deleteClothes(@Field("id_clothes") int id_clothes, @Field("name_photo") String name_photo, @Field("table_date") String table_date);

    @FormUrlEncoded
    @POST("clothes/updateActiveClothes.php")
    Call<Result> updateActiveClothes(@Field("id_clothes") int id_clothes, @Field("is_active") int is_active, @Field("table_date") String table_date);

    //NOTIFICATION
    @FormUrlEncoded
    @POST("fcm/sendNotification.php")
    Call<Result> sendNotification(@Field("title") String title, @Field("message") String message);

    //SPLASH
    @FormUrlEncoded
    @POST("splash/splash.php")
    Call<Result> sendDateTable(@Field("date_table") String date_table, @Field("category") String category, @Field("subcategory") String subcategory, @Field("clothes") String clothes);
    //@FormUrlEncoded
    @POST("splash/splash.php")
    Call<Result> getAllData();

*/
}
