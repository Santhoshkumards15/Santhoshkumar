package com.barathsandy.food.rider;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.barathsandy.food.DonationList;
import com.barathsandy.food.R;

import java.util.ArrayList;

public class AllDonationsActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ArrayList<DonationList> list;
    DonationsAdapter donationsAdapter;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_donations);

        recyclerView = findViewById(R.id.alldonations);
        databaseReference = FirebaseDatabase.getInstance().getReference("Donations");

        recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));


        list = new ArrayList<>();
        donationsAdapter = new DonationsAdapter(this,list);
        recyclerView.setAdapter(donationsAdapter);

        ////////////////////retrive data to list View////////////////////////////////////////////////////
        databaseReference.orderByChild("state").equalTo(1).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {


//                    Log.d("GetDonorId","DataSnapshot :"+dataSnapshot.getKey());

                    String key = dataSnapshot.getKey();

//                 key = dataSnapshot.getKey();

                    Log.d("GetDonorId","DataSnapshot :"+key);

                    DonationList donationList = dataSnapshot.getValue(DonationList.class);
                    list.add(donationList);
//                    list.add(dataSnapshot.getKey());
                }
                donationsAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }

    public void backaction(View view) {
        Intent intent = new Intent(AllDonationsActivity.this,RiderHomeActivity.class);
        startActivity(intent);
    }
}