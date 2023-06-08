package com.barathsandy.food;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;
import com.tomer.fadingtextview.FadingTextView;

import java.util.concurrent.TimeUnit;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomeActivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener {

     String TAG = "HomeActivity";
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;

private EditText fullName,email,phone;
private TextView verifyMsg,headertext;
FirebaseAuth fAuth;
FirebaseFirestore fStore;
String userID;
Button resendCode;
CircleImageView imageView;
private StorageReference storageReference;
TextView textView,hometext;

private FadingTextView fadingTextView,welcomefadingText;

LinearLayout verifyMsgLayout;


FirebaseUser currentUser;
    FirebaseUser user;

    private Button logout;

    SharedPreferences sharedPreferences;

    private static final String SHARED_PREF_NAME = "mypref";
    private static final String KEY_NAME = "email";


    //to  check user availability
    SharedPreferences user_session;




    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_home);
        headertext = findViewById(R.id.headertext);

        imageView = findViewById(R.id.imageView);
        textView = findViewById(R.id.homename);
        hometext = findViewById(R.id.hometext);
//        fadingTextView = findViewById(R.id.fading_text_view);
        welcomefadingText = findViewById(R.id.welcomefadingText);
        verifyMsgLayout = findViewById(R.id.verifyMsgLayout);

        String[] example1 = {"Welcome", "Welcome","Welcome"};
        welcomefadingText.setTexts(example1);
        welcomefadingText.setTimeout(300, TimeUnit.MILLISECONDS);


//        String[] example = {"And", "this","is","example 2"};
//        fadingTextView.setTexts(example);
//        fadingTextView.setTimeout(300, TimeUnit.MILLISECONDS);

        hometext.startAnimation((Animation) AnimationUtils.
                loadAnimation(HomeActivity.this,R.anim.translate));

//        logout = findViewById(R.id.btn_logout);

        resendCode = findViewById(R.id.resendCode);
        verifyMsg = findViewById(R.id.verifyMsg);

        //Drawer hooks
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);

        storageReference = FirebaseStorage.getInstance().getReference();

        fAuth = FirebaseAuth.getInstance();
        currentUser = fAuth.getCurrentUser();


        userID = fAuth.getCurrentUser().getUid();
        Log.d(TAG,"user"+userID);


        ///////////////////image get from firebase//////////////////////////////////
        StorageReference profileRef = storageReference.child("users/"+
                FirebaseAuth.getInstance().getCurrentUser().getUid()+"/profile.jpg");
        profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(imageView);
            }
        });
        userID = fAuth.getCurrentUser().getUid();
        user = fAuth.getCurrentUser();
        fStore = FirebaseFirestore.getInstance();

        DocumentReference documentReference = fStore.collection("users").document(userID);
        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot documentSnapshot,
                                        @Nullable FirebaseFirestoreException error){
                        textView.setText(documentSnapshot.getString("fName"));

                    }
                });



        sharedPreferences = getSharedPreferences(SHARED_PREF_NAME,MODE_PRIVATE);

        String isDonator = sharedPreferences.getString(KEY_NAME,null);


        /////////////////////////////Tool Bar///////////////////////////
        setSupportActionBar(toolbar);

        ///////////////////////////   //Navigation Drawer Menu///////////
        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,
                drawerLayout,toolbar,R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);

        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();


        ///////////////////////Navigation  Drawner item Clicked////////////

        navigationView.setNavigationItemSelectedListener(this);
        updateNavHeader();

        navigationView.setCheckedItem(R.id.nav_home);





        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();



        userID = fAuth.getCurrentUser().getUid();
        FirebaseUser  user = fAuth.getCurrentUser();



        if (!user.isEmailVerified()){
            resendCode.setVisibility(View.VISIBLE);
            verifyMsg.setVisibility(View.VISIBLE);
            verifyMsgLayout.setVisibility(View.VISIBLE);
            

            verifyMsg.startAnimation((Animation) AnimationUtils.
                    loadAnimation(HomeActivity.this,R.anim.animation_logo));


                  //verify Button Ckicked


            resendCode.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {

                user.sendEmailVerification().
                   addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(HomeActivity.
                               this,"Verification Email Has been Sent",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d("TAG","onFailure Email sent " +
                                    e.getMessage());
                        }
                    });
                }
            });
        }
