package com.barathsandy.food;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DonationActivity extends AppCompatActivity {

    public static final String TAG = "TAG";
    private StorageReference storageReference;
    private DatabaseReference databaseReference;
    FirebaseAuth fAuth;
    FirebaseUser user;
    String userID;
    FirebaseFirestore fStore;
    private Button submitButton;
    ProgressBar progressBar;
    Donations donations;
    String key;



    String Date;
    SimpleDateFormat simpleDateFormat;
    Calendar calendar;
    String fnamedone;

//    long maxid;

    EditText mobile, address, street, tvLatitude, tvLongitude;

    private RadioButton onlyanimals, onlypeoples, both, weght1, weght2, weght3, weght4,
            vehicle1, vehicle2, vehicle3, vehicle4;
    private String donationType = "", donationweight = "", donationvehiclet = "",
            currentUserID,DonationDate,DonationTime, locationLatitude, locationLongitude,
            currentLocation;

    FusedLocationProviderClient fusedLocationProviderClient;
    private DatabaseReference userDatabaseReference;
    private ProgressDialog loadingBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donation);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient
                (DonationActivity.this);
        getLocation();


        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        currentUserID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        donations = new Donations();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Donations");
//        key = databaseReference.push().getKey();
        storageReference = FirebaseStorage.getInstance().getReference();


        onlyanimals = findViewById(R.id.onlyanimals);
        onlypeoples = findViewById(R.id.onlypeople);
        both = findViewById(R.id.both);
        weght1 = findViewById(R.id.weght1);
        weght2 = findViewById(R.id.weght2);
        weght3 = findViewById(R.id.weght3);
        weght4 = findViewById(R.id.weght4);
        vehicle1 = findViewById(R.id.vehicle1);
        vehicle2 = findViewById(R.id.vehicle2);
        vehicle3 = findViewById(R.id.vehicle3);
        vehicle4 = findViewById(R.id.vehicle4);
        mobile = findViewById(R.id.mobile);
        address = findViewById(R.id.address);
        street = findViewById(R.id.district);
        tvLatitude = findViewById(R.id.tv_latitude);
        tvLongitude = findViewById(R.id.tv_longitude);
        submitButton = findViewById(R.id.submitButton);



        onlyanimals.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onlypeoples.setChecked(false);
                both.setChecked(false);
                donationType = "onlyanimals";
            }
        });

        onlypeoples.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onlyanimals.setChecked(false);
                both.setChecked(false);
                donationType = "onlypeoples";
            }
        });

        both.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onlyanimals.setChecked(false);
                onlypeoples.setChecked(false);
                donationType = "both";
            }
        });

        weght1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                weght2.setChecked(false);
                weght3.setChecked(false);
                weght4.setChecked(false);
                donationweight = "1-10Kg";
            }
        });

        weght2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                weght1.setChecked(false);
                weght3.setChecked(false);
                weght4.setChecked(false);
                donationweight = "10-30Kg";
            }
        });

        weght3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                weght1.setChecked(false);
                weght2.setChecked(false);
                weght4.setChecked(false);
                donationweight = "30-50Kg";
            }
        });

        weght4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                weght1.setChecked(false);
                weght2.setChecked(false);
                weght3.setChecked(false);
                donationweight = "50-100Kg";
            }
        });

        vehicle1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vehicle2.setChecked(false);
                vehicle3.setChecked(false);
                vehicle4.setChecked(false);
                donationvehiclet = "Bike";
            }
        });

        vehicle2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vehicle1.setChecked(false);
                vehicle3.setChecked(false);
                vehicle4.setChecked(false);
                donationvehiclet = "Car";
            }
        });

        vehicle3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vehicle1.setChecked(false);
                vehicle2.setChecked(false);
                vehicle4.setChecked(false);
                donationvehiclet = "Van";
            }
        });

        vehicle4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vehicle1.setChecked(false);
                vehicle2.setChecked(false);
                vehicle3.setChecked(false);
                donationvehiclet = "Truck";
            }
        });

        userID = fAuth.getCurrentUser().getUid();
        user = fAuth.getCurrentUser();

        DocumentReference documentReference = fStore.collection("users").
                document(userID);
        documentReference.
                addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot documentSnapshot,
                                        @Nullable FirebaseFirestoreException error) {
                        mobile.setText(documentSnapshot.getString("phone"));
                        fnamedone = documentSnapshot.getString("fName");

                    }
                });

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String animals = onlyanimals.getText().toString();
                String people = onlypeoples.getText().toString();
                String all = both.getText().toString();
                String w1 = weght1.getText().toString();
                String w2 = weght2.getText().toString();
                String w3 = weght3.getText().toString();
                String w4 = weght4.getText().toString();
                String v1 = vehicle1.getText().toString();
                String v2 = vehicle2.getText().toString();
                String v3 = vehicle3.getText().toString();
                String v4 = vehicle4.getText().toString();


                String txt_address = address.getText().toString();
                String txt_street = street.getText().toString();
                String txt_mobile = mobile.getText().toString();

                String txt_latitude = tvLatitude.getText().toString();
                String txt_longitude = tvLongitude.getText().toString();

                Calendar calendar1 = Calendar.getInstance();
                SimpleDateFormat currentDate = new SimpleDateFormat("yyyy:MM:dd");
                String date = currentDate.format(calendar1.getTime());

                Calendar calendar2 = Calendar.getInstance();
                SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss");
                String time = currentTime.format(calendar2.getTime());


                DonationDate = date;
                DonationTime = time;


                if (donationType.isEmpty()){

                    Toast.makeText(DonationActivity.this,
                            "Please select the type.",
                            Toast.LENGTH_SHORT).show();

                }else if (donationweight.isEmpty()){
                    Toast.makeText(DonationActivity.this,
                            "Please select the Donation Weight.",
                            Toast.LENGTH_SHORT).show();

                }else if (donationvehiclet.isEmpty()){
                    Toast.makeText(DonationActivity.this,
                            "Please select the Vehicle type.",
                            Toast.LENGTH_SHORT).show();

                }else if (txt_address.isEmpty()){
                    address.setError("Address is Empty");

                }else if (txt_street.isEmpty()){
                    street.setError("Street is Empty");

                }else if (txt_mobile.isEmpty()){
                    mobile.setError("Mobile is Empty");

                }else if (txt_latitude.isEmpty()){
                    tvLatitude.setError("Latitude is Empty");

                }else if (txt_longitude.isEmpty()){
                    tvLongitude.setError("Longitude is Empty");
                }

                else {
                    if (onlyanimals.isChecked()) {
                        donations.setDonationType(animals);

                    } else if (onlypeoples.isChecked()) {
                        donations.setDonationType(people);

                    } else {
                        donations.setDonationType(all);

                    }
                    if (weght1.isChecked()) {
                        donations.setDonationWeight(w1);

                    } else if (weght2.isChecked()) {
                        donations.setDonationWeight(w2);

                    } else if (weght3.isChecked()) {
                        donations.setDonationWeight(w3);

                    } else {
                        donations.setDonationWeight(w4);

                    }

                    if (vehicle1.isChecked()) {
                        donations.setDonationVehivle(v1);

                    } else if (vehicle2.isChecked()) {
                        donations.setDonationVehivle(v2);

                    } else if (vehicle3.isChecked()) {
                        donations.setDonationVehivle(v3);

                    } else {
                        donations.setDonationVehivle(v4);
                    }


                    donations.setDonationdate(date);
                    donations.setDonationtime(time);
                    donations.setAddress(address.getText().toString().trim());
                    donations.setStreet(street.getText().toString().trim());
                    donations.setMobile(mobile.getText().toString().trim());
                    donations.setLatitude(tvLatitude.getText().toString().trim());
                    donations.setLongitude(tvLongitude.getText().toString().trim());
                    donations.setUserID(fAuth.getUid());
                    donations.setfName(fnamedone);
                    String keys = databaseReference.push().getKey();
                    donations.setKey(keys);
                    donations.setState(1);
                    donations.setRiderid("Pending");
                    donations.setRidertime("waiting");
                    donations.setRiderdate("waiting");
                    donations.setConnectingkey("waiting");
                    donations.setRiderkey("waiting");
//                    donations.setDonatorconnectingkey("waiting");



                    ////////save data with Mobile No//////////////////
//                     databaseReference.child(txt_mobile).push().setValue(donations);

                    ////////save data with UserId//////////////////
                    databaseReference.child(keys).setValue(donations);


                    Intent intent = new Intent(DonationActivity.this,
                            DonationActivity.class);
                    Toast.makeText(DonationActivity.this, "Success !",
                            Toast.LENGTH_SHORT).show();
                    Intent intent1 = new Intent(DonationActivity.this,HomeActivity.class);
                    startActivity(intent1);

                }

            }
        });

    }

    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(DonationActivity.this,Manifest.
                permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
        && ActivityCompat.checkSelfPermission(DonationActivity.this,Manifest.
                permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            //////////When Both permissions are granted/////////////

            /////////////call Method/////////
            getCurrentLocation();
        }
        else {
            //////When permission is not granted
            //////Request permission
            ActivityCompat.requestPermissions(DonationActivity.this,new
                    String[]{Manifest.
                    permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION},100);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull  String[]
            permissions,
          @NonNull int[] grantResults) {
        ///////////Check condition
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100 && grantResults.length > 0 && (grantResults[0] +
                grantResults[1]
                == PackageManager.PERMISSION_GRANTED)) {
            ///////////////////when permission Granted
            ///Call Methods
            getCurrentLocation();
        } else {
            //////////////////when permissions are denied
            Toast.makeText(DonationActivity.this, "Permission denied",
                    Toast.LENGTH_SHORT).show();
        }
    }

    @SuppressLint("MissingPermission")
    private void getCurrentLocation() {
        //////////////Intialize Location manager////////////////////
        LocationManager locationManager = (LocationManager) getSystemService(Context.
                LOCATION_SERVICE);

        ////////////////check condition/////////////////////
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)){

            ///////////////When Location is enabled//////////////////
            ///////////Get Last Location////////////////////////////
            fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new
               OnCompleteListener<Location>() {
                @Override
                public void onComplete(@NonNull Task<Location> task) {
                    ////////////Initialize Location////////////////////
                    Location location = task.getResult();
                    /////check condition//////
                    if (location !=null){
                        ////////////// when location result is not null///////////
                        //// Set Lalitude
                        tvLatitude.setText(String.valueOf(location.getLatitude()));
                        /////set Longitude
                        tvLongitude.setText(String.valueOf(location.getLongitude()));

                    }else {
                        ///When Location result is null
                        /////Intialize Location request
                        LocationRequest locationRequest = new LocationRequest().setPriority
                                (LocationRequest.PRIORITY_HIGH_ACCURACY)
                                .setInterval(1000).setFastestInterval(1000).setNumUpdates(1);

                        ///////////Initialize Location call back
                        LocationCallback locationCallback = new LocationCallback(){
                            @Override
                            public void onLocationResult(LocationResult locationResult) {
                            super.onLocationResult(locationResult);
                                //////////Initialize Location
                          Location location1 = locationResult.getLastLocation();
                                ///////////set Latitude
                          tvLatitude.setText(String.valueOf(location1.getLatitude()));
                                /////////set Longitude
                          tvLongitude.setText(String.valueOf(location1.getLongitude()));

                            }
                        };

                        ////////////Request Location updates
                        fusedLocationProviderClient.requestLocationUpdates(locationRequest,
                                locationCallback,Looper.myLooper());
                    }

                }
            });

        }else {
            ///////////When Location service is not enabled
            //////////Open Location Setting
            startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS).
                    setFlags(Intent.
                    FLAG_ACTIVITY_NEW_TASK));
        }
    }


    public void refresh(View view) {
        Toast.makeText(DonationActivity.this,"Refresh",Toast.LENGTH_SHORT).show();
        getLocation();

    }

    public void backflotingbtn(View view) {
        Intent intent = new Intent(DonationActivity.this,HomeActivity.class);
        startActivity(intent);
    }
}