package com.barathsandy.food.rider;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.barathsandy.food.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class UploadActivity extends AppCompatActivity {

    private static final int RESULT_LOAD_IMAGE = 1;
    Button mSelectButton;
    RecyclerView mUploadList;

    TextView namedetail,iddetail,riderid;

    private StorageReference storageReference;
    private FirebaseAuth firebaseAuth;
    FirebaseUser currentUser;
    private DatabaseReference databaseReference;

    private List<String> fileNameList;
    private List<String> fileDoneList;
    String key;
    String userID;
//    DonationsAdapter donationsAdapter;
//    ArrayList<DonationList> list;

    private UploadListAdapter uploadListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);

        storageReference = FirebaseStorage.getInstance().getReference();
        databaseReference = FirebaseDatabase.getInstance().getReference("Donations");


        firebaseAuth = FirebaseAuth.getInstance();
        currentUser = firebaseAuth.getCurrentUser();
        userID = firebaseAuth.getCurrentUser().getUid();

//        Log.d("TAG","lolbro" + userID);




        namedetail = findViewById(R.id.namedetail);
        iddetail = findViewById(R.id.iddetail);
        riderid = findViewById(R.id.riderid);


        mSelectButton = findViewById(R.id.uploadbutton);
        mUploadList = findViewById(R.id.recycleView);

        fileNameList = new ArrayList<>();
        fileDoneList = new ArrayList<>();
        uploadListAdapter = new UploadListAdapter(fileNameList,fileDoneList);

        mUploadList.setLayoutManager(new LinearLayoutManager(this));
        mUploadList.setHasFixedSize(true);
        mUploadList.setAdapter(uploadListAdapter);

        String Name = getIntent().getStringExtra("Name");
        String id = getIntent().getStringExtra("id");

        key = id;



        mSelectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent();
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE,true);
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Select Image"),RESULT_LOAD_IMAGE);
            }
        });

        namedetail.setText(Name);
        iddetail.setText(id);
        riderid.setText(userID);


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK){
            if (data.getClipData() !=null){
                int totalItemsSelected = data.getClipData().getItemCount();

                for (int i = 0; i < totalItemsSelected; i++){

                    Uri fileUri = data.getClipData().getItemAt(i).getUri();

                    String fileName = getFileName(fileUri);

                    fileNameList.add(fileName);
                    fileDoneList.add("uploading");
                    uploadListAdapter.notifyDataSetChanged();

//                    Log.d("TAG","nameisbro" + key);


                    StorageReference fileupload = storageReference.child("RiderCompleDonations").child(userID).child(key).child(fileName);
                    int finalI = i;
                    fileupload.putFile(fileUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            fileDoneList.remove(finalI);
                            fileDoneList.add(finalI,"done");
                            uploadListAdapter.notifyDataSetChanged();
                            Toast.makeText(UploadActivity.this,"Completed",Toast.LENGTH_SHORT).show();






                        }


                    }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            fileupload.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {

                                   String url = String.valueOf(uri);
                                    StoreLink(url);
                                }
                            });
                        }
                    });



//                    storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//                        @Override
//                        public void onSuccess(Uri uri) {
//
//                            databaseReference = FirebaseDatabase.getInstance().getReference().child("Donations").child(key).child("userpic");
//                            databaseReference.setValue(uri.toString());
//
//                        }
//                    });

                }
//                Toast.makeText(UploadActivity.this,"Select Multiple Files",Toast.LENGTH_SHORT).show();

            }else if (data.getData() != null){
                Toast.makeText(UploadActivity.this,"Selected Single Files",Toast.LENGTH_SHORT).show();

            }
//            else {
//                Intent intent = new Intent(UploadActivity.this,CompleDonationActivity.class);
//                    startActivity(intent);
//            }

        }


    }

    private void StoreLink(String url) {
        databaseReference.child(key).orderByChild("riderid").equalTo(userID);
        HashMap<String,String> hashMap = new HashMap<>();
        hashMap.put("ImageLink",url);

        databaseReference.child(key).child("Images").child(userID).push().setValue(hashMap);


        Toast.makeText(UploadActivity.this,"Images Upload Successfully",Toast.LENGTH_SHORT).show();
    }

    public String getFileName(Uri uri){
        String result = null;
        if (uri.getScheme().equals("content")){
            Cursor cursor = getContentResolver().query(uri,null,null,null,null);
            try {
                if (cursor !=null && cursor.moveToFirst()){
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }

            }finally {
                cursor.close();

            }
        }
        if (result == null){
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1){
                result = result.substring(cut + 1);
            }
        }
        return result;

    }

    public void backaction(View view) {
        Intent intent = new Intent(UploadActivity.this,CompleDonationActivity.class);
        startActivity(intent);
        finish();
    }
}