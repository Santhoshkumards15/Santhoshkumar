package com.barathsandy.food;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.barathsandy.food.databinding.ActivityRetrieveMapBinding;

import java.util.ArrayList;

public class RetrieveMapActivity extends FragmentActivity implements OnMapReadyCallback {

    String TAG = "RetrieveMapActivity";
    private GoogleMap mMap;
    private ActivityRetrieveMapBinding binding;

    private String CurrentUser;
    private DatabaseReference databaseReference;
  

    DonationListAdapter donationListAdapter;
    ArrayList<DonationList> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityRetrieveMapBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
//    int position;
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        CurrentUser = FirebaseAuth.getInstance().getCurrentUser().getUid();

//        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Donations");
        databaseReference = FirebaseDatabase.getInstance().getReference("Donations");

//
//        DonationList donationList = list.get(position);
//        Double latitude = Double.valueOf(donationList.getLatitude());
//        Log.d(TAG,"new"+latitude);
//
        ValueEventListener listener = databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot dataSnapshot : snapshot.getChildren()){

                    String latitude = (String) dataSnapshot.child("latitude").getValue();
                    String longitude = (String) dataSnapshot.child("longitude").getValue();

                    Double d1 = Double.valueOf(latitude);
                    Double d2 = Double.valueOf(longitude);

//                    Log.d(TAG,"new"+latitude + longitude);
//
                    LatLng location = new LatLng(d1,d2);



                   mMap.addMarker(new MarkerOptions().position(location).title("Marker in Current Location"));
                   mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location,14F));

                }

//                Toast.makeText(RetrieveMapActivity.this,"Click Maker to get Location",Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

//        // Add a marker in Sydney and move the camera
//        LatLng sydney = new LatLng(-34, 151);
//        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }
}