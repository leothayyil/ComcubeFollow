package com.example.user.comcubefollow;

import android.content.Context;
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
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import es.dmoral.toasty.Toasty;

public class ShopActivity extends AppCompatActivity implements LocationListener {

    LocationManager locationManager;
    String provider,lat,lon;
    Button btn_V,btn_F;
    CardView normal,expanded;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);

      btn_V=findViewById(R.id.btn_shpSubmit);
      btn_F=findViewById(R.id.btn_shpSubmit_fb);
      normal=findViewById(R.id.cardNormal);
        expanded=findViewById(R.id.cardExpanded);

      btn_F.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              normal.setVisibility(View.VISIBLE);
              expanded.setVisibility(View.GONE);

          }
      });

      btn_V.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              expanded.setVisibility(View.VISIBLE);
              normal.setVisibility(View.GONE);


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

    @Override
    public void onLocationChanged(Location location) {
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