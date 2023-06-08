package com.barathsandy.food.rider;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.barathsandy.food.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageViewHolder> {

//    private ArrayList<Model> mList;
    private ArrayList<Images> imagesList;
    private Context mContext;


    public ImageAdapter(Context context, ArrayList<Images> imagesList){

        this.mContext = context;
        this.imagesList = imagesList;
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.imageview,parent,false);
        return new ImageViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {

//        Glide.with(context).load(mList.get(position).getImageUrl()).into(holder.imageView);
//        Glide.with(context).load(mList.get(position).getImageUrl()).into(holder.imageView);

        Picasso.get().load(imagesList.get(position).getUrl()).into(holder.imageView);



    }


    @Override
    public int getItemCount() {
        return imagesList.size();
    }

    public static class ImageViewHolder extends RecyclerView.ViewHolder{

        ImageView imageView;

        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.m_image);
        }
    }
}
