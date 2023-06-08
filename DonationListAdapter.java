package com.barathsandy.food;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.maps.GoogleMap;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class DonationListAdapter extends RecyclerView.Adapter<DonationListAdapter.DonationViewHolder> {

    Context context;
    ArrayList<DonationList> list;
    private String CurrentUser;

    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String userID;
    FirebaseUser user;
    private Activity activity;

    private DatabaseReference databaseReference;

    String TAG = "DonationListAdapter";
    private FusedLocationProviderClient mLocationClient;
    GoogleMap mGoogleMap;


    public DonationListAdapter(Context context, ArrayList<DonationList> lists) {
        this.context = context;
        this.list = lists;
    }

    @NonNull
    @Override
    public DonationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.donater_history, parent, false);
        return new DonationViewHolder(v);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull DonationViewHolder holder, int position) {

        DonationList donationList = list.get(position);


        holder.donater_date.setText(donationList.getDonationdate());
        holder.donater_category.setText(donationList.getDonationType());
        holder.donater_vehicle.setText(donationList.getDonationVehivle());
        holder.donator_time.setText(donationList.getDonationtime());
        holder.riderid.setText(donationList.getRiderkey());
//        holder.donater_status.setText(donationList.getStatus());
//        holder.donater_status.setText(donationList.getStatus());


        if (donationList.getState() == 1){

            holder.donater_status.setText("Pending");
            holder.donater_colour_bar.setBackgroundColor(Color.parseColor("#FFF44B3E"));
            holder.deleteBtn.setVisibility(View.VISIBLE);

        } else if (donationList.getState() == 2) {

            holder.donater_status.setText("Approve");
            holder.donater_colour_bar.setBackgroundColor(Color.parseColor("#ff9900"));
            holder.deleteBtn.setVisibility(View.INVISIBLE);


        } else if (donationList.getState() == 3) {

            holder.donater_status.setText("Delivery");
            holder.donater_colour_bar.setBackgroundColor(Color.parseColor("#0ED689"));
            holder.deleteBtn.setVisibility(View.INVISIBLE);

        }

        holder.deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                databaseReference = FirebaseDatabase.getInstance().getReference("Donations");
                databaseReference.child(donationList.getKey()).removeValue();
            }
        });



//

//        holder.donationType.setText(donationList.getDonationType());
//        holder.donationWeight.setText(donationList.getDonationWeight());
//        holder.mobile.setText(donationList.getMobile());
//        holder.donationVehicle.setText(donationList.getDonationVehivle());
//        holder.date.setText(donationList.getDonationdate());
//        holder.time.setText(donationList.getDonationtime());
////        holder.districtLocation.setText(donationList.getStreet());
//        holder.adapter_more_info.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                AlertDialog.Builder builder = new AlertDialog.Builder(v.getRootView().getContext());
//                View dialogView = LayoutInflater.from(v.getRootView().getContext()).inflate(R.layout.moredetails,null);
//                TextView fname;
//                fname = dialogView.findViewById(R.id.fname);
//                fname.setText(donationList.getLatitude());
//                builder.setView(dialogView);
//                builder.setCancelable(true);
//                builder.show();
//
//
//
//            }
//        });
//
//        holder.location.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                Intent myactivity = new Intent(context.getApplicationContext(), RetrieveMapActivity.class);
//                context.getApplicationContext().startActivity(myactivity);
//
////
////                Double latitude = Double.valueOf(donationList.getLatitude());
////                Double longitude = Double.valueOf(donationList.getLongitude());
////
////                LatLng Location = new LatLng(latitude, longitude);
//////                Log.d(TAG,"new"+latitude+longitude);
////                mGoogleMap.addMarker(new MarkerOptions().position(Location).title("Maker in Position"));
////                mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(Location,14F));
//            }
//        });

    }





    @Override
    public int getItemCount() {
        return list.size();
    }

    public class DonationViewHolder extends RecyclerView.ViewHolder{

        TextView donationType,donationWeight,mobile,date,time,donationVehicle,fname,districtLocation,riderid;
        TextView donater_date,donater_category,donater_vehicle,donater_status,donator_time;
        LinearLayout location,adapter_more_info,donater_colour_bar,colorLayout;
        ImageButton deleteBtn;




        public DonationViewHolder(@NonNull View itemView) {
            super(itemView);

            donater_date = itemView.findViewById(R.id.donater_date);
            donater_category = itemView.findViewById(R.id.donater_category);
            donater_vehicle = itemView.findViewById(R.id.donater_vehicle);
            donater_status = itemView.findViewById(R.id.donater_status);
            donater_colour_bar = itemView.findViewById(R.id.donater_colour_bar);
            donator_time = itemView.findViewById(R.id.donator_time);
            colorLayout = itemView.findViewById(R.id.colorLayout);
            riderid = itemView.findViewById(R.id.riderid);
            deleteBtn = itemView.findViewById(R.id.deleteBtn);

//            donationType = itemView.findViewById(R.id.tvdonationtype);
//            donationWeight = itemView.findViewById(R.id.tvdonationweight);
//            mobile = itemView.findViewById(R.id.tvmobile);
//            date = itemView.findViewById(R.id.tvdate);
//            time = itemView.findViewById(R.id.tvtime);
//            donationVehicle = itemView.findViewById(R.id.tvdonatiovehicle);
//            location = itemView.findViewById(R.id.tvlocation);
//            adapter_more_info = itemView.findViewById(R.id.adapter_more_info);
//            districtLocation = itemView.findViewById(R.id.districtLocation);



//      location.setOnClickListener(new View.OnClickListener() {
//         @Override
//    public void onClick(View v) {
//
//
//
//
//
////             CurrentUser = FirebaseAuth.getInstance().getCurrentUser().getUid();
////             DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Donations").child(CurrentUser);
//////
////             ValueEventListener listener = databaseReference.addValueEventListener(new ValueEventListener() {
////                 @Override
////                 public void onDataChange(@NonNull DataSnapshot snapshot) {
//////                     Double latitude = snapshot
////                     Double latitude = snapshot.child("latitude").getValue(Double.class);
////                     Log.d(TAG,"locationss"+latitude);
//////                     Double longitude = snapshot.child("longitude").getValue(Double.class);
////
////
//////                     LatLng location = new LatLng(latitude,longitude);
////
////                 }
////
////                 @Override
////                 public void onCancelled(@NonNull DatabaseError error) {
////
////                 }
////             });
//
//    }
//
//});
        }
    }
}