//
//        logout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                FirebaseAuth.getInstance().signOut();
//                SharedPreferences.Editor editor = sharedPreferences.edit();
//                editor.clear();
//                editor.commit();
//                Toast.makeText(HomeActivity.
//                        this,"Logeed Out!",Toast.LENGTH_SHORT).show();
//                startActivity(new Intent(HomeActivity.
//                        this,LoginActivity.class));
//                finish();
//            }
//        });
    }

    private void updateNavHeader() {

        View headerView = navigationView.getHeaderView(0);
        TextView navEmail = (TextView) headerView.findViewById(R.id.headertext);
        navEmail.setText(currentUser.getEmail());
        
    }

    ///////////////////////Navigation  Drawner item Clicked//////////////////////
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        switch (menuItem.getItemId()){
            case R.id.nav_home:
                Intent intent = new Intent(HomeActivity.this,
                        HomeActivity.class);
                startActivity(intent);
                break;
            case R.id.nav_profile:
                Intent intent1 = new Intent(HomeActivity.this,
                        ProfileActivity.class);
                startActivity(intent1);
                break;

//            case R.id.nav_login:
//                Toast.makeText(HomeActivity.this,"You clicked Bus",
//                        Toast.LENGTH_SHORT).show();
//                break;

            case R.id.nav_logout:
                showWarningDialog();
                break;

//            case R.id.nav_currentLocation:
//                Intent intent2 = new Intent(HomeActivity.this,MapActivity.class);
//                startActivity(intent2);

        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }


    private void showWarningDialog(){
        AlertDialog.Builder builder =  new AlertDialog.Builder
                (HomeActivity.this,R.style.AlertDialogTheme);
        View view = LayoutInflater.from(HomeActivity.this).inflate(
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

                LogOut();
//                FirebaseAuth.getInstance().signOut();
//                SharedPreferences.Editor editor = sharedPreferences.edit();
//
//                editor.clear();
//                editor.commit();
//                Toast.makeText(HomeActivity.
//                        this,"Logeed Out!",Toast.LENGTH_SHORT).show();
//                startActivity(new Intent(HomeActivity.
//                        this,LoginActivity.class));
//                finish();
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

        //Firebase Sign out
        FirebaseAuth.getInstance().signOut();
        //User session
        user_session = this.getSharedPreferences("user_details", MODE_PRIVATE);


        //sessions clear when user log out
        SharedPreferences.Editor editor = user_session.edit();
        editor.clear();
        editor.putBoolean("isLoggedIn",false);
        editor.putString("cu_type", "");
        editor.commit();


        startActivity(new Intent(HomeActivity.this,SplashActivity.class));
        finish();



    }

    public void adddonation(View view) {
        Intent intent = new Intent(HomeActivity.this,DonationActivity.class);
        startActivity(intent);
    }

    public void recycle(View view) {
        Intent intent = new Intent(HomeActivity.this,DonationListActivity.class);
        startActivity(intent);
    }

    public void add(View view) {

        Intent intent = new Intent(HomeActivity.this,DonationActivity.class);
        startActivity(intent);
    }

    public void adds(View view) {
        Intent intent = new Intent(HomeActivity.this,DonationActivity.class);
        startActivity(intent);

    }

    public void DonationImagebtn(View view) {
        Intent intent = new Intent(HomeActivity.this,DonationActivity.class);
        startActivity(intent);
    }

    public void HistoryImgBtn(View view) {
        Intent intent = new Intent(HomeActivity.this,DonationListActivity.class);
        startActivity(intent);
    }

    public void logoutImgbtn(View view) {
        showWarningDialog();
    }

    public void histortbtn(View view) {
        Intent intent = new Intent(HomeActivity.this, HistoryActivity.class);
        startActivity(intent);
    }
}