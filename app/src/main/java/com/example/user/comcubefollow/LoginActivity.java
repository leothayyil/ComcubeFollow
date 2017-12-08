package com.example.user.comcubefollow;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.user.comcubefollow.retrofit.RetrofitHelper;
import com.google.gson.JsonElement;

import org.json.JSONException;
import org.json.JSONObject;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class LoginActivity extends AppCompatActivity implements LocationListener {

    EditText userName, password;
    Button login;
    ProgressDialog progress;
    String lat,lon;

    private LocationManager locationManager;
    private String provider;

    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        userName = findViewById(R.id.edt_username);
        password = findViewById(R.id.edt_password);
        login = findViewById(R.id.btn_login);
        progress = new ProgressDialog(this);

        preferences=this.getSharedPreferences("user_id",0);
        editor=preferences.edit();

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
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progress.setTitle("Loading");
                progress.setMessage("Wait while loading...");
                progress.setCancelable(false);

                String username = userName.getText().toString();
                String passw = password.getText().toString();

                if (userName.getText().toString().isEmpty()) {
                    Toasty.warning(LoginActivity.this, "Username field is blank!", Toast.LENGTH_SHORT).show();
                } else if (password.getText().toString().isEmpty()) {
                    Toasty.warning(LoginActivity.this, "Password field is blank!", Toast.LENGTH_SHORT).show();
                } else {
                    progress.show();
                    loginMeth(username,passw);
                }
            }
        });


    }

    private void loginMeth(String user,String passw)  {
        new RetrofitHelper(LoginActivity.this).getApIs().
                login(user, passw, lat, lon).enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {

                String userId = "";
                try {
                    JSONObject jsonObject = new JSONObject(response.body().toString());
                    String status = jsonObject.getString("status");
                    userId = jsonObject.getString("user_id");
                    String dateTime = jsonObject.getString("dt_time");

                    editor.putString("user_id_preff",userId);
                    editor.commit();

                    progress.dismiss();
                    if (userId.isEmpty()){
                        Toasty.warning(LoginActivity.this, "Check your login details!", Toast.LENGTH_SHORT).show();
                    }else {
                        Toasty.success(LoginActivity.this,"Success",Toast.LENGTH_SHORT);

                        Intent intent=new Intent(LoginActivity.this,MainActivity.class);
                        startActivity(intent);
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
    protected void onPause() {
        super.onPause();
        locationManager.removeUpdates((LocationListener) this);
    }

    @Override
    public void onLocationChanged(Location location) {

         lat= String.valueOf(location.getLatitude());
         lon= String.valueOf(location.getLongitude());

        Log.i("TAG", lat+","+lon);
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
