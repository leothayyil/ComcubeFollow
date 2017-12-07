package com.example.user.comcubefollow.retrofit;

import android.content.Context;

import com.example.user.comcubefollow.LoginActivity;
import com.example.user.comcubefollow.MainActivity;

import java.sql.Time;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by USER on 07-12-2017.
 */

public class RetrofitHelper {

    private static APIs apIs;





    public RetrofitHelper(LoginActivity loginActivity) {
        initResstAdapter();
    }

    public RetrofitHelper(MainActivity mainActivity) {
        initResstAdapter();
    }

    public static APIs getApIs() {
        return apIs;
    }

    public static void setApIs(APIs apIs) {
        RetrofitHelper.apIs = apIs;
    }

    private void initResstAdapter(){

        Retrofit  retrofit = new Retrofit.Builder()
                .baseUrl("http://comcubecochin.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        setApIs(retrofit.create(APIs.class));
   }
}
