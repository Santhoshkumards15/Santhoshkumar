package com.barathsandy.food;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.annotations.NotNull;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.barathsandy.food.rider.RiderHomeActivity;

public class LoginActivity extends AppCompatActivity {

    private static final String KEY_USER_TYPE = "USERTYPE";
    private EditText mEmail,mPassword;
    TextView forgotTextLink;
    private Button mLoginBtn;
    private FirebaseAuth fAuth;
    ProgressBar progressBar;

    SharedPreferences sharedPreferences;
    DocumentReference documentReference;
    FirebaseFirestore fStore;
    String userID;
    String TAG ="LoginActivity";

    private static final String SHARED_PREF_NAME = "mypref";


    private static final String KEY_NAME = "email";
    private static final String KEY_NAME_ONE = "email";


    //Login
    SharedPreferences user_session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        user_session =getSharedPreferences("user_details", MODE_PRIVATE);



        checkInternetAvailable();

        mEmail = findViewById(R.id.email);
        mPassword = findViewById(R.id.password);
        mLoginBtn = findViewById(R.id.btn_login);
        progressBar = findViewById(R.id.progressBar);
        forgotTextLink = findViewById(R.id.forgotpassword);

        fStore = FirebaseFirestore.getInstance();

        fAuth = FirebaseAuth.getInstance();
        sharedPreferences = getSharedPreferences(SHARED_PREF_NAME,MODE_PRIVATE);

       /// String isDonator = sharedPreferences.getString(KEY_NAME, null);






//        if (isDonator != null){
//
//            Log.d(TAG,"isDonator");
//            Intent intent = new Intent(LoginActivity.this,HomeActivity.class);
//            startActivity(intent);
//
//        }else {
//
//        }
//
//        sharedPreferences = getSharedPreferences(SHARED_PREF_NAME1,MODE_PRIVATE);
//        String isRider= sharedPreferences.getString(KEY_NAME_ONE,null);
//
//        if (isRider != null){
//            Log.d(TAG,"isRider");
//            Intent intent = new Intent(LoginActivity.this,RiderHomeActivity.class);
//            startActivity(intent);
//        }else {
//
//        }



        mLoginBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                checkInternetAvailable();
                String email = mEmail.getText().toString().trim();
                String password = mPassword.getText().toString().trim();

                if (TextUtils.isEmpty(email)) {
                    mEmail.setError("Email is Required");
                    return;

                }
                if (TextUtils.isEmpty(password)) {
                    mPassword.setError("Password is Required");
                    return;
                }
                if (password.length() < 6) {
                    mPassword.setError("Password  Must be >= 6 Characters");
                }

                progressBar.setVisibility(View.VISIBLE);

                //authenticate the user

                fAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
//                        Toast.makeText(LoginActivity.this, "Logged in Successfully", Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);
                        check(fAuth.getCurrentUser().getUid());
//
//
//
//                            Toast.makeText(LoginActivity.this, "Logged in Successfully", Toast.LENGTH_SHORT).show();
//                            startActivity(new Intent(LoginActivity.this, HomeActivity.class));
//
                        } else {
                            Toast.makeText(LoginActivity.this, "Error !" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });






            }
        });

        ///////forgot password text Ckicked

        forgotTextLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkInternetAvailable();
                EditText resetMail = new EditText(v.getContext());
                AlertDialog.Builder passwordResetDialog = new
                        AlertDialog.Builder(v.getContext());

                passwordResetDialog.setTitle("Reset Password ?");
                passwordResetDialog.setMessage("Enter Your Email");

                passwordResetDialog.setView(resetMail);

                passwordResetDialog.setPositiveButton("Yes",
                 new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //extract the email and send reset link

                    String mail = resetMail.getText().toString();
                    fAuth.sendPasswordResetEmail(mail).
                         addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(LoginActivity.
                                  this,"Reset Link Sent To Your Email",
                                        Toast.LENGTH_SHORT).show();
                            }
                     }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                             Toast.makeText(LoginActivity.this,
                             "Error ! Reset Link Sent To Your Email"+ e.getMessage(),
                                        Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
                passwordResetDialog.setNegativeButton("No",
                        new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //close the dialog box
                    }
                });
                passwordResetDialog.create().show();

            }
        });
    }

    private void check(String uid) {

        DocumentReference df = fStore.collection("users").document(uid);
        ///
        df.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Log.d(TAG,"onSuccess" + documentSnapshot.getData());
                if (documentSnapshot.getString("isDonator") != null){

//                    SharedPreferences.Editor editor = sharedPreferences.edit();
//                    editor.putString(KEY_NAME, mEmail.getText().toString());
//                    editor.putString(KEY_USER_TYPE, "Donator");
//                    editor.apply();
//                    Log.d(TAG,"isDonatorSession");
//                    ///

                    SharedPreferences.Editor editor = user_session.edit();
                    editor.putString("cu_type", "Donator");
                    editor.putBoolean("isLoggedIn",true);

                    editor.commit();
                    Toast.makeText(LoginActivity.this, "Donator in Successfully", Toast.LENGTH_SHORT).show();

                    startActivity(new Intent(getApplicationContext(),HomeActivity.class));
                    finish();


                }

                if (documentSnapshot.getString("isRider") !=null){

//                    SharedPreferences.Editor editor2 = sharedPreferences.edit();
//                    editor2.putString(KEY_NAME_ONE, mEmail.getText().toString());
//                    editor2.putString(KEY_USER_TYPE, "Rider");
//                    editor2.apply();
////                    Log.d(TAG,"isRiderSession");
//


                    SharedPreferences.Editor editor = user_session.edit();
                    editor.putString("cu_type", "Rider");
                    editor.putBoolean("isLoggedIn",true);

                    editor.commit();
                    Toast.makeText(LoginActivity.this, " Rider Logged in Successfully", Toast.LENGTH_SHORT).show();

                    startActivity(new Intent(getApplicationContext(),RiderHomeActivity.class));
                    finish();

                }
            }
        });
    }

    private void checkInternetAvailable() {

        ////////////Initialize ConnectionManager/////////////////
        ConnectivityManager connectivityManager = (ConnectivityManager)
                getApplicationContext().getSystemService(Context.
                        CONNECTIVITY_SERVICE);

        //////////Get active network info////////////////////////
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        ////////////////check network status////////////////////
        if (networkInfo == null || !networkInfo.isConnected() ||
                !networkInfo.isAvailable()) {
///////////when internet is  inactive/////////////////


            /////////initialize dialog//////////////////////////////
            Dialog dialog = new Dialog(this);
            ////////set Content View///////////////////////////
            dialog.setContentView(R.layout.alert_dialog);
            ///set outside touch////////
            dialog.setCanceledOnTouchOutside(false);
            /////set dialog width and height///////////
            dialog.getWindow().setLayout(WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.WRAP_CONTENT);

            //////////////set transparent Background///////////////
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            //////set animation
            dialog.getWindow().getAttributes().windowAnimations =
                    android.R.style.Animation_Dialog;

            /////////Initialize dialog variable
            Button btTryAgain = dialog.findViewById(R.id.bt_try_again);
            //////Perform onClick listner
            btTryAgain.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //////call method
                    recreate();
                }
            });

            //show dialog
            dialog.show();

        }

    }


    public void Logintext(View view) {
        startActivity(new Intent(LoginActivity.this,RegisterActivity.class));
        finish();
    }

    public void registerText(View view) {
        startActivity(new Intent(LoginActivity.this,RegisterActivity.class));
        finish();
    }


    public void loginBtnlink(View view) {
        startActivity(new Intent(LoginActivity.this, LoginActivity.class));
        finish();
    }
}