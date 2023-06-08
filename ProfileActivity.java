package com.barathsandy.food;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;





public class ProfileActivity extends AppCompatActivity {

    private static final String TAG = "ProfileActivity" ;
    private EditText fullName,email,phone;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String userID;
    TextView proftext;
    Button resetPassLocal,changeProfileImage;
    FirebaseUser user;
    ImageView profileImage,profilefull;
    private ProgressDialog mProgressDialog;

    private CircleImageView profileImageView;
    private Button closeButton,saveButton;
    private TextView profileChangeBtn;

    private DatabaseReference databaseReference;
    private Uri imageUri;
    private String myUri = "";
    private StorageTask uploadTask;
    private StorageReference storageReference;
    int TAKE_IMAGE_CODE = 10001;
    ProgressBar progressBar;


    Bitmap original_image;


    private static final int PReqCode = 2 ;
    private static final int REQ_CODE = 1000;

    ImageButton editusername,editphone;
    Button save;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);



        fullName = findViewById(R.id.profileName);
        email = findViewById(R.id.profileEmail);
        phone = findViewById(R.id.profilePhone);
        resetPassLocal = findViewById(R.id.resetPasswordLocal);
        profileImage = findViewById(R.id.profile_image);
        changeProfileImage = findViewById(R.id.changeProfile);
        progressBar = findViewById(R.id.progressBar);
        profilefull = findViewById(R.id.profilefull);
        proftext =findViewById(R.id.proftext);
        save = findViewById(R.id.save);

//        editusername = findViewById(R.id.editusername);
//        editphone = findViewById(R.id.editphone);

        proftext.startAnimation((Animation) AnimationUtils.
                loadAnimation(ProfileActivity.this,R.anim.translate));

//        editusername.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                fullName.setInputType(InputType.TYPE_NULL);
////               fullName.setTextIsSelectable(true);
////
////                fullName.setEnabled(true);
////                fullName.setFocusable(true);
////                fullName.setActivated(true);
////                fullName.clearFocus();
////                fullName.setFocusableInTouchMode(true);
////                fullName.requestFocus();
////                fullName.setSelection(0, fullName.length());
////                fullName.setClickable(true);
////                fullName.getSelectionEnd();
////                fullName.setCursorVisible(true);
////                fullName.setPressed(true);
////                fullName.setOnKeyListener(null);
//
//
////                 fullName.requestFocusFromTouch();
//////                int cursorpossition = fullName.getSelectionStart();
//////                CharSequence enterdText = fullName.getText().toString();
//////                CharSequence cursorToEnd = enterdText.subSequence(cursorpossition, enterdText.length());
////
////
////
//            }
//        });

//        editphone.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
////
////                editphone.setEnabled(true);
////                editphone.setFocusable(true);
////                editphone.setActivated(true);
////                editphone.setFocusableInTouchMode(true);
////                editphone.requestFocus();
////                editphone.setClickable(true);
////                editphone.setPressed(true);
////                editphone.setOnKeyListener(null);
////                editphone.requestFocusFromTouch();
////                editphone.setSaveEnabled(true);
//            }
//        });

        mProgressDialog = new ProgressDialog(this);


        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();

        ////////////Update Profile/////////////////////

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fullName.getText().toString().isEmpty() || email.getText().
                        toString().isEmpty() || phone.getText().toString()
                        .isEmpty()){
                    Toast.makeText(ProfileActivity.this,
                       "One of Many fields are empty",Toast.LENGTH_SHORT).show();
                    return;
                }
                String profile_email = email.getText().toString();
                user.updateEmail(profile_email).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        DocumentReference docRef = fStore.
                                collection("users").document(user.
                                getUid());
                        Map<String,Object> edited = new HashMap<>();
                        edited.put("email",profile_email);
                        edited.put("fName",fullName.getText().toString());
                        edited.put("phone",phone.getText().toString());
                        docRef.update(edited).addOnSuccessListener(new
                             OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(ProfileActivity.this,
                                        "ProfileUpdated",Toast.LENGTH_SHORT)
                                        .show();
                            }
                        });
                        Toast.makeText(ProfileActivity.this,"ProfileUpdated",
                                Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull  Exception e) {
                        Toast.makeText(ProfileActivity.this,e.getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });


