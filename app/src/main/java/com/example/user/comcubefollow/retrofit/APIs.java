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

    @FormUrlEncoded
    @POST("web-api/api-comcube.php")
    Call<JsonElement>login(@Field("user_name") String user_name, @Field("password")
            String password, @Field("pos_lat") String pos_lat, @Field("pos_long") String pos_long,@Field("action") String login);

    @FormUrlEncoded
    @POST("/web-api/api-comcube.php")
    Call<JsonElement>logout(@Field("action") String shopName,  @Field("user_id") String user_id,@Field("login_id")String loginId,
                              @Field("pos_lat") String pos_lat, @Field("pos_long") String pos_lon);

    @FormUrlEncoded
    @POST("/web-api/api-comcube.php")
    Call<JsonElement>personUpdate(@Field("action") String action, @Field("user_id") String user_id,
                            @Field("pos_lat") String pos_lat, @Field("pos_long") String pos_lon, @Field("contact_phone")
                                          String phone,@Field("contact_name") String contact_name,@Field("contact_email")
                                          String contact_email,@Field("feedback")String feedback);
    @FormUrlEncoded
    @POST("/web-api/api-comcube.php")
    Call<JsonElement>visitUpdate(@Field("action") String action, @Field("user_id") String user_id,
                                  @Field("pos_lat") String pos_lat, @Field("pos_long") String pos_lon,
                                 @Field("shop_name") String shopName,@Field("shop_phone") String shopPhone);
    @FormUrlEncoded
    @POST("/web-api/api-comcube.php")
    Call<JsonElement>feedbackUpdate(@Field("action") String action, @Field("user_id") String user_id,
                                  @Field("pos_lat") String pos_lat, @Field("pos_long") String pos_lon, @Field("shop_id") String shopId
            , @Field("shop_phone")String phone, @Field("contact_email")String email,@Field("feedback") String feedback);

}
