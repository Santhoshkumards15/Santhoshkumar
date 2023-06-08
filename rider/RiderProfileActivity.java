package com.barathsandy.food.rider;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.etebarian.meowbottomnavigation.MeowBottomNavigation;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.barathsandy.food.R;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class RiderProfileActivity extends AppCompatActivity {

    private EditText username, email, NationalId, mobile, vehicleno, vehicletype;
    private CircleImageView profileimg;
    FloatingActionButton changeProfile;

    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String userID;
    private StorageReference storageReference;
    FirebaseUser user;
    private Uri imageUri;
    ProgressBar progressBar;

    Button save,resetPasswordLocal;
    TextView profileusername,profileemail;

    private final int ID_HOME = 1;
    private final int ID_PROFILE = 2;
//    private final int ID_NOTIFICATION = 3;
//    private final int ID_ACCOUNT = 4;

    private static final int PReqCode = 2;
    private static final int REQ_CODE = 1000;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rider_profile);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();


        username = findViewById(R.id.txtusername);
        email = findViewById(R.id.txtemail);
        NationalId = findViewById(R.id.txtid);
        mobile = findViewById(R.id.txtmobile);
        vehicleno = findViewById(R.id.txtvehicleno);
        vehicletype = findViewById(R.id.txtvehicletype);
        progressBar = findViewById(R.id.progressBar);
        changeProfile = findViewById(R.id.changeProfile);
        profileimg = findViewById(R.id.profileimg);
        save = findViewById(R.id.save);
        resetPasswordLocal = findViewById(R.id.resetPasswordLocal);
        profileusername = findViewById(R.id.profileusername);
        profileemail = findViewById(R.id.profileemail);

        MeowBottomNavigation bottomNavigation = findViewById(R.id.bottomNavigation);

        bottomNavigation.add(new MeowBottomNavigation.Model(ID_HOME, R.drawable.ic_home));
        bottomNavigation.add(new MeowBottomNavigation.Model(ID_PROFILE, R.drawable.ic_user));
//        bottomNavigation.add(new MeowBottomNavigation.Model(ID_NOTIFICATION,R.drawable.ic_no));
//        bottomNavigation.add(new MeowBottomNavigation.Model(ID_ACCOUNT,R.drawable.ic_address));

        bottomNavigation.setOnClickMenuListener(new MeowBottomNavigation.ClickListener() {
            @Override
            public void onClickItem(MeowBottomNavigation.Model item) {

                switch (item.getId()) {
                    case ID_HOME:
                        startActivity(new Intent(RiderProfileActivity.this, RiderHomeActivity.class));
                        finish();
                        break;
//                        bottomNavigation.setClickable(false);
                }
//                Toast.makeText(RiderHomeActivity.this,"clicked bitch"+item.getId(),Toast.LENGTH_SHORT).show();
            }
        });

        bottomNavigation.setOnReselectListener(new MeowBottomNavigation.ReselectListener() {
            @Override
            public void onReselectItem(MeowBottomNavigation.Model item) {

                switch (item.getId()) {
                    case ID_PROFILE:
                        bottomNavigation.setClickable(false);
                        break;
                }
            }
        });


        bottomNavigation.setOnShowListener(new MeowBottomNavigation.ShowListener() {
            @Override
            public void onShowItem(MeowBottomNavigation.Model item) {
                String name;
                switch (item.getId()) {
                    case ID_HOME:
                        name = "Home";
                        break;

                    case ID_PROFILE:
                        name = "Profile";
                        break;

//                    case ID_NOTIFICATION: name = "Notification";
//                        break;
//
//                    case ID_ACCOUNT: name = "Account";
//                        break;

                    default:
                        name = "";

                }
//                .setText(R.string.main_page_selected);
            }
        });

