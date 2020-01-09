package com.tylerlyczak.hikerswatch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    LocationManager locationManager;

    LocationListener locationListener;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1)   {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
                }
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

                Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
                try {
                    List<Address> addressList = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);

                    if (addressList!= null && addressList.size() > 0)   {
                        String address = "";

                        // Gets the address from the location given to the app
                        if (addressList.get(0).getThoroughfare() != null)  {
                            address += addressList.get(0).getThoroughfare() + ", ";
                        }

                        if (addressList.get(0).getLocality() != null)  {
                            address += addressList.get(0).getLocality() + ", ";
                        }

                        if (addressList.get(0).getPostalCode() != null)  {
                            address += addressList.get(0).getPostalCode() + ", ";
                        }

                        if (addressList.get(0).getAdminArea() != null)  {
                            address += addressList.get(0).getAdminArea();
                        }

                        // Sets the latitude
                        TextView latitudeText = (TextView) findViewById(R.id.latText);
                        latitudeText.setText("Latitude: " + String.valueOf(addressList.get(0).getLatitude()));

                        // Sets the longitude
                        TextView longitudeText = (TextView) findViewById(R.id.longText);
                        longitudeText.setText("Longitude: " + String.valueOf(addressList.get(0).getLongitude()));

                        // Sets the accuracy
                        TextView accuracyText = (TextView) findViewById(R.id.accuracyText);
                        accuracyText.setText("Accuracy: " + String.valueOf(addressList.get(0).getCountryCode()));

                        // Sets the address
                        TextView addressText = (TextView) findViewById(R.id.addressText);
                        addressText.setText("Address: " + address);

                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

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
        };

        if (Build.VERSION.SDK_INT < 23) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        }
        else    {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }
            else {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
                Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            }
        }
    }
}
