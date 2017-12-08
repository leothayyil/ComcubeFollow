package com.example.user.comcubefollow;

import android.app.ActionBar;
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
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.comcubefollow.retrofit.RetrofitHelper;
import com.google.gson.JsonElement;

import org.json.JSONException;
import org.json.JSONObject;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShopActivity extends AppCompatActivity implements LocationListener {

    LocationManager locationManager;
    String provider,lat,lon;
    Button btn_V,btn_F;
    CardView normal,expanded;
    String fshopName,fPhone,fEmail,fFeedback;
    String vshopName,vPhone,vEmail,vFeedback;
    EditText edt_shpNameV,edt_phoneV;
    EditText edt_shpNameF,edt_phoneF,edt_emailF,edt_feedbackF,managerName,managerPhone;
    String user_id,visitedShop,visitedPhone,shop_id;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    String actionV="shop_add";
    String actionF="shop_update";


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
        setContentView(R.layout.activity_shop);

      btn_V=findViewById(R.id.btn_shpSubmit);
      btn_F=findViewById(R.id.btn_shpSubmit_fb);
      normal=findViewById(R.id.cardNormal);
        edt_shpNameV=findViewById(R.id.edt_ShopNameV);
        edt_phoneV=findViewById(R.id.edt_phone_V);
        edt_shpNameF=findViewById(R.id.tv_shopName_fb);
        edt_emailF=findViewById(R.id.edt_email_fb);
        edt_phoneF=findViewById(R.id.tv_phone_fb);
        edt_feedbackF=findViewById(R.id.edt_feedback_fb);
        expanded=findViewById(R.id.cardExpanded);
        managerPhone=findViewById(R.id.edt_managerPhone_fb);
        managerName=findViewById(R.id.edt_managerName_fb);





        preferences = this.getSharedPreferences("user_details", 0);
        editor = preferences.edit();
        user_id = preferences.getString("user_idd", "0");
        visitedShop = preferences.getString("shopName", "0");
        visitedPhone = preferences.getString("phone", "0");
        shop_id=preferences.getString("shop_idd","0");



        if (user_id == "0") {
            Intent logIntent = new Intent(ShopActivity.this, LoginActivity.class);
            startActivity(logIntent);
        } else if (visitedShop.equals("0")) {
            expanded.setVisibility(View.GONE);
            normal.setVisibility(View.VISIBLE);
        }else {
            edt_shpNameF.setText(visitedShop);
            edt_phoneF.setText(visitedPhone);
        }


        btn_F.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {

              if (lat==null){
                  Toasty.warning(getBaseContext(), "Location Can't be retrieved!\"+\"\\n\"+\" Check your Gps status!",
                          Toast.LENGTH_SHORT).show();

              }
              else if (edt_shpNameF.getText().toString().equals("")){
                  Toasty.warning(getBaseContext(), "Shop name field is blank!", Toast.LENGTH_SHORT).show();
}

              else if (edt_emailF.getText().toString().equals("")){
                  Toasty.warning(getBaseContext(), "Email id field is blank!", Toast.LENGTH_SHORT).show();

              } else if (managerName.getText().toString().equals("")){
                  Toasty.warning(getBaseContext(), "Contact persons name field is blank!", Toast.LENGTH_SHORT).show();
              } else if (managerPhone.getText().toString().equals("")){
                  Toasty.warning(getBaseContext(), "Contact number field is blank!", Toast.LENGTH_SHORT).show();
              }else if (edt_feedbackF.getText().toString().equals("")){
                  Toasty.warning(getBaseContext(), "What about feedback?", Toast.LENGTH_SHORT).show();

              }else {
                  fshopName=edt_shpNameF.getText().toString();
                  fPhone=edt_phoneF.getText().toString();
                  fEmail=edt_emailF.getText().toString();
                  fFeedback=edt_feedbackF.getText().toString();
                  feedBackupdate();
              }
          }
      });

      btn_V.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {

              if (lat==null){
                  Toasty.warning(getBaseContext(), "Location Can't be retrieved!\"" +
                                  "+\"\\n\"+\" Check your Gps status!",
                          Toast.LENGTH_SHORT).show();

              }else if (edt_shpNameV.getText().toString().equals("")){
                  Toasty.warning(getBaseContext(), "Shop name field is blank!", Toast.LENGTH_SHORT).show();


              }else if (edt_phoneV.getText().toString().equals("")){
                  Toasty.warning(getBaseContext(), "Phone number field is blank!", Toast.LENGTH_SHORT).show();

              }else{
                  vshopName=edt_shpNameV.getText().toString();
                  vPhone=edt_phoneV.getText().toString();

                  visitUpdate();

              }
          }
      });
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
    }

    private void visitUpdate() {
        new RetrofitHelper(ShopActivity.this).getApIs().visitUpdate(actionV,user_id,lat,lon,vshopName,vPhone)
        .enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                try {
                    JSONObject jsonObject=new JSONObject(response.body().toString());
                    String status=jsonObject.getString("status");
                    shop_id=jsonObject.getString("shop_id");
                    if (status.equals("Success")){
                        editor.putString("shopName",vshopName);
                        editor.putString("phone",vPhone);
                        editor.putString("shop_idd",shop_id);
                        editor.commit();
                        Intent mainIntent=new Intent(ShopActivity.this,MainActivity.class);
                        startActivity(mainIntent);
                        Toasty.success(getApplicationContext(),status,Toast.LENGTH_SHORT).show();
                        edt_shpNameV.setText("");
                        edt_phoneV.setText("");

                    }else {
                        Toasty.error(getApplicationContext(),"Oops! attempt failed try again.",Toast.LENGTH_SHORT).show();

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

    private void feedBackupdate() {
        new RetrofitHelper(ShopActivity.this).getApIs().feedbackUpdate(actionF,user_id,lat,lon,shop_id,fPhone,fEmail,fFeedback)
                .enqueue(new Callback<JsonElement>() {
                    @Override
                    public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                        try {
                            JSONObject jsonObject=new JSONObject(response.body().toString());
                            String status=jsonObject.getString("status");
                            if (status.equals("Success")){
                                Toasty.success(getApplicationContext(),status,Toast.LENGTH_SHORT).show();
                                expanded.setVisibility(View.GONE);
                                edt_shpNameV.setText("");
                                edt_phoneV.setText("");
                                Intent mainIntent=new Intent(ShopActivity.this,MainActivity.class);
                                startActivity(mainIntent);
                                editor.remove("visited");
                                editor.remove("shopName");
                                editor.remove("phone");
                                editor.remove("shop_idd");
                                editor.commit();
                            }else {
                                Toasty.error(getApplicationContext(),"Oops! attempt failed try again.",
                                        Toast.LENGTH_SHORT).show();

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
