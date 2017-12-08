package com.example.user.comcubefollow;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.Toast;

import com.example.user.comcubefollow.retrofit.RetrofitHelper;
import com.google.gson.JsonElement;

import org.json.JSONException;
import org.json.JSONObject;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements LocationListener {

    CardView shopCard, personCard, finishCard;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    String userId;
    String shopname = "logout";
    String phone = "logout";
    String lat = "lat";
    String lon = "lon";
    String feedb = "logout";
    LocationManager locationManager;
    String provider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        personCard = findViewById(R.id.person_view);
        shopCard = findViewById(R.id.shop_card);
        finishCard = findViewById(R.id.finish_view);


        Toasty.Config.getInstance()
                .setErrorColor(ContextCompat.getColor(getApplicationContext(), R.color.red))
                .setInfoColor(ContextCompat.getColor(getApplicationContext(), R.color.blue))
                .setSuccessColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary))
                .setWarningColor(ContextCompat.getColor(getApplicationContext(), R.color.gray))
                .setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.white))
                .tintIcon(true)
                .apply();
        preferences = this.getSharedPreferences("user_id", 0);
        editor = preferences.edit();
        userId = preferences.getString("user_id_preff", "0");

        if (userId == "0") {
            Intent logIntent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(logIntent);
        }

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        Criteria criteria = new Criteria();
        provider = locationManager.getBestProvider(criteria, false);

        if (provider != null && !provider.equals("")) {

            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) !=
                    PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            Location location = locationManager.getLastKnownLocation(provider);

            locationManager.requestLocationUpdates(provider, 1000, 1, this);

            if(location!=null)
                onLocationChanged(location);
            else
                Toasty.error(getBaseContext(), "Location can't be retrieved", Toast.LENGTH_SHORT).show();
        }else{
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},1);
        }

        personCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent persIntent=new Intent(MainActivity.this,PersonActivity.class);
                startActivity(persIntent);
            }
        });
        shopCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent shopIntent=new Intent(MainActivity.this,ShopActivity.class);
                startActivity(shopIntent);
            }
        });
        finishCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOut();
            }
        });
    }

    private void signOut() {
        new RetrofitHelper(MainActivity.this).getApIs().logout(shopname,phone,userId,lat,lon,feedb)
                .enqueue(new Callback<JsonElement>() {
                    @Override
                    public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                        try {
                            JSONObject jsonObject=new JSONObject(response.body().toString());
                            String status=jsonObject.getString("status");
                            if (status.equals("Success")){
                                Intent logIntent = new Intent(MainActivity.this, LoginActivity.class);
                                startActivity(logIntent);
                                Toasty.success(getBaseContext(), status, Toast.LENGTH_SHORT).show();
                            }else
                            {
                                Toasty.error(getBaseContext(), "Oops! Try again.", Toast.LENGTH_SHORT).show();

                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    @Override
                    public void onFailure(Call<JsonElement> call, Throwable t) {
                    }
                });
    }

    @Override
    public void onLocationChanged(Location location) {
        lat= String.valueOf(location.getLatitude());
        lon= String.valueOf(location.getLongitude());
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
