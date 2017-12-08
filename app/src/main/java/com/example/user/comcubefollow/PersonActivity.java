package com.example.user.comcubefollow;

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
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.user.comcubefollow.retrofit.RetrofitHelper;
import com.google.gson.JsonElement;

import org.json.JSONException;
import org.json.JSONObject;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PersonActivity extends AppCompatActivity implements LocationListener{

    LocationManager locationManager;
    String provider,lat,lon;
    Button submit;
    EditText etPerson,etEmail,etPhone,etfb;
    String sPerson,sEmail,sPhone,sFeedback;
    String action="personal";
    String userId;

    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person);

        etPerson=findViewById(R.id.edt_personsName);
        etEmail=findViewById(R.id.edt_personEmail);
        etPhone=findViewById(R.id.edt_personsPhone);
        etfb=findViewById(R.id.edt_Per_feedback);
        submit=findViewById(R.id.btn_submitPerson);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        preferences = this.getSharedPreferences("user_details", 0);
        editor = preferences.edit();
        userId = preferences.getString("user_idd", "0");

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

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (lat==null){
                    Toasty.warning(PersonActivity.this, "Location Can't be retrieved!"+"\n"+" Check your Gps status.", Toast.LENGTH_SHORT).show();
                }
                 else if (etPerson.getText().toString().equals("")){
                    Toasty.warning(getBaseContext(), "Name field is blank!", Toast.LENGTH_SHORT).show();
                }else if (etEmail.getText().toString().equals("")){
                    Toasty.warning(getBaseContext(), "Email id field is blank!", Toast.LENGTH_SHORT).show();
                }else if (etPhone.getText().toString().equals("")){
                    Toasty.warning(getBaseContext(), "Phone number field is blank!", Toast.LENGTH_SHORT).show();
                }else if (etfb.getText().toString().equals("")){
                    Toasty.warning(getBaseContext(), "Feedback field is blank!", Toast.LENGTH_SHORT).show();
                }else{

                    sPerson=etPerson.getText().toString();
                    sEmail=etEmail.getText().toString();
                    sPhone=etPhone.getText().toString();
                    sFeedback=etfb.getText().toString();
                    submitFb();
                }
            }
        });


    }

    private void submitFb() {
        new RetrofitHelper(PersonActivity.this).getApIs().personUpdate(action,userId,lat,lon,sPhone,sPerson,sEmail,sFeedback)
                .enqueue(new Callback<JsonElement>() {
                    @Override
                    public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                        try {
                            JSONObject jsonObject=new JSONObject(response.body().toString());
                            String status=jsonObject.getString("status");
                            if (status.equals("Success")){
                                Toasty.success(getBaseContext(), status, Toast.LENGTH_SHORT).show();

                                Intent mainIntent=new Intent(PersonActivity.this,MainActivity.class);
                                startActivity(mainIntent);
                            }else {
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
        Log.i("TAG", lat+","+lon);
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