//        bottomNavigation.setCount(ID_NOTIFICATION,"4");
        bottomNavigation.show(ID_PROFILE, true);

        save.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);

                if (username.getText().toString().isEmpty() || email.getText().
                        toString().isEmpty() || NationalId.getText().toString()
                        .isEmpty() || mobile.getText().toString().isEmpty() || vehicleno.getText().toString().isEmpty()
                        || vehicletype.getText().toString().isEmpty())  {
                    Toast.makeText(RiderProfileActivity.this,
                            "One of Many fields are empty", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.INVISIBLE);
                    return;
                }
                progressBar.setVisibility(View.VISIBLE);
                String profile_email = email.getText().toString();
                user.updateEmail(profile_email).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        DocumentReference docRef = fStore.
                                collection("users").document(user.
                                getUid());
                        Map<String, Object> edited = new HashMap<>();
                        edited.put("email", profile_email);
                        edited.put("fName", username.getText().toString());
                        edited.put("phone", mobile.getText().toString());
                        edited.put("NationalId",NationalId.getText().toString());
                        edited.put("vehicleno", vehicleno.getText().toString());
                        edited.put("vehicletype", vehicletype.getText().toString());
                        docRef.update(edited).addOnSuccessListener(new
                         OnSuccessListener<Void>() {
                               @Override
                                   public void onSuccess(Void unused) {
                                      Toast.makeText(RiderProfileActivity.this,"ProfileUpdated", Toast.LENGTH_SHORT).show();
                                   progressBar.setVisibility(View.INVISIBLE);
                               }
                          });
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(RiderProfileActivity.this, e.getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        ///////////reset Password

        resetPasswordLocal.setOnClickListener(new View.OnClickListener() {
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
                                                Toast.makeText(RiderProfileActivity.this,
                                                        "Password Reset Successfully",
                                                        Toast.LENGTH_SHORT)
                                                        .show();
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(RiderProfileActivity.
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


        changeProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAndRequestForPermission();
            }
        });

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();

        userID = fAuth.getCurrentUser().getUid();
        user = fAuth.getCurrentUser();

        ///////////////////image get from firebase//////////////////////////////////
        StorageReference profileRef = storageReference.child("users/" +
                FirebaseAuth.getInstance().getCurrentUser().getUid() + "/Riderprofile.jpg");
        profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(profileimg);
            }
        });

        DocumentReference documentReference = fStore.
                collection("users").document(userID);
        documentReference.
                addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot documentSnapshot,
                                        @Nullable FirebaseFirestoreException error) {
                        username.setText(documentSnapshot.getString("fName"));
                        email.setText(documentSnapshot.getString("email"));
                        mobile.setText(documentSnapshot.getString("phone"));
                        NationalId.setText(documentSnapshot.getString("NationalId"));
                        vehicleno.setText(documentSnapshot.getString("vehicleno"));
                        vehicletype.setText(documentSnapshot.getString("vehicletype"));

                        profileusername.setText(documentSnapshot.getString("fName"));
                        profileemail.setText(documentSnapshot.getString("email"));
                    }
                });

    }

    private void checkAndRequestForPermission() {
        if (ContextCompat.checkSelfPermission(RiderProfileActivity.this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(RiderProfileActivity.
                    this, Manifest.permission.READ_EXTERNAL_STORAGE)) {

                Toast.makeText(RiderProfileActivity.this,
                        "Please accept for required permission",
                        Toast.LENGTH_SHORT).show();
            } else {
                ActivityCompat.requestPermissions(RiderProfileActivity.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        PReqCode);
            }
            return;
        } else {
            Intent OpenGalleryIntent = new Intent(Intent.ACTION_PICK,
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(OpenGalleryIntent, REQ_CODE);
        }
    }

    @Override
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

    private void UploadToDatabase(Uri imageUri) {
        progressBar.setVisibility(View.VISIBLE);
        StorageReference fileRef = storageReference.child("users/" + FirebaseAuth.
                getInstance().getCurrentUser().getUid() + "/Riderprofile.jpg");
        fileRef.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.
                TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Toast.makeText(RiderProfileActivity.this,
                                "Profile Uploading Sucess ....", Toast.LENGTH_SHORT)
                                .show();
                        progressBar.setVisibility(View.GONE);
                        Picasso.get().load(uri).into(profileimg);


                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(RiderProfileActivity.this,
                        "Profile Uploading Failed ....", Toast.LENGTH_SHORT)
                        .show();
            }
        });

    }

    @Override
    public void onBackPressed() {
        this.moveTaskToBack(true);
    }
}