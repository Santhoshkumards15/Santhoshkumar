package com.barathsandy.food.rider;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.etebarian.meowbottomnavigation.MeowBottomNavigation;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.barathsandy.food.DonationList;
import com.barathsandy.food.LoginActivity;
import com.barathsandy.food.R;
import com.barathsandy.food.SplashActivity;
import com.squareup.picasso.Picasso;
import com.tomer.fadingtextview.FadingTextView;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class RiderHomeActivity extends AppCompatActivity {

    private static final String TAG ="RiderHomeActivity" ;
    private ImageView profileimg;
    TextView txtusername,txtemail;

    String key;

    SharedPreferences sharedPreferences;

    private static final String SHARED_PREF_NAME1 = "myprefs";
    private static final String KEY_NAME_ONE = "email";

    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String userID;

    ArrayList<DonationList> list;
    DonationsAdapter donationsAdapter;
    private DatabaseReference databaseReference;

    ArrayList<DonationList> users;
    private DatabaseReference databaseReference1;

    FirebaseUser currentUser;
    FirebaseUser user;
    private StorageReference storageReference;

    private LinearLayout alldonationslist;

    private RecyclerView.Adapter adapter;
    private RecyclerView recyclerViewList;
    private FadingTextView riderfadingtext;

    //to  check user availability
    SharedPreferences user_session;



    private final int ID_HOME = 1;
    private final int ID_PROFILE = 2;
//    private final int ID_NOTIFICATION = 3;
//    private final int ID_ACCOUNT = 4;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rider_home);



        storageReference = FirebaseStorage.getInstance().getReference();
        databaseReference = FirebaseDatabase.getInstance().getReference("Donations");



        fAuth = FirebaseAuth.getInstance();
        currentUser = fAuth.getCurrentUser();


        profileimg = findViewById(R.id.profileimg);
        txtusername = findViewById(R.id.txtusername);
        txtemail = findViewById(R.id.txtemail);
        recyclerViewList = findViewById(R.id.view);
//        alldonationsrecyclerView = findViewById(R.id.alldonations);


        ///////////////////image get from firebase//////////////////////////////////
        StorageReference profileRef = storageReference.child("users/"+
                FirebaseAuth.getInstance().getCurrentUser().getUid()+"/Riderprofile.jpg");
        profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(profileimg);
            }
        });
        userID = fAuth.getCurrentUser().getUid();
        user = fAuth.getCurrentUser();
        fStore = FirebaseFirestore.getInstance();

//        txtemail.setText(fAuth.getUid());

        DocumentReference documentReference = fStore.collection("users").document(userID);
        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot,
                                @Nullable FirebaseFirestoreException error){
                txtusername.setText(documentSnapshot.getString("fName"));
                txtemail.setText(documentSnapshot.getString("email"));

            }
        });


        /////////////bottom Navigation//////////////////////////
        MeowBottomNavigation bottomNavigation = findViewById(R.id.bottomNavigation);

        bottomNavigation.add(new MeowBottomNavigation.Model(ID_HOME,R.drawable.ic_home));
        bottomNavigation.add(new MeowBottomNavigation.Model(ID_PROFILE,R.drawable.ic_user));
//        bottomNavigation.add(new MeowBottomNavigation.Model(ID_NOTIFICATION,R.drawable.ic_no));
//        bottomNavigation.add(new MeowBottomNavigation.Model(ID_ACCOUNT,R.drawable.ic_address));

        bottomNavigation.setOnClickMenuListener(new MeowBottomNavigation.ClickListener() {
            @Override
            public void onClickItem(MeowBottomNavigation.Model item) {

                switch (item.getId()){
                    case ID_PROFILE: startActivity(new Intent(RiderHomeActivity.this,RiderProfileActivity.class));
                        finish();
                        break;
                }
            }
        });

        bottomNavigation.setOnReselectListener(new MeowBottomNavigation.ReselectListener() {
            @Override
            public void onReselectItem(MeowBottomNavigation.Model item) {
                switch (item.getId()){
                    case ID_HOME: bottomNavigation.setClickable(false);
                    break;
                }
            }
        });


        bottomNavigation.setOnShowListener(new MeowBottomNavigation.ShowListener() {
            @Override
            public void onShowItem(MeowBottomNavigation.Model item) {
                String name;
                switch (item.getId()){
                    case ID_HOME: name = "Home";
                    break;

                    case ID_PROFILE: name = "Profile";
                     break;

//                    case ID_NOTIFICATION: name = "Notification";
//                    break;
//
//                    case ID_ACCOUNT: name = "Account";
//                    break;

                    default: name = "";

                }
//                .setText(R.string.main_page_selected);
            }
        });

