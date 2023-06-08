package com.barathsandy.food.rider;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.StorageReference;
import com.barathsandy.food.DonationList;
import com.barathsandy.food.Donations;
import com.barathsandy.food.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class CompleteAdapter extends RecyclerView.Adapter<CompleteAdapter.CompleteViewHolder>{


    private static final int RESULT_LOAD_IMAGE = 101;
    private static final int INTRODUCTION_VIEW_TYPE = 2;
    private static final int IMPLEMENTATION_VIEW_TYPE = 3;
    Context context;
    ArrayList<DonationList> list;
    private DatabaseReference databaseReference;
    Donations donations;
    DonationList donationList;
    String currentUserID;
    private Uri imageUri;
//    String key;
    ImageView imageViews;
    private StorageReference storageReference;

    private FirebaseAuth firebaseAuth;
    String userID;



    public CompleteAdapter(Context context, ArrayList<DonationList> list) {
        this.context = context;
        this.list = list;




    }

    @NonNull
    @Override
    public CompleteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(context).inflate(R.layout.completeitem, parent, false);
        ImageView imageView  = (ImageView) v.findViewById(R.id.imageView);
        return new CompleteViewHolder(v);

    }

    @Override
    public void onBindViewHolder(@NonNull CompleteAdapter.CompleteViewHolder holder, int position) {

        DonationList donationList = list.get(position);

        firebaseAuth = FirebaseAuth.getInstance();
        userID = firebaseAuth.getCurrentUser().getUid();

        holder.tvname.setText(donationList.getfName());
        holder.tvdonationid.setText(donationList.getKey());

        String namedetail = donationList.getfName();
        String iddetail = donationList.getKey();
//        key = donationList.getKey();
//
//        Log.d("TAG","ineed"+key);
//
//        holder.addflotingButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {

////                Intent intent = new Intent();
////                intent.setType("image/*");
////                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE,true);
////                intent.setAction(Intent.ACTION_GET_CONTENT);
////                ((Activity)context).startActivityForResult(Intent.createChooser(intent,"Select Picure"),RESULT_LOAD_IMAGE);
//            }
//        });
        holder.uploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                Intent i = new Intent(context.getApplicationContext(), UploadActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i.putExtra("Name",namedetail);
                i.putExtra("id",iddetail);

                Log.d("TAG","balla"+namedetail +iddetail);
                context.getApplicationContext().startActivity(i);



//                selectImage();

//                databaseReference1 = FirebaseDatabase.getInstance().getReference().child("Donations");
//                databaseReference1.child(donationList.getKey()).child("state").setValue(3);
//                Picasso.get().load(uri).into(imageUri);
            }
        });

        holder.complebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Calendar calendar1 = Calendar.getInstance();
                SimpleDateFormat currentDate = new SimpleDateFormat("yyyy:MM:dd");
                String date = currentDate.format(calendar1.getTime());

                Calendar calendar2 = Calendar.getInstance();
                SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss");
                String time = currentTime.format(calendar2.getTime());


                databaseReference = FirebaseDatabase.getInstance().getReference().child("Donations");
                databaseReference.child(donationList.getKey()).child("riderdate").setValue(date);
                databaseReference.child(donationList.getKey()).child("ridertime").setValue(time);
                databaseReference.child(donationList.getKey()).child("state").setValue(3);
                databaseReference.child(donationList.getKey()).child("connectingkey").setValue(userID+3);
                Toast.makeText(context.getApplicationContext(),"Donation Successfully Completed",Toast.LENGTH_SHORT).show();

            }
        });


    }

//    public void selectImage(){
//
//        final CharSequence[] options = {"Choose from Gallery","Cancel" };
//        AlertDialog.Builder builder = new AlertDialog.Builder(context);
//        builder.setTitle("Select Photo!");
//        builder.setItems(options, new DialogInterface.OnClickListener() {
//
//            @Override
//            public void onClick(DialogInterface dialog, int item) {
//
////                if (options[item].equals("Take Photo"))
////                {
////                    dispatchTakePictureIntent(activity);
////                }
//                if (options[item].equals("Choose from Gallery"))
//                {
//                    Intent intent = new   Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//                    ((Activity) context).startActivityForResult(intent, 101);
//                }
//                else if (options[item].equals("Cancel")) {
//                    dialog.dismiss();
//                }
//            }
//        });
//        builder.show();
//    }


    @Override
    public int getItemCount() {
        return list.size();
    }



    public class CompleteViewHolder extends RecyclerView.ViewHolder {

        TextView tvname,tvdonationid;
        Button uploadBtn,complebtn;
        ImageView imageView;
//        FloatingActionButton addflotingButton;

        public CompleteViewHolder(@NonNull View itemView) {
            super(itemView);

            tvname = itemView.findViewById(R.id.tvname);
            tvdonationid = itemView.findViewById(R.id.tvdonationid);
            uploadBtn = itemView.findViewById(R.id.uploadBtn);
//            addflotingButton = itemView.findViewById(R.id.addflotingButton);
            imageView = itemView.findViewById(R.id.imageView);
            complebtn = itemView.findViewById(R.id.complebtn);



        }
    }

}
