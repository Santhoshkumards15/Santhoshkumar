package com.barathsandy.food.rider;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
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
import com.barathsandy.food.R;

import java.util.ArrayList;

public class ShowImagesActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
//    private ArrayList<Model> list;

//    private ImageAdapter imageAdapter;
    private Context mContext = ShowImagesActivity.this;

    private ArrayList<Images> imagesList;
    private ImageAdapter imageAdapter;
    String key;

    FirebaseAuth fAuth;
    String curreuntUser;


    private DatabaseReference databaseReference;
    private StorageReference mStorageRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_images);

        recyclerView = findViewById(R.id.allimages);

        fAuth = FirebaseAuth.getInstance();
        curreuntUser = fAuth.getCurrentUser().getUid();

        Log.d("TAG","iduder"+curreuntUser);

        String id = getIntent().getStringExtra("id");
        key =id;

        databaseReference = FirebaseDatabase.getInstance().getReference("Donations");



//recyclerView.setHasFixedSize(true);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(imageAdapter);

        recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));

        imagesList = new ArrayList<>();
        imageAdapter = new ImageAdapter(mContext,imagesList);
        recyclerView.setAdapter(imageAdapter);
        init();




//        list = new ArrayList<>();

//        recyclerView.setAdapter(imageAdapter);

//        databaseReference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
//                    Model model = dataSnapshot.getValue(Model.class);
//                    list.add(model);
//                }
//                imageAdapter.notifyDataSetChanged();
//            }
//
//            @Override
//            public void onCancelled(@NonNull  DatabaseError error) {
//
//            }
//        });
    }
    private void init(){
        clearAll();

//        databaseReference.orderByChild("riderid").equalTo(curreuntUser);

        databaseReference.child(key).child("Images").child(curreuntUser).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull  DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren() ) {
                    Images images = new Images();
                    images.setUrl(dataSnapshot.child("ImageLink").getValue().toString());
                    Log.d("TAG","pleasselol"+images);

                    imagesList.add(images);

                }
                imageAdapter = new ImageAdapter(mContext,imagesList);
                recyclerView.setAdapter(imageAdapter);
                imageAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull  DatabaseError error) {

            }
        });

    }

    private void clearAll() {
        if (imagesList != null){
            imagesList.clear();

            if (imageAdapter != null){
                imageAdapter.notifyDataSetChanged();
            }
        }
        imagesList = new ArrayList<>();
    }

    public void backaction(View view) {
        Intent intent = new Intent(ShowImagesActivity.this,RiderHistoryActivity.class);
        startActivity(intent);
    }
}