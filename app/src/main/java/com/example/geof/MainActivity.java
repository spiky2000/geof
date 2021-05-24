package com.example.geof;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.internal.Constants;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private List<Geofence> geofenceList = new ArrayList<>();
    private PendingIntent geofencePendingIntent;
    private GeofencingClient geofencingClient;
    Button btLocation;
    TextView textView1, textView2, textView3, textView4, textView5;
    FusedLocationProviderClient fusedLocationProviderClient;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btLocation = findViewById(R.id.bt_location);
        textView1 = findViewById(R.id.textview1);
        textView2 = findViewById(R.id.textview2);
        textView3 = findViewById(R.id.textview3);
        textView4 = findViewById(R.id.textview4);
        textView5 = findViewById(R.id.textview5);
        final double[] Latitude = new double[1];
        final double[] Longitude = new double[1];
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        btLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    getLocation();
                }else {
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
                }
            }
        });
    }

    @SuppressLint("MissingPermission")
    private void getLocation() {
        fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                Location location = task.getResult();
                if (location != null) {
                    try {
                        Geocoder geocoder = new Geocoder(MainActivity.this, Locale.getDefault());
                        List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);

                        geofenceList.add(new Geofence.Builder()
                                // Set the request ID of the geofence. This is a string to identify this
                                // geofence.
                                .setRequestId("unique id")
                                .setCircularRegion(location.getLatitude(),location.getLongitude (),(float) 100.00)
                                .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER | Geofence.GEOFENCE_TRANSITION_EXIT)
                                .build());

                    }catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        }
    }