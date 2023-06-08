package com.barathsandy.food.rider;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.barathsandy.food.DonationList;
import com.barathsandy.food.R;

import java.util.ArrayList;

public class CompleDonationActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ArrayList<DonationList> list;
    CompleteAdapter completeAdapter;
    private DatabaseReference databaseReference;
    LinearLayoutManager linearLayoutManager;

    private FirebaseAuth firebaseAuth;
    String userID;


    public static final int REQUEST_CODE = 101;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comple_donation);

        firebaseAuth = FirebaseAuth.getInstance();
        userID = firebaseAuth.getCurrentUser().getUid();

        recyclerView = findViewById(R.id.completedonations);
        databaseReference = FirebaseDatabase.getInstance().getReference("Donations");


        recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));


        list = new ArrayList<>();
        completeAdapter = new CompleteAdapter(this,list);
        recyclerView.setAdapter(completeAdapter);
//        linearLayoutManager.setReverseLayout(true);
//        linearLayoutManager.setStackFromEnd(true);

        ////////////////////retrive data to list View////////////////////////////////////////////////////
        databaseReference.orderByChild("connectingkey").equalTo(userID+2).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    String key = dataSnapshot.getKey();

                    Log.d("GetDonorId","DataSnapshot :"+key);

                    DonationList donationList = dataSnapshot.getValue(DonationList.class);
                    list.add(donationList);

                }
                completeAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    public void backaction(View view) {
        Intent intent = new Intent(CompleDonationActivity.this,RiderHomeActivity.class);
        startActivity(intent);
    }
}