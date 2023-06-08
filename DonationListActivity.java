package com.barathsandy.food;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class DonationListActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    private DatabaseReference databaseReference;
    DonationListAdapter donationListAdapter;
    ArrayList<DonationList> list;
    FirebaseUser user;
    private String CurrentUser;
    private String phoneNo;

    private String currentUserID;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donation_list);

        recyclerView = findViewById(R.id.userList);
        CurrentUser = FirebaseAuth.getInstance().getCurrentUser().getUid();
        currentUserID = FirebaseAuth.getInstance().getCurrentUser().getUid();
//        phoneNo = FirebaseFirestore.getInstance().collection("users").document(CurrentUser).get("phone");
//        Log.d(TAG,"no pass"+CurrentUser);
        databaseReference = FirebaseDatabase.getInstance().getReference("Donations");
        String keys = databaseReference.push().getKey();



//        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        recyclerView.setHasFixedSize(true);


        list = new ArrayList<>();
        donationListAdapter = new DonationListAdapter(this,list);
        recyclerView.setAdapter(donationListAdapter);





        databaseReference.orderByChild("userID").equalTo(currentUserID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();

                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    DonationList donationList = dataSnapshot.getValue(DonationList.class);
                    list.add(donationList);
                }
                donationListAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}