//        bottomNavigation.setCount(ID_NOTIFICATION,"4");
        bottomNavigation.show(ID_HOME,true);


        riderfadingtext = findViewById(R.id.riderfadingtext);

        String[] example2 = {"Welcome", "Rider"};
        riderfadingtext.setTexts(example2);
        riderfadingtext.setTimeout(300, TimeUnit.MILLISECONDS);

        recyclerViewList.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));

        list = new ArrayList<>();
        donationsAdapter = new DonationsAdapter(this,list);
        recyclerViewList.setAdapter(donationsAdapter);


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
            public void onCancelled(@NonNull  DatabaseError error) {

            }
        });



        sharedPreferences = getSharedPreferences(SHARED_PREF_NAME1,MODE_PRIVATE);

        String isRider = sharedPreferences.getString(KEY_NAME_ONE,null);
    }


    private void showWarningDialog() {

        AlertDialog.Builder builder =  new AlertDialog.Builder
                (RiderHomeActivity.this,R.style.AlertDialogTheme);
        View view = LayoutInflater.from(RiderHomeActivity.this).inflate(
                R.layout.layout_warning_dialog,
                (ConstraintLayout) findViewById(R.id.layoutDialogContainer)
        );
        builder.setView(view);
        ((TextView) view.findViewById(R.id.textTitile)).
                setText(getResources().getString(R.string.warning_title));
        ((TextView) view.findViewById(R.id.textMessage)).
                setText(getResources().getString(R.string.dummy_text));
        ((Button) view.findViewById(R.id.buttonYes)).
                setText(getResources().getString(R.string.yes));
        ((Button) view.findViewById(R.id.buttonNo)).
                setText(getResources().getString(R.string.no));
        ((ImageView) view.findViewById(R.id.imageIcon)).
                setImageResource(R.drawable.ic_warning);

        final AlertDialog alertDialog = builder.create();

        view.findViewById(R.id.buttonYes).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
//                FirebaseAuth.getInstance().signOut();
                LogOut();
//                SharedPreferences.Editor editor2 = sharedPreferences.edit();
//                editor2.clear();
//                editor2.commit();
                Toast.makeText(RiderHomeActivity.this,"Logeed Out!",Toast.LENGTH_SHORT).show();
                startActivity(new Intent(RiderHomeActivity.this, LoginActivity.class));
                finish();
            }
        });

        view.findViewById(R.id.buttonNo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();


            }
        });

        if (alertDialog.getWindow() != null){
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }
        alertDialog.show();
    }

    private void LogOut() {

        FirebaseAuth.getInstance().signOut();
        //User session
        user_session = this.getSharedPreferences("user_details", MODE_PRIVATE);


        //sessions clear when user log out
        SharedPreferences.Editor editor = user_session.edit();
        editor.clear();
        editor.putBoolean("isLoggedIn",false);
        editor.putString("cu_type", "");
        editor.commit();




        startActivity(new Intent(RiderHomeActivity.this, SplashActivity.class));
        finish();
    }

    @Override
    public void onBackPressed() {
        this.moveTaskToBack(true);
    }

    public void logoutriderImgbtn(View view) {
        showWarningDialog();
    }

    public void alldonations(View view) {

        Intent intent = new Intent(RiderHomeActivity.this,AllDonationsActivity.class);
        startActivity(intent);

//
//        list = new ArrayList<>();
//        donationsAdapter = new DonationsAdapter(this,list);
//        alldonationsrecyclerView.setAdapter(donationsAdapter);
//        alldonationsrecyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
    }

    public void completeDonation(View view) {
        Intent intent = new Intent(RiderHomeActivity.this,CompleDonationActivity.class);
        startActivity(intent);
    }

    public void btnclicked(View view) {
        Intent intent = new Intent(RiderHomeActivity.this,RiderHistoryActivity.class);
        startActivity(intent);
    }

    public void donationbtn(View view) {

        Intent intent = new Intent(RiderHomeActivity.this,AllDonationsActivity.class);
        startActivity(intent);
    }
}