///////////////////image get from firebase//////////////////////////////////
        StorageReference profileRef = storageReference.child("users/"+
                FirebaseAuth.getInstance().getCurrentUser().getUid()+"/profile.jpg");
        profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(profileImage);
            }
        });
        profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(profilefull);
            }
        });

        userID = fAuth.getCurrentUser().getUid();
        user = fAuth.getCurrentUser();

        DocumentReference documentReference = fStore.
                collection("users").document(userID);
        documentReference.
                addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot documentSnapshot,
                                        @Nullable FirebaseFirestoreException error){
                        phone.setText(documentSnapshot.getString("phone"));
                        fullName.setText(documentSnapshot.getString("fName"));
                        email.setText(documentSnapshot.getString("email"));
                    }
                });

        //////////////Reset Password Button Clicked

        resetPassLocal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText resetPassword = new EditText(v.getContext());
                AlertDialog.Builder passwordResetDialog = new
                        AlertDialog.Builder(v.getContext());

                passwordResetDialog.setTitle("Reset Password ?");
                passwordResetDialog.setMessage("Enter New Password > 6 Characters long.");

                passwordResetDialog.setView(resetPassword);

                passwordResetDialog.setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //extract the email and send reset link

                                String newPassword = resetPassword.getText().toString();
                                user.updatePassword(newPassword).
                                    addOnSuccessListener(new OnSuccessListener<Void>()
                                        {
                                            @Override
                                            public void onSuccess(Void unused) {
                                             Toast.makeText(ProfileActivity.this,
                                                    "Password Reset Successfully",
                                                        Toast.LENGTH_SHORT)
                                                        .show();
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(ProfileActivity.
                                                this,"Password Reset Failed",
                                                Toast.LENGTH_SHORT)
                                                .show();
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

        changeProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAndRequestForPermission();
            }
        });

    }

    private void checkAndRequestForPermission() {
        if (ContextCompat.checkSelfPermission(ProfileActivity.this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(ProfileActivity.
                    this, Manifest.permission.READ_EXTERNAL_STORAGE)) {

                Toast.makeText(ProfileActivity.this,
                        "Please accept for required permission",
                        Toast.LENGTH_SHORT).show();
            }
            else
            {
                ActivityCompat.requestPermissions(ProfileActivity.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        PReqCode);
            }
            return;
        }else{
            Intent OpenGalleryIntent= new Intent(Intent.ACTION_PICK,
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(OpenGalleryIntent,REQ_CODE);
        }
    }   @Override
    public void onActivityResult(int requestCode, int resultCode,
                                 @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1000) {
            if (resultCode == Activity.RESULT_OK) {


                imageUri = data.getData();
                try {

                    UploadToDatabase(imageUri);

                } catch (Exception e) {
                    Log.e("TAG", "Error : " + e);
                }

            }

        }

    }



    ///////////////////////upload image to Firebase/////////////////////////////
    private void UploadToDatabase(Uri imageUri) {
        progressBar.setVisibility(View.VISIBLE);
        StorageReference fileRef = storageReference.child("users/"+ FirebaseAuth.getInstance().getCurrentUser().getUid()+"/profile.jpg");
        fileRef.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.
                TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>()
                {
                    @Override
                    public void onSuccess(Uri uri) {
                        Toast.makeText(ProfileActivity.this, "Profile Uploading Sucess ....",Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);
                        Picasso.get().load(uri).into(profileImage);
                        Picasso.get().load(uri).into(profilefull);

                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ProfileActivity.this,
                        "Profile Uploading Failed ....",Toast.LENGTH_SHORT)
                        .show();
            }
        });


    }



    public void dialogBox(String message,String title) {

        AlertDialog alertDialog = new AlertDialog.Builder(ProfileActivity.
                this).create();
        alertDialog.setTitle(title);
        alertDialog.setMessage(message);
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
                new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        alertDialog.show();
    }


    public void backflotingbtn(View view) {
        Intent intent = new Intent(ProfileActivity.this,HomeActivity.class);
        startActivity(intent);
        finish();
    }

//    public void logoutflotingbtn(View view) {
//        FirebaseAuth.getInstance().signOut();
//        Toast.makeText(ProfileActivity.this,"Logeed Out!",Toast.LENGTH_SHORT).show();
//        startActivity(new Intent(ProfileActivity.this,LoginActivity.class));
//        finish();
//
//    }
}


