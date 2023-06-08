package com.barathsandy.food.rider;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.barathsandy.food.LoginActivity;
import com.barathsandy.food.R;
import com.barathsandy.food.RegisterActivity;

import java.util.HashMap;
import java.util.Map;

public class RiderRegisterActivity extends AppCompatActivity {

    private EditText mfullName,memail,mphone,mvehicletype,mvehicleno,mNationalId,mpassword;
    private Button mRegisterBtn;


    private FirebaseAuth fAuth;
    FirebaseFirestore fStore;

    String userID;

    ProgressBar progressBar;

    String TAG ="RiderRegisterActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rider_register);

        mfullName = findViewById(R.id.fullName);
        memail = findViewById(R.id.email);
        mphone = findViewById(R.id.phone);
        mvehicletype = findViewById(R.id.vehicletype);
        mvehicleno = findViewById(R.id.vehicleno);
        mNationalId = findViewById(R.id.NationalId);
        mpassword = findViewById(R.id.password);
        mRegisterBtn = findViewById(R.id.registerBtn);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();


        progressBar = findViewById(R.id.progressBar);




        mRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String fullname = mfullName.getText().toString().trim();
                String email = memail.getText().toString().trim();
                String phone = mphone.getText().toString();
                String vehicletype = mvehicletype.getText().toString();
                String vehicleno = mvehicleno.getText().toString();
                String nationalid = mNationalId.getText().toString();
                String password = mpassword.getText().toString();

                if (TextUtils.isEmpty(fullname)){
                    mfullName.setError("Full Name is Required");
                    return;
                }
                if (TextUtils.isEmpty(email)){
                    memail.setError("Email is Required");
                    return;
                }
                if (TextUtils.isEmpty(phone)){
                    mphone.setError("Phone is Required ");
                    return;
                }
                if (phone.length() < 10){
                    mphone.setError("Invalid Number");
                }
                if (TextUtils.isEmpty(vehicletype)){
                    mvehicletype.setError("Vehicle Type Required");
                }
                if (TextUtils.isEmpty(vehicleno)){
                    mvehicleno.setError("Vehicle No Required");
                }
                if (TextUtils.isEmpty(nationalid)){
                    mNationalId.setError("National Id Required");
                }
                if (nationalid.length() < 12){
                    mNationalId.setError("Please Check Again");
                }

                if (TextUtils.isEmpty(password)){
                    mpassword.setError("Password is Required");
                    return;
                }
                if (password.length() < 6){
                    mpassword.setError("Password  Must be >= 6 Characters");
                }
                progressBar.setVisibility(View.VISIBLE);


     fAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
         @Override
         public void onComplete(@NonNull Task<AuthResult> task) {
             if (task.isSuccessful()){
                 FirebaseUser fuser = fAuth.getCurrentUser();
//                 currentUserID = fAuth.getCurrentUser().getUid();
//                 fuser.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
//                     @Override
//                     public void onSuccess(Void unused) {
//                         Toast.makeText(RiderRegisterActivity.this,"Verification Email Has been Sent",
//                                 Toast.LENGTH_SHORT).show();
//                     }
//                 }).addOnFailureListener(new OnFailureListener() {
//                     @Override
//                     public void onFailure(@NonNull  Exception e) {
//                         Log.d(TAG,"onFailure Email sent " + e.getMessage());
//                     }
//                 });

                 Toast.makeText(RiderRegisterActivity.this,"Rider is Created",Toast.LENGTH_SHORT).show();
                 userID = fAuth.getCurrentUser().getUid();
                 DocumentReference documentReference = fStore.collection("users").document(userID);
                 Map<String,Object> rider = new HashMap<>();
                 rider.put("fName",fullname);
                 rider.put("email",email);
                 rider.put("phone",phone);
                 rider.put("vehicletype",vehicletype);
                 rider.put("vehicleno",vehicleno);
                 rider.put("NationalId",nationalid);
                 rider.put("Password",password);
                 rider.put("isRider","1");
                 documentReference.set(rider).addOnSuccessListener(new OnSuccessListener<Void>() {
                     @Override
                     public void onSuccess(Void unused) {
                         Log.d(TAG,"onSuccess: user Profile is created for "+ userID);

                     }
                 });
                 startActivity(new Intent(RiderRegisterActivity.this,LoginActivity.class));

             }else {
             Toast.makeText(RiderRegisterActivity.this, "Error !" + task.getException().getMessage(),
                     Toast.LENGTH_SHORT).show();
             progressBar.setVisibility(View.GONE);

         }

         }
     });

            }
        });


    }

    public void donatorBtn(View view) {
        startActivity(new Intent(RiderRegisterActivity.this, RegisterActivity.class));
        finish();
    }

    public void Loginlink(View view) {
        startActivity(new Intent(RiderRegisterActivity.this, LoginActivity.class));

    }
}