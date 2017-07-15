package com.jaylerrs.bikesquad.biking;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.jaylerrs.bikesquad.R;
import com.jaylerrs.bikesquad.main.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.nlopez.smartlocation.OnLocationUpdatedListener;
import io.nlopez.smartlocation.SmartLocation;

public class BikingActivity extends BaseActivity implements OnLocationUpdatedListener {

    private static String TAG = "Location";
    @BindView(R.id.txt_lat)
    TextView mLat;
    @BindView(R.id.txt_long)
    TextView mLong;
    @BindView(R.id.fab_location)
    FloatingActionButton fab;

    LocationManager locationManager;
    LocationListener locationListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_biking);
        ButterKnife.bind(this);

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                mLat.setText(String.valueOf(location.getLatitude()));
                mLong.setText(String.valueOf(location.getLongitude()));
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        };

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.INTERNET
                }, 10);
            }
            return;
        } else {
            configuration();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 10:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    configuration();
        }
    }

    private void configuration() {
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                locationManager.requestLocationUpdates("gps", 500, 0, locationListener);
            }
        });
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        SmartLocation.with(this)
                .location()
                .start(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        SmartLocation.with(this)
                .location()
                .stop();
    }

    @Override
    public void onLocationUpdated(Location location) {
        mLat.setText(String.valueOf(location.getLatitude()));
        mLong.setText(String.valueOf(location.getLongitude()));
        Toast.makeText(getApplicationContext(), ""+location.getLongitude() , Toast.LENGTH_SHORT).show();
    }
}
