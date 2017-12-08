package com.example.user.comcubefollow.retrofit;

import com.google.gson.JsonElement;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by USER on 07-12-2017.
 */

public interface APIs {

    @GET("web-api/user_login.php?")
    Call<JsonElement>login(@Query("user_name") String user_name, @Query("password")
            String password, @Query("pos_lat") String pos_lat, @Query("pos_long") String pos_long);

    @FormUrlEncoded
    @POST("/web-api/insert_shop.php?")
    Call<JsonElement>logout(@Field("shop_name") String shopName, @Field("phone")String phone, @Field("user_id") String user_id,
                              @Field("pos_lat") String pos_lat, @Field("pos_long") String pos_lon, @Field("feedback") String feedback);

    @FormUrlEncoded
    @POST("/web-api/insert_shop.php?")
    Call<JsonElement>personUpdate(@Field("shop_name") String name, @Field("phone")String email, @Field("user_id") String user_id,
                            @Field("pos_lat") String pos_lat, @Field("pos_long") String pos_lon, @Field("feedback") String feedback);

}
