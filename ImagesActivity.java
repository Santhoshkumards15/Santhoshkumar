package com.barathsandy.food;

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
import com.barathsandy.food.rider.Images;

import java.util.ArrayList;

public class ImagesActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
//    private ArrayList<Model> list;

    //    private ImageAdapter imageAdapter;
    private Context mContext = ImagesActivity.this;

    private ArrayList<Images> imagesList;
    private ImagesAdapter imagesAdapter;
    String key;
    String riderKey;

    FirebaseAuth fAuth;
    String curreuntUser;

    private DatabaseReference databaseReference;
    private StorageReference mStorageRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_images);

        recyclerView = findViewById(R.id.allimages);

        fAuth = FirebaseAuth.getInstance();
        curreuntUser = fAuth.getCurrentUser().getUid();

        Log.d("TAG","iduder"+curreuntUser);

        String id = getIntent().getStringExtra("id");
        key =id;
        Log.d("TAG","piyumi"+key);

        String riderid = getIntent().getStringExtra("riderid");
        riderKey = riderid;

        databaseReference = FirebaseDatabase.getInstance().getReference("Donations");

        recyclerView.setAdapter(imagesAdapter);

        recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));

        imagesList = new ArrayList<>();
        imagesAdapter = new ImagesAdapter(mContext,imagesList);
        recyclerView.setAdapter(imagesAdapter);
        init();
    }

    private void init(){
        clearAll();

//        databaseReference.orderByChild("riderid").equalTo(curreuntUser);

        databaseReference.child(key).child("Images").child(riderKey).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren() ) {
                    Images images = new Images();
                    images.setUrl(dataSnapshot.child("ImageLink").getValue().toString());
                    Log.d("TAG","pleasselol"+images);

                    imagesList.add(images);

                }
                imagesAdapter = new ImagesAdapter(mContext,imagesList);
                recyclerView.setAdapter(imagesAdapter);
                imagesAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void clearAll() {
        if (imagesList != null){
            imagesList.clear();

            if (imagesAdapter != null){
                imagesAdapter.notifyDataSetChanged();
            }
        }
        imagesList = new ArrayList<>();
    }

    public void backaction(View view) {
        Intent intent = new Intent(ImagesActivity.this,HistoryActivity.class);
        startActivity(intent);
    }
}