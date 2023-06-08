package com.barathsandy.food;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.barathsandy.food.rider.Images;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ImagesAdapter extends RecyclerView.Adapter<ImagesAdapter.ImagesViewHolder> {
    //    private ArrayList<Model> mList;
    private ArrayList<Images> imagesList;
    private Context mContext;

    public ImagesAdapter(Context context, ArrayList<Images> imagesList){

        this.mContext = context;
        this.imagesList = imagesList;
    }

    @NonNull
    @Override
    public ImagesViewHolder onCreateViewHolder(@NonNull  ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.imageview,parent,false);
        return new ImagesAdapter.ImagesViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull  ImagesAdapter.ImagesViewHolder holder, int position) {
        Picasso.get().load(imagesList.get(position).getUrl()).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return imagesList.size();
    }

    public class ImagesViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        public ImagesViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.m_image);
        }
    }
}
