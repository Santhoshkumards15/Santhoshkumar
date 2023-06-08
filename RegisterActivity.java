package com.barathsandy.food;

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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.annotations.NotNull;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.barathsandy.food.rider.RiderRegisterActivity;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    public static final String TAG = "TAG";
    private EditText mFullName,mEmail,mPassword,mPhone;
    private Button mRegisterBtn;
    ProgressBar progressBar;

    private FirebaseAuth fAuth;
    FirebaseFirestore fStore;

    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_register);

      mFullName = findViewById(R.id.fullName);
      mEmail = findViewById(R.id.email);
      mPassword = findViewById(R.id.password);
      mPhone = findViewById(R.id.phone);
      mRegisterBtn = findViewById(R.id.registerBtn);

      fAuth = FirebaseAuth.getInstance();
      fStore = FirebaseFirestore.getInstance();
      progressBar = findViewById(R.id.progressBar);

      if (fAuth.getCurrentUser() != null){
        startActivity(new Intent(RegisterActivity.this,LoginActivity.class));
        finish();
      }

        mRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = mEmail.getText().toString().trim();
                String password = mPassword.getText().toString().trim();
                String fullName = mFullName.getText().toString();
                String phone = mPhone.getText().toString();


                if (TextUtils.isEmpty(fullName)){
                    mFullName.setError("Full Name is Required");
                    return;
                }
                if (TextUtils.isEmpty(email)){
                    mEmail.setError("Email is Required");
                    return;
                }
                if (TextUtils.isEmpty(phone)){
                    mPhone.setError("Phone is Required ");
                    return;
                }
                if (phone.length() < 10){
                    mPhone.setError("Invalid Number");
                }
                if (TextUtils.isEmpty(password)){
                    mPassword.setError("Password is Required");
                    return;
                }
                if (password.length() < 6){
                    mPassword.setError("Password  Must be >= 6 Characters");
                }

                progressBar.setVisibility(View.VISIBLE);

                //register the user
       fAuth.createUserWithEmailAndPassword(email,password)
               .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
       @Override
       public void onComplete(@NonNull @NotNull Task<AuthResult> task) {
                 if (task.isSuccessful()){

                    ////send verification link onece user Registerd

                     FirebaseUser fuser = fAuth.getCurrentUser();
                     fuser.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                         @Override
                         public void onSuccess(Void unused) {
                             Toast.makeText(RegisterActivity.this,"Verification Email Has been Sent",
                                     Toast.LENGTH_SHORT).show();
                         }
                     }).addOnFailureListener(new OnFailureListener() {
                         @Override
                         public void onFailure(@NonNull Exception e) {
                             Log.d(TAG,"onFailure Email sent " + e.getMessage());
                         }
                     });


                   Toast.makeText(RegisterActivity.this,"User is Created",Toast.LENGTH_SHORT).show();
                   userID = fAuth.getCurrentUser().getUid();
                   DocumentReference documentReference = fStore.
                                    collection("users").document(userID);
                            Map<String,Object> user = new HashMap<>();
                            user.put("fName",fullName);
                            user.put("email",email);
                            user.put("phone",phone);
                            user.put("isDonator","1");
                   documentReference.set(user).
                       addOnSuccessListener(new OnSuccessListener<Void>() {
                   @Override
                   public void onSuccess(Void unused) {
                   Log.d(TAG,"onSuccess: user Profile is created for "+ userID);
                                }

                            });
                            startActivity(new Intent(RegisterActivity.
                                    this,LoginActivity.class));

                        }else {
                            Toast.makeText(RegisterActivity.this,
                                    "Error !" + task.getException().getMessage(),
                                    Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);

                        }

                    }
                });

            }
        });

    }

    public void Registertext(View view) {
        startActivity(new Intent(RegisterActivity.this,LoginActivity.class));
        finish();
    }

    public void RiderRegisterbtn(View view) {
        startActivity(new Intent(RegisterActivity.this, RiderRegisterActivity.class));
        finish();
    }


}