package com.barathsandy.food.rider;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.barathsandy.food.DonationList;
import com.barathsandy.food.R;

import java.util.ArrayList;

public class RiderHistoryAdapter extends RecyclerView.Adapter<RiderHistoryAdapter.RiderHistoryViewHolder>  {

    Context context;
    ArrayList<DonationList> list;
    private FirebaseAuth firebaseAuth;
    String userID;
    private DatabaseReference databaseReference;

    public RiderHistoryAdapter(Context context, ArrayList<DonationList> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public RiderHistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.rider_history, parent, false);
        return new RiderHistoryAdapter.RiderHistoryViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull  RiderHistoryAdapter.RiderHistoryViewHolder holder, int position) {


        DonationList donationList = list.get(position);

        firebaseAuth = FirebaseAuth.getInstance();
        userID = firebaseAuth.getCurrentUser().getUid();

        holder.rideerid.setText(donationList.getRiderid());
        holder.donatorname.setText(donationList.getfName());
        holder.donatorno.setText(donationList.getKey());

        holder.date.setText(donationList.getRiderdate());
        holder.time.setText(donationList.getRidertime());

        String iddetail = donationList.getKey();
        Log.d("TAG","keysofuser"+iddetail);

        databaseReference = FirebaseDatabase.getInstance().getReference("Donations").child("Images");

        holder.seeMorebtn.setVisibility(View.VISIBLE);
        holder.seeMorebtn.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Intent i = new Intent(context.getApplicationContext(),ShowImagesActivity.class);
               i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
               i.putExtra("id",iddetail);
               context.getApplicationContext().startActivity(i);
           }
       });







    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class RiderHistoryViewHolder extends RecyclerView.ViewHolder {

        TextView rideerid,donatorname,donatorno,date,time;
        LinearLayout seeMorebtn;

        public RiderHistoryViewHolder(@NonNull View itemView) {
            super(itemView);

            rideerid = itemView.findViewById(R.id.rideerid);
            donatorname = itemView.findViewById(R.id.donatorname);
            donatorno = itemView.findViewById(R.id.donatorno);
            date = itemView.findViewById(R.id.date);
            time = itemView.findViewById(R.id.time);
            seeMorebtn = itemView.findViewById(R.id.seeMorebtn);



        }
    }

}
