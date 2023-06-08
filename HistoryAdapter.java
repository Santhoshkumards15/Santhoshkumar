package com.barathsandy.food;

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

import java.util.ArrayList;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder> {

    Context context;
    ArrayList<DonationList> list;
    private FirebaseAuth firebaseAuth;
    String userID;
    private DatabaseReference databaseReference;

    public HistoryAdapter(Context context, ArrayList<DonationList> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public HistoryAdapter.HistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.rider_history, parent, false);
        return new HistoryAdapter.HistoryViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryAdapter.HistoryViewHolder holder, int position) {

        DonationList donationList = list.get(position);

        firebaseAuth = FirebaseAuth.getInstance();
        userID = firebaseAuth.getCurrentUser().getUid();
        String iddetail = donationList.getKey();
        Log.d("TAG","keysofuser"+iddetail);

        databaseReference = FirebaseDatabase.getInstance().getReference("Donations").child("Images");

        holder.rideerid.setText(donationList.getRiderid());

        String riderid = donationList.getRiderid();

        holder.donatorname.setText(donationList.getfName());
        holder.donatorno.setText(donationList.getKey());

        holder.date.setText(donationList.getRiderdate());
        if (holder.date.equals("waiting")){
            holder.seeMorebtn.setVisibility(View.INVISIBLE);

        }else{
            holder.seeMorebtn.setVisibility(View.VISIBLE);
            holder.seeMorebtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(context.getApplicationContext(), ImagesActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    i.putExtra("id",iddetail);
                    i.putExtra("riderid",riderid);

                    context.getApplicationContext().startActivity(i);
                }
            });
        }
        holder.time.setText(donationList.getRidertime());








    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class HistoryViewHolder extends RecyclerView.ViewHolder {

        TextView rideerid,donatorname,donatorno,date,time;
        LinearLayout seeMorebtn;

        public HistoryViewHolder(@NonNull View itemView) {
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
