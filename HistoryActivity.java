package com.barathsandy.food;

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
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class HistoryActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ArrayList<DonationList> list;
    HistoryAdapter historyAdapter;

    private StorageReference storageReference;
    private DatabaseReference databaseReference;
    FirebaseAuth fAuth;
    String curreuntUser;
    String key;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        recyclerView = findViewById(R.id.allhistory);




        fAuth = FirebaseAuth.getInstance();
        curreuntUser = fAuth.getCurrentUser().getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference("Donations");

        Log.d("TAG","chathuri"+curreuntUser);

        recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));

        list = new ArrayList<>();
        historyAdapter = new HistoryAdapter(this,list);
        recyclerView.setAdapter(historyAdapter);

        databaseReference.orderByChild("userID").equalTo(curreuntUser).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull  DataSnapshot snapshot) {
                list.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {


                    String key = dataSnapshot.getKey();


                    Log.d("GetDonorId","DataSnapshot :"+key);

                    DonationList donationList = dataSnapshot.getValue(DonationList.class);
                    list.add(donationList);

                }
                historyAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull  DatabaseError error) {

            }
        });
    }

    public void backaction(View view) {
        Intent intent = new Intent(HistoryActivity.this,HomeActivity.class);
        startActivity(intent);
    }
}