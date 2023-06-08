package com.barathsandy.food.rider;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.barathsandy.food.DonationList;
import com.barathsandy.food.Donations;
import com.barathsandy.food.R;

import java.util.ArrayList;
import java.util.List;

public class UploadListAdapter extends RecyclerView.Adapter<UploadListAdapter.ViewHolder> {

    Context context;
    public List<String> fileNameList;
    public List<String> fileDoneList;
    ArrayList<DonationList> list;

    private DatabaseReference databaseReference1;
    Donations donations;

    public UploadListAdapter(List<String> fileNameList,List<String> fileDoneList){
        this.fileDoneList  =fileDoneList;
        this.fileNameList  = fileNameList;
        this.context = context;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull  ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_single,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull UploadListAdapter.ViewHolder holder, int position) {

        String fileName = fileNameList.get(position);
//        DonationList donationList = list.get(position);
        holder.fileNameView.setText(fileName);

        String filedone = fileDoneList.get(position);

        if (filedone.equals("uploading")){
            holder.fileDoneView.setImageResource(R.drawable.progress);
        }

//        else if (filedone.equals("done")){
//            Intent myactivity = new Intent(context.getApplicationContext(), CompleDonationActivity.class);
//            myactivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            context.getApplicationContext().startActivity(myactivity);
//        }
        else {
            holder.fileDoneView.setImageResource(R.drawable.checked);
//            Toast.makeText(context.getApplicationContext(), "Clicked Laugh Vote", Toast.LENGTH_SHORT).show();

        }
//            Intent myactivity = new Intent(context.getApplicationContext(), CompleDonationActivity.class);
//            myactivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            context.getApplicationContext().startActivity(myactivity);




//        databaseReference1 = FirebaseDatabase.getInstance().getReference().child("Donations");
//        databaseReference1.child(donationList.getKey()).child("state").setValue(3);




    }



    @Override
    public int getItemCount() {
        return fileNameList.size();
    }

    public  class ViewHolder extends RecyclerView.ViewHolder{

        View mView;

        public TextView fileNameView,namedetail,iddetail;
        public ImageView fileDoneView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            mView = itemView;

            fileNameView = itemView.findViewById(R.id.upload_filename);
            namedetail = itemView.findViewById(R.id.namedetail);
            iddetail = itemView.findViewById(R.id.iddetail);

            fileDoneView = itemView.findViewById(R.id.upload_loading);

        }
    }

